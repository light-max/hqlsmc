package com.hql.scm.model.td;

import com.hql.scm.util.datetranslate.DateParameter;
import com.hql.scm.util.datetranslate.DateTranslate;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NoticeTD implements DateTranslate {
    private Integer id;
    private String tittle;
    private String des;
    @DateParameter
    private Long createTime;
    private Integer grade;

    public Boolean isTop() {
        return grade != null && grade > 0;
    }
}
