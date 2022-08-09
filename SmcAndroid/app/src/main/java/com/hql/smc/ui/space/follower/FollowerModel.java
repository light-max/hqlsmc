package com.hql.smc.ui.space.follower;

import com.hql.smc.api.Api;
import com.hql.smc.async.Async;
import com.hql.smc.base.mvp.BaseModel;
import com.hql.smc.data.Pager;
import com.hql.smc.data.PagerData;
import com.hql.smc.data.result.Follow;

import java.util.ArrayList;
import java.util.List;

public class FollowerModel extends BaseModel<FollowerFragment> {
    public List<Follow> list = new ArrayList<>();

    public Async.Builder<PagerData<Follow>> load(int userId) {
        Integer page = base.map("page");
        page = page == null ? 1 : page;
        return Api.getFollowerList(page, userId)
                .success(data -> {
                    Pager pager = data.getPager();
                    if (pager.getSize() > 0) {
                        list.addAll(data.getList());
                    } else if (pager.getCurrentPage() > 0) {
                        base.toast("没有更多了");
                    }
                    base.map("page", pager.getCurrentPage() + 1);
                });
    }

    public List<Follow> getList() {
        return list;
    }
}
