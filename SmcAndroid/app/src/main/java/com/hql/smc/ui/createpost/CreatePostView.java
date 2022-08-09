package com.hql.smc.ui.createpost;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hql.smc.R;
import com.hql.smc.base.call.Base;
import com.hql.smc.base.mvp.BaseView;

public class CreatePostView extends BaseView<CreatePostActivity> {
    private RecyclerView recycler;
    private TextView save;
    private TextView add;
    private Button submit;
    private EditText title;
    private EditText content;
    private AlertDialog dialog;

    @Override
    public void onCreate(Base base, Bundle savedInstanceState) {
        super.onCreate(base, savedInstanceState);
        recycler = get(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(base.getContext(), 3));
        save = get(R.id.save);
        add = get(R.id.add_image);
        submit = get(R.id.submit);
        title = get(R.id.title);
        content = get(R.id.content);
    }

    public RecyclerView getRecycler() {
        return recycler;
    }

    public TextView getSave() {
        return save;
    }

    public TextView getAdd() {
        return add;
    }

    public Button getSubmit() {
        return submit;
    }

    public EditText getTitle() {
        return title;
    }

    public EditText getContent() {
        return content;
    }

    public void showDialog() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(base)
                    .setCancelable(false)
                    .setView(new ProgressBar(base))
                    .create();
        }
        dialog.show();
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
