package com.hql.smc.ui.search.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.hql.smc.R;
import com.hql.smc.api.Api;
import com.hql.smc.base.fragment.NoMvpFragment;
import com.hql.smc.ui.space.SpaceActivity;
import com.hql.smc.view.SearchView;

public class UserSearchFragment extends NoMvpFragment implements SearchView.OnSearchListener {
    private UserListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recycler = get(R.id.recycler);
        adapter = new UserListAdapter();
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener((data, position) -> {
            SpaceActivity.start(requireContext(), data.getId());
        });
    }

    @Override
    public void onSearch(String text) {
        Api.searchUser(text)
                .error((message, e) -> toast(message))
                .success(data -> adapter.setNewData(data))
                .run();
    }
}
