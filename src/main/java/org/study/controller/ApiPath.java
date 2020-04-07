package org.study.controller;

/**
 * @author fanqie
 * @date 2020/1/5
 */
public class ApiPath {

    public static class User {
        /* 登录 */
        public static final String LOGIN = "/api/user/login";
        /* 注册 */
        public static final String REGISTRY = "/api/user/registry";
        /* 用户名存在性检测 */
        public static final String EXIST = "/api/user/exist";
        /* 商品页用户信息 */
        public static final String PART_INFO = "/api/user/part_info";
        /* 根据session获取用户登录信息 */
        public static final String SESSION_CHECKING = "/api/user/session_status";
        /* 用户登出, 删除session */
        public static final String LOGOUT = "/api/user/logout";
        /* 更换头像 */
        public static final String UPDATE_ICON = "/api/user/update_icon";
    }

    public static class Encrypt {
        /* 网页端获取RSA公钥 */
        public static final String PUB_KEY = "/api/encrypt/public";
    }

    public static class Product {
        /* 创建商品*/
        public static final String CREATE = "/api/product/create";
        /* 获取一页商品列表 */
        public static final String PAGE = "/api/product/page";
        /* 获取商品详情 */
        public static final String DETAIL = "/api/product/detail";
        /* 获取用户所有发布商品的状态 */
        public static final String USER_PRODUCTS = "/api/product/list";
        /* 获取主页展示的商品 */
        public static final String HOME_PRODUCTS = "/api/product/home";
    }

    public static class LoadingFile {
        /* 上传图片 */
        public static final String UPLOAD = "/api/file/upload";
    }

    public static class Order {
        /* 创建订单 */
        public static final String CREATE = "/api/order/create";
        /* 根据订单号查询 */
        public static final String QUERY_BY_ORDER_ID = "/api/order/query_id";
        /* 订单支付回调, 更改为支付状态 */
        public static final String TRADE_PAY = "/api/order/trade_pay";
        /* 卖家查看未支付订单 */
        public static final String CREATED_ORDER_WITH_SELLER = "/api/order/created_seller";
        /* 卖家查看已支付未发货订单 */
        public static final String PAID_ORDER_WITH_SELLER = "/api/order/paid_seller";
        /* 卖家查看已发货未收货订单 */
        public static final String SENT_ORDER_WITH_SELLER = "/api/order/sent_seller";
        /* 卖家查看已经交易完成的订单 */
        public static final String FINISHED_ORDER_WITH_SELLER = "/api/order/finished_seller";
        /* 买家查看未付款订单 */
        public static final String CREATED_ORDER_WITH_BUYER = "/api/order/created_buyer";
        /* 买家查看已付款未发货订单 */
        public static final String PAID_ORDER_WITH_BUYER = "/api/order/paid_buyer";
        /* 买家查看已发货未收货订单 */
        public static final String SENT_ORDER_WITH_BUYER = "/api/order/sent_buyer";
        /* 买家查看已经交易完成的订单 */
        public static final String FINISHED_ORDER_WITH_BUYER = "/api/order/finished_buyer";
        /* 将订单状态改为已发货 */
        public static final String SENT_STATUS = "/api/order/status_sent";
        /* 将订单状态改为已收货 */
        public static final String RECEIVED = "/api/order/status_received";
        /* 买家取消订单 */
        public static final String CANCEL = "/api/order/status_cancel";
    }

    public static class AddressInfo {
        /* 获取商家所有地址信息 */
        public static final String GET_USER_ADDRESS_INFO = "/api/address/get";
        /* 新建一条地址信息 */
        public static final String INSERT_USER_ADDRESS_INFO = "/api/address/insert";
        /* 更新地址信息 */
        public static final String UPDATE_USER_ADDRESS_INFO = "/api/address/update";
        /* 删除地址信息 */
        public static final String DELETE_USER_ADDRESS_INFO = "/api/address/delete";
        /* 重新设置当前收货地址 */
        public static final String RESET_SELECTED_ADDRESS = "/api/address/reset";
        /* 获取商家设置的收货地址 */
        public static final String DEFAULT_INFO = "/api/address/default";
    }

    public static class Cart {
        /* 获取商家购物车数据 */
        public static final String GET = "/api/cart/get";
        /* 往购物车添加一个商品 */
        public static final String ADD = "/api/cart/add";
        /* 删除购物车的一个商品 */
        public static final String DELETE = "/api/cart/delete";
        /* 批量删除购物车中的商品 */
        public static final String DELETE_CART = "/api/cart/delete_all";
    }

    public static class Trade {
        /* 支付宝接口 */
        public static final String PAY = "/api/trade/pay";
    }
}