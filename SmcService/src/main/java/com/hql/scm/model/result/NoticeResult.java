package com.hql.scm.model.result;

import com.hql.scm.model.entity.Notice;
import com.hql.scm.util.datetranslate.DateFormatUtil;
import lombok.Getter;

@Getter
public class NoticeResult {
    private final int id;
    private final String title;
    private final String des;
    private final long createTime;
    private final int grade;

    private final boolean hasImages;
    private final Images images;
    private final boolean top;
    private final String time;
    private final String monthday;

    public NoticeResult(Notice notice, Images images) {
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.des = notice.getDes();
        this.createTime = notice.getCreateTime();
        this.grade = notice.getGrade();
        this.hasImages = images != null
                && !(images.getUrls() == null)
                && !images.getUrls().isEmpty();
        this.images = images;
        this.top = notice.grade > 0;
        this.time = DateFormatUtil.format(createTime, "yyyy-MM-dd HH:mm:ss");
        this.monthday = DateFormatUtil.format(createTime, "MM-dd");
    }
}
