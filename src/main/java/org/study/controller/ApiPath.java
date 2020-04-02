package org.study.controller;

/**
 * @author fanqie
 * @date 2020/1/5
 */
public class ApiPath {

    public static class User {
        /* 登录 */
        public static final String LOGIN = "/user/login";
        /* 注册 */
        public static final String REGISTRY = "/user/registry";
        /* 用户名存在性检测 */
        public static final String EXIST = "/user/exist";
        /* 商品页用户信息 */
        public static final String PART_INFO = "/user/part_info";
        /* 根据session获取用户登录信息 */
        public static final String SESSION_CHECKING = "/user/session_status";
        /* 用户登出, 删除session */
        public static final String LOGOUT = "/user/logout";
        /* 更换头像 */
        public static final String UPDATE_ICON = "/user/update_icon";
    }

    public static class Encrypt {
        /* 网页端获取RSA公钥 */
        public static final String PUB_KEY = "/encrypt/public";
    }

    public static class Product {
        /* 创建商品*/
        public static final String CREATE = "/product/create";
        /* 获取一页商品列表 */
        public static final String PAGE = "/product/page";
        /* 获取商品详情 */
        public static final String DETAIL = "/product/detail";
        /* 获取用户所有发布商品的状态 */
        public static final String USER_PRODUCTS = "/product/list";
        /* 获取主页展示的商品 */
        public static final String HOME_PRODUCTS = "/product/home";
    }

    public static class LoadingFile {
        /* 上传图片 */
        public static final String UPLOAD = "/file/upload";
    }

    public static class Order {
        /* 创建订单 */
        public static final String CREATE = "/order/create";
        /* 根据订单号查询 */
        public static final String QUERY_BY_ORDER_ID = "/order/query_id";
        /* 订单支付回调, 更改为支付状态 */
        public static final String TRADE_PAY = "/order/trade_pay";
        /* 卖家查看未支付订单 */
        public static final String CREATED_ORDER_WITH_SELLER = "/order/created_seller";
        /* 卖家查看已支付未发货订单 */
        public static final String PAID_ORDER_WITH_SELLER = "/order/paid_seller";
        /* 卖家查看已发货未收货订单 */
        public static final String SENT_ORDER_WITH_SELLER = "/order/sent_seller";
        /* 卖家查看已经交易完成的订单 */
        public static final String FINISHED_ORDER_WITH_SELLER = "/order/finished_seller";
        /* 买家查看未付款订单 */
        public static final String CREATED_ORDER_WITH_BUYER = "/order/created_buyer";
        /* 买家查看已付款未发货订单 */
        public static final String PAID_ORDER_WITH_BUYER = "/order/paid_buyer";
        /* 买家查看已发货未收货订单 */
        public static final String SENT_ORDER_WITH_BUYER = "/order/sent_buyer";
        /* 买家查看已经交易完成的订单 */
        public static final String FINISHED_ORDER_WITH_BUYER = "/order/finished_buyer";
        /* 将订单状态改为已发货 */
        public static final String SENT_STATUS = "/order/status_sent";
        /* 将订单状态改为已收货 */
        public static final String RECEIVED = "/order/status_received";
        /* 买家取消订单 */
        public static final String CANCEL = "/order/status_cancel";
    }

    public static class AddressInfo {
        /* 获取商家所有地址信息 */
        public static final String GET_USER_ADDRESS_INFO = "/address/get";
        /* 新建一条地址信息 */
        public static final String INSERT_USER_ADDRESS_INFO = "/address/insert";
        /* 更新地址信息 */
        public static final String UPDATE_USER_ADDRESS_INFO = "/address/update";
        /* 删除地址信息 */
        public static final String DELETE_USER_ADDRESS_INFO = "/address/delete";
        /* 重新设置当前收货地址 */
        public static final String RESET_SELECTED_ADDRESS = "/address/reset";
        /* 获取商家设置的收货地址 */
        public static final String DEFAULT_INFO = "/address/default";
    }

    public static class Cart {
        /* 获取商家购物车数据 */
        public static final String GET = "/cart/get";
        /* 往购物车添加一个商品 */
        public static final String ADD = "/cart/add";
        /* 删除购物车的一个商品 */
        public static final String DELETE = "/cart/delete";
        /* 批量删除购物车中的商品 */
        public static final String DELETE_CART = "/cart/delete_all";
    }

    public static class Trade {
        /* 支付宝接口 */
        public static final String PAY = "/trade/pay";
    }
}