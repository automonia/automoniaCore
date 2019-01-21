package com.automonia.core.utils;


import com.automonia.core.base.BaseEnum;
import com.automonia.core.base.exception.NotSupportedEnumException;
import com.automonia.core.base.exception.NotSupportedTypeException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

/**
 * 反射工具类
 *
 * @author 作者 温腾
 * @创建时间 2017年5月9日 上午12:28:14
 */
public enum ReflectUtils {

    singleton;

    /**
     * 从HttpServletRequest解析请求参数，并将其封装到Map结构中
     *
     * @param request servlet请求对象
     * @return map 请求参数集合
     */
    public Map<String, Object> getParameterMapFromServletReqeust(HttpServletRequest request) {
        Map<String, Object> values = new HashMap<String, Object>();
        Enumeration parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement().toString();
            values.put(parameterName, request.getParameter(parameterName));
        }
        return values;
    }

    /**
     * 将从数据库查找的内容实例化到对象属性上
     *
     * @param values      数据库查找数据内容集合
     * @param targetClass 实体化对象的Class对象
     */
    public <T> List<T> instantObjectList(List<Map<String, Object>> values, Class<T> targetClass) throws NoSuchMethodException, NotSupportedTypeException, IllegalAccessException, InstantiationException, InvocationTargetException, NotSupportedEnumException {
        if (values == null) {
            return null;
        }
        List<T> objectList = new ArrayList<>();
        for (Map<String, Object> item : values) {
            objectList.add(instantObject(item, targetClass));
        }
        return objectList;
    }

    public <T> T instantObject(Map<String, Object> values, Class<T> targetClass) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, NotSupportedTypeException, NotSupportedEnumException {
        if (values == null) {
            return null;
        }
        T targetObject = targetClass.newInstance();
        for (Field item : getAllFields(targetClass)) {
            Object value = values.get(item.getName());

            if (value == null || StringUtils.isEmpty(value.toString())) {
                continue;
            }
            Class itemTypeClass = item.getType();
            Method setFieldMethod = targetClass.getMethod(StringUtils.getSetMethod(item.getName()), item.getType());

            /**
             * 将枚举数值转化为枚举对象
             */
            if (itemTypeClass.isEnum()) {
                setFieldMethod.invoke(targetObject, getBaseEnumValue(itemTypeClass, value.toString()));
            }

            /**
             * 将Boolean，Date，基本数据类型的封装类实例化
             */
            else {
                setFieldMethod.invoke(targetObject, getCommonTypeValue(itemTypeClass, value.toString()));
            }
        }
        return targetObject;
    }

    /**
     * 获取所有的字段信息,包括所有父类的字段信息
     *
     * @param targetClass 目标Class对象
     * @return Field字段信息
     */
    public Field[] getAllFields(Class targetClass) {
        Field[] fields = targetClass.getDeclaredFields();
        Class superClass = targetClass.getSuperclass();
        while (superClass != null) {
            fields = CollectionUtils.singleton.union(fields, superClass.getDeclaredFields());
            superClass = superClass.getSuperclass();
        }
        return fields;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Object getCommonTypeValue(Class typeClass, String typeValue) throws NotSupportedTypeException {
        // String
        if (String.class.isAssignableFrom(typeClass)) {
            return typeValue;
        }
        // Integer
        else if (Integer.class.isAssignableFrom(typeClass)) {
            return Integer.valueOf(typeValue);
        }
        // Double
        else if (Double.class.isAssignableFrom(typeClass)) {
            return Double.valueOf(typeValue);
        }
        // Date
        else if (Date.class.isAssignableFrom(typeClass)) {
            return DateUtils.singleton.getAllFormatDate(typeValue);
        }
        // Boolean
        else if (Boolean.class.isAssignableFrom(typeClass)) {
            if ("1".equals(typeValue)) {
                return true;
            }
            if ("0".equals(typeValue)) {
                return false;
            }
            return Boolean.valueOf(typeValue);
        }
        // Float
        else if (Float.class.isAssignableFrom(typeClass)) {
            return Float.valueOf(typeValue);
        }
        // Short
        else if (Short.class.isAssignableFrom(typeClass)) {
            return Short.valueOf(typeValue);
        }
        // Long
        else if (Long.class.isAssignableFrom(typeClass)) {
            return Long.valueOf(typeValue);
        }
        // Byte
        else if (Byte.class.isAssignableFrom(typeClass)) {
            return Byte.valueOf(typeValue);
        }
        // BigDecimal
        else if (BigDecimal.class.isAssignableFrom(typeClass)) {
            return BigDecimal.valueOf(Double.valueOf(typeValue));
        }
        throw new NotSupportedTypeException("请提供支持类型" + typeClass.getSimpleName() + ",(请使用基本类型的封装类)");
    }

    /**
     * 获取对应的枚举对象
     * <p>
     * value是不为空的，在调用该函数的前面处理了非空逻辑
     *
     * @param enumTypeClass 枚举Class对象
     * @param enumValue     枚举的value数值
     */
    private BaseEnum getBaseEnumValue(Class enumTypeClass, String enumValue) throws NotSupportedEnumException {
        for (Object enumObject : enumTypeClass.getEnumConstants()) {
            BaseEnum baseEnumItem = (BaseEnum) enumObject;
            if (enumValue.equals(baseEnumItem.getValue().toString()) || enumValue.equals(baseEnumItem.toString())) {
                return baseEnumItem;
            }
        }
        throw new NotSupportedEnumException(enumTypeClass.getSimpleName() + "未包含有项的value = " + enumValue);
    }
}
