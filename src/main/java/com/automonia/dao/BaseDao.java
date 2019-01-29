package com.automonia.dao;


import com.automonia.dialect.model.WTModel;
import com.automonia.dialect.query.WTQuery;
import com.automonia.tools.exception.ParameterIsNullException;
import com.automonia.tools.exception.WTException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * dao层对象的基础类
 *
 * @author 作者 温腾
 * @date 2017年5月9日 下午10:33:57
 */
public interface BaseDao {


    /**
     * 分页的查询函数
     *
     * @param query      分页查询对象
     * @param modelClass 数据模型Class对象
     * @return 分页查询的列表数据集合
     * @throws Exception
     */
    <M extends WTModel> List<M> selectPageList(WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, WTException;

    <M extends WTModel> List<M> selectPageList(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, WTException;

    /**
     * 将字段内容自增1的逻辑函数
     *
     * @param fieldName  属性名称
     * @param query      查询对象
     * @param modelClass 数据模型class对象
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws ParameterIsNullException
     */
    <M extends WTModel> void increment(String fieldName, WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, WTException;

    <M extends WTModel> void decrement(String fieldName, WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, WTException;


    /**
     * 无分页的列表查询函数
     *
     * @param query      查询对象
     * @param modelClass 数据模型Class对象
     * @return 查询的列表数据集合
     * @throws Exception
     */
    <M extends WTModel> List<M> selectList(WTQuery query, Class<M> modelClass) throws NoSuchFieldException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, WTException;

    <M extends WTModel> List<M> selectList(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws NoSuchFieldException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, WTException;

    /**
     * 统计数据函数
     *
     * @param query      查询对象
     * @param modelClass 数据模型Class对象
     * @return 统计数量
     * @throws Exception
     */
    <M extends WTModel> Long selectCount(WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, WTException;


    /**
     * @param query      查询对象
     * @param modelClass 实体Class对象
     * @return 获取符合条件的一条数据记录
     * @throws Exception
     */
    <M extends WTModel> M selectOne(WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, WTException;

    <M extends WTModel> M selectOne(List<String> fieldNames, WTQuery query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, WTException;

    /**
     * 更新满足query的model记录
     * model确定了需要更新的内容， query确定更新那些数据
     *
     * @param query 查询对象
     * @param model 实体对象
     * @return 更新的数量
     * @throws Exception
     */
    void update(WTQuery query, WTModel model) throws ClassNotFoundException, NoSuchFieldException, WTException;

    void update(String sql) throws ParameterIsNullException;

    /**
     * 保存实体对象
     *
     * @param model 实体对象
     * @throws Exception
     */
    <M extends WTModel> String save(M model) throws ClassNotFoundException, NoSuchFieldException, WTException;

    /**
     * 新建实体对象，保存进数据库
     * 直接忽视数据的检查以识别是新增还是更新操作
     *
     * @param model 实体对象
     * @return 新增数据的主键ID
     * @throws Exception
     */
    <M extends WTModel> String create(M model) throws NoSuchFieldException, WTException, ClassNotFoundException;


    /**
     * 创建实体对象，保存进数据库，创建的实体对象的主键不为空，不采用新的UUID
     *
     * @param model 待创建的实体对象
     * @return 创建数据的主键ID
     * @throws Exception
     */
    <M extends WTModel> String createWithId(M model) throws NoSuchFieldException, WTException, ClassNotFoundException;

    /**
     * 从数据库中真实的删除数据
     * 删除的数据满足查询对象规则
     *
     * @param query      查询对象
     * @param modelClass 数据模型对象的Class对象
     * @return 真实删除的数据的数量
     * @throws Exception
     */
    <M extends WTModel, Q extends WTQuery> void realDelete(Q query, Class<M> modelClass) throws ClassNotFoundException, NoSuchFieldException, WTException;


}
















