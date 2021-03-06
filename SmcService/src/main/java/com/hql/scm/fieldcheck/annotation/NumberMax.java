package com.hql.scm.fieldcheck.annotation;

import com.hql.scm.fieldcheck.Handler;
import com.hql.scm.fieldcheck.handler.NumberSizeHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数字的最大值
 *
 * @author 李凤强
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Handler(NumberSizeHandler.class)
public @interface NumberMax {
    /**
     * 错误信息
     * 可以使用${value}将{@link #value()}输出到错误信息中
     */
    String msg() default "";

    long value();
}
