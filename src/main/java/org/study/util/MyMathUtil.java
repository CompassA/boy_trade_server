package org.study.util;

import java.math.BigDecimal;

/**
 * @author fanqie
 * @date 2020/1/12
 */
public final class MyMathUtil {

    private MyMathUtil() {
    }

    public static boolean isZeroOrNegative(final BigDecimal num) {
        return num.doubleValue() - 0 <= 0.000001;
    }
}
