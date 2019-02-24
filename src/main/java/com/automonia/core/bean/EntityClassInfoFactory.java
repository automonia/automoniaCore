package com.automonia.core.bean;


import com.automonia.core.exception.ParameterIsNullException;
import com.automonia.core.exception.WTException;
import com.automonia.core.model.WTEntityClassInfo;
import com.automonia.core.model.WTModel;
import com.automonia.core.tools.LogUtils;
import com.automonia.core.tools.StringUtils;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 该类只在DataFactory中创建一次
 * 与实体类相关的所有操作都由这个类负责
 * <p>
 * initEntityClassInfoMap函数在系统初始化后调用。
 *
 * @作者 温腾
 * @创建时间 2018年12月15日 01:21
 */
@Component
public class EntityClassInfoFactory {

    // 实体类存放的目录
    @Value("${automonia.entity.path}")
    private String entityPath;


    public String getEntityPath() {
        return entityPath;
    }

    ////////////////////////////////////////////////////////////////////////////////////

    /*
    缓存系统使用到的实体类的信息
    数据模型类的Class对象的simpleName作为key。实体类信息对象作为value
     */
    private final Map<String, WTEntityClassInfo> modelClassInfoMap = new HashMap<>();


    ////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////


    /**
     * 该函数需要在系统启动后调用
     * 初始化建立实体类映射信息
     */
    public void initEntityClassInfoMap() throws WTException {
        if (StringUtils.singleton.isEmpty(getEntityPath())) {
            LogUtils.singleton.error("系统未设置实体类目录");
            return;
        }
        // 对所有类进行遍历，并找出继承了WTModel类对实体类
        Reflections reflections = new Reflections(getEntityPath());

        for (Class<? extends WTModel> entityClass : reflections.getSubTypesOf(WTModel.class)) {
            LogUtils.singleton.info("实体类 = " + entityClass.getSimpleName());
            modelClassInfoMap.put(entityClass.getSimpleName(), WTEntityClassInfo.factory(entityClass));
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////


    /**
     * 获取数据模型类的实体类信息对象
     *
     * @param modelClass 数据模型类
     * @return 实体类信息
     * @throws ParameterIsNullException
     */
    public <M extends WTModel> WTEntityClassInfo getEntityClassInfo(Class<M> modelClass) throws WTException {
        if (modelClass == null) {
            throw new ParameterIsNullException("数据模型类为空，无法获取其实体类");
        }

//        /*
//        如果modelClassInfoMap缓存了modelClass对应实体类的信息，那么直接返回
//         */
//        if (!modelClassInfoMap.containsKey(modelClass.getSimpleName())) {
//            /*
//            通过继承关系找到实体类，并将其添加到modelClassInfoMap中缓存。
//             */
//            Class classValue = modelClass;
//            while (!classValue.getSuperclass().isAssignableFrom(WTModel.class)) {
//                classValue = classValue.getSuperclass();
//            }
//
//            modelClassInfoMap.put(modelClass.getSimpleName(), WTEntityClassInfo.factory(classValue));
//        }

        return modelClassInfoMap.get(modelClass.getSimpleName());
    }


    /**
     * 从modelClassInfoMap获取entityName代表的实体类的信息，并从中获取fieldName映射的数据库字段
     * <p>
     * modelClassInfoMap不一定含有entityName实体类信息的
     *
     * @param entityName 实体类名称
     * @param fieldName  属性名称
     * @throws ParameterIsNullException
     * @throws ClassNotFoundException
     */
    public String getColumnName(String entityName, String fieldName) throws WTException, ClassNotFoundException {
        if (StringUtils.singleton.isEmptyOr(entityName, fieldName)) {
            throw new ParameterIsNullException("实体类名，属性名为空");
        }

//        /*
//        modelClassInfoMap缓存了entityName实体类信息，从中获取fieldName映射的数据库字段
//         */
//        if (!modelClassInfoMap.containsKey(entityName)) {
//            modelClassInfoMap.put(entityName, WTEntityClassInfo.factory(loadEntityClass(entityName)));
//        }

        return modelClassInfoMap.get(entityName).getFieldMappingColumnMap().get(fieldName);
    }


    public String getTableName(String entityName) throws WTException, ClassNotFoundException {
        if (StringUtils.singleton.isEmpty(entityName)) {
            throw null;
        }

//        // 如果modelClassInfoMap未包含entityName的信息，那么将其添加进去
//        if (!modelClassInfoMap.containsKey(entityName)) {
//            modelClassInfoMap.put(entityName, WTEntityClassInfo.factory(loadEntityClass(entityName)));
//        }

        return modelClassInfoMap.get(entityName).getTableName();
    }


    /**
     * 加载Class对象，
     *
     * @param entityName 实体类名称，非空参数
     */
    private Class loadEntityClass(String entityName) throws ClassNotFoundException {
        return Class.forName(getEntityPath() + "." + entityName);
    }


}

























