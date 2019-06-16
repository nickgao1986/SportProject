package com.nick.apps.pregnancy11.mypedometer.fragment.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import androidx.annotation.NonNull;


public class ThemeDialogAnim {

    private static final long DURATION_CREATE_MILLIS = 400L;
    private static final long DURATION_FINISH_MILLIS = 400L;

    public ThemeDialogAnim() {
    }

    public void createAnim(@NonNull View animView, AnimationListenerAdapter listenerAdapter) {
        Animation slideAnim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f
        );
        slideAnim.setFillAfter(true);
        slideAnim.setFillEnabled(true);
        slideAnim.setAnimationListener(listenerAdapter);
        slideAnim.setDuration(DURATION_CREATE_MILLIS);
        animView.startAnimation(slideAnim);
    }

    public void finishAnim(View animView, AnimationListenerAdapter listenerAdapter) {
        Animation anim = animView.getAnimation();
        if (anim != null && !anim.hasEnded()) {
            return;
        }
        Animation slideAnim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f
        );
        slideAnim.setFillAfter(true);
        slideAnim.setFillEnabled(true);
        slideAnim.setDuration(DURATION_FINISH_MILLIS);
        slideAnim.setAnimationListener(listenerAdapter);
        animView.startAnimation(slideAnim);
    }

    public static abstract class AnimationListenerAdapter implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

}
