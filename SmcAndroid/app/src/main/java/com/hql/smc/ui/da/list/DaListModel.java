package com.hql.smc.ui.da.list;

import com.hql.smc.api.Api;
import com.hql.smc.async.Async;
import com.hql.smc.base.mvp.BaseModel;
import com.hql.smc.data.Pager;
import com.hql.smc.data.PagerData;
import com.hql.smc.data.result.Da;

import java.util.ArrayList;
import java.util.List;

public class DaListModel extends BaseModel<DaListActivity> {
    private final List<Da> list = new ArrayList<>();

    public Async.Builder<PagerData<Da>> load() {
        Integer page = base.map("page");
        page = page == null ? 1 : page;
        return Api.getMyDaList(page)
                .success(data -> {
                    Pager pager = data.getPager();
                    if (pager.getSize() > 0) {
                        list.addAll(data.getList());
                    } else if (pager.getCurrentPage() > 1) {
                        base.toast("没有更多了");
                    }
                });
    }

    public List<Da> getList() {
        return list;
    }
}
