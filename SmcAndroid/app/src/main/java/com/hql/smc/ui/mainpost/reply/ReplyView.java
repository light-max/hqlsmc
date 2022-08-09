package com.hql.smc.ui.mainpost.reply;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.hql.smc.R;
import com.hql.smc.base.call.Base;
import com.hql.smc.base.mvp.BaseView;

public class ReplyView extends BaseView<ReplyFragment> {
    private RecyclerView recycler;
    private EditText content;
    private Button send;

    @Override
    public void onViewCreated(Base base, Bundle savedInstanceState) {
        recycler = get(R.id.recycler);
        content = get(R.id.content);
        send = get(R.id.send);
    }

    public RecyclerView getRecycler() {
        return recycler;
    }

    public EditText getContent() {
        return content;
    }

    public String getContentString() {
        return getContent().getText().toString();
    }

    public Button getSend() {
        return send;
    }
}
