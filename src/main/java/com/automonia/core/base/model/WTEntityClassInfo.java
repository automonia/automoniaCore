package com.automonia.core.base.model;


import com.automonia.core.annotation.entity.WTColumn;
import com.automonia.core.annotation.entity.WTTable;
import com.automonia.core.base.exception.BusinessException;
import com.automonia.core.base.exception.ParameterIsNullException;

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
    private Class entityClass;

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

    public static <E extends WTModel> WTEntityClassInfo factory(Class<E> entityClass) throws ParameterIsNullException, BusinessException {
        if (entityClass == null) {
            throw new ParameterIsNullException("实体类的Class对象为空");
        }

        WTEntityClassInfo entityClassInfo = new WTEntityClassInfo();

        //
        entityClassInfo.setEntityClass(entityClass);

        //
        WTTable tableAnnotation = entityClass.getAnnotation(WTTable.class);
        if (tableAnnotation == null) {
            throw new BusinessException("实体类:" + entityClass.getSimpleName() + "未指定映射的数据库名");
        }
        entityClassInfo.setTableName(tableAnnotation.name());

        //
        entityClassInfo.buildFieldMappingToColumnInfo();

        return entityClassInfo;
    }

    private void buildFieldMappingToColumnInfo() throws BusinessException {
        for (Field fieldItem : entityClass.getDeclaredFields()) {
            WTColumn columnAnnotation = fieldItem.getAnnotation(WTColumn.class);
            if (columnAnnotation == null) {
                throw new BusinessException("实体类的属性" + fieldItem.getName() + "未使用@Column指定映射的数据库字段");
            }
            fieldMappingColumnMap.put(fieldItem.getName(), columnAnnotation.name());
        }
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class entityClass) {
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
