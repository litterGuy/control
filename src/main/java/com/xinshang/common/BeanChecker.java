package com.xinshang.common;

import com.xinshang.common.exception.ParameterException;

import java.math.BigDecimal;

public class BeanChecker {

    protected static void assertIsNull(Object object, String message) {
        if (object == null) {
            throw new ParameterException(message);
        }
    }

    protected static void assertIsEmpty(BigDecimal object, String message) {
        if (object == null || object.equals(BigDecimal.ZERO)) {
            throw new ParameterException(message);
        }
    }

    protected static void assertIsEmpty(String object, String message) {
        if (object == null || object.length() <= 0) {
            throw new ParameterException(message);
        }
    }
}
