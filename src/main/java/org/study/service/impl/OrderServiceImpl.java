package org.study.service.impl;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.study.dao.OrderDetailMapper;
import org.study.dao.OrderMasterMapper;
import org.study.data.OrderDetailDO;
import org.study.data.OrderMasterDO;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.service.DelayService;
import org.study.service.OrderService;
import org.study.service.ProductService;
import org.study.service.SequenceService;
import org.study.service.UserService;
import org.study.service.model.OrderDetailModel;
import org.study.service.model.OrderModel;
import org.study.service.model.OrderMsgModel;
import org.study.service.model.ProductModel;
import org.study.service.model.enumdata.OrderStatus;
import org.study.service.model.enumdata.ProductStatus;
import org.study.util.DataToModelUtil;
import org.study.util.ModelToDataUtil;
import org.study.util.MyTimeUtil;
import org.study.view.UserVO;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author fanqie
 * @date 2020/1/26
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private DelayService delayService;

    @Autowired
    private OrderMasterMapper orderMasterMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderMsgModel createOrder(final OrderModel orderModel) throws ServerException {
        //validate order
        final OrderCache orderUsefulData = validateOrder(orderModel);

        //decrease product stock in redis
        final Map<Integer, Integer> decreasedRecords = Maps.newHashMap();
        for (final Integer productId : orderUsefulData.getProductIds()) {
            final Integer productAmount = orderUsefulData.getProductAmount(productId);
            if (!productService.decreaseStockIncreaseSales(productId, productAmount)) {
                rollBackStockDecrease(decreasedRecords);
                throw new ServerException(ServerExceptionBean.PRODUCT_STOCK_NOT_ENOUGH_EXCEPTION);
            }
            decreasedRecords.put(productId, productAmount);
        }

        //generate the order id
        final String orderId = sequenceService.generateNewSequence(orderModel.getUserId());

        //store the order data
        orderModel.setOrderId(orderId).setOrderAmount(orderUsefulData.getOrderTotalPrice())
                .setPayStatus(OrderStatus.CREATED.getValue())
                .setOrderStatus(OrderStatus.CREATED.getValue())
                .setCreateTime(MyTimeUtil.getCurrentTimestamp());
        try {
            final int masterInsertedNum = ModelToDataUtil.getOrderMaster(orderModel)
                    .map(orderMasterMapper::insertOrderMaster)
                    .orElse(0);
            final int detailInsertedNum = ModelToDataUtil
                    .getOrderDetails(orderModel.getProductDetails(), orderId)
                    .map(orderDetailMapper::insertOrderDetails)
                    .orElse(0);
            if (masterInsertedNum != 1 || detailInsertedNum != orderUsefulData.getProductNum()) {
                throw new Exception();
            }
        } catch (final Exception e) {
            rollBackStockDecrease(decreasedRecords);
            throw new ServerException(ServerExceptionBean.ORDER_FAIL_BY_INSERT_EXCEPTION);
        }

        delayService.submitTask(orderId, orderModel.getCreateTime());

        //return the decreased data and order data to the producer
        return OrderMsgModel.builder()
                .decreaseRecords(decreasedRecords)
                .orderModel(orderModel)
                .build();
    }

    /**
     * inspection order data
     * cache the total price of order and product amount in order
     * @param orderModel order data
     * @return cache data
     * @throws ServerException order is invalid
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderCache validateOrder(final OrderModel orderModel) throws ServerException {
        //validate buyer presence
        final Optional<UserVO> userVO = userService.queryByPrimaryKey(orderModel.getUserId());
        if (!userVO.isPresent()) {
            throw new ServerException(ServerExceptionBean.USER_QUERY_EXCEPTION);
        }

        final Map<Integer, Integer> amountMap = Maps.newHashMap();
        BigDecimal totalPrice = new BigDecimal(0);
        for (final OrderDetailModel productDetail : orderModel.getProductDetails()) {
            //throw ServerException when the product doesn't exist
            final Integer productId = productDetail.getProductId();
            final ProductModel productModel = productService.selectWithoutStockAndSales(productId);

            //check if the product was sold out
            if (ProductStatus.SOLD_OUT.getValue() == productModel.getPayStatus() ||
                    productService.isSoldOut(productId)) {
                throw new ServerException(ServerExceptionBean.ORDER_FAIL_BY_SOLD_OUT_EXCEPTION);
            }

            //validate the owner of the product
            if (!productModel.getUserId().equals(productDetail.getOwnerId())) {
                throw new ServerException(ServerExceptionBean.PRODUCT_OWNER_EXCEPTION);
            }

            //validate the product amount in order
            final int amount = productDetail.getProductAmount();
            if (amount < 1) {
                throw new ServerException(ServerExceptionBean.ORDER_FAIL_BY_AMOUNT_EXCEPTION);
            }

            //calculate the total price of the order
            final BigDecimal price = productService.getProductPrice(productModel);
            totalPrice = totalPrice.add(price.multiply(new BigDecimal(amount)));

            //inspection completed, cache price and amount
            amountMap.put(productId, amount);
        }
        return new OrderCache(amountMap, totalPrice);
    }


    @Override
    public List<OrderModel> selectByUserId(final Integer userId, final OrderStatus orderStatus,
            final OrderStatus payStatus) throws ServerException {
        final List<OrderMasterDO> masters = orderMasterMapper
                .selectByUserId(userId, orderStatus.getValue(), payStatus.getValue());
        return DataToModelUtil.getOrderModel(masters, orderDetailMapper);
    }

    @Override
    public Optional<OrderModel> selectOrderById(final String orderId) {
        return DataToModelUtil.getOrderModel(
                orderMasterMapper.selectOrderById(orderId),
                orderDetailMapper.selectDetailByOrderId(orderId));
    }

    @Override
    public List<OrderModel> selectCreatedOrderWithSeller(final Integer sellerId)
            throws ServerException {
        final Byte created = OrderStatus.CREATED.getValue();
        return DataToModelUtil.getOrderModel(
                orderMasterMapper.selectBySellerId(sellerId, created, created),
                orderDetailMapper);
    }

    @Override
    public List<OrderModel> selectPaidOrderWithSeller(final Integer sellerId)
            throws ServerException {
        final Byte paid = OrderStatus.PAID.getValue();
        return DataToModelUtil.getOrderModel(
                orderMasterMapper.selectBySellerId(sellerId, paid, paid),
                orderDetailMapper);
    }

    @Override
    public List<OrderModel> selectSentOrderWithSeller(final Integer sellerId)
            throws ServerException {
        final List<OrderMasterDO> masters = orderMasterMapper.selectBySellerId(
                sellerId, OrderStatus.SENT.getValue(), OrderStatus.PAID.getValue());
        return DataToModelUtil.getOrderModel(masters, orderDetailMapper);
    }

    @Override
    public List<OrderModel> selectFinishedOrderWithSeller(final Integer sellerId)
            throws ServerException {
        final List<OrderMasterDO> masters = orderMasterMapper.selectBySellerId(
                sellerId, OrderStatus.FINISHED.getValue(), OrderStatus.PAID.getValue());
        return DataToModelUtil.getOrderModel(masters, orderDetailMapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(final String orderId) throws ServerException {
        //validate the order
        final List<OrderDetailDO> details = orderDetailMapper.selectDetailByOrderId(orderId);
        if (CollectionUtils.isEmpty(details)) {
            throw new ServerException(ServerExceptionBean.ORDER_CANCEL_EXCEPTION);
        }

        //change order status
        if (orderMasterMapper.cancelOrder(orderId) < 1) {
            return;
        }

        //increase stock and decrease sales
        final Map<Integer, Integer> rollBackRecords = new HashMap<>(details.size());
        for (final OrderDetailDO detail : details) {
            final Integer productId = detail.getProductId();
            final Integer productAmount = detail.getProductAmount();
            //redis
            if (productService.increaseStockDecreaseSales(productId, productAmount)) {
                rollBackRecords.put(productId, productAmount);
            } else {
                this.rollBackStockIncrease(rollBackRecords);
            }

            //mysql
            if (!productService.decreaseSales(productId, productAmount) ||
                    !productService.increaseStock(productId, productAmount) ||
                    !productService.reInSale(productId)) {
                this.rollBackStockIncrease(rollBackRecords);
                throw new ServerException(ServerExceptionBean.ORDER_CANCEL_EXCEPTION);
            } else {
                productService.delDetailCache(productId);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderStatus(final String orderId, final OrderStatus orderStatus,
            final OrderStatus payStatus) {
        return orderMasterMapper.updateStatus(
                orderId, orderStatus.getValue(), payStatus.getValue()) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rollBackStockDecrease(final Map<Integer, Integer> decreasedRecord) {
        decreasedRecord.forEach((k, v) -> productService.increaseStockDecreaseSales(k, v));
    }

    public void rollBackStockIncrease(final Map<Integer, Integer> increasedMap) {
        increasedMap.forEach((k, v) -> productService.decreaseStockIncreaseSales(k, v));
    }

    @Override
    public boolean isOrderExpired(final Timestamp createTime) {
        final Timestamp deadline = MyTimeUtil.getDeadline(createTime, HOUR_PERIOD);
        return MyTimeUtil.getCurrentTimestamp().after(deadline);
    }

    private static class OrderCache {
        private final Map<Integer, Integer> amountMap;
        private final BigDecimal totalPrice;

        public OrderCache(final Map<Integer, Integer> amountMap, final BigDecimal totalPrice) {
            this.amountMap = amountMap;
            this.totalPrice = totalPrice;
        }

        public BigDecimal getOrderTotalPrice() {
            return this.totalPrice;
        }

        public int getProductAmount(final Integer productId) {
            return amountMap.getOrDefault(productId, 0);
        }

        public Set<Integer> getProductIds() {
            return amountMap.keySet();
        }

        public int getProductNum() {
            return amountMap.size();
        }
    }
}
