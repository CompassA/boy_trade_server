package org.study.aspects.annotation;

import org.study.service.model.UserModel;

/**
 * @author Tomato
 * Created on 2022.12.31
 */
public class UserInfoContext {

    public static final ThreadLocal<UserModel> USER_MODEL_THREAD_LOCAL = new ThreadLocal<>();

    public static UserModel get() {
        return USER_MODEL_THREAD_LOCAL.get();
    }

    public static void set(UserModel userModel) {
        USER_MODEL_THREAD_LOCAL.set(userModel);
    }

    public static void remove() {
        USER_MODEL_THREAD_LOCAL.remove();
    }
}
