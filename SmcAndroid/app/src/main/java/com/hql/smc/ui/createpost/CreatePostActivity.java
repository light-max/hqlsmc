package com.hql.smc.ui.createpost;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import com.hql.smc.R;
import com.hql.smc.api.Api;
import com.hql.smc.base.activity.BaseActivity;
import com.hql.smc.ui.mainpost.MainpostActivity;
import com.hql.smc.utils.FileUtil;
import com.hql.smc.utils.PermissionUtil;

import java.io.File;

public class CreatePostActivity extends BaseActivity<CreatePostModel, CreatePostView> {
    public static final int PICTURE = 0x2;
    private ImageListAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_post;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        adapter = new ImageListAdapter();
        getView().getRecycler().setAdapter(adapter);
        adapter.setOnDeleteImageListener((data, position) -> {
            deleteImage(data);
        });
        click(R.id.back, this::finish);
        click(getView().getSave(), this::saveDraft);
        click(getView().getAdd(), () -> {
            if (PermissionUtil.localStorage(this)) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                this.startActivityForResult(intent, PICTURE);
            } else {
                toast("没有文件读取权限");
            }
        });
        click(getView().getSubmit(), this::submitDraft);
        loadDraft();
        loadImages();
    }

    private void loadDraft() {
        Api.getDraft().success(data -> {
            getView().getTitle().setText(data.getTitle());
            getView().getContent().setText(data.getContent());
        }).run();
    }

    private void loadImages() {
        Api.getDraftImages().success(data -> {
            adapter.setNewData(data.getUrls());
        }).run();
    }

    private void saveDraft() {
        String title = getView().getTitle().getText().toString();
        String content = getView().getContent().getText().toString();
        Api.setDraft(title, content)
                .error((message, e) -> toast(message))
                .success(data -> toast("草稿已保存"))
                .run();
    }

    private void addImage(File file) {
        Api.addDraftImage(file)
                .before(() -> getView().showDialog())
                .after(() -> getView().dismissDialog())
                .error((message, e) -> toast(message))
                .success(data -> loadImages())
                .run();
    }

    private void deleteImage(String url){
        Api.deleteDraftImage(url)
                .error((message, e) -> toast(message))
                .success(this::loadImages)
                .run();
    }

    private void submitDraft() {
        Api.submitDraft()
                .error((message, e) -> toast(message))
                .success(data -> {
                    toast("发布成功");
                    finish();
                    MainpostActivity.start(this, data.getId());
                }).run();
    }

    @SuppressLint("Recycle")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
        }
        // 选择照片
        else if (requestCode == PICTURE) {
            Uri uri = data.getData();
            assert uri != null;
            String filePath = FileUtil.getFilePathByUri(this, uri);
            File file = new File(filePath);
            addImage(file);
        }
    }
}
