package com.automonia.core.query;

/**
 * Created by jackie on
 * 2018/1/2.
 */
public class WTGroupBy {

    private String fieldName;

    public WTGroupBy(String fieldName) {
        this.fieldName = fieldName;
    }

    public WTGroupBy() {
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
