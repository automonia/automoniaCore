package com.automonia.core.utils.datas;


import com.automonia.core.base.BaseEnum;

/**
 * @作者 温腾
 * @创建时间 2018年05月08日 下午4:51
 */
public enum ParameterType implements BaseEnum {

    json(1, "json"),

    xml(2, "xml");


    private Integer value;

    private String message;

    ParameterType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
