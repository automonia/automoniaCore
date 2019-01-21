package com.automonia.core.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * json的工具类
 *
 * @author wenteng
 */
public class JSONUtils {

    public static ObjectMapper objectMapper;

    static {
        /**
         * 支持将Date数据按照特定的格式转换为String
         */
        JSONUtils.objectMapper = new ObjectMapper();
        JSONUtils.objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 操作成功的json接口，
     * status 采用标准的 httpStatus 状态码
     *
     * @return
     */
    public static String operateSuccess(Object value) {
        return operator("200", "操作成功", value);
    }

    /**
     * 操作失败的json接口
     * status 采用标准的 httpStatus 状态码
     *
     * @return 错误的json数据
     */
    public static String operateError(String message) {
        return operator("500", message, null);
    }

    /**
     * 未登录的json数据
     *
     * @return json数据
     */
    public static void responseNotLoginJSON(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            printWriter.append(operator("401", "未登录", null));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    /**
     * 解析json字符串内容生成对应的对象
     *
     * @param jsonObject  json字符串内容
     * @param targetClass 目标对象Class对象
     * @return 对象
     */
    public static <T> T parseJsonObject(String jsonObject, Class<T> targetClass) {
        try {
            return objectMapper.readValue(jsonObject, targetClass);
        } catch (IOException e) {
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////

    private static String operator(String status, String message, Object value) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("status", status);
        jsonMap.put("message", message);
        jsonMap.put("data", value);
        try {
            return toJson(jsonMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "\"status\" : \"500\", \"message\" : \"json解析异常\"";
    }

    /**
     * 将对象转化为json的静态方法
     *
     * @param value json数据的value，并没有提供json的key
     * @return json数据
     */
    public static String toJson(Object value) throws JsonProcessingException {
        return JSONUtils.objectMapper.writeValueAsString(value);
    }

    public static void main(String[] args) {
        System.out.println(JSONUtils.operateSuccess("sdf"));
    }
}
