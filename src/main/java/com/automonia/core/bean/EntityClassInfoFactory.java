package com.automonia.core.bean;


import com.automonia.core.base.exception.BusinessException;
import com.automonia.core.base.exception.EntityWithoutTableAnnotationException;
import com.automonia.core.base.exception.ParameterIsNullException;
import com.automonia.core.base.exception.WithoutEntityAnnotationException;
import com.automonia.core.base.model.WTEntityClassInfo;
import com.automonia.core.base.model.WTModel;
import com.automonia.core.utils.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 该类只在DataFactory中创建一次
 * 与实体类相关的所有操作都由这个类负责
 *
 * @作者 温腾
 * @创建时间 2018年12月15日 01:21
 */
@Component
public class EntityClassInfoFactory {

    /*
    缓存系统使用到的实体类的信息

    数据模型类的Class对象的simpleName作为key。实体类信息对象作为value
     */
    private Map<String, WTEntityClassInfo> modelClassInfoMap = new HashMap<>();

    private final Class<WTModel> wtModelClass = WTModel.class;


    /**
     * 获取数据模型类的实体类
     *
     * @param <M>
     * @param modelClass 数据模型类
     * @return
     * @throws WithoutEntityAnnotationException
     */
    public <M extends WTModel, E extends WTModel> Class<E> getEntityClass(Class<M> modelClass) throws WithoutEntityAnnotationException, ParameterIsNullException, BusinessException, EntityWithoutTableAnnotationException {
        if (modelClass == null) {
            throw new ParameterIsNullException("数据模型类为空，无法获取其实体类");
        }

        /*
        如果modelClassInfoMap缓存了modelClass对应实体类的信息，那么直接返回
         */
        if (modelClassInfoMap.containsKey(modelClass.getSimpleName())) {
            return (Class<E>) modelClassInfoMap.get(modelClass.getSimpleName()).getEntityClass();
        }

        /*
        通过继承关系找到实体类，并将其添加到modelClassInfoMap中缓存。
         */
        Class classValue = modelClass;
        while (!classValue.getSuperclass().isAssignableFrom(wtModelClass)) {
            classValue = classValue.getSuperclass();
        }

        WTEntityClassInfo entityClassInfo = WTEntityClassInfo.factory(classValue);
        modelClassInfoMap.put(modelClass.getSimpleName(), entityClassInfo);

        /*
        如果modelClass是实体扩展类，那么也存储其实体类名称作为key的映射
         */
        if (classValue != modelClass) {
            modelClassInfoMap.put(classValue.getSimpleName(), entityClassInfo);
        }

        return (Class<E>) classValue;
    }


    /**
     * 从modelClassInfoMap获取entityName代表的实体类的信息，并从中获取fieldName映射的数据库字段
     * <p>
     * modelClassInfoMap不一定含有entityName实体类信息的
     *
     * @param entityName 实体类名称
     * @param fieldName  属性名称
     * @return
     * @throws ParameterIsNullException
     * @throws BusinessException
     * @throws ClassNotFoundException
     */
    public String getColumnName(String entityName, String fieldName) throws ParameterIsNullException, BusinessException, ClassNotFoundException, EntityWithoutTableAnnotationException {
        if (StringUtils.isEmptyOr(entityName, fieldName)) {
            throw new ParameterIsNullException("实体类名，属性名为空");
        }

        /*
        modelClassInfoMap缓存了entityName实体类信息，从中获取fieldName映射的数据库字段
         */
        if (!modelClassInfoMap.containsKey(entityName)) {
            modelClassInfoMap.put(entityName, WTEntityClassInfo.factory(loadEntityClass(entityName)));
        }

        return getColumnNameFromModelClassInfoMap(entityName, fieldName);
    }

//    public <E extends WTModel> String getColumnName(Class<E> entityClass, String fieldName) throws ParameterIsNullException, BusinessException, EntityWithoutTableAnnotationException {
//        if (entityClass == null || StringUtils.isEmpty(fieldName)) {
//            throw new ParameterIsNullException("实体类为空，属性名为空");
//        }
//        String entityName = entityClass.getSimpleName();
//        if (!modelClassInfoMap.containsKey(entityName)) {
//            modelClassInfoMap.put(entityName, WTEntityClassInfo.factory(entityClass));
//        }
//
//        return getColumnNameFromModelClassInfoMap(entityName, fieldName);
//    }


    /**
     * 从modelClassInfoMap获取entityName代表的实体类的信息，并从中获取fieldName映射的数据库字段
     * <p>
     * modelClassInfoMap是含有entityName实体类信息的
     *
     * @param entityName 实体类名称
     * @param fieldName  属性名称
     * @return
     */
    private String getColumnNameFromModelClassInfoMap(String entityName, String fieldName) throws BusinessException {
        String columnName = modelClassInfoMap.get(entityName).getFieldMappingColumnMap().get(fieldName);
        if (StringUtils.isEmpty(columnName)) {
            throw new BusinessException("属性:" + fieldName + "未能找到其数据库映射字段，请检查");
        }
        return columnName;
    }


    public Set<String> getFieldCollection(String entityName) throws ParameterIsNullException, ClassNotFoundException, BusinessException, EntityWithoutTableAnnotationException {
        if (StringUtils.isEmpty(entityName)) {
            throw new ParameterIsNullException("实体类名称为空");
        }

        if (!modelClassInfoMap.containsKey(entityName)) {
            modelClassInfoMap.put(entityName, WTEntityClassInfo.factory(loadEntityClass(entityName)));
        }

        return modelClassInfoMap.get(entityName).getFieldMappingColumnMap().keySet();
    }


    public String getTableName(String entityName) throws ParameterIsNullException, ClassNotFoundException, EntityWithoutTableAnnotationException, BusinessException {
        if (StringUtils.isEmpty(entityName)) {
            throw new ParameterIsNullException("实体类为空，属性名为空");
        }

        if (!modelClassInfoMap.containsKey(entityName)) {
            modelClassInfoMap.put(entityName, WTEntityClassInfo.factory(loadEntityClass(entityName)));
        }

        return modelClassInfoMap.get(entityName).getTableName();
    }


    /**
     * 加载Class对象，
     *
     * @param entityName 实体类名称，非空参数
     * @return
     */
    private Class loadEntityClass(String entityName) throws ClassNotFoundException {
        return Class.forName("com.corleone.model.entity." + entityName);
    }

//    private <E extends WTModel> void buildEntityClassInfo(Class<E> entityClass) throws EntityWithoutTableAnnotationException, BusinessException, ParameterIsNullException {
//        modelClassInfoMap.put(entityClass.getSimpleName(), WTEntityClassInfo.factory(entityClass));
//    }

}

























