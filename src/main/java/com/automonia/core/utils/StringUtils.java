package com.automonia.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 字符工具类
 *
 * @auther 作者 温腾
 * @创建时间 2017年5月9日 上午12:30:49
 */
public class StringUtils {

    public static String packSkimCountInfo(Integer skimCount) {

        // 浏览次数为空，无需进行任何信息的转换
        if (skimCount == null || skimCount <= 0) {
            return null;
        }
        // 浏览次数不为空，需要进行浏览信息的转换
        else {

            Integer zeroCount = 0;
            Double scientNumber = Double.valueOf(skimCount);
            while (scientNumber > 10) {
                scientNumber = scientNumber / 10;
                zeroCount += 1;
            }

            return String.format("%.1f", scientNumber) + StringUtils.getMathematicalCountingUnit(zeroCount);
        }
    }

    public static String getMathematicalCountingUnit(Integer count) {
        if (count == null || count <= 0) {
            return null;
        }

        String unit = "";
        switch (count.toString()) {
            case "1":
                unit = "十";
                break;
            case "2":
                unit = "百";
                break;
            case "3":
                unit = "千";
                break;
            case "4":
                unit = "万";
                break;
            case "5":
                unit = "十万";
                break;
            case "6":
                unit = "百万";
                break;
            case "7":
                unit = "千万";
                break;
            case "8":
                unit = "亿";
                break;
            case "9":
                unit = "十亿";
                break;
        }
        return unit;
    }

    private static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};
    ///

    /**
     * 生成32位的UUID值
     *
     * @return 唯一的uuid字符串
     */
    public static String generateUUID() {
        String id = UUID.randomUUID().toString();
        id = id.substring(0, 8) + id.substring(9, 13) + id.substring(14, 18) + id.substring(19, 23) + id.substring(24);
        return id;
    }

    /**
     * 生成8位的UUID值
     *
     * @return 唯一的uuid字符串
     */
    public static String generate8Uuid() {
        StringBuilder shortBuffer = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

    /**
     * 判断str是否是空的
     * 如果str是null，"" 都算是空的，返回true，否则返回false
     *
     * @param str 判断是否为空的字符
     * @return true 是空字符，false 非空字符
     */
    public static boolean isEmpty(String str) {
        return str == null || str.equals("") || str.equals("null") || str.length() <= 0 || str.trim().length() <= 0;
    }

    /**
     * 至少一个字符是空的
     *
     * @param strs 判断的字符集
     * @return true 至少有一个为空， false 所有都是非空的
     */
    public static boolean isEmptyOr(String... strs) {
        if (strs == null || strs.length == 0) {
            return false;
        }
        for (String str : strs) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 所有字符都是非空的
     *
     * @param strs 判断的字符集
     * @return true 所有都是空，false 至少有一个不为空
     */
    public static boolean isEmptyAnd(String... strs) {
        if (strs == null) return true;
        for (String str : strs) {
            if (!isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将第一个字符转成大写的
     *
     * @param str 需转成的字符
     * @return 头字母大写的字符
     */
    public static String toUpperFirstCase(String str) {
        if (StringUtils.isEmpty(str)) return null;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 将第一个字符转成小写的
     *
     * @param str 需转化的字符
     * @return 头字母小写的字符
     */
    public static String toLowerFirstCase(String str) {
        if (StringUtils.isEmpty(str)) return null;
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 根据属性的名称获取相应的get函数的名称
     *
     * @param fieldName 属性名称
     * @return 属性的get函数名称
     */
    public static String getGetMethod(String fieldName) {
        return "get" + toUpperFirstCase(fieldName);
    }


    /**
     * 根据属性的名称获取相应的set函数的名称
     *
     * @param fieldName 属性名称
     * @return 属性的set函数名称
     */
    public static String getSetMethod(String fieldName) {
        return "set" + toUpperFirstCase(fieldName);
    }


    /**
     * 将strs分隔成List，strs的每一项是用，(逗号)分隔的
     *
     * @param strs 逗号分隔的strs
     * @return list集合
     */
    public static List<String> toArray(String strs) {
        if (isEmpty(strs)) {
            return null;
        }
        /**
         * list的toString函数的生成格式。
         * 一头一尾用[]包括起来，每项用，分隔
         */
        if (strs.startsWith("[")) {
            strs = strs.substring(1);
        }
        if (strs.endsWith("]")) {
            strs = strs.substring(0, strs.length() - 1);
        }

        List<String> results = new ArrayList<>();
        for (String item : strs.split(",")) {
            results.add(item);
        }
        return results;
    }

    public static List<String> split(String str, String split) {
        if (StringUtils.isEmptyOr(str, split)) {
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>();
        for (String item : str.split(split)) {
            result.add(item);
        }
        return result;
    }

    public static String getInSql(List<String> valueList) {
        if (valueList != null && !valueList.isEmpty()) {
            StringBuilder valueSqlTemp = new StringBuilder();
            for (String item : valueList) {
                valueSqlTemp.append(",")
                        .append("'")
                        .append(item.trim())
                        .append("'");
            }
            return valueSqlTemp.substring(1);
        }
        return null;
    }

    /**
     * 获取特定长度的随机数
     *
     * @param length 随机数的长度
     * @return
     */
    public static String generateNumber(Integer length) {
        String no = "";
        int num[] = new int[length];
        int c = 0;
        for (int i = 0; i < length; i++) {
            num[i] = new Random().nextInt(10);
            c = num[i];
            for (int j = 0; j < i; j++) {
                if (num[j] == c) {
                    i--;
                    break;
                }
            }
        }
        if (num.length > 0) {
            for (int i = 0; i < num.length; i++) {
                no += num[i];
            }
        }
        return no;
    }

    /**
     * 将任何类型转换为string，即时该对象为空
     *
     * @param value 需要转化的对象
     * @return 转换后的string数据
     */
    public static String getString(Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }


    public static void main(String[] args) {
        List<String> ids = new ArrayList<>();

        ids.add("daf");
        ids.add("asdfadf");
        ids.add("asdfasdfd");

        System.out.println(ids.toString());
    }
}



























