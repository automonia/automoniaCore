package com.automonia.core.model;


import com.automonia.core.annotation.entity.WTColumn;
import com.automonia.core.annotation.entity.WTTable;
import com.automonia.core.exception.WTException;
import com.automonia.core.tools.LogUtils;
import com.automonia.core.tools.ReflectUtils;
import com.automonia.core.tools.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于缓存的实体数据库信息
 *
 * @作者 温腾
 * @创建时间 2018年12月15日 01:13
 */
public class WTEntityClassInfo {

    /*
    实体类的Class对象
     */
    private Class<? extends WTModel> entityClass;

    /*
    实体类映射的数据库名称
     */
    private String tableName;

    /*
    实体类的属性映射到字段关系的集合
     */
    private Map<String, String> fieldMappingColumnMap = new HashMap<>();

    public WTEntityClassInfo() {
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * 根据实体类的class对象建立该实体类的数据库映射信息
     *
     * @param entityClass 实体类的class对象
     * @param <E>         范性
     * @return 实体类的数据库映射信息对象
     * @throws WTException
     */
    public static <E extends WTModel> WTEntityClassInfo factory(Class<E> entityClass) throws WTException {
        if (entityClass == null) {
            return null;
        }
        WTEntityClassInfo entityClassInfo = new WTEntityClassInfo();
        entityClassInfo.setEntityClass(entityClass);

        WTTable tableAnnotation = entityClass.getAnnotation(WTTable.class);
        if (tableAnnotation == null || StringUtils.singleton.isEmpty(tableAnnotation.name())) {
            throw new WTException("实体类:" + entityClass.getSimpleName() + "未指定映射的数据库名");
        }
        entityClassInfo.setTableName(tableAnnotation.name());

        entityClassInfo.buildFieldMappingToColumnInfo();

        return entityClassInfo;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * 解析实体类的属性，建立列的信息
     *
     * @throws WTException
     */
    private void buildFieldMappingToColumnInfo() throws WTException {

        // 检索entityClass自身类，包括继承的WTModel的属性
        for (Field fieldItem : ReflectUtils.singleton.getFieldsWithInherit(entityClass)) {
            WTColumn columnAnnotation = fieldItem.getAnnotation(WTColumn.class);

            // columnAnnotation为空
            if (columnAnnotation == null || StringUtils.singleton.isEmpty(columnAnnotation.name())) {
                LogUtils.singleton.error("实体类的属性" + fieldItem.getName() + "未使用@WTColumn指定映射的数据库字段");
            }
            // columnAnnotation不为空
            else {
                fieldMappingColumnMap.put(fieldItem.getName(), columnAnnotation.name());
            }
        }
    }

    public Class<? extends WTModel> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<? extends WTModel> entityClass) {
        this.entityClass = entityClass;
    }

    public Map<String, String> getFieldMappingColumnMap() {
        return fieldMappingColumnMap;
    }

    public void setFieldMappingColumnMap(Map<String, String> fieldMappingColumnMap) {
        this.fieldMappingColumnMap = fieldMappingColumnMap;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
