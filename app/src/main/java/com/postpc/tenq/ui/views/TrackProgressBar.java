package com.postpc.tenq.ui.views;

import android.os.Handler;
import android.widget.SeekBar;

public class TrackProgressBar {

    private static final int LOOP_DURATION = 500;
    private final SeekBar seekBarView;
    private final Handler handler;

    private final Runnable mSeekRunnable =
            new Runnable() {
                @Override
                public void run() {
                    int progress = seekBarView.getProgress();
                    seekBarView.setProgress(progress + LOOP_DURATION);
                    handler.postDelayed(mSeekRunnable, LOOP_DURATION);
                }
            };

    public TrackProgressBar(SeekBar seekBar) {
        this.seekBarView = seekBar;
        handler = new Handler();
    }

    public void setDuration(long duration) {
        seekBarView.setMax((int) duration);
    }

    public void update(long progress) {
        seekBarView.setProgress((int) progress);
    }

    public void pause() {
        handler.removeCallbacks(mSeekRunnable);
    }

    public void unpause() {
        handler.removeCallbacks(mSeekRunnable);
        handler.postDelayed(mSeekRunnable, LOOP_DURATION);
    }
}
