package com.hql.smc.ui.mainpost;

import android.os.Bundle;

import com.hql.smc.api.Api;
import com.hql.smc.async.Async;
import com.hql.smc.base.call.Base;
import com.hql.smc.base.mvp.BaseModel;
import com.hql.smc.data.Pager;
import com.hql.smc.data.PagerData;
import com.hql.smc.data.result.Post;

import java.util.ArrayList;
import java.util.List;

public class MainpostModel extends BaseModel<MainpostActivity> {
    private List<Post> posts = new ArrayList<>();

    @Override
    public void onCreate(Base base, Bundle savedInstanceState) {
        super.onCreate(base, savedInstanceState);
        base.map("postPage", 1);
    }

    /**
     * 获取回帖
     */
    public Async.Builder<PagerData<Post>> loadPost(int mainPostId) {
        return Api.getReplyPost(base.map("postPage"), mainPostId)
                .error((message, e) -> base.toast(message))
                .success(data -> {
                    Pager pager = data.getPager();
                    if (pager.getSize() > 0) {
                        base.map("page", pager.getCurrentPage() + 1);
                        posts.addAll(data.getList());
                    } else if (pager.getCurrentPage() > 1) {
                        base.toast("没有更多内容了");
                    }
                    base.map("postPage", pager.getCurrentPage() + 1);
                });
    }

    public List<Post> getPosts() {
        return posts;
    }
}
