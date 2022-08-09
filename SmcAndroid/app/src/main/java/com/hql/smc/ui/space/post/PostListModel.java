package com.hql.smc.ui.space.post;

import com.hql.smc.api.Api;
import com.hql.smc.async.Async;
import com.hql.smc.base.mvp.BaseModel;
import com.hql.smc.data.Pager;
import com.hql.smc.data.PagerData;
import com.hql.smc.data.result.MainPost;

import java.util.ArrayList;
import java.util.List;

public class PostListModel extends BaseModel<PostListFragment> {
    private final List<MainPost> list = new ArrayList<>();

    public Async.Builder<PagerData<MainPost>> load(int userId) {
        Integer page = base.map("page");
        page = page == null ? 1 : page;
        return Api.getNewestMainPostByUserId(page, userId)
                .success(data -> {
                    Pager pager = data.getPager();
                    if (pager.getSize() > 0) {
                        list.addAll(data.getList());
                    } else if (pager.getCurrentPage() > 1) {
                        base.toast("没有更多内容了");
                    }
                    base.map("page", pager.getCurrentPage() + 1);
                });
    }

    public List<MainPost> getList() {
        return list;
    }
}
