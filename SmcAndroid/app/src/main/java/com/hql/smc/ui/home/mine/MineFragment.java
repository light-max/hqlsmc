package com.hql.smc.ui.home.mine;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.hql.smc.R;
import com.hql.smc.api.Api;
import com.hql.smc.api.ExRequestBuilder;
import com.hql.smc.base.fragment.NoMvpFragment;
import com.hql.smc.data.livedata.UserData;
import com.hql.smc.ui.da.list.DaListActivity;
import com.hql.smc.ui.login.LoginActivity;
import com.hql.smc.ui.postmanager.PostManagerActivity;
import com.hql.smc.ui.review.list.ReviewListActivity;
import com.hql.smc.ui.useinfo.PasswordSetActivity;
import com.hql.smc.ui.useinfo.UserInfoSetActivity;
import com.hql.smc.utils.FileUtil;
import com.hql.smc.utils.GlideUtils;
import com.hql.smc.utils.PermissionUtil;

import java.io.File;

public class MineFragment extends NoMvpFragment {
    public static final int PICTURE = 0x2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView head = get(R.id.head);
        TextView nickname = get(R.id.nickname);
        TextView des = get(R.id.des);
        UserData.getInstance().observe(this, user -> {
            Glide.with(this)
                    .load(ExRequestBuilder.getUrl("/head/" + user.getId()))
                    .apply(GlideUtils.skipCache())
                    .into(head);
            nickname.setText(user.getNickname());
            des.setText(user.getDes());
        });
        click(R.id.head, () -> {
            if (PermissionUtil.localStorage(getActivity())) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                this.startActivityForResult(intent, PICTURE);
            } else {
                toast("没有文件读取权限");
            }
        });
        click(R.id.set_info, () -> {
            Intent intent = new Intent(requireContext(), UserInfoSetActivity.class);
            requireActivity().startActivity(intent);
        });
        click(R.id.my_da, () -> {
            Intent intent = new Intent(requireContext(), DaListActivity.class);
            requireActivity().startActivity(intent);
        });
        click(R.id.review_da, () -> {
            Intent intent = new Intent(requireContext(), ReviewListActivity.class);
            requireActivity().startActivity(intent);
        });
        click(R.id.post, () -> {
            Intent intent = new Intent(requireContext(), PostManagerActivity.class);
            requireActivity().startActivity(intent);
        });
        click(R.id.set_pwd, () -> {
            Intent intent = new Intent(requireContext(), PasswordSetActivity.class);
            requireActivity().startActivity(intent);
        });
        click(R.id.logout, () -> {
            new AlertDialog.Builder(getContext())
                    .setMessage("你确定要退出登录吗?")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", (dialog, which) -> {
                        Api.logout().run();
                        requireActivity().finish();
                        Intent intent = new Intent(requireContext(), LoginActivity.class);
                        requireActivity().startActivity(intent);
                    }).show();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
        }
        // 选择照片
        else if (requestCode == PICTURE) {
            Uri uri = data.getData();
            assert uri != null;
            String filePath = FileUtil.getFilePathByUri(getContext(), uri);
            File file = new File(filePath);
            Api.setHead(file)
                    .error((message, e) -> toast(message))
                    .success(() -> {
                        UserData user = UserData.getInstance();
                        user.postValue(user.getValue());
                    }).run();
        }
    }
}
