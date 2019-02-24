package com.automonia.core.tools;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * json的工具类
 *
 * @author wenteng
 */
public enum JSONUtils {

    singleton;

    public ObjectMapper objectMapper = new ObjectMapper();

    JSONUtils() {
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 操作成功的json接口，
     * status 采用标准的 httpStatus 状态码
     */
    public String operateSuccess(Object value) {
        return operator("200", "操作成功", value);
    }

    /**
     * 操作失败的json接口
     * status 采用标准的 httpStatus 状态码
     *
     * @return 错误的json数据
     */
    public String operateError(String message) {
        return operator("500", message, null);
    }

    public <T> List<T> parseJsonArray(String jsonObject, Class<T> targetClass) {
        try {
            return objectMapper.readValue(jsonObject, getCollectionType(List.class, targetClass));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 解析json字符串内容生成对应的对象
     *
     * @param jsonObject  json字符串内容
     * @param targetClass 目标对象Class对象
     * @return 对象
     */
    public <T> T parseJsonObject(String jsonObject, Class<T> targetClass) {
        try {
            return objectMapper.readValue(jsonObject, targetClass);
        } catch (IOException e) {
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////

    public String operator(String status, String message, Object value) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("status", status);
        jsonMap.put("message", message);
        jsonMap.put("data", value);

        return toJson(jsonMap);
    }

    /**
     * 将对象转化为json的静态方法
     *
     * @param value json数据的value，并没有提供json的key
     * @return json数据
     */
    public String toJson(Object value) {
        try {
            //将参数转成json格式
            String jsonValue = objectMapper.writeValueAsString(value);

            //做日志信息打印
            LogUtils.singleton.info(jsonValue);

            return jsonValue;
        } catch (JsonProcessingException e) {
            LogUtils.singleton.exception(e);
        }
        return null;
    }


    ////////

    private JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
}
