package com.postpc.tenq.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postpc.tenq.R;
import com.postpc.tenq.models.User;
import com.postpc.tenq.ui.adapters.viewholders.UserNameViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserNamesAdapter extends RecyclerView.Adapter<UserNameViewHolder>{

    private final List<User> userItems;
    private boolean binding;

    public UserNamesAdapter(List<User> items) {
        this.userItems = items;
    }


    @NonNull
    @NotNull
    @Override
    public UserNameViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserNameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserNameViewHolder holder, int position) {
        this.binding = true;
        User currentItem = userItems.get(position);
        holder.userName.setText(currentItem.getName());
        this.binding = false;
    }

    @Override
    public int getItemCount() {
        return userItems.size();
    }
}
