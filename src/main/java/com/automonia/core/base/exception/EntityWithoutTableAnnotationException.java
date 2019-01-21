package com.automonia.core.base.exception;

/**
 * @作者 温腾
 * @创建时间 2018年04月08日 下午11:09
 */
public class EntityWithoutTableAnnotationException extends WTException {
    public EntityWithoutTableAnnotationException(String message) {
        super(message);
    }
}
