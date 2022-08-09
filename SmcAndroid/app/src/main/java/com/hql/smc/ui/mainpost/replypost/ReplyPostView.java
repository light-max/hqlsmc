package com.hql.smc.ui.mainpost.replypost;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hql.smc.R;
import com.hql.smc.base.call.Base;
import com.hql.smc.base.mvp.BaseView;

import java.io.File;
import java.util.List;

public class ReplyPostView extends BaseView<ReplyPostFragment> {
    private RecyclerView recycler;
    private ImageListAdapter adapter;
    private EditText content;
    private AlertDialog dialog;

    @Override
    public void onViewCreated(Base base, Bundle savedInstanceState) {
        content = get(R.id.content);
        adapter = new ImageListAdapter();
        recycler = get(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(base.getContext(), 3));
        recycler.setAdapter(adapter);
        dialog = new AlertDialog.Builder(base.getContext())
                .setView(new ProgressBar(base.getContext()))
                .setCancelable(false)
                .create();
    }

    public ImageListAdapter getAdapter() {
        return adapter;
    }

    public List<File> getImages() {
        return adapter.getData();
    }

    public String getContent() {
        return content.getText().toString();
    }

    public void clearData() {
        adapter.getData().clear();
        adapter.notifyDataSetChanged();
        content.setText("");
    }

    public void showDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
