package com.automonia.core.annotation.query;


import com.automonia.core.tools.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 查询方式
 * Created by jackie on
 * 2017/11/7.
 */
public enum WTSearchType {

    equal("等于 ="),

    notEqual("不等于 !="),

    like("匹配 %xx%"),

    likeLeft("左匹配 %xx"),

    likeRight("右匹配 xx%"),

    nullable("为空 is null"),

    in("in查询 in ('', '')对象要求为List"),

    notIn("not in查询 not in ('', '')对象要求为List"),

    greaterThen("大于"),

    lessThen("小于"),

    greaterThenEqual("大于等于"),

    lessThenEqual("小于等于");

    private String message;

    WTSearchType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getSearchValue(String valueStr) {

        String searchSqlValue = "";
        switch (this) {
            case like:
                if (!StringUtils.singleton.isEmpty(valueStr)) {
                    searchSqlValue = " like '%" + valueStr + "%'";
                }
                break;
            case likeLeft:
                if (!StringUtils.singleton.isEmpty(valueStr)) {
                    searchSqlValue = " like '%" + valueStr + "'";
                }
                break;
            case likeRight:
                if (!StringUtils.singleton.isEmpty(valueStr)) {
                    searchSqlValue = " like '" + valueStr + "%'";
                }
                break;
            case nullable:
                if (StringUtils.singleton.isEmpty(valueStr)) {
                    searchSqlValue = " is null";
                }
                break;
            case equal:
                if (!StringUtils.singleton.isEmpty(valueStr)) {
                    searchSqlValue = " = '" + valueStr + "'";
                }
                break;
            case in:
                if (!StringUtils.singleton.isEmpty(valueStr)) {
                    String insql = getInSql(toArray(valueStr));
                    if (!StringUtils.singleton.isEmpty(insql)) {
                        searchSqlValue = " in (" + insql + ")";
                    }
                }
                break;
            case notIn:
                if (!StringUtils.singleton.isEmpty(valueStr)) {
                    String insql = getInSql(toArray(valueStr));
                    if (!StringUtils.singleton.isEmpty(insql)) {
                        searchSqlValue = " not in (" + insql + ")";
                    }
                }
                break;
            case notEqual:
                if (!StringUtils.singleton.isEmpty(valueStr)) {
                    searchSqlValue = " != '" + valueStr + "'";
                }
                break;
            case greaterThen:
                if (!StringUtils.singleton.isEmpty(valueStr)) {
                    searchSqlValue = " > '" + valueStr + "'";
                }
                break;
            case lessThen:
                if (!StringUtils.singleton.isEmpty(valueStr)) {
                    searchSqlValue = " < '" + valueStr + "'";
                }
                break;
            case greaterThenEqual:
                if (!StringUtils.singleton.isEmpty(valueStr)) {
                    searchSqlValue = " >= '" + valueStr + "'";
                }
                break;
            case lessThenEqual:
                if (!StringUtils.singleton.isEmpty(valueStr)) {
                    searchSqlValue = " <= '" + valueStr + "'";
                }
                break;
        }
        return searchSqlValue;
    }


    private String getInSql(List<String> valueList) {
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
     * 将strs分隔成List，strs的每一项是用，(逗号)分隔的
     *
     * @param strs 逗号分隔的strs
     * @return list集合
     */
    private List<String> toArray(String strs) {
        if (StringUtils.singleton.isEmpty(strs)) {
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
        Collections.addAll(results, strs.split(","));

        return results;
    }
}


