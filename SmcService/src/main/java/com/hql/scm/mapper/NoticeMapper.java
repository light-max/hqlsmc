package com.hql.scm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hql.scm.model.entity.Notice;
import org.apache.ibatis.annotations.Select;

public interface NoticeMapper extends BaseMapper<Notice> {
    @Select("select max(grade) from t_notice where grade>0")
    Integer getTopGrade();
}
