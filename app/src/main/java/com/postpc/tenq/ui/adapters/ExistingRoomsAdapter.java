package com.postpc.tenq.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postpc.tenq.R;
import com.postpc.tenq.models.Room;
import com.postpc.tenq.ui.adapters.viewholders.ExistingRoomViewHolder;
import com.postpc.tenq.ui.listeners.IRoomActionListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ExistingRoomsAdapter extends RecyclerView.Adapter<ExistingRoomViewHolder> {

    private static final SimpleDateFormat dateFormat;

    static {
        dateFormat = new SimpleDateFormat("dd/MM/yy, hh:mm", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getDefault());
    }

    private final List<Room> items;
    private final IRoomActionListener actionListener;
    private final View.OnClickListener onItemClick;
    private boolean binding;
    private final String currentUserId;


    public ExistingRoomsAdapter(List<Room> items, IRoomActionListener actionListener, View.OnClickListener onItemClick, String currentUserId) {
        this.items = items;
        this.actionListener = actionListener;
        this.onItemClick = onItemClick;
        this.currentUserId = currentUserId;
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
        view.setOnClickListener(onItemClick);
        return new ExistingRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ExistingRoomViewHolder holder, int position) {
        binding = true;
        Room room = this.items.get(position);
        boolean isHost = room.getHost().getId().equals(currentUserId);
        holder.roomName.setText(room.getName());
        holder.roomDescription.setText(String.format("Opened by %s at %s",
                room.getHost().getName(), dateFormat.format(new Date(room.getCreationTime()))));
        if (room.isActive()) {
            holder.indicator.setVisibility(View.VISIBLE);
            if (isHost) {
                holder.closeRoomIcon.setVisibility(View.VISIBLE);
            }
            holder.deleteRoomIcon.setVisibility(View.GONE);
        } else {
            holder.indicator.setVisibility(View.GONE);
            holder.closeRoomIcon.setVisibility(View.GONE);
            if (isHost) {
                holder.deleteRoomIcon.setVisibility(View.VISIBLE);
            }
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
