package com.automonia.dialect.query;


import com.automonia.dialect.annotation.query.WTSearch;
import com.automonia.dialect.model.WTModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询对象类型
 *
 * @author 作者 温腾
 * @创建时间 2017年5月9日 上午12:10:08
 */
public class WTQuery {

    @WTSearch
    private String id;

    /**
     * 排序属性集合
     */
    private List<WTOrder> orderList;

    /**
     * 分组
     */
    private WTGroupBy groupBy;

    /**
     * 分页的初始化页码为 1
     */
    private Integer pageNo = 1;

    /**
     * 分页情况的每页的大小
     */
    private Integer pageSize = 15;


    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 根据排序的属性名称添加排序对象
     * 默认采用倒序
     *
     * @param fieldName 排序属性名称
     */
    public void addOrder(String fieldName) {
        addOrder(fieldName, OrderType.desc);
    }

    /**
     * 根据排序的属性名称和排序类型添加排序对象
     *
     * @param fieldName 排序属性名称
     * @param orderType 排序类型
     */
    public <M extends WTModel> void addOrder(String fieldName, OrderType orderType) {
        WTOrder order = new WTOrder();
        order.setFieldName(fieldName);
        order.setOrderType(orderType);

        addOrder(order);
    }


    /**
     * 添加排序对象
     *
     * @param order 排序对象
     */
    public void addOrder(WTOrder order) {
        if (getOrderList() == null) {
            setOrderList(new ArrayList<WTOrder>());
        }
        getOrderList().add(order);
    }

    public <M extends WTModel> void addGroupBy(String fieldName) {
        WTGroupBy groupBy = new WTGroupBy();
        groupBy.setFieldName(fieldName);

        setGroupBy(groupBy);
    }

    public WTQuery() {
    }

    /**
     * 使用主键创建查询对象
     *
     * @param id
     */
    public WTQuery(String id) {
        this.id = id;
    }


    //////////////////////////////////////////////////////////////////////


    public List<WTOrder> getOrderList() {
        return orderList;
    }

    public WTQuery setOrderList(List<WTOrder> orderList) {
        this.orderList = orderList;
        return this;
    }

    public String getId() {
        return id;
    }

    public WTQuery setId(String id) {
        this.id = id;
        return this;
    }

    public WTGroupBy getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(WTGroupBy groupBy) {
        this.groupBy = groupBy;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public WTQuery setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

}
