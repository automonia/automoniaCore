package com.automonia.core.bean;


import com.automonia.core.base.BaseDao;
import com.automonia.core.base.exception.*;
import com.automonia.core.base.model.WTModel;
import com.automonia.core.base.query.WTQuery;
import com.automonia.core.utils.ReflectUtils;
import com.automonia.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

/**
 * Created by wenteng on
 * 2017/9/2.
 */
@Repository
public class BaseDaoImpl implements BaseDao {

    /**
     * sql内容的解析
     */
    @Autowired
    private DataBaseFactory dataBaseFactory;

    /**
     * spring的jdbc模块
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////


    @Override
    public <M extends WTModel> List<M> selectPageList(WTQuery query, Class<M> modelClass) throws EntityWithoutTableAnnotationException, WithoutEntityAnnotationException, EntityFieldWithoutColumnAnnotationException, ClassNotFoundException, NotSupportedTypeException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, NotSupportedEnumException, IllegalAccessException, BusinessException, ParameterIsNullException {
        return selectPageList(null, query, modelClass);
    }

    @Override
    public <M extends WTModel> List<M> selectPageList(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws EntityWithoutTableAnnotationException, WithoutEntityAnnotationException, EntityFieldWithoutColumnAnnotationException, ClassNotFoundException, NotSupportedTypeException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, NotSupportedEnumException, IllegalAccessException, ParameterIsNullException, BusinessException {
        return executeSqlMappingModelList(dataBaseFactory.getSqlOfSelectPage(fieldNames, query, modelClass), modelClass);
    }

    @Override
    public <M extends WTModel> void increment(String fieldName, WTQuery query, Class<M> modelClass) throws ClassNotFoundException, WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, ParameterIsNullException, BusinessException {
        executeSql(dataBaseFactory.getIncrementOrDecrementSql(fieldName, query, modelClass, true));
    }

    @Override
    public <M extends WTModel> void decrement(String fieldName, WTQuery query, Class<M> modelClass) throws ClassNotFoundException, WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, ParameterIsNullException, BusinessException {
        executeSql(dataBaseFactory.getIncrementOrDecrementSql(fieldName, query, modelClass, false));
    }

    @Override
    public <M extends WTModel> List<M> selectList(WTQuery query, Class<M> modelClass) throws EntityWithoutTableAnnotationException, WithoutEntityAnnotationException, EntityFieldWithoutColumnAnnotationException, NoSuchFieldException, NotSupportedTypeException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, NotSupportedEnumException, IllegalAccessException, ParameterIsNullException, BusinessException {
        return executeSqlMappingModelList(dataBaseFactory.getSqlOfSelectList(query, modelClass), modelClass);
    }

    @Override
    public <M extends WTModel> List<M> selectList(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws EntityWithoutTableAnnotationException, WithoutEntityAnnotationException, EntityFieldWithoutColumnAnnotationException, NoSuchFieldException, NotSupportedTypeException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, NotSupportedEnumException, IllegalAccessException, ParameterIsNullException, BusinessException {
        return executeSqlMappingModelList(dataBaseFactory.getSqlOfSelectList(fieldNames, query, modelClass), modelClass);
    }


    public <M extends WTModel> Long selectCount(WTQuery query, Class<M> modelClass) throws ClassNotFoundException, WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, ParameterIsNullException, BusinessException {
        String sqlValue = dataBaseFactory.getSqlOfSelectCount(query, modelClass);
        if (StringUtils.isEmpty(sqlValue)) {
            throw new ParameterIsNullException("可执行的sql语句为空");
        }
        return jdbcTemplate.queryForObject(sqlValue, Long.class);
    }

    public <M extends WTModel> M selectOne(WTQuery query, Class<M> modelClass) throws EntityWithoutTableAnnotationException, WithoutEntityAnnotationException, EntityFieldWithoutColumnAnnotationException, ClassNotFoundException, NotSupportedTypeException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, NotSupportedEnumException, IllegalAccessException, ParameterIsNullException, BusinessException {
        return executeSqlMappingModelOne(dataBaseFactory.getSqlOfSelectOne(null, query, modelClass), modelClass);
    }

    public <M extends WTModel> M selectOne(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws EntityWithoutTableAnnotationException, WithoutEntityAnnotationException, EntityFieldWithoutColumnAnnotationException, ClassNotFoundException, NotSupportedTypeException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, NotSupportedEnumException, IllegalAccessException, ParameterIsNullException, BusinessException {
        return executeSqlMappingModelOne(dataBaseFactory.getSqlOfSelectOne(fieldNames, query, modelClass), modelClass);
    }

    public void update(WTQuery query, WTModel model) throws ClassNotFoundException, WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, ParameterIsNullException, BusinessException {
        // 每次更新操作都更新updateDate
        model.setUpdateDate(new Date());

        executeSql(dataBaseFactory.getSqlOfUpdate(query, model));
    }


    public <M extends WTModel> String save(M model) throws ClassNotFoundException, WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, ParameterIsNullException, BusinessException {
        if (model == null) {
            return null;
        }
        // 新增
        if (StringUtils.isEmpty(model.getId())) {
            return create(model);
        }
        // 更新
        else {
            update(new WTQuery(model.getId()), model);
            return model.getId();
        }
    }


    public <M extends WTModel> String create(M model) throws WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, BusinessException, ParameterIsNullException, ClassNotFoundException {
        if (model == null) {
            return null;
        }
        // 代码创建uuid设置给保存对象的主键。
        model.setId(StringUtils.generateUUID());

        return createWithId(model);
    }

    public <M extends WTModel> String createWithId(M model) throws WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, BusinessException, ParameterIsNullException, ClassNotFoundException {
        if (model == null) {
            return null;
        }

        model.setCreateDate(new Date());
        model.setUpdateDate(new Date());

        executeSql(dataBaseFactory.getSqlOfInsert(model));

        return model.getId();
    }

    public <M extends WTModel, Q extends WTQuery> void realDelete(Q query, Class<M> modelClass) throws ClassNotFoundException, WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, ParameterIsNullException, BusinessException {
        executeSql(dataBaseFactory.getSqlOfDelete(query, modelClass));
    }


    /**
     * 执行sql并将其结果集实例化成modelClass对象集合
     *
     * @param sqlValue   可执行的sql语句
     * @param modelClass 数据模型对象
     * @param <M>
     * @return
     */
    private <M extends WTModel> List<M> executeSqlMappingModelList(String sqlValue, Class<M> modelClass) throws ParameterIsNullException, NoSuchMethodException, IllegalAccessException, InstantiationException, NotSupportedTypeException, NotSupportedEnumException, InvocationTargetException {
        if (StringUtils.isEmpty(sqlValue)) {
            throw new ParameterIsNullException("可执行的sql语句");
        }
        if (modelClass == null) {
            throw new ParameterIsNullException("未指定数据模型类");
        }

        return ReflectUtils.singleton.instantObjectList(jdbcTemplate.queryForList(sqlValue), modelClass);
    }

    private <M extends WTModel> M executeSqlMappingModelOne(String sqlValue, Class<M> modelClass) throws ParameterIsNullException, NoSuchMethodException, IllegalAccessException, InstantiationException, NotSupportedTypeException, NotSupportedEnumException, InvocationTargetException {
        if (StringUtils.isEmpty(sqlValue)) {
            throw new ParameterIsNullException("可执行的sql语句");
        }
        if (modelClass == null) {
            throw new ParameterIsNullException("未指定数据模型类");
        }

        return ReflectUtils.singleton.instantObject(jdbcTemplate.queryForMap(sqlValue), modelClass);
    }


    private void executeSql(String sqlValue) throws ParameterIsNullException {
        if (StringUtils.isEmpty(sqlValue)) {
            throw new ParameterIsNullException("可执行的sql语句");
        }
        jdbcTemplate.update(sqlValue);
    }


}
