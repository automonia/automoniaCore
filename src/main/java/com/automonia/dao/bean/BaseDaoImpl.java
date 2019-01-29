package com.automonia.dao.bean;


import com.automonia.dao.BaseDao;
import com.automonia.dialect.DataBaseFactory;
import com.automonia.dialect.model.WTModel;
import com.automonia.dialect.query.WTQuery;
import com.automonia.tools.LogUtils;
import com.automonia.tools.ReflectUtils;
import com.automonia.tools.exception.ParameterIsNullException;
import com.automonia.tools.exception.WTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wenteng on
 * 2017/9/2.
 */
@Repository
public class BaseDaoImpl implements BaseDao {

    /**
     * sql内容的解析
     */
    private DataBaseFactory dataBaseFactory = DataBaseFactory.singleton;

    /**
     * spring的jdbc模块
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////


    @Override
    public <M extends WTModel> List<M> selectPageList(WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, WTException {
        return selectPageList(null, query, modelClass);
    }

    @Override
    public <M extends WTModel> List<M> selectPageList(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, WTException {
        return executeSqlMappingModelList(dataBaseFactory.getSqlOfSelectPage(fieldNames, query, modelClass), modelClass);
    }

    @Override
    public <M extends WTModel> void increment(String fieldName, WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, WTException {
        executeSql(dataBaseFactory.getIncrementOrDecrementSql(fieldName, query, modelClass, true));
    }

    @Override
    public <M extends WTModel> void decrement(String fieldName, WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, WTException {
        executeSql(dataBaseFactory.getIncrementOrDecrementSql(fieldName, query, modelClass, false));
    }

    @Override
    public <M extends WTModel> List<M> selectList(WTQuery query, Class<M> modelClass) throws NoSuchFieldException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, WTException {
        return executeSqlMappingModelList(dataBaseFactory.getSqlOfSelectList(query, modelClass), modelClass);
    }

    @Override
    public <M extends WTModel> List<M> selectList(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws NoSuchFieldException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, WTException {
        return executeSqlMappingModelList(dataBaseFactory.getSqlOfSelectList(fieldNames, query, modelClass), modelClass);
    }


    public <M extends WTModel> Long selectCount(WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, WTException {
        String sqlValue = dataBaseFactory.getSqlOfSelectCount(query, modelClass);
        if (StringUtils.isEmpty(sqlValue)) {
            throw new ParameterIsNullException("可执行的sql语句为空");
        }

        LogUtils.singleton.info("sqlValue = " + sqlValue);

        return jdbcTemplate.queryForObject(sqlValue, Long.class);
    }

    public <M extends WTModel> M selectOne(WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, WTException {
        return executeSqlMappingModelOne(dataBaseFactory.getSqlOfSelectOne(null, query, modelClass), modelClass);
    }

    public <M extends WTModel> M selectOne(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, WTException {
        return executeSqlMappingModelOne(dataBaseFactory.getSqlOfSelectOne(fieldNames, query, modelClass), modelClass);
    }

    public void update(WTQuery query, WTModel model) throws ClassNotFoundException, NoSuchFieldException, WTException {

        // 每次更新操作都更新updateDate
//        setUpdateDate(model);

        executeSql(dataBaseFactory.getSqlOfUpdate(query, model));
    }

    @Override
    public void update(String sql) throws ParameterIsNullException {
        executeSql(sql);
    }

    public <M extends WTModel> String save(M model) throws ClassNotFoundException, NoSuchFieldException, WTException {
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


    public <M extends WTModel> String create(M model) throws NoSuchFieldException, WTException, ClassNotFoundException {
        if (model == null) {
            return null;
        }
        // 代码创建uuid设置给保存对象的主键。
        model.setId(com.automonia.tools.StringUtils.singleton.generateUuid());

        return createWithId(model);
    }

    public <M extends WTModel> String createWithId(M model) throws NoSuchFieldException, WTException, ClassNotFoundException {
        if (model == null) {
            return null;
        }

//        setUpdateDate(model);
//        setCreateDate(model);

        executeSql(dataBaseFactory.getSqlOfInsert(model));

        return model.getId();
    }

    public <M extends WTModel, Q extends WTQuery> void realDelete(Q query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, WTException {
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
    private <M extends WTModel> List<M> executeSqlMappingModelList(String sqlValue, Class<M> modelClass) throws ParameterIsNullException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        if (StringUtils.isEmpty(sqlValue)) {
            throw new ParameterIsNullException("可执行的sql语句");
        }
        if (modelClass == null) {
            throw new ParameterIsNullException("未指定数据模型类");
        }

        LogUtils.singleton.info("sqlValue = " + sqlValue);

        return ReflectUtils.singleton.instanceObjectList(jdbcTemplate.queryForList(sqlValue), modelClass);
    }

    private <M extends WTModel> M executeSqlMappingModelOne(String sqlValue, Class<M> modelClass) throws ParameterIsNullException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        if (StringUtils.isEmpty(sqlValue)) {
            throw new ParameterIsNullException("可执行的sql语句");
        }
        if (modelClass == null) {
            throw new ParameterIsNullException("未指定数据模型类");
        }

        LogUtils.singleton.info("sqlValue = " + sqlValue);

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sqlValue);
        if (results.isEmpty() || results.size() <= 0) {
            return null;
        }

        return ReflectUtils.singleton.instanceObject(results.get(0), modelClass);
    }


    private void executeSql(String sqlValue) throws ParameterIsNullException {
        if (StringUtils.isEmpty(sqlValue)) {
            throw new ParameterIsNullException("可执行的sql语句");
        }

        LogUtils.singleton.info("sqlValue = " + sqlValue);

        jdbcTemplate.update(sqlValue);
    }

    private void setCreateDate(WTModel model) {
        try {
            Method setCreateDateMethod = model.getClass().getMethod("setCreateDate", Date.class);
            setCreateDateMethod.invoke(model, new Date());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void setUpdateDate(WTModel model) {
        try {
            Method setUpdateDateMethod = model.getClass().getMethod("setUpdateDate", Date.class);
            setUpdateDateMethod.invoke(model, new Date());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
