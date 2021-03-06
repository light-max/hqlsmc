package com.hql.scm.fieldcheck.handler;

import com.hql.scm.fieldcheck.FieldCheckException;
import com.hql.scm.fieldcheck.annotation.NumberEnum;

import java.lang.annotation.Annotation;

/**
 * 数字枚举程序
 *
 * @author 李凤强
 */
public class NumberEnumHandler implements BaseHandler {
    @Override
    public void process(Object field, Annotation rule) throws FieldCheckException {
        try {
            long number = Long.parseLong(String.valueOf(field));
            NumberEnum a = (NumberEnum) rule;
            boolean b = false;
            for (long l : a.value()) {
                if (number == l) {
                    b = true;
                    break;
                }
            }
            if (!b) throw new FieldCheckException(a.msg());
        } catch (NumberFormatException e) {
            throw new RuntimeException(field + "无法转换为数字");
        }
    }
}
