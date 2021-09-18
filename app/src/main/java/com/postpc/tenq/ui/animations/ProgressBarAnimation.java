package com.postpc.tenq.ui.animations;

import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

public class ProgressBarAnimation extends Animation {

    private final ProgressBar progressBar;

    public ProgressBarAnimation(ProgressBar progressBar) {
        super();
        this.progressBar = progressBar;
        progressBar.setInterpolator(new DecelerateInterpolator());
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = 100f * interpolatedTime;
        progressBar.setProgress((int) value);
    }

}
