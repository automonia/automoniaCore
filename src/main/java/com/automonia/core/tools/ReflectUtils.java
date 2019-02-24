package com.automonia.core.tools;


import com.automonia.core.annotation.WTEnum;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @作者 温腾
 * @创建时间 2019年01月26日 17:10
 */
public enum ReflectUtils {

    singleton;


    public <T> List<T> instanceObjectList(List<Map<String, Object>> values, Class<T> targetClass) {
        if (targetClass == null || values == null || values.isEmpty()) {
            return null;
        }
        List<T> targetObjectList = new ArrayList<>();

        // 依次实例化，并要求实例化的对象不为空
        for (Map<String, Object> item : values) {
            T targetObject = instanceObject(item, targetClass);
            if (targetObject != null) {
                targetObjectList.add(targetObject);
            }
        }

        return targetObjectList;
    }


    /**
     * 用于将HttpServletRequest的参数封装成对象
     *
     * @param values      servlet的参数集合
     * @param targetClass 目标封装对象
     * @return 对象
     */
    public <T> T instanceObjectByParameterMapOfServletRequest(Map<String, String[]> values, Class<T> targetClass) {
        if (targetClass == null || values == null || values.isEmpty()) {
            return null;
        }

        // 将Map<String, Object[]>转换为Map<String, Object>
        Map<String, Object> parameters = new HashMap<>();
        for (String key : values.keySet()) {
            Object[] objects = values.get(key);
            if (objects != null && objects.length >= 1) {
                parameters.put(key, objects[0]);
            }
        }

        return instanceObject(parameters, targetClass);
    }

    public <T> T instanceObject(Map<String, Object> values, Class<T> targetClass) {
        if (targetClass == null || values == null || values.isEmpty()) {
            return null;
        }
        T targetObject = instanceObject(targetClass);

        if (targetObject == null) {
            return null;
        }

        // 在targetObject不为空，并且values有内容情况下进行属性的设置, 调用属性的set函数赋值
        for (Field field : getFieldsWithInherit(targetClass)) {

            // 获取属性的set函数对象
            Method method = getSetMethod(targetClass, field);
            if (method == null) {
                LogUtils.singleton.info("类(" + targetClass.getSimpleName() + ")的属性(" + field.getName() + ")未能找到set函数");
                continue;
            }

            // 获取属性在values中同名的数值对象
            String value = StringUtils.singleton.getString(values.get(field.getName()));
            if (StringUtils.singleton.isEmpty(value)) {
                continue;
            }

            // 调用set函数赋值，如果出现赋值操作出现异常，则打印异常并继续下一个属性赋值操作
            try {
                method.invoke(targetObject, getTypeValue(field.getType(), value));
            } catch (IllegalAccessException | InvocationTargetException e) {
                LogUtils.singleton.exception(e);
            }
        }

        return targetObject;
    }

    /**
     * 实例化class的对象
     *
     * @param targetClass class 对象
     * @return 实例化后的对象
     */
    public <T> T instanceObject(Class<T> targetClass) {
        if (targetClass == null) {
            return null;
        }
        try {
            return targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LogUtils.singleton.exception(e);
        }
        return null;
    }


    /**
     * 将对象的属性拆成map形式，
     * 属性名作为key， 属性值作为value
     *
     * @param object 对象
     * @return map集合
     */
    public Map<String, Object> instanceMap(Object object) {
        if (object == null) {
            return null;
        }
        Class objectClass = object.getClass();
        Map<String, Object> value = new HashMap<>();

        for (Field field : getFieldsWithInherit(objectClass)) {
            Method method = getGetMethod(objectClass, field);
            if (method == null) {
                LogUtils.singleton.info("类(" + objectClass.getSimpleName() + ")的属性(" + field.getName() + ")未能找到get函数");
            } else {
                try {
                    value.put(field.getName(), method.invoke(object));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LogUtils.singleton.exception(e);
                }
            }
        }

        return value;
    }


    /**
     * 将value转换为特定对象的数值
     *
     * @param typeClass class对象
     * @param value     字符串
     * @return value对象的typeClass的对象
     */
    public <T> T getTypeValue(Class<T> typeClass, String value) {
        if (typeClass == null || StringUtils.singleton.isEmpty(value)) {
            return null;
        }
        Object valueObject = null;
        // String
        if (String.class.isAssignableFrom(typeClass)) {
            valueObject = value;
        }
        // Integer
        else if (Integer.class.isAssignableFrom(typeClass)) {
            valueObject = Integer.valueOf(value);
        }
        // Date
        else if (Date.class.isAssignableFrom(typeClass)) {
            valueObject = DateUtils.singleton.getDateTime(value);
        }
        // WTEnum
        else if (WTEnum.class.isAssignableFrom(typeClass)) {
            for (Object enumObject : typeClass.getEnumConstants()) {
                WTEnum baseEnumItem = (WTEnum) enumObject;
                if (value.equals(baseEnumItem.getValue().toString()) || value.equals(baseEnumItem.toString())) {
                    valueObject = baseEnumItem;
                    break;
                }
            }
        }
        // Double
        else if (Double.class.isAssignableFrom(typeClass)) {
            valueObject = Double.valueOf(value);
        }
        // Boolean
        else if (Boolean.class.isAssignableFrom(typeClass)) {
            valueObject = Boolean.valueOf(value);
        }
        // Float
        else if (Float.class.isAssignableFrom(typeClass)) {
            valueObject = Float.valueOf(value);
        }
        // Long
        else if (Long.class.isAssignableFrom(typeClass)) {
            valueObject = Long.valueOf(value);
        }
        // Short
        else if (Short.class.isAssignableFrom(typeClass)) {
            valueObject = Short.valueOf(value);
        }

        return (T) valueObject;
    }


    /**
     * 获取类的所有属性，包括继承的类的属性
     *
     * @param targetClass 类
     */
    public Field[] getFieldsWithInherit(Class targetClass) {
        if (targetClass == null) {
            return null;
        }
        Field[] fields = targetClass.getDeclaredFields();
        Class superClass = targetClass.getSuperclass();
        while (superClass != null) {
            fields = CollectionUtils.singleton.union(fields, superClass.getDeclaredFields());
            superClass = superClass.getSuperclass();
        }
        return fields;
    }


    /**
     * 给object的属性field设置数值value
     * 前提是object要有field属性，并且属性类型和value类型对应得上
     *
     * @param object    对象
     * @param fieldName 属性名
     * @param value     属性值
     */
    public void setFieldValue(Object object, String fieldName, Object value) {
        if (object == null || StringUtils.singleton.isEmpty(fieldName)) {
            return;
        }

        try {
            // 存在fieldName的属性
            Field field = object.getClass().getDeclaredField(fieldName);
            Method method = getSetMethod(value.getClass(), field);
            method.invoke(object, value);
        } catch (NoSuchFieldException | InvocationTargetException | IllegalAccessException e) {
            // 不存在fieldName的属性，则返回
            LogUtils.singleton.exception(e);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Method getSetMethod(Class<?> targetClass, Field field) {
        if (targetClass == null || field == null) {
            return null;
        }
        try {
            return targetClass.getMethod(StringUtils.singleton.getSetMethodName(field), field.getType());
        } catch (NoSuchMethodException e) {
            LogUtils.singleton.exception(e);
        }
        return null;
    }

    private Method getGetMethod(Class<?> targetClass, Field field) {
        if (targetClass == null || field == null) {
            return null;
        }
        try {
            return targetClass.getMethod(StringUtils.singleton.getGetMethodName(field));
        } catch (NoSuchMethodException e) {
            LogUtils.singleton.exception(e);
        }
        return null;
    }


    public static void main(String[] args) {
    }
}
