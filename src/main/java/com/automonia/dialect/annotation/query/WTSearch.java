package com.automonia.dialect.annotation.query;


import java.lang.annotation.*;

/**
 * 声明查询方式
 * <p>
 * Created by jackie on
 * 2017/11/7.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WTSearch {

    /**
     * 查询的实体对象的属性名称
     * 默认采用该注解标注的属性名称
     */
    String fieldName() default "";

    /**
     * 查询的实体对象名称
     * 默认查询的WTModel实体对象指定的实体对象
     */
    String entityName() default "";

    String group() default "";

    /**
     * 关联其他数据库表的情况下
     * select xx from xxx left join xxxx on xxx.fromOn = xxxx.joinOn
     */
    String fromOn() default "";

    String joinOn() default "";

    /**
     * 是否考虑数值为空的情况
     * false
     * 数值为空，则不进行关联查询
     * 数值不为空，则进行关联查询
     * true
     * 数值为空也进行关联查询
     *
     * @return Boolean
     */
    boolean nullable() default false;

    /**
     * 查询方式,详情查看WTSearchType枚举
     */
    WTSearchType type() default WTSearchType.equal;


    // select * from xx left join midEntityName m on xx.fromON = m.midFromOn left join entityName e on e.joinOn = m.midJoinON

    /**
     * 三级关联查询使用的中间实体对象名称
     */
    String midEntityName() default "";

    /**
     * 中间表与from的查询的关联属性名称
     */
    String midFromOn() default "";

    /**
     * 中间表与第三级关联查询的属性名称
     */
    String midJoinOn() default "";
}
