package com.postpc.tenq.ui.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postpc.tenq.R;

import org.jetbrains.annotations.NotNull;

public class ExistingRoomViewHolder extends RecyclerView.ViewHolder {

    TextView roomName;
    TextView roomDescription;
    ImageView indicator;
    ImageView closeRoomIcon;
    ImageView exportRoomIcon;
    ImageView deleteRoomIcon;

    public ExistingRoomViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        roomName = itemView.findViewById(R.id.txt_room_name);
        roomDescription = itemView.findViewById(R.id.txt_room_description);
        indicator = itemView.findViewById(R.id.indicator_room_status);
        closeRoomIcon = itemView.findViewById(R.id.icon_close_room);
        exportRoomIcon = itemView.findViewById(R.id.icon_export_room);
        deleteRoomIcon = itemView.findViewById(R.id.icon_delete_room);
    }

}
