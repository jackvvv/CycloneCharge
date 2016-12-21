package com.gelitenight.waveview.library;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.gelitenight.waveview.library.WaveView;

import java.util.ArrayList;
import java.util.List;

public class WaveHelper {
    private WaveView mWaveView;

    private AnimatorSet mAnimatorSet;
    private ObjectAnimator waterLevelAnim;
    private ObjectAnimator waveShiftAnim;
    private ObjectAnimator amplitudeAnim;
    private List<Animator> animators;

    public WaveHelper(WaveView waveView) {
        mWaveView = waveView;
        animators = new ArrayList<>();
        initAnimation();
    }

    public void start() {
        mWaveView.setShowWave(true);
        if (mAnimatorSet != null) {
            mAnimatorSet.start();
        }
    }

    private void initAnimation() {


        // horizontal animation.
        // wave waves infinitely.
        waveShiftAnim = ObjectAnimator.ofFloat(
                mWaveView, "waveShiftRatio", 0f, 1f);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(1000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());

        // vertical animation.
        // water level increases from 0 to center of WaveView

        // amplitude animation.
        // wave grows big then grows small, repeatedly
        amplitudeAnim = ObjectAnimator.ofFloat(
                mWaveView, "amplitudeRatio", 0.0001f, 0.05f);
        amplitudeAnim.setRepeatCount(ValueAnimator.INFINITE);
        amplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
        amplitudeAnim.setDuration(5000);
        amplitudeAnim.setInterpolator(new LinearInterpolator());
    }

    public void cancel() {
        if (mAnimatorSet != null) {
//            mAnimatorSet.cancel();
            mAnimatorSet.end();
        }
    }

    public void setProgress(float s, float e) {
        animators.clear();
        cancel();
        mAnimatorSet = new AnimatorSet();
        waterLevelAnim = ObjectAnimator.ofFloat(
                mWaveView, "waterLevelRatio", s / 100, e / 100);
        waterLevelAnim.setDuration(1000);
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        animators.add(waveShiftAnim);
        animators.add(waterLevelAnim);
        animators.add(amplitudeAnim);

        mAnimatorSet.playTogether(animators);
        start();
    }
}
