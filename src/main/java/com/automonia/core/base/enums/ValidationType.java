package com.automonia.core.base.enums;


import com.automonia.core.base.BaseEnum;

/**
 * 默认是 - 有效的
 *
 * @作者 温腾
 * @创建时间 2018年04月09日 下午7:27
 */
public enum ValidationType implements BaseEnum {

    valid(0, "有效"),

    invalid(1, "无效");

    private Integer value;

    private String message;

    ValidationType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
