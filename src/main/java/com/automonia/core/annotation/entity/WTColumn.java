package com.automonia.core.annotation.entity;

import java.lang.annotation.*;

/**
 * @author 作者 温腾
 * @创建时间 2017年5月9日 上午12:37:37
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WTColumn {

    /**
     * 对应数据库的表明
     */
    String name() default "";

}
