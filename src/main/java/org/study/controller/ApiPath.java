package org.study.controller;

/**
 * @author fanqie
 * @date 2020/1/5
 */
public class ApiPath {

    public static class User {
        public static final String LOGIN = "/user/login";
        public static final String REGISTRY = "/user/registry";
        public static final String EXIST = "/user/exist";
        public static final String PART_INFO = "/user/part_info";
        public static final String SESSION_CHECKING = "/user/session_status";
        public static final String LOGOUT = "/user/logout";
    }

    public static class Encrypt {
        public static final String PUB_KEY = "/encrypt/public";
    }

    public static class Product {
        public static final String CREATE = "/product/create";
        public static final String INFO = "/product/info";
        public static final String DETAIL = "/product/detail";
        public static final String USER_PRODUCTS = "/product/list";
    }

    public static class LoadingFile {
        public static final String UPLOAD = "/file/upload";
    }

    public static class Order {
        public static final String CREATE = "/order/create";
        public static final String QUERY_BY_ORDER_ID = "/order/query_id";
        public static final String TRADE_PAY = "/order/trade_pay";
        public static final String CREATED_ORDER_WITH_SELLER = "/order/created_seller";
        public static final String PAID_ORDER_WITH_SELLER = "/order/paid_seller";
        public static final String SENT_ORDER_WITH_SELLER = "/order/sent_seller";
        public static final String FINISHED_ORDER_WITH_SELLER = "/order/finished_seller";
        public static final String CREATED_ORDER_WITH_BUYER = "/order/created_buyer";
        public static final String PAID_ORDER_WITH_BUYER = "/order/paid_buyer";
        public static final String SENT_ORDER_WITH_BUYER = "/order/sent_buyer";
        public static final String FINISHED_ORDER_WITH_BUYER = "/order/finished_buyer";
        public static final String SENT_STATUS = "/order/status_sent";
        public static final String RECEIVED = "/order/status_received";
        public static final String CANCEL = "/order/status_cancel";
    }

    public static class AddressInfo {
        public static final String GET_USER_ADDRESS_INFO = "/address/get";
        public static final String INSERT_USER_ADDRESS_INFO = "/address/insert";
        public static final String UPDATE_USER_ADDRESS_INFO = "/address/update";
        public static final String DELETE_USER_ADDRESS_INFO = "/address/delete";
        public static final String RESET_SELECTED_ADDRESS = "/address/reset";
        public static final String DEFAULT_INFO = "/address/default";
    }

    public static class Cart {
        public static final String GET = "/cart/get";
        public static final String ADD = "/cart/add";
        public static final String DELETE = "/cart/delete";
        public static final String DELETE_CART = "/cart/delete_all";
    }
}