package com.automonia.dialect.model;

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
public interface WTModel {


    //
    void setId(String id);

    String getId();
}
