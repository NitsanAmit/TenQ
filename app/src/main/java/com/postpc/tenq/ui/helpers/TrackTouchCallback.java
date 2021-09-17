package com.postpc.tenq.ui.helpers;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.postpc.tenq.ui.adapters.TracksAdapter;
import com.postpc.tenq.ui.adapters.viewholders.ItemTouchHelperViewHolder;
import com.postpc.tenq.viewmodels.RoomActivityViewModel;

import org.jetbrains.annotations.NotNull;

public class TrackTouchCallback extends ItemTouchHelper.SimpleCallback {

    private final RoomActivityViewModel model;
    private final TracksAdapter adapter;
    private int fromPosition;
    private int toPosition;

    public TrackTouchCallback(RoomActivityViewModel model, TracksAdapter adapter, boolean isActionsAllowed) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
        if (!isActionsAllowed) {
            this.setDefaultSwipeDirs(0); // enable swiping left if action's not allowed
        }
        this.model = model;
        this.adapter = adapter;
    }

    @Override
    public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
        model.removeTrack(viewHolder.getAdapterPosition());
        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder source, @NonNull @NotNull RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        fromPosition = source.getAdapterPosition();
        toPosition = target.getAdapterPosition();
        return true;
    }

    @Override
    public void onChildDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        // Fade out the view as it is swiped out of the list (delete)
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;
            final float alpha = 1 - Math.abs(dX) / (float) itemView.getWidth();
            itemView.setAlpha(alpha);
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            // Let the view holder know that this item is being moved or dragged
            ((ItemTouchHelperViewHolder) viewHolder).onItemSelected();
        } else if (fromPosition != toPosition) {
            model.reorderPlaylistItem(fromPosition, toPosition, adapter.getCurrentList());
            fromPosition = 0;
            toPosition = 0;
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        // Let the view holder know that it is no longer being moved or dragged
        viewHolder.itemView.setAlpha(1f);
        ((ItemTouchHelperViewHolder) viewHolder).onItemClear();
    }

    public void setIsActionsAllowed(boolean isActionsAllowed) {
        if (!isActionsAllowed)  {
            this.setDefaultSwipeDirs(0);
        }
        else {
            this.setDefaultSwipeDirs(ItemTouchHelper.LEFT);
        }
    }

}