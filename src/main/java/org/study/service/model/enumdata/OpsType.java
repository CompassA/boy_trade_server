package org.study.service.model.enumdata;

import lombok.Getter;

/**
 * @author fanqie
 * Created on 2020/4/29
 */
@Getter
public enum OpsType {
    CREATE_PRODUCT("p")
    ;

    private final String val;

    OpsType(final String val) {
        this.val = val;
    }
}
