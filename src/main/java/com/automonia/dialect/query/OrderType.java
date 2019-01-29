package com.automonia.dialect.query;

/**
 * Created by jackie on
 * 2017/11/7.
 */
public enum OrderType {

    desc("倒序"),

    asc("顺序");

    private String message;

    OrderType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
