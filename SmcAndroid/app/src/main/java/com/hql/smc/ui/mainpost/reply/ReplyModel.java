package com.hql.smc.ui.mainpost.reply;

import com.hql.smc.api.Api;
import com.hql.smc.async.Async;
import com.hql.smc.base.mvp.BaseModel;
import com.hql.smc.data.Pager;
import com.hql.smc.data.PagerData;
import com.hql.smc.data.result.Reply;

import java.util.ArrayList;
import java.util.List;

public class ReplyModel extends BaseModel<ReplyFragment> {
    private final List<Reply> list = new ArrayList<>();

    public Async.Builder<PagerData<Reply>> load() {
        return Api.getReply(base.map("postId"), base.map("page"))
                .error((message, e) -> base.toast(message))
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

    public List<Reply> getList() {
        return list;
    }
}
