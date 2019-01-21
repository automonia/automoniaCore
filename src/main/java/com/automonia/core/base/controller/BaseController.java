package com.automonia.core.base.controller;


import com.automonia.core.base.model.WTModel;
import com.automonia.core.base.query.WTQuery;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 控制类的基础对象,
 *
 * @作者 温腾
 * @创建时间 2018年04月11日 上午12:19
 */
public abstract class BaseController<M extends WTModel, Q extends WTQuery> {

    /**
     * 继承控制层的基础类使用的范型信息,
     * M数据对象， Q查询对象
     * 0-M, 1-Q
     */
    private Type[] parameterTypes;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * 获取该类的Q范型使用的查询对象类型的Class对象
     *
     * @return 查询对象的Class对象
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public Class<Q> getQueryClass() throws ClassNotFoundException {
        return (Class<Q>) Class.forName(getParameterTypes()[1].getTypeName());
    }

    /**
     * 获取该类的M范型使用的数据模型对象类型的Class对象
     *
     * @return 数据模型对象的Class对象
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public Class<M> getModelClass() throws ClassNotFoundException {
        return (Class<M>) Class.forName(getParameterTypes()[0].getTypeName());
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * 获取该类使用到范型参数数据
     *
     * @return 范型代表到实际类型的信息
     */
    private Type[] getParameterTypes() {
        if (parameterTypes == null) {
            parameterTypes = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
        }
        return parameterTypes;
    }


}
