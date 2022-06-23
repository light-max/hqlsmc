package com.hql.scm.fieldcheck.handler;

import com.hql.scm.fieldcheck.FieldCheckException;
import com.hql.scm.fieldcheck.annotation.StringRegex;

import java.lang.annotation.Annotation;
import java.util.regex.PatternSyntaxException;

/**
 * 正则表达式处理程序
 *
 * @author 李凤强
 */
public class StringRegexHandler implements BaseHandler {
    @Override
    public void process(Object field, Annotation rule) throws FieldCheckException {
        try {
            String string = String.valueOf(field);
            StringRegex regex = (StringRegex) rule;
            if (!string.matches(regex.value())) {
                throw new FieldCheckException(regex.msg());
            }
        } catch (PatternSyntaxException e) {
            System.err.println("正则表达式错误");
            throw new RuntimeException(e);
        }
    }
}
