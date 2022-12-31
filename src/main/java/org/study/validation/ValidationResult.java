package org.study.validation;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author fanqie
 * Created on 2020/1/11
 */
public class ValidationResult {

    private boolean hasErrors = false;

    private final Map<String, String> errMsgMap = Maps.newHashMap();

    public void addErrMsg(final String propertyName, final String errMsg) {
        errMsgMap.put(propertyName, errMsg);
    }

    public boolean hasErrors() {
        return hasErrors;
    }

    public void setHasErrorsTrue() {
        hasErrors = true;
    }

    public String getErrorMsg() {
        return Joiner.on("")
                .skipNulls()
                .join(errMsgMap.values());
    }
}
