package com.hql.smc.ui.mainpost.reply;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.hql.smc.R;
import com.hql.smc.api.Api;
import com.hql.smc.base.fragment.BaseFragment;
import com.hql.smc.base.recycler.OnLoadMoreListener;
import com.hql.smc.data.result.Reply;
import com.hql.smc.ui.space.SpaceActivity;

public class ReplyFragment extends BaseFragment<ReplyModel, ReplyView> {
    private final MutableLiveData<Integer> liveData = new MediatorLiveData<>();
    private OnGetPostIdListener onGetPostIdListener;
    private OnOptionListener onOptionListener;
    private ReplyListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reply, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        click(R.id.back, () -> {
            if (onOptionListener != null) {
                onOptionListener.onCancelReply();
            }
        });
        // 设置适配器
        adapter = new ReplyListAdapter(getModel().getList());
        getV().getRecycler().setAdapter(adapter);
        adapter.setOnReplyListener((data, position) -> {
            View layout = View.inflate(requireContext(), R.layout.dlg_reply_reply_edit_layout, null);
            EditText content = layout.findViewById(R.id.content);
            AlertDialog dialog = new AlertDialog.Builder(requireContext())
                    .setMessage("回复: @" + data.getNickname())
                    .setView(layout)
                    .setNegativeButton("取消", null)
                    .show();
            layout.findViewById(R.id.send).setOnClickListener(v -> {
                replyReply(data.getId(),
                        content.getText().toString(),
                        dialog::dismiss
                );
            });
        });
        adapter.setOnDeleteListener((data, position) -> {
            new AlertDialog.Builder(requireContext())
                    .setMessage("你确定要删除这条评论吗?")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", (dialog, which) -> {
                        deleteReply(data.getId(), position);
                    }).show();
        });
        adapter.setOnOpenSpaceListener((data, position) -> {
            SpaceActivity.start(requireContext(), data);
        });
        // 当PostId发生变化时加载新的评论
        liveData.observe(this, postId -> {
            map("postId", onGetPostIdListener.getPostId());
            map("page", 1);
            getModel().getList().clear();
            getModel().load().success(data -> {
                adapter.notifyDataSetChanged();
            }).run();
        });
        // 设置上拉加载更多
        getV().getRecycler().addOnScrollListener(new OnLoadMoreListener(listener -> {
            getModel().load()
                    .before(() -> listener.setLoadMoreIng(true))
                    .after(() -> listener.setLoadMoreIng(false))
                    .success(data -> {
                        if (data.getPager().getSize() > 0) {
                            adapter.notifyDataSetChanged();
                        }
                    }).run();
        }));
        // 发送评论
        click(getV().getSend(), () -> {
            Integer postId = map("postId");
            if (postId == null) return;
            Api.sendPostReply(postId, getV().getContentString())
                    .error((message, e) -> toast(message))
                    .success(data -> {
                        getV().getContent().setText("");
                        adapter.getData().add(data);
                        adapter.notifyItemInserted(adapter.getItemCount() - 1);
                        if (onOptionListener != null) {
                            onOptionListener.onSubmitReply(data);
                        }
                    }).run();
        });
    }

    private void deleteReply(int replyId, int position) {
        Api.deleteReply(replyId)
                .error((message, e) -> toast(message))
                .success(() -> {
                    adapter.getData().remove(position);
                    adapter.notifyItemRemoved(position);
                }).run();
    }

    private void replyReply(int replyId, String content, Runnable success) {
        Api.sendReplyReply(replyId,content)
                .success(data -> {
                    success.run();
                    adapter.getData().add(data);
                    adapter.notifyItemInserted(adapter.getItemCount() - 1);
                    if (onOptionListener != null) {
                        onOptionListener.onSubmitReply(data);
                    }
                }).run();
    }

    public void setOnGetPostIdListener(OnGetPostIdListener onGetPostIdListener) {
        this.onGetPostIdListener = onGetPostIdListener;
        liveData.postValue(onGetPostIdListener.getPostId());
    }


    public void setOnOptionListener(OnOptionListener onOptionListener) {
        this.onOptionListener = onOptionListener;
    }

    public interface OnGetPostIdListener {
        int getPostId();
    }

    public interface OnOptionListener {
        void onCancelReply();

        void onSubmitReply(Reply reply);
    }
}
