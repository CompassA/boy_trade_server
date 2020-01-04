package org.study.util;

import org.study.data.UserDO;
import org.study.data.UserPasswordDO;
import org.study.model.UserModel;

import java.util.Objects;
import java.util.Optional;

/**
 * @author fanqie
 * @date 2020/1/4
 */
public final class DataToModelUtil {

    private DataToModelUtil() {
    }

    public static Optional<UserModel> getUserModel(
            final UserDO userDO, final UserPasswordDO userPasswordDO) {
        if (Objects.isNull(userDO) || Objects.isNull(userPasswordDO)) {
            return Optional.empty();
        }
        return Optional.of(new UserModel()
                .setUserId(userDO.getUserId())
                .setAccount(userDO.getAccount())
                .setName(userDO.getName())
                .setIconUrl(userDO.getIconUrl())
                .setCreateTime(userDO.getCreateTime())
                .setUpdateTime(userDO.getUpdateTime())
                .setPassword(userPasswordDO.getPassword()));
    }
}
