package com.postpc.tenq.ui.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postpc.tenq.R;

import org.jetbrains.annotations.NotNull;

public class UserNameViewHolder extends RecyclerView.ViewHolder{

    TextView userName;
    ImageView deleteUser;

    public UserNameViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
         userName = itemView.findViewById(R.id.txt_user_name);
         deleteUser = itemView.findViewById(R.id.icon_disable_user);

    }

}
