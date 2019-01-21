package com.automonia.core.base.controller;

import com.automonia.core.base.BaseService;
import com.automonia.core.base.exception.*;
import com.automonia.core.base.model.WTModel;
import com.automonia.core.base.query.OrderType;
import com.automonia.core.base.query.WTQuery;
import com.automonia.core.utils.JSONUtils;
import com.automonia.core.utils.ReflectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 列表页面的控制器的基础类, 包含了列表页面的CRUD功能接口
 *
 * @author wenteng
 */
public class ListController<M extends WTModel, Q extends WTQuery> extends BaseController<M, Q> {

    @Autowired
    private BaseService baseService;


    /**
     * 列表页面的list接口
     *
     * @param request
     * @param response
     * @return 列表的json数据
     */
    @ResponseBody
    @RequestMapping(value = "list")
    public final String list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        /**
         * 将request对象的请求参数实例化到查询对象的属性上，并创建查询对象实例
         * 添加排序条件：按照数据的创建时间的倒序进行排列
         */
        Q query = ReflectUtils.singleton.instantObject(ReflectUtils.singleton.getParameterMapFromServletReqeust(request), getQueryClass());
        query.addOrder("createDate", OrderType.desc);

        return JSONUtils.operateSuccess(getBaseService().selectPageModel(query, getModelClass()));
    }

    /**
     * 详情接口，根据数据的主键ID获取特定记录的详情内容
     *
     * @param request
     * @param response
     * @param id       数据记录的主键ID
     */
    @ResponseBody
    @RequestMapping(value = "view/{id}")
    public final String view(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) throws Exception {
        return JSONUtils.operateSuccess(view(id));
    }


    /**
     * 删除数据的接口
     *
     * @param id 需要删除数据的主键ID
     */
    @ResponseBody
    @RequestMapping(value = "remove/{id}", method = RequestMethod.DELETE)
    public final String remove(@PathVariable String id) throws Exception {
        delete(id);
        return JSONUtils.operateSuccess("删除成功");
    }


    /**
     * 保存数据接口
     *
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public final String saveOrUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> parameterMap = ReflectUtils.singleton.getParameterMapFromServletReqeust(request);
        return JSONUtils.operateSuccess(save(ReflectUtils.singleton.instantObject(parameterMap, getModelClass())));
    }


    /**
     * 方便实现类重写save逻辑函数
     *
     * @param model 数据模型封装类
     * @return 保存数据的主键ID
     * @throws EntityWithoutTableAnnotationException
     * @throws WithoutEntityAnnotationException
     * @throws NoSuchFieldException
     * @throws ParameterIsNullException
     * @throws NotSupportedTypeException
     * @throws ClassNotFoundException
     */
    public String save(M model) throws EntityWithoutTableAnnotationException, WithoutEntityAnnotationException, NoSuchFieldException, ParameterIsNullException, NotSupportedTypeException, ClassNotFoundException, CryptoException, BusinessException, Exception {
        return getBaseService().save(model);
    }


    /**
     * 方便实现类重写remove逻辑函数
     *
     * @param id 主键ID
     * @throws ClassNotFoundException
     * @throws WithoutEntityAnnotationException
     * @throws EntityWithoutTableAnnotationException
     * @throws NotSupportedTypeException
     * @throws ParameterIsNullException
     * @throws NoSuchFieldException
     */
    public void delete(String id) throws ClassNotFoundException, WithoutEntityAnnotationException, EntityWithoutTableAnnotationException, NotSupportedTypeException, ParameterIsNullException, NoSuchFieldException, BusinessException, Exception {
        getBaseService().realDelete(id, getModelClass());
    }


    /**
     * 方便实现类重写view逻辑函数
     *
     * @param id 主键ID
     * @return 实体对象
     * @throws ClassNotFoundException
     * @throws NotSupportedTypeException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws EntityWithoutTableAnnotationException
     * @throws NotSupportedEnumException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws WithoutEntityAnnotationException
     * @throws EntityFieldWithoutColumnAnnotationException
     */
    public M view(String id) throws ClassNotFoundException, NotSupportedTypeException, InstantiationException, InvocationTargetException, NoSuchMethodException, EntityWithoutTableAnnotationException, NotSupportedEnumException, IllegalAccessException, NoSuchFieldException, WithoutEntityAnnotationException, EntityFieldWithoutColumnAnnotationException, ParameterIsNullException, BusinessException {
        return getBaseService().selectOne(new WTQuery().setId(id), getModelClass());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////


    public BaseService getBaseService() {
        return baseService;
    }
}












