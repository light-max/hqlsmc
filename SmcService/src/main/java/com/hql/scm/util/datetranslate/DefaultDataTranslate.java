package com.hql.scm.util.datetranslate;

public class DefaultDataTranslate implements DateTranslate {
    @DateParameter(empty = true)
    private Long time;

    public DefaultDataTranslate(Long time) {
        this.time = time;
    }
}
