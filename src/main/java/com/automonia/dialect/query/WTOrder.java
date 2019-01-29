package com.automonia.dialect.query;

/**
 * 排序查询的对象
 * Created by jackie on
 * 2017/11/7.
 */
public class WTOrder {

    /**
     * 实体对象的属性名称
     */
    private String fieldName;

    /**
     * 默认是倒序
     */
    private OrderType orderType = OrderType.desc;

    public WTOrder() {
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

}
