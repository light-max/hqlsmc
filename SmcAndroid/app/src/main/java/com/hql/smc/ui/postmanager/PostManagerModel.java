package com.hql.smc.ui.postmanager;

import com.hql.smc.api.Api;
import com.hql.smc.async.Async;
import com.hql.smc.base.mvp.BaseModel;
import com.hql.smc.data.Pager;
import com.hql.smc.data.PagerData;
import com.hql.smc.data.livedata.UserData;
import com.hql.smc.data.result.MainPost;
import com.hql.smc.data.result.User;

import java.util.ArrayList;
import java.util.List;

public class PostManagerModel extends BaseModel<PostManagerActivity> {
    private final List<MainPost> list = new ArrayList<>();

    public Async.Builder<PagerData<MainPost>> load() {
        Integer page = base.map("page");
        page = page == null ? 1 : page;
        User user = UserData.getInstance().getValue();
        int userId = user == null ? 0 : user.getId();
        return Api.getNewestMainPostByUserId(page, userId)
                .success(data -> {
                    Pager pager = data.getPager();
                    if (pager.getSize() > 0) {
                        list.addAll(data.getList());
                    } else if (pager.getCurrentPage() > 1) {
                        base.toast("没有更多内容了");
                    }
                });
    }

    public List<MainPost> getList() {
        return list;
    }
}
