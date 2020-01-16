package org.study.controller;

/**
 * @author fanqie
 * @date 2020/1/5
 */
public class ApiPath {

    public static class User {
        public static final String LOGIN = "/user/login";
        public static final String REGISTRY = "/user/registry";
    }

    public static class Encrypt {
        public static final String PUB_KEY = "/encrypt/public";
    }

    public static class Product {
        public static final String CREATE = "/product/create";
    }
}
