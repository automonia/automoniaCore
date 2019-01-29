package com.automonia.dialect.annotation.entity;

import java.lang.annotation.*;

/**
 * 用于标记属性的来源，
 * 用于WTModel类型
 * 模型对象的属性可能是两个实体类的组合，继承其中一个类，扩展另一个的属性，这时候该注解就可以实现属性的扩展。
 * 转成sql语句查询时候，会解析entityName并做左连接，查询并加载该属性, 之后将查询结果反射到该属性上
 *
 * @author 作者 温腾
 * @创建时间 2017年5月9日 上午12:37:37
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WTField {

    /**
     * 实体扩展类的扩展对象的属性名
     *
     * @return 其他实体对象的属性名
     */
    String fieldName() default "";

    /**
     * 扩展实体类的所属性名所在类的类名
     *
     * @return 类名
     */
    String entityName() default "";

    /**
     * from实体对象用于关联的字段名称，被关联的使用id主键
     * <p>
     * select xx from xxx left join xxxx on xxx.fromOn = xxxx.joinOn
     */
    String fromOn() default "";

    String joinOn() default "";


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
