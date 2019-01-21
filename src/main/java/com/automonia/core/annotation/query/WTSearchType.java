package com.automonia.core.annotation.query;


import com.automonia.core.utils.StringUtils;

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
                if (!StringUtils.isEmpty(valueStr)) {
                    searchSqlValue = " like '%" + valueStr + "%'";
                }
                break;
            case likeLeft:
                if (!StringUtils.isEmpty(valueStr)) {
                    searchSqlValue = " like '%" + valueStr + "'";
                }
                break;
            case likeRight:
                if (!StringUtils.isEmpty(valueStr)) {
                    searchSqlValue = " like '" + valueStr + "%'";
                }
                break;
            case nullable:
                if (StringUtils.isEmpty(valueStr)) {
                    searchSqlValue = " is null";
                }
                break;
            case equal:
                if (!StringUtils.isEmpty(valueStr)) {
                    searchSqlValue = " = '" + valueStr + "'";
                }
                break;
            case in:
                if (!StringUtils.isEmpty(valueStr)) {
                    String insql = StringUtils.getInSql(StringUtils.toArray(valueStr));
                    if (!StringUtils.isEmpty(insql)) {
                        searchSqlValue = " in (" + insql + ")";
                    }
                }
                break;
            case notIn:
                if (!StringUtils.isEmpty(valueStr)) {
                    String insql = StringUtils.getInSql(StringUtils.toArray(valueStr));
                    if (!StringUtils.isEmpty(insql)) {
                        searchSqlValue = " not in (" + insql + ")";
                    }
                }
                break;
            case notEqual:
                if (!StringUtils.isEmpty(valueStr)) {
                    searchSqlValue = " != '" + valueStr + "'";
                }
                break;
            case greaterThen:
                if (!StringUtils.isEmpty(valueStr)) {
                    searchSqlValue = " > '" + valueStr + "'";
                }
                break;
            case lessThen:
                if (!StringUtils.isEmpty(valueStr)) {
                    searchSqlValue = " < '" + valueStr + "'";
                }
                break;
            case greaterThenEqual:
                if (!StringUtils.isEmpty(valueStr)) {
                    searchSqlValue = " >= '" + valueStr + "'";
                }
                break;
            case lessThenEqual:
                if (!StringUtils.isEmpty(valueStr)) {
                    searchSqlValue = " <= '" + valueStr + "'";
                }
                break;
        }
        return searchSqlValue;
    }
}
