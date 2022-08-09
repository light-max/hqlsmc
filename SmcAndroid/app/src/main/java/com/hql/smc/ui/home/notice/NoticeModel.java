package com.hql.smc.ui.home.notice;

import com.hql.smc.api.Api;
import com.hql.smc.async.Async;
import com.hql.smc.base.mvp.BaseModel;
import com.hql.smc.data.Pager;
import com.hql.smc.data.PagerData;
import com.hql.smc.data.result.Notice;

import java.util.ArrayList;
import java.util.List;

public class NoticeModel extends BaseModel<NoticeFragment> {
    private final List<Notice> list = new ArrayList<>();

    public Async.Builder<PagerData<Notice>> load() {
        Integer page = base.map("page");
        page = page == null ? 1 : page;
        return Api.getNoticeList(page).success(data -> {
            Pager pager = data.getPager();
            if (pager.getSize() > 0) {
                list.addAll(data.getList());
            } else if (pager.getCurrentPage() > 1) {
                base.toast("没有更多了");
            }
            base.map("page", pager.getCurrentPage() + 1);
        });
    }

    public List<Notice> getList() {
        return list;
    }
}
