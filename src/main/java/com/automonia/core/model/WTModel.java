package com.automonia.core.model;


import com.automonia.core.annotation.entity.WTColumn;

import java.util.Date;

/**
 * 实体对象的基本的接口
 * 对象实体对象要求有这么几个基本的属性
 * <p>
 * id               做主键，统一的名称
 * createDate       记录的创建时间
 * <p>
 * Created by wenteng on
 * 2017/8/20.
 */
public class WTModel {

    /**
     * 主键ID
     */
    @WTColumn(name = "id")
    private String id;

    /**
     * 记录的创建时间
     */
    @WTColumn(name = "create_date")
    private Date createDate;


    /**
     * 最新的修改时间
     */
    @WTColumn(name = "update_date")
    private Date updateDate;

    //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WTModel)) return false;

        WTModel wtModel = (WTModel) o;

        return getId().equals(wtModel.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }


    ///

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
