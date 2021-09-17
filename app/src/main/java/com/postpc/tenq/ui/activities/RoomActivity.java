package com.postpc.tenq.ui.activities;

import static com.postpc.tenq.network.SpotifyClient.CLIENT_ID;
import static com.postpc.tenq.network.SpotifyClient.REDIRECT_URI;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.postpc.tenq.R;
import com.postpc.tenq.core.TenQActivity;
import com.postpc.tenq.core.TenQApplication;
import com.postpc.tenq.databinding.ActivityRoomBinding;
import com.postpc.tenq.models.Room;
import com.postpc.tenq.services.RecorderService;
import com.postpc.tenq.services.SoundAwarenessService;
import com.postpc.tenq.ui.listeners.IOnDragStartListener;
import com.postpc.tenq.ui.listeners.PagingScrollListener;
import com.postpc.tenq.ui.adapters.TracksAdapter;
import com.postpc.tenq.ui.helpers.TrackTouchCallback;
import com.postpc.tenq.ui.helpers.RoomSharingDialogUtil;
import com.postpc.tenq.ui.listeners.ITrackActionListener;
import com.postpc.tenq.ui.listeners.TrackActionListener;
import com.postpc.tenq.ui.views.TrackProgressBar;
import com.postpc.tenq.viewmodels.RoomActivityViewModel;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.PlayerState;


public class RoomActivity extends TenQActivity {

    private static final int RECORD_REQUEST_CODE = 10;
    private ActivityRoomBinding binding;
    private ItemTouchHelper itemTouchHelper;
    private Room room;
    private SoundAwarenessService soundAwarenessService;
    private TrackTouchCallback trackTouchCallback;
    private final ConnectionParams connectionParams = new ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build();

    private final ActivityResultLauncher<Intent> launchRoomSettingsActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            this::onSettingsResult);

    private final ActivityResultLauncher<Intent> songActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), this::onSongsSearchResult);

    private boolean foreignContext = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUiLoadingState(true);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            room = (Room) extras.getSerializable("room");
            if (room == null) {
                String roomId = extras.getString("roomId");
                fetchRoomFromFirestore(roomId);
                return;
            }
        }
        initRecyclerViewAndLoadPlaylist();
        initPlayer();

        soundAwarenessService = ((TenQApplication) getApplication()).getSoundAwarenessService();
        if (soundAwarenessService.getRecorderService().isUserSetRecorderOn() && soundAwarenessService.getRecorderService().isDeviceConnected()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_REQUEST_CODE);
            } else {
                soundAwarenessService.getRecorderService().startRecorder();
            }
        }
    }

    private void initPlayer() {
        // Get the activity's view model
        RoomActivityViewModel model = new ViewModelProvider(this).get(RoomActivityViewModel.class);

        // Subscribe to state and context
        model.getPlayerBitmap().observe(this, bitmap -> Glide.with(this.binding.imgAlbumCover).load(bitmap).into(this.binding.imgAlbumCover));
        model.getPlayerState().observe(this, getPlayerStateObserver());
        model.getPlayerContext().observe(this, playerContext -> {
            if (playerContext != null) {
                if (!playerContext.uri.equals(room.getPlaylist().getUri())) {
                    foreignContext = true;
                    clearPlayerState(new TrackProgressBar(binding.seekBar));
                } else {
                    foreignContext = false;
                }
            }
        });
        model.getPlayerError().observe(this, playerError -> {
            if (playerError == null) return;
            switch (playerError.getType()) {
                case SPOTIFY_NOT_INSTALLED:
                    showDownloadSpotifyDialog();
                    break;
                case SPOTIFY_NOT_AUTHENTICATED:
                    showLogInSpotifyDialog();
                    break;
                case SPOTIFY_CONNECTION_ERROR:
                case PLAYBACK_ERROR:
                    if (playerError.getThrowable() != null) {
                        Toast.makeText(this, "Error connecting to Spotify: " + playerError.getThrowable().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error connecting to Spotify.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        });

        // Bind views
        this.binding.iconPlay.setOnClickListener(v -> model.play(room.getPlaylist().getUri()));
        this.binding.iconPause.setOnClickListener(v -> model.pause());
        this.binding.iconSkipNext.setOnClickListener(v -> model.next());
        this.binding.iconSkipPrevious.setOnClickListener(v -> model.previous());
        this.binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                model.seek(seekBar.getProgress());
            }
        });
    }

    private void clearPlayerState(TrackProgressBar trackProgressBar) {
        this.binding.txtCurrentSong.setVisibility(View.GONE);
        this.binding.imgAlbumCover.setImageBitmap(null);
        this.binding.iconPause.setVisibility(View.GONE);
        this.binding.imgAlbumCoverCard.setVisibility(View.GONE);
        this.binding.iconPlay.setVisibility(View.VISIBLE);
        this.binding.seekBar.setEnabled(false);
        this.binding.iconSkipPrevious.setAlpha(0.6f);
        this.binding.iconSkipNext.setAlpha(0.6f);
        trackProgressBar.update(0);
    }

    private void updatePlayerState(PlayerState playerState, TrackProgressBar trackProgressBar) {
        this.binding.txtCurrentSong.setVisibility(View.VISIBLE);
        this.binding.imgAlbumCoverCard.setVisibility(View.VISIBLE);
        this.binding.seekBar.setEnabled(true);
        this.binding.iconSkipPrevious.setAlpha(1f);
        this.binding.iconSkipNext.setAlpha(1f);

        this.binding.txtCurrentSong.setText(String.format("%s - %s", playerState.track.artist.name, playerState.track.name));
        trackProgressBar.setDuration(playerState.track.duration);
        trackProgressBar.update(playerState.playbackPosition);
        // Update play and pause buttons
        if (playerState.isPaused) {
            this.binding.iconPause.setVisibility(View.GONE);
            this.binding.iconPlay.setVisibility(View.VISIBLE);
        } else {
            this.binding.iconPlay.setVisibility(View.GONE);
            this.binding.iconPause.setVisibility(View.VISIBLE);
        }

        // Update seekbar progress
        if (playerState.playbackSpeed > 0) {
            trackProgressBar.unpause();
        } else {
            trackProgressBar.pause();
        }
    }

    @NonNull
    private Observer<PlayerState> getPlayerStateObserver() {
        TrackProgressBar trackProgressBar = new TrackProgressBar(this.binding.seekBar);
        return playerState -> {
            if (playerState != null && playerState.track != null) {
                if (!foreignContext) {
                    updatePlayerState(playerState, trackProgressBar);
                }
            } else {
                clearPlayerState(trackProgressBar);
            }
        };
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RECORD_REQUEST_CODE) {
            if (soundAwarenessService.getRecorderService().isDeviceConnected() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                soundAwarenessService.getRecorderService().startRecorder();
            }
        }
    }

    private void fetchRoomFromFirestore(String roomId) {
        FirebaseFirestore.getInstance()
                .collection("rooms")
                .document(roomId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        room = task.getResult().toObject(Room.class);
                        initRecyclerViewAndLoadPlaylist();
                        initPlayer();
                    } else {
                        Log.e("RoomActivity", "Can't fetch room", task.getException());
                        binding.txtErrorState.setVisibility(View.VISIBLE);
                        binding.player.setVisibility(View.GONE);
                        binding.fabAddSong.setVisibility(View.GONE);
                        binding.fabExportPlaylist.setVisibility(View.GONE);
                    }
                });
    }

    private void initRecyclerViewAndLoadPlaylist() {
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(room.getName());
        binding.fabAddSong.setOnClickListener(v ->
                songActivityResultLauncher.launch(new Intent(RoomActivity.this, SongSearchActivity.class).putExtra("room", room)));
        // Set the layout manager
        binding.recyclerTracks.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        // Get the activity's view model
        RoomActivityViewModel model = new ViewModelProvider(this).get(RoomActivityViewModel.class);

        // Set "Add song to library" action listener (when user clicks the heart icon)
        ITrackActionListener listener = new TrackActionListener(this::setTrackLiked, index -> model.skipToIndex(room.getPlaylist().getUri(), index));
        IOnDragStartListener onDragStartListener = viewHolder -> itemTouchHelper.startDrag(viewHolder);
        // Create the recycler view adapter
        TracksAdapter adapter = new TracksAdapter(listener, onDragStartListener, binding.progressLoadingMore, room, getAuthService().getCurrentUser());

        // Set a "load next page" listener when user scroll to the bottom of the list
        binding.recyclerTracks.addOnScrollListener(new PagingScrollListener(model::loadNextPage));

        // Set an ItemTouchHelper for removing tracks from the list with a swipe motion
        boolean isActionsAllowed = room.isUserActionsAllowed() | getAuthService().getCurrentUser().getId().equals(room.getHost().getId());
        trackTouchCallback = new TrackTouchCallback(model, adapter, isActionsAllowed);
        itemTouchHelper = new ItemTouchHelper(trackTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerTracks);

        binding.recyclerTracks.setAdapter(adapter);

        // Fetch the first page of tracks
        model.getTracks(room.getPlaylist().getId()).observe(this, list -> {
            adapter.submitList(list);
            if (list != null && binding.progressListLoading.getVisibility() == View.VISIBLE) {
                setUiLoadingState(false);
            }
        });

        model.getIsLoadingPage().observe(this, isLoadingPage -> {
            if (isLoadingPage && binding.progressListLoading.getVisibility() == View.GONE)
                binding.progressLoadingMore.setVisibility(View.VISIBLE);
        });

    }

    private void setTrackLiked(int trackPosition, boolean isLiked) {
        TracksAdapter tracksAdapter = (TracksAdapter) binding.recyclerTracks.getAdapter();
        tracksAdapter.getCurrentList().get(trackPosition).getTrack().setInUserLibrary(isLiked);
        tracksAdapter.notifyItemChanged(trackPosition);
    }

    private void setUiLoadingState(boolean isLoading) {
        int progressBarVisibility = isLoading ? View.VISIBLE : View.GONE;
        int otherViewsVisibility = isLoading ? View.GONE : View.VISIBLE;
        binding.progressListLoading.setVisibility(progressBarVisibility);
        binding.recyclerTracks.setVisibility(otherViewsVisibility);
        binding.player.setVisibility(otherViewsVisibility);
        binding.fabAddSong.setVisibility(otherViewsVisibility);
        binding.fabExportPlaylist.setVisibility(otherViewsVisibility);
    }

    /**
     * inflate the menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_room_activity, menu);
        if (!room.getHost().getId().equals(getAuthService().getCurrentUserId())) {
            menu.findItem(R.id.settings).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * control operations fpr clicking on the action buttons
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.share) { // for debugging use this room id: "yYQK9C19BGy8nt5C4zxY"
            AlertDialog dialog = RoomSharingDialogUtil.getShareDialogForRoom(this, room.getId());
            if (dialog != null && !isFinishing() && !isDestroyed()) {
                dialog.show();
            }
        } else if (itemId == R.id.settings) {
            Intent intent = new Intent(RoomActivity.this, RoomSettingsActivity.class);
            intent.putExtra("roomId", room.getId()); // for debugging use this room id: "yYQK9C19BGy8nt5C4zxY"
            launchRoomSettingsActivity.launch(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    private void onSettingsResult(androidx.activity.result.ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            boolean actionsAllowed = room.isUserActionsAllowed();
            if (intent != null) {
                actionsAllowed = intent.getBooleanExtra("actionsAllowed", false);
            }
            room.setUserActionsAllowed(actionsAllowed);
            trackTouchCallback.setIsActionsAllowed(actionsAllowed | getAuthService().getCurrentUser().getId().equals(room.getHost().getId()));
            TracksAdapter adapter = ((TracksAdapter) binding.recyclerTracks.getAdapter());
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void onSongsSearchResult(androidx.activity.result.ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent == null) return;
            if (intent.getBooleanExtra("didAddSongs", false)) {
                RoomActivityViewModel model = new ViewModelProvider(this).get(RoomActivityViewModel.class);
                model.reloadList();
            }
        }
    }

    @Override
    protected void onStop() {
        new ViewModelProvider(this).get(RoomActivityViewModel.class).disconnectPlayerService();
        super.onStop();
    }


    @Override
    protected void onResume() {
        super.onResume();
        binding.iconPlay.setClickable(false);
        RoomActivityViewModel model = new ViewModelProvider(this).get(RoomActivityViewModel.class);
        // Connect to the user's Spotify app on their phone.
        SpotifyAppRemote.connect(this, connectionParams, new Connector.ConnectionListener() {
            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                model.connectPlayerService(spotifyAppRemote);
                binding.iconPlay.setClickable(true);
            }

            public void onFailure(Throwable throwable) {
                model.spotifyAppRemoteConnectionFailure(throwable);
                binding.iconPlay.setClickable(true);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RecorderService recorder = ((TenQApplication) getApplication()).getSoundAwarenessService().getRecorderService();
        if (recorder.isRecorderOn()) recorder.stopRecorder();
    }

    private void showDownloadSpotifyDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Spotify app not found")
                .setMessage("In order to play your playlist, it is required that the Spotify app is installed on your device.")
                .setNeutralButton("Dismiss", (dialog, i) -> dialog.dismiss())
                .setPositiveButton("Download Spotify", (dialog, i) -> {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.spotify.music")));
                    } catch (android.content.ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.spotify.music")));
                    }
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    private void showLogInSpotifyDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Can't connect to Spotify")
                .setMessage("In order to play your playlist, you must be logged in in your Spotify app. Open it and complete the authentication process, and then return to the TenQ app.")
                .setNeutralButton("Dismiss", (dialog, i) -> dialog.dismiss())
                .setPositiveButton("Open Spotify", (dialog, i) -> {
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.spotify.music");
                    if (launchIntent != null) {
                        startActivity(launchIntent);
                    }
                    dialog.dismiss();
                })
                .create()
                .show();
    }

}