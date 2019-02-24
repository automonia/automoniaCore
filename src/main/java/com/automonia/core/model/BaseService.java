package com.automonia.core.model;


import com.automonia.core.exception.WTException;
import com.automonia.core.query.WTQuery;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author 作者 温腾
 * @date 2017年5月10日 上午7:50:26
 */
public interface BaseService {

    <M extends WTModel> WTPageModel<M> selectPageModel(WTQuery query, Class<M> modelClass) throws IllegalAccessException, NoSuchMethodException, InstantiationException, NoSuchFieldException, InvocationTargetException, ClassNotFoundException, WTException;

    <M extends WTModel> WTPageModel<M> selectPageModel(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws IllegalAccessException, NoSuchMethodException, InstantiationException, NoSuchFieldException, InvocationTargetException, ClassNotFoundException, WTException;

    <M extends WTModel> List<M> selectList(WTQuery query, Class<M> modelClass) throws NoSuchMethodException, NoSuchFieldException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, WTException;

    <M extends WTModel> List<M> selectList(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws NoSuchMethodException, NoSuchFieldException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, WTException;

    <M extends WTModel> Integer selectCount(WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, WTException;

    <M extends WTModel> M selectOne(WTQuery query, Class<M> modelClass) throws IllegalAccessException, NoSuchMethodException, InstantiationException, NoSuchFieldException, InvocationTargetException, ClassNotFoundException, WTException;

    <M extends WTModel> M selectOne(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws IllegalAccessException, NoSuchMethodException, InstantiationException, NoSuchFieldException, InvocationTargetException, ClassNotFoundException, WTException;

    <M extends WTModel> void increment(String fieldName, WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, WTException;

    <M extends WTModel> void decrement(String fieldName, WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, WTException;

    <M extends WTModel> M selectOne(Class<M> modelClass, String id) throws InstantiationException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException, WTException;

    void update(WTQuery query, WTModel model) throws ClassNotFoundException, NoSuchFieldException, WTException;

    <M extends WTModel> String save(M model) throws ClassNotFoundException, NoSuchFieldException, WTException;

    <M extends WTModel, Q extends WTQuery> void realDelete(Q query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, WTException;

    <M extends WTModel> void realDelete(String id, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, WTException;

}
