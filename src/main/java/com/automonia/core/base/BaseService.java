package com.automonia.core.base;


import com.automonia.core.base.exception.*;
import com.automonia.core.base.model.WTModel;
import com.automonia.core.base.model.WTPageModel;
import com.automonia.core.base.query.WTQuery;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author 作者 温腾
 * @date 2017年5月10日 上午7:50:26
 */
public interface BaseService {

    <M extends WTModel> WTPageModel<M> selectPageModel(WTQuery query, Class<M> modelClass) throws IllegalAccessException, NoSuchMethodException, EntityFieldWithoutColumnAnnotationException, NotSupportedTypeException, InstantiationException, NotSupportedEnumException, NoSuchFieldException, InvocationTargetException, WithoutEntityAnnotationException, ClassNotFoundException, EntityWithoutTableAnnotationException, ParameterIsNullException, BusinessException;

    <M extends WTModel> WTPageModel<M> selectPageModel(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws IllegalAccessException, NoSuchMethodException, EntityFieldWithoutColumnAnnotationException, NotSupportedTypeException, InstantiationException, NotSupportedEnumException, NoSuchFieldException, InvocationTargetException, WithoutEntityAnnotationException, ClassNotFoundException, EntityWithoutTableAnnotationException, BusinessException, ParameterIsNullException;

    <M extends WTModel> List<M> selectList(WTQuery query, Class<M> modelClass) throws NotSupportedTypeException, NoSuchMethodException, EntityFieldWithoutColumnAnnotationException, NoSuchFieldException, InstantiationException, NotSupportedEnumException, IllegalAccessException, InvocationTargetException, WithoutEntityAnnotationException, ClassNotFoundException, EntityWithoutTableAnnotationException, BusinessException, ParameterIsNullException;

    <M extends WTModel> List<M> selectList(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws NotSupportedTypeException, NoSuchMethodException, EntityFieldWithoutColumnAnnotationException, NoSuchFieldException, InstantiationException, NotSupportedEnumException, IllegalAccessException, InvocationTargetException, WithoutEntityAnnotationException, ClassNotFoundException, EntityWithoutTableAnnotationException, BusinessException, ParameterIsNullException;

    <M extends WTModel> Integer selectCount(WTQuery query, Class<M> modelClass) throws ClassNotFoundException, WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, BusinessException, ParameterIsNullException;

    <M extends WTModel> M selectOne(WTQuery query, Class<M> modelClass) throws IllegalAccessException, NoSuchMethodException, EntityFieldWithoutColumnAnnotationException, NotSupportedTypeException, InstantiationException, NotSupportedEnumException, NoSuchFieldException, InvocationTargetException, WithoutEntityAnnotationException, ClassNotFoundException, EntityWithoutTableAnnotationException, BusinessException, ParameterIsNullException;

    <M extends WTModel> M selectOne(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws IllegalAccessException, NoSuchMethodException, EntityFieldWithoutColumnAnnotationException, NotSupportedTypeException, InstantiationException, NotSupportedEnumException, NoSuchFieldException, InvocationTargetException, WithoutEntityAnnotationException, ClassNotFoundException, EntityWithoutTableAnnotationException, BusinessException, ParameterIsNullException;

    <M extends WTModel> void increment(String fieldName, WTQuery query, Class<M> modelClass) throws ClassNotFoundException, WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, ParameterIsNullException, BusinessException;

    <M extends WTModel> void decrement(String fieldName, WTQuery query, Class<M> modelClass) throws ClassNotFoundException, WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, ParameterIsNullException, BusinessException;

    <M extends WTModel> M selectOne(Class<M> modelClass, String id) throws NotSupportedTypeException, InstantiationException, InvocationTargetException, NoSuchMethodException, EntityWithoutTableAnnotationException, NotSupportedEnumException, IllegalAccessException, NoSuchFieldException, WithoutEntityAnnotationException, ClassNotFoundException, EntityFieldWithoutColumnAnnotationException, ParameterIsNullException, BusinessException;

    void update(WTQuery query, WTModel model) throws ClassNotFoundException, WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, ParameterIsNullException, BusinessException;

    <M extends WTModel> String save(M model) throws ClassNotFoundException, WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, ParameterIsNullException, BusinessException;

    <M extends WTModel> String createWithId(M model) throws WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, BusinessException, ParameterIsNullException, ClassNotFoundException;

    <M extends WTModel, Q extends WTQuery> void realDelete(Q query, Class<M> modelClass) throws ClassNotFoundException, WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, ParameterIsNullException, BusinessException;

    <M extends WTModel> void realDelete(String id, Class<M> modelClass) throws ClassNotFoundException, WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, NoSuchFieldException, ParameterIsNullException, BusinessException;

}
