package org.study.service.model.enumdata;

import lombok.Getter;

/**
 * @author fanqie
 * @date 2020/2/7
 */
@Getter
public enum Gender {

    /* 未知性别 */
    UNKNOWN((byte) 0, "未知"),
    MAN((byte) 1, "男"),
    WOMAN((byte) 2, "女"),
    ;

    private byte value;

    private String str;

    Gender(final byte value, final String str) {
        this.value = value;
        this.str = str;
    }
}
