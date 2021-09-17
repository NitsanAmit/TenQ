package com.postpc.tenq.ui.listeners;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.postpc.tenq.ui.listeners.IOnLoadNextPageListener;

import org.jetbrains.annotations.NotNull;

public class PagingScrollListener extends RecyclerView.OnScrollListener {

    private int cursor = 0;
    private final IOnLoadNextPageListener listener;

    public PagingScrollListener(IOnLoadNextPageListener listener) {
        this.listener = listener;
    }

    @Override
    public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        RecyclerView.Adapter<?> adapter = (RecyclerView.Adapter<?>) recyclerView.getAdapter();
        int itemCount = adapter == null ? 0 : adapter.getItemCount();

        int current = layoutManager.findLastVisibleItemPosition();
        if (current != cursor && current == itemCount - 1 && adapter != null) {
            cursor = current;
            listener.loadMore();
        }
    }
}