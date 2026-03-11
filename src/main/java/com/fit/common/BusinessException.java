package com.fit.common;

/**
 * 业务异常：用于返回可预期的业务错误信息
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}

