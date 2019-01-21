package com.automonia.core.base;

import com.automonia.core.base.exception.*;
import com.automonia.core.base.model.WTModel;
import com.automonia.core.base.model.WTPageModel;
import com.automonia.core.base.query.WTQuery;
import com.automonia.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by wenteng on
 * 2017/9/2.
 */
public class BaseServiceImpl implements BaseService {

    @Autowired
    private BaseDao baseDao;


    public <M extends WTModel> WTPageModel<M> selectPageModel(WTQuery query, Class<M> modelClass) throws IllegalAccessException, NoSuchMethodException, EntityFieldWithoutColumnAnnotationException, NotSupportedTypeException, InstantiationException, NotSupportedEnumException, NoSuchFieldException, InvocationTargetException, WithoutEntityAnnotationException, ClassNotFoundException, EntityWithoutTableAnnotationException, ParameterIsNullException, BusinessException {
        return this.selectPageModel(null, query, modelClass);
    }

    @Override
    public <M extends WTModel> WTPageModel<M> selectPageModel(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws IllegalAccessException, NoSuchMethodException, EntityFieldWithoutColumnAnnotationException, NotSupportedTypeException, InstantiationException, NotSupportedEnumException, NoSuchFieldException, InvocationTargetException, WithoutEntityAnnotationException, ClassNotFoundException, EntityWithoutTableAnnotationException, BusinessException, ParameterIsNullException {
        List<M> datas = baseDao.selectPageList(fieldNames, query, modelClass);
        Integer totalCount = selectCount(query, modelClass);
        return new WTPageModel<M>().initPageModel(datas, totalCount, query.getPageNo(), query.getPageSize());
    }

    public <M extends WTModel> List<M> selectList(WTQuery query, Class<M> modelClass) throws NotSupportedTypeException, NoSuchMethodException, EntityFieldWithoutColumnAnnotationException, NoSuchFieldException, InstantiationException, NotSupportedEnumException, IllegalAccessException, InvocationTargetException, WithoutEntityAnnotationException, ClassNotFoundException, EntityWithoutTableAnnotationException, BusinessException, ParameterIsNullException {
        return baseDao.selectList(query, modelClass);
    }

    public <M extends WTModel> List<M> selectList(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws NotSupportedTypeException, NoSuchMethodException, EntityFieldWithoutColumnAnnotationException, NoSuchFieldException, InstantiationException, NotSupportedEnumException, IllegalAccessException, InvocationTargetException, WithoutEntityAnnotationException, ClassNotFoundException, EntityWithoutTableAnnotationException, BusinessException, ParameterIsNullException {
        return baseDao.selectList(fieldNames, query, modelClass);
    }

    public <M extends WTModel> Integer selectCount(WTQuery query, Class<M> modelClass) throws ClassNotFoundException, WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, BusinessException, ParameterIsNullException {
        return baseDao.selectCount(query, modelClass).intValue();
    }

    public <M extends WTModel> M selectOne(WTQuery query, Class<M> modelClass) throws IllegalAccessException, NoSuchMethodException, EntityFieldWithoutColumnAnnotationException, NotSupportedTypeException, InstantiationException, NotSupportedEnumException, NoSuchFieldException, InvocationTargetException, WithoutEntityAnnotationException, ClassNotFoundException, EntityWithoutTableAnnotationException, BusinessException, ParameterIsNullException {
        return baseDao.selectOne(query, modelClass);
    }

    public <M extends WTModel> M selectOne(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws IllegalAccessException, NoSuchMethodException, EntityFieldWithoutColumnAnnotationException, NotSupportedTypeException, InstantiationException, NotSupportedEnumException, NoSuchFieldException, InvocationTargetException, WithoutEntityAnnotationException, ClassNotFoundException, EntityWithoutTableAnnotationException, BusinessException, ParameterIsNullException {
        return baseDao.selectOne(fieldNames, query, modelClass);
    }

    @Override
    public <M extends WTModel> void increment(String fieldName, WTQuery query, Class<M> modelClass) throws ClassNotFoundException, WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, ParameterIsNullException, BusinessException {
        baseDao.increment(fieldName, query, modelClass);
    }

    @Override
    public <M extends WTModel> void decrement(String fieldName, WTQuery query, Class<M> modelClass) throws ClassNotFoundException, WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, ParameterIsNullException, BusinessException {
        baseDao.decrement(fieldName, query, modelClass);
    }

    public <M extends WTModel> M selectOne(Class<M> modelClass, String id) throws NotSupportedTypeException, InstantiationException, InvocationTargetException, NoSuchMethodException, EntityWithoutTableAnnotationException, NotSupportedEnumException, IllegalAccessException, NoSuchFieldException, WithoutEntityAnnotationException, ClassNotFoundException, EntityFieldWithoutColumnAnnotationException, ParameterIsNullException, BusinessException {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        return selectOne(new WTQuery().setId(id), modelClass);
    }

    public void update(WTQuery query, WTModel model) throws ClassNotFoundException, WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, ParameterIsNullException, BusinessException {
        baseDao.update(query, model);
    }

    public <M extends WTModel> String save(M model) throws ClassNotFoundException, WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, ParameterIsNullException, BusinessException {
        return baseDao.save(model);
    }

    public <M extends WTModel> String createWithId(M model) throws WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, BusinessException, ParameterIsNullException, ClassNotFoundException {
        return baseDao.createWithId(model);
    }

    public <M extends WTModel, Q extends WTQuery> void realDelete(Q query, Class<M> modelClass) throws ClassNotFoundException, WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, ParameterIsNullException, BusinessException {
        baseDao.realDelete(query, modelClass);
    }

    public <M extends WTModel> void realDelete(String id, Class<M> modelClass) throws ClassNotFoundException, WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, ParameterIsNullException, BusinessException {
        if (StringUtils.isEmpty(id)) {
            return;
        }
        realDelete(new WTQuery().setId(id), modelClass);
    }


}

