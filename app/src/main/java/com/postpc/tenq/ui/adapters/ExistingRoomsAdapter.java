package com.postpc.tenq.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postpc.tenq.R;
import com.postpc.tenq.models.Room;
import com.postpc.tenq.ui.listeners.IRoomActionListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExistingRoomsAdapter extends RecyclerView.Adapter<ExistingRoomViewHolder> {

    private final List<Room> items;
    private final IRoomActionListener actionListener;
    private boolean binding;


    public ExistingRoomsAdapter(List<Room> items, IRoomActionListener actionListener) {
        this.items = items;
        this.actionListener = actionListener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @NotNull
    @Override
    public ExistingRoomViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_existing_room, parent, false);
        return new ExistingRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ExistingRoomViewHolder holder, int position) {
        binding = true;
        Room room = this.items.get(position);
        holder.roomName.setText(room.getName());
        holder.roomDescription.setText(String.format("Opened by %s at %s",
                room.getHost().getName(), room.getCreationTime()));
        if (room.isActive()) {
            holder.indicator.setVisibility(View.VISIBLE);
            holder.closeRoomIcon.setVisibility(View.VISIBLE);
            holder.deleteRoomIcon.setVisibility(View.GONE);
        } else {
            holder.indicator.setVisibility(View.GONE);
            holder.closeRoomIcon.setVisibility(View.GONE);
            holder.deleteRoomIcon.setVisibility(View.VISIBLE);
        }
        holder.closeRoomIcon.setOnClickListener(v -> {
            if (!binding) actionListener.onRoomClose(room);
        });
        holder.deleteRoomIcon.setOnClickListener(v -> {
            if (!binding) actionListener.onRoomDelete(room);
        });
        holder.exportRoomIcon.setOnClickListener(v -> {
            if (!binding) actionListener.onRoomExport(room);
        });
        binding = false;
    }
}
