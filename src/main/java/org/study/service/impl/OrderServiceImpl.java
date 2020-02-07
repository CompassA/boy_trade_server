package org.study.service.impl;

import com.google.common.collect.Lists;
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
import org.study.service.OrderService;
import org.study.service.ProductService;
import org.study.service.SequenceService;
import org.study.service.UserService;
import org.study.service.model.OrderDetailModel;
import org.study.service.model.OrderModel;
import org.study.service.model.enumdata.OrderStatus;
import org.study.service.model.ProductModel;
import org.study.util.DataToModelUtil;
import org.study.util.ModelToDataUtil;
import org.study.util.TimeUtil;
import org.study.view.UserVO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private OrderMasterMapper orderMasterMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderModel createOrder(final OrderModel orderModel) throws ServerException {
        //校验用户存在性
        final Optional<UserVO> userVO = userService.queryByPrimaryKey(orderModel.getUserId());
        if (!userVO.isPresent()) {
            throw new ServerException(ServerExceptionBean.USER_QUERY_EXCEPTION);
        }
        //从数据库查询商品信息进行校验
        final List<ProductModel> models = Lists.newArrayList();
        final Map<Integer, OrderDetailModel> productDetails = Maps.newHashMap();
        for (final OrderDetailModel productDetail : orderModel.getProductDetails()) {
            final Integer productId = productDetail.getProductId();
            //未查询到商品会抛异常
            final ProductModel productModel = productService.selectWithoutStockAndSales(productId);
            //验证购买数量
            if (productDetail.getProductAmount() < 1) {
                throw new ServerException(ServerExceptionBean.ORDER_FAIL_BY_AMOUNT_EXCEPTION);
            }
            //验证商品持有者
            if (!productModel.getUserId().equals(productDetail.getOwnerId())) {
                throw new ServerException(ServerExceptionBean.PRODUCT_OWNER_EXCEPTION);
            }
            models.add(productModel);
            productDetails.put(productModel.getProductId(), productDetail);
        }

        //落单减库存并计算订单总金额
        BigDecimal orderAmount = new BigDecimal(0);
        for (final ProductModel model : models) {
            final Integer productId = model.getProductId();
            final OrderDetailModel productDetail = productDetails.get(productId);
            final Integer productAmount = productDetail.getProductAmount();
            if (!productService.decreaseStock(productId, productAmount) ||
                    !productService.increaseSales(productId, productAmount)) {
                throw new ServerException(ServerExceptionBean.PRODUCT_STOCK_NOT_ENOUGH_EXCEPTION);
            }
            orderAmount = orderAmount.add(
                    productService.getProductPrice(model).multiply(new BigDecimal(productAmount)));
        }

        //生成订单编号
        final String orderId = LocalDateTime.now()
                .format(DateTimeFormatter.ISO_DATE).replace("-", "")
                + sequenceService.generateNewSequence(SequenceService.DEFAULT_SEQUENCE_ID)
                + this.userIdMod(orderModel.getUserId());

        //订单入库
        orderModel.setOrderId(orderId)
                .setOrderAmount(orderAmount)
                .setPayStatus(OrderStatus.CREATED.getValue())
                .setOrderStatus(OrderStatus.CREATED.getValue())
                .setCreateTime(TimeUtil.getCurrentTimestamp());
        ModelToDataUtil.getOrderMaster(orderModel).ifPresent(orderMasterMapper::insertOrderMaster);
        ModelToDataUtil.getOrderDetails(orderModel.getProductDetails(), orderId)
                .ifPresent(orderDetailMapper::insertOrderDetails);

        //返回前端
        return orderModel;
    }

    @Override
    public List<OrderModel> selectByUserId(final Integer userId, final OrderStatus orderStatus,
            final OrderStatus payStatus) throws ServerException {
        return this.convertCore(
                orderMasterMapper.selectByUserId(userId,
                        orderStatus.getValue(), payStatus.getValue()));
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
        return this.convertCore(orderMasterMapper.selectBySellerId(sellerId, created, created));
    }

    @Override
    public List<OrderModel> selectPaidOrderWithSeller(final Integer sellerId)
            throws ServerException {
        final Byte paid = OrderStatus.PAID.getValue();
        return this.convertCore(orderMasterMapper.selectBySellerId(sellerId, paid, paid));
    }

    @Override
    public List<OrderModel> selectSentOrderWithSeller(final Integer sellerId)
            throws ServerException {
        return this.convertCore(orderMasterMapper.selectBySellerId(
                sellerId, OrderStatus.SENT.getValue(), OrderStatus.PAID.getValue()));
    }

    @Override
    public List<OrderModel> selectFinishedOrderWithSeller(final Integer sellerId)
            throws ServerException {
        return this.convertCore(orderMasterMapper.selectBySellerId(sellerId,
                OrderStatus.FINISHED.getValue(), OrderStatus.PAID.getValue()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(final String orderId, final Integer userId) throws ServerException {
        //获取订单详情并校验
        final List<OrderDetailDO> details = orderDetailMapper.selectDetailByOrderId(orderId);
        if (CollectionUtils.isEmpty(details)) {
            throw new ServerException(ServerExceptionBean.ORDER_CANCEL_EXCEPTION);
        }

        //更改订单状态
        if (orderMasterMapper.cancelOrder(orderId) < 1) {
            throw new ServerException(ServerExceptionBean.ORDER_CANCEL_EXCEPTION);
        }

        //增库存、减销量
        for (final OrderDetailDO detail : details) {
            final Integer productId = detail.getProductId();
            final Integer productAmount = detail.getProductAmount();
            if (!productService.increaseStock(productId, productAmount)
                    || !productService.decreaseSales(productId, productAmount)) {
                throw new ServerException(ServerExceptionBean.ORDER_CANCEL_EXCEPTION);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderStatus(
            final String orderId, final OrderStatus orderStatus, final OrderStatus payStatus) {
        return orderMasterMapper.updateStatus(
                orderId, orderStatus.getValue(), payStatus.getValue()) > 0;
    }

    /**;
     * 根据用户编号生成分库分表位
     * @param userId 用户编号
     * @return 高16位低16位相异或并模100
     */
    private String userIdMod(final Integer userId) {
        return String.format("%02d", ((userId) ^ (userId >>> 16)) % 100);
    }

    private List<OrderModel> convertCore(final List<OrderMasterDO> orderMasters)
            throws ServerException {
        final List<OrderModel> models = orderMasters.stream()
                .map(orderMasterDO -> DataToModelUtil.getOrderModel(
                        orderMasterDO,
                        orderDetailMapper.selectDetailByOrderId(orderMasterDO.getOrderId())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        if (models.size() == orderMasters.size()) {
            return models;
        }
        throw new ServerException(ServerExceptionBean.ORDER_FAIL_BY_SYSTEM_EXCEPTION);
    }
}
