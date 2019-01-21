package com.automonia.core.base.model;

import java.util.Date;

/**
 * 实体对象的基本的接口
 * 对象实体对象要求有这么几个基本的属性
 * <p>
 * id               做主键，统一的名称
 * createDate       记录的创建时间
 * updateDate       记录的更新时间
 * <p>
 * Created by wenteng on
 * 2017/8/20.
 */
public interface WTModel {

    String getId();

    void setId(String id);

    Date getCreateDate();

    void setCreateDate(Date createDate);

    Date getUpdateDate();

    void setUpdateDate(Date updateDate);

}
