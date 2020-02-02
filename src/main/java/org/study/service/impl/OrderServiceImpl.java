package org.study.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.dao.OrderDetailMapper;
import org.study.dao.OrderMasterMapper;
import org.study.data.OrderMasterDO;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.service.OrderService;
import org.study.service.ProductService;
import org.study.service.SequenceService;
import org.study.service.UserService;
import org.study.service.model.OrderDetailModel;
import org.study.service.model.OrderModel;
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
        //校验用户存在性、商品存在性、商品数量合法性
        final Optional<UserVO> userVO = userService.queryByPrimaryKey(orderModel.getUserId());
        if (!userVO.isPresent()) {
            throw new ServerException(ServerExceptionBean.USER_QUERY_EXCEPTION);
        }
        //从数据库查询商品信息进行校验，这里查询失败会抛异常
        final List<ProductModel> models = Lists.newArrayList();
        final Map<Integer, OrderDetailModel> productDetails = Maps.newHashMap();
        for (final OrderDetailModel productDetail : orderModel.getProductDetails()) {
            final Integer productId = productDetail.getProductId();
            final ProductModel productModel = productService.selectByPrimaryKey(productId);
            final Integer amount = productDetail.getProductAmount();
            if (productModel.getStock() < amount || amount < 1) {
                throw new ServerException(ServerExceptionBean.ORDER_FAIL_BY_STOCK_EXCEPTION);
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
                throw new ServerException(ServerExceptionBean.ORDER_FAIL_BY_STOCK_EXCEPTION);
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
                .setPayStatus((byte) 0)
                .setOrderStatus((byte) 0)
                .setCreateTime(TimeUtil.getCurrentTimestamp());
        ModelToDataUtil.getOrderMaster(orderModel).ifPresent(orderMasterMapper::insertOrderMaster);
        ModelToDataUtil.getOrderDetails(orderModel.getProductDetails(), orderId)
                .ifPresent(orderDetailMapper::insertOrderDetails);

        //返回前端
        return orderModel;
    }

    @Override
    public List<OrderModel> selectOrdersByUserId(final Integer userId) throws ServerException {
        final List<OrderMasterDO> orderMasters = orderMasterMapper.selectOrdersByUserId(userId);
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

    @Override
    public Optional<OrderModel> selectOrderById(final String orderId) {
        return DataToModelUtil.getOrderModel(
                orderMasterMapper.selectOrderById(orderId),
                orderDetailMapper.selectDetailByOrderId(orderId));
    }

    @Override
    public List<OrderModel> selectByUserIdAndStatus(
            final Integer userId, final Byte status) throws ServerException {
        return selectOrdersByUserId(userId).stream()
                .filter(orderModel -> orderModel.getOrderStatus().equals(status))
                .collect(Collectors.toList());
    }

    @Override
    public int updateOrderStatus(
            final String orderId, final Byte orderStatus, final Byte payStatus) {
        return orderMasterMapper.updateStatus(orderId, orderStatus, payStatus);
    }

    /**
     * 根据用户编号生成分库分表位
     * @param userId 用户编号
     * @return 高16位低16位相异或并模100
     */
    private String userIdMod(final Integer userId) {
        return String.format("%02d", ((userId) ^ (userId >>> 16)) % 100);
    }
}
