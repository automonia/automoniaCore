package com.automonia.core.base.exception;

/**
 * @作者 温腾
 * @创建时间 2018年04月08日 下午11:12
 */
public class EntityFieldWithoutColumnAnnotationException extends WTException {
    public EntityFieldWithoutColumnAnnotationException(String message) {
        super(message);
    }
}
