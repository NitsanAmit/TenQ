package com.postpc.tenq.ui.adapters;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.postpc.tenq.R;
import com.postpc.tenq.models.PlaylistTrack;
import com.postpc.tenq.models.Room;
import com.postpc.tenq.models.Track;
import com.postpc.tenq.models.User;
import com.postpc.tenq.ui.adapters.viewholders.TrackViewHolder;
import com.postpc.tenq.ui.helpers.PlaylistTracksDiffItemCallback;
import com.postpc.tenq.ui.listeners.IOnDragStartListener;
import com.postpc.tenq.ui.listeners.ITrackActionListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;


public class TracksAdapter extends ListAdapter<PlaylistTrack, TrackViewHolder> {

    private static final SimpleDateFormat dateFormat;

    static {
        dateFormat = new SimpleDateFormat("dd/MM/yy, hh:mm", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getDefault());
    }

    private final ITrackActionListener actionListener;
    private final IOnDragStartListener dragStartListener;
    private final ProgressBar progressBar;
    private final Room room;
    private final User currentUser;
    private boolean binding;

    public TracksAdapter(ITrackActionListener actionListener, IOnDragStartListener dragStartListener, ProgressBar progressLoading, Room room, User currentUser) {
        super(new PlaylistTracksDiffItemCallback());
        this.actionListener = actionListener;
        this.dragStartListener = dragStartListener;
        this.progressBar = progressLoading;
        this.room = room;
        this.currentUser = currentUser;
    }

    @NonNull
    @NotNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track, parent, false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TrackViewHolder holder, int position) {
        binding = true;
        PlaylistTrack track = getItem(position);
        Track trackData = track.getTrack();
        holder.trackName.setText(trackData.getName());
        holder.trackDescription.setText(getTrackDescription(track));
        if (hasCoverImage(trackData)) {
            Glide.with(holder.albumCover).load(trackData.getAlbum().getImages()[0].getUrl()).into(holder.albumCover);
        } else {
            Glide.with(holder.albumCover).load(R.drawable.ic_music_note).into(holder.albumCover);
        }
        if (trackData.isInUserLibrary()) {
            holder.emptyLikeIcon.setVisibility(View.GONE);
            holder.emptyLikeIcon.setOnClickListener(null);
            holder.fullLikeIcon.setVisibility(View.VISIBLE);
            holder.fullLikeIcon.setOnClickListener(v -> {
                if (!binding) {
                    actionListener.onTrackUnliked(position, trackData.getId());
                }
            });
        } else {
            holder.fullLikeIcon.setVisibility(View.GONE);
            holder.fullLikeIcon.setOnClickListener(null);
            holder.emptyLikeIcon.setVisibility(View.VISIBLE);
            holder.emptyLikeIcon.setOnClickListener(v -> {
                if (!binding) {
                    actionListener.onTrackLiked(position, trackData.getId());
                }
            });
        }

        holder.reorderIcon.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dragStartListener.onStartDrag(holder);
            }
            return false;
        });

        holder.reorderIcon.setVisibility((room.isUserActionsAllowed() | currentUser.getId().equals(room.getHost().getId())) ? View.VISIBLE : View.GONE);
        binding = false;
    }

    @Override
    public void onCurrentListChanged(@NonNull @NotNull List<PlaylistTrack> previousList, @NonNull @NotNull List<PlaylistTrack> currentList) {
        super.onCurrentListChanged(previousList, currentList);
        progressBar.setVisibility(View.GONE);
        notifyDataSetChanged();
    }

    @NotNull
    private String getTrackDescription(PlaylistTrack track) {
        String addedById = track.getAddedBy().getId();
        AtomicReference<String> addedByName = new AtomicReference<>();
        if (addedById.equals(currentUser.getId())) {
            addedByName.set("You");
        } else if (addedById.equals(room.getHost().getId())) {
            addedByName.set(room.getHost().getName());
        }else {
            room.getGuests().stream().filter(g -> g.getId().equals(addedById)).findFirst().ifPresent(user -> addedByName.set(user.getName()));
        }
        return !TextUtils.isEmpty(addedByName.get())
                ? String.format("%s added at %s", addedByName.get(), dateFormat.format(track.getAddedAt()))
                : String.format("Added %s", dateFormat.format(track.getAddedAt()));
    }

    private boolean hasCoverImage(Track trackData) {
        return trackData.getAlbum() != null && trackData.getAlbum().getImages() != null && trackData.getAlbum().getImages().length > 0;
    }


    public void swapItems(int fromPosition, int toPosition) {
        List<PlaylistTrack> items = new ArrayList<>(getCurrentList());
        Collections.swap(items, fromPosition, toPosition);
        submitList(items, () -> {
            notifyItemMoved(fromPosition, toPosition);
        });
    }

}
