package com.hql.smc.ui.createda;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hql.smc.R;
import com.hql.smc.base.call.Base;
import com.hql.smc.base.mvp.BaseView;

public class CreateDaView extends BaseView<CreateDaActivity> {
    private RecyclerView recycler;
    private EditText nickname;
    private EditText des;
    private TextView submit;

    @Override
    public void onCreate(Base base, Bundle savedInstanceState) {
        super.onCreate(base, savedInstanceState);
        recycler = get(R.id.recycler);
        nickname = get(R.id.review_nickname);
        des = get(R.id.des);
        submit = get(R.id.submit);
    }

    public RecyclerView getRecycler() {
        return recycler;
    }

    public EditText getNickname() {
        return nickname;
    }

    public EditText getDes() {
        return des;
    }

    public TextView getSubmit() {
        return submit;
    }
}
