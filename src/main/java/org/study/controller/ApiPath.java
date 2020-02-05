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
    }

    public static class LoadingFile {
        public static final String UPLOAD = "/file/upload";
    }

    public static class Order {
        public static final String CREATE = "/order/create";
        public static final String QUERY_BY_USER_ID = "/order/query_user";
        public static final String QUERY_BY_ORDER_ID = "/order/query_id";
        public static final String QUERY_BY_STATUS = "/order/query_status";
        public static final String TRADE_PAY = "/order/trade_pay";
        public static final String PAID_ORDER_WITH_SELLER = "/order/paid_seller";
        public static final String SENT_ORDER_WITH_SELLER = "/order/sent_seller";
        public static final String FINISHED_ORDER_WITH_SELLER = "/order/finished_seller";
    }
}