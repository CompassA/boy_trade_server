package org.study.util;

import org.study.model.UserModel;
import org.study.view.UserVO;

import java.util.Objects;
import java.util.Optional;

/**
 * @author fanqie
 * @date 2020/1/4
 */
public final class ModelToViewUtil {

    private ModelToViewUtil() {
    }

    public static Optional<UserVO> getUserVO(final UserModel userModel) {
        if (Objects.isNull(userModel)) {
            return Optional.empty();
        }
        return Optional.of(new UserVO()
                .setAccount(userModel.getAccount())
                .setName(userModel.getName())
                .setUserId(userModel.getUserId())
                .setIconUrl(userModel.getIconUrl()));
    }
}
