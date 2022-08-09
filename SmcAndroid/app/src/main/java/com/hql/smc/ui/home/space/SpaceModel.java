package com.hql.smc.ui.home.space;

import android.os.Bundle;

import com.hql.smc.api.Api;
import com.hql.smc.async.Async;
import com.hql.smc.base.call.Base;
import com.hql.smc.base.mvp.BaseModel;
import com.hql.smc.data.Pager;
import com.hql.smc.data.PagerData;
import com.hql.smc.data.result.MainPost;

import java.util.ArrayList;
import java.util.List;

public class SpaceModel extends BaseModel<SpaceFragment> {
    private final List<MainPost> list = new ArrayList<>();

    @Override
    public void onCreate(Base base, Bundle savedInstanceState) {
        super.onCreate(base, savedInstanceState);
        base.map("page", 1);
    }

    public Async.Builder<PagerData<MainPost>> load() {
        return Api.getSpaceMainPost(base.map("page"))
                .error((message, e) -> base.toast(message))
                .success(data -> {
                    Pager pager = data.getPager();
                    if (pager.getSize() > 0) {
                        base.map("page", pager.getCurrentPage() + 1);
                        list.addAll(data.getList());
                    } else if (pager.getCurrentPage() > 1) {
                        base.toast("没有更多内容了");
                    }
                });
    }

    public List<MainPost> getList() {
        return list;
    }

    public void clear() {
        base.map("page", 1);
        list.clear();
    }
}
