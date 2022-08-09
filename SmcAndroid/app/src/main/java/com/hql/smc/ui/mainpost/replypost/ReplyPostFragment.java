package com.hql.smc.ui.mainpost.replypost;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.hql.smc.R;
import com.hql.smc.api.Api;
import com.hql.smc.base.fragment.BaseFragment;
import com.hql.smc.data.result.Post;
import com.hql.smc.utils.FileUtil;
import com.hql.smc.utils.PermissionUtil;

import java.io.File;

public class ReplyPostFragment extends BaseFragment<ReplyPostModel, ReplyPostView> {
    public static final int PICTURE = 0x2;

    private OnOptionListener onOptionListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reply_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        click(R.id.back, () -> {
            hideKeyBody();
            getV().clearData();
            if (onOptionListener != null) {
                onOptionListener.onCancelReply();
            }
        });
        click(R.id.submit, this::send);
        click(R.id.add_image, () -> {
            if (PermissionUtil.localStorage(requireActivity())) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, PICTURE);
            } else {
                toast("没有文件读取权限");
            }
        });
    }

    private void send() {
        if (onOptionListener == null) {
            return;
        }
        Api.sendPost(onOptionListener.getMainPostId(), getV().getContent(), getV().getImages())
                .error((message, e) -> toast(message))
                .before(() -> getV().showDialog())
                .after(() -> getV().dismissDialog())
                .success(data -> {
                    hideKeyBody();
                    getV().clearData();
                    onOptionListener.onSubmitReplyPost(data);
                }).run();
    }

    @SuppressLint("Recycle")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
        }
        // 选择照片
        else if (requestCode == PICTURE) {
            Uri uri = data.getData();
            assert uri != null;
            String filePath = FileUtil.getFilePathByUri(requireContext(), uri);
            File file = new File(filePath);
            ImageListAdapter adapter = getV().getAdapter();
            adapter.getData().add(file);
            adapter.notifyItemInserted(adapter.getItemCount() - 1);
        }
    }

    private void hideKeyBody() {
        try {
            FragmentActivity activity = requireActivity();
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnOptionListener(OnOptionListener onOptionListener) {
        this.onOptionListener = onOptionListener;
    }

    public interface OnOptionListener {
        int getMainPostId();

        void onCancelReply();

        void onSubmitReplyPost(Post post);
    }
}
