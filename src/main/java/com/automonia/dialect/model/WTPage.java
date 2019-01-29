package com.automonia.dialect.model;


/**
 * 统一的分页查询类
 * pageNo 从1开始计数
 * <p>
 * Created by wenteng on 2017/7/30.
 */
public class WTPage {

    /**
     * 列表分页的总页数
     */
    private Integer pageCount;

    /**
     * 页码
     */
    private Integer pageNo;

    /**
     * 数据总数
     */
    private Integer totalCount;

    /**
     * 一页大小
     */
    private Integer pageSize = 15;

    /**
     * 是否有下一页
     */
    private boolean hasNextPage;

    /**
     * 是否有上一页
     */
    private boolean hasPreviousPage;

    /**
     * 当前页面的数据的开始下标
     */
    private Integer startIndex;

    /**
     * 当前页面的数据的结束下标
     */
    private Integer endIndex;

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }
}
