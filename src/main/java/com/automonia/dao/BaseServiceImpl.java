package com.automonia.dao;

import com.automonia.dialect.model.WTModel;
import com.automonia.dialect.model.WTPageModel;
import com.automonia.dialect.query.WTQuery;
import com.automonia.tools.exception.ParameterIsNullException;
import com.automonia.tools.exception.WTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by wenteng on
 * 2017/9/2.
 */
public class BaseServiceImpl implements BaseService {

    @Autowired
    private BaseDao baseDao;


    public <M extends WTModel> WTPageModel<M> selectPageModel(WTQuery query, Class<M> modelClass) throws IllegalAccessException, NoSuchMethodException, InstantiationException, NoSuchFieldException, InvocationTargetException, ClassNotFoundException, WTException {
        return this.selectPageModel(null, query, modelClass);
    }

    @Override
    public <M extends WTModel> WTPageModel<M> selectPageModel(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws IllegalAccessException, NoSuchMethodException, NoSuchFieldException, InvocationTargetException, ClassNotFoundException, WTException, InstantiationException {
        List<M> datas = baseDao.selectPageList(fieldNames, query, modelClass);
        Integer totalCount = selectCount(query, modelClass);
        return new WTPageModel<M>().initPageModel(datas, totalCount, query.getPageNo(), query.getPageSize());
    }

    public <M extends WTModel> List<M> selectList(WTQuery query, Class<M> modelClass) throws NoSuchMethodException, NoSuchFieldException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, WTException {
        return baseDao.selectList(query, modelClass);
    }

    public <M extends WTModel> List<M> selectList(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws NoSuchMethodException, NoSuchFieldException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, WTException {
        return baseDao.selectList(fieldNames, query, modelClass);
    }

    public <M extends WTModel> Integer selectCount(WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, WTException {
        return baseDao.selectCount(query, modelClass).intValue();
    }

    public <M extends WTModel> M selectOne(WTQuery query, Class<M> modelClass) throws IllegalAccessException, NoSuchMethodException, InstantiationException, NoSuchFieldException, InvocationTargetException, ClassNotFoundException, WTException {
        return baseDao.selectOne(query, modelClass);
    }

    public <M extends WTModel> M selectOne(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws IllegalAccessException, NoSuchMethodException, InstantiationException, NoSuchFieldException, InvocationTargetException, ClassNotFoundException, WTException {
        return baseDao.selectOne(fieldNames, query, modelClass);
    }

    @Override
    public <M extends WTModel> void increment(String fieldName, WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, WTException {
        baseDao.increment(fieldName, query, modelClass);
    }

    @Override
    public <M extends WTModel> void decrement(String fieldName, WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, WTException {
        baseDao.decrement(fieldName, query, modelClass);
    }

    public <M extends WTModel> M selectOne(Class<M> modelClass, String id) throws InstantiationException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException, WTException, ClassNotFoundException {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        return selectOne(new WTQuery().setId(id), modelClass);
    }

    public void update(WTQuery query, WTModel model) throws ClassNotFoundException, NoSuchFieldException, WTException {
        baseDao.update(query, model);
    }

    @Override
    public void update(String sql) throws ParameterIsNullException {
        baseDao.update(sql);
    }

    public <M extends WTModel> String save(M model) throws ClassNotFoundException, NoSuchFieldException, WTException {
        return baseDao.save(model);
    }

    public <M extends WTModel> String createWithId(M model) throws NoSuchFieldException, WTException, ClassNotFoundException {
        return baseDao.createWithId(model);
    }

    public <M extends WTModel, Q extends WTQuery> void realDelete(Q query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, WTException {
        baseDao.realDelete(query, modelClass);
    }

    public <M extends WTModel> void realDelete(String id, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, WTException {
        if (StringUtils.isEmpty(id)) {
            return;
        }
        realDelete(new WTQuery().setId(id), modelClass);
    }


}

