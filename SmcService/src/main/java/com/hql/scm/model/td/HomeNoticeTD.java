package com.hql.scm.model.td;

import com.hql.scm.model.entity.Notice;
import com.hql.scm.util.datetranslate.DateParameter;
import com.hql.scm.util.datetranslate.DateTranslate;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class HomeNoticeTD implements DateTranslate {
    private int id;
    private String title;
    @DateParameter("MM-dd")
    private Long createTime;

    public static List<HomeNoticeTD> list(List<Notice> notices) {
        List<HomeNoticeTD> list = new ArrayList<>();
        for (Notice notice : notices) {
            list.add(HomeNoticeTD.builder()
                    .id(notice.getId())
                    .title(notice.getTitle())
                    .createTime(notice.getCreateTime())
                    .build());
        }
        return list;
    }
}
