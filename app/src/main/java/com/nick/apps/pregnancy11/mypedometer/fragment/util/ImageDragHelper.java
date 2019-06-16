package com.nick.apps.pregnancy11.mypedometer.fragment.util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import com.nick.apps.pregnancy11.mypedometer.fragment.view.DragRelativeLayout;



public class ImageDragHelper implements DragRelativeLayout.OnMDragListener {

    private static final int DURATION = 200;
    private static final int EXIT_DRAG_SLOP = 250;

    private Activity activity;//当前Activity
    private IGetCurrentViewAdapter adapter;//图片适配器
    private View mContainerLayout;//主容器布局
    private View mOtherViewLayout;//其他视图布局
    private ObjectAnimator mBackOrExitAnimator;//拖动视图后退或者Activity退出的动画

    public ImageDragHelper(Activity activity, IGetCurrentViewAdapter adapter, View containerLayout, View otherViewLayout) {
        this.activity = activity;
        this.adapter = adapter;
        this.mContainerLayout = containerLayout;
        this.mOtherViewLayout = otherViewLayout;
    }

    @Override
    public boolean isAnimationRunning() {
        if (mBackOrExitAnimator == null)
            return false;
        return mBackOrExitAnimator.isRunning();
    }

    @Override
    public void onDrag(float dy) {
        View currentView = adapter.getCurrentView();
        if (currentView == null)
            return;
        float translationY = ViewCompat.getTranslationY(currentView);
        float finalTranslationY = translationY + dy;
        ViewCompat.setTranslationY(currentView, finalTranslationY);
        setAlpha(finalTranslationY);
    }

    @Override
    public void onRelease() {
        View currentView = adapter.getCurrentView();
        float from = ViewCompat.getTranslationY(currentView);
        final boolean isExit = Math.abs(from) > EXIT_DRAG_SLOP;
        float to;
        if (isExit) {
            if (from > 0) {
                to = currentView.getHeight();
            } else {
                to = -currentView.getHeight();
            }
        } else {
            to = 0;
        }
        mBackOrExitAnimator = ObjectAnimator.ofFloat(currentView, "translationY", from, to).setDuration(DURATION);
        mBackOrExitAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                setAlpha(value);
            }
        });
        mBackOrExitAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isExit) {
                    activity.finish();
                    activity.overridePendingTransition(0, 0);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mBackOrExitAnimator.start();
    }

    @Override
    public View getDragView() {
        return adapter.getCurrentView();
    }

    /**
     * 设置背景和其他视图的alpha
     *
     * @param translationY
     */
    private void setAlpha(float translationY) {
        float allFadeY = EXIT_DRAG_SLOP * 2;
        float y = Math.abs(translationY);
        if (y > allFadeY) {
            y = allFadeY;
        }
        float alpha = 1 - y / allFadeY;
        Drawable containerBG = mContainerLayout.getBackground();
        if (containerBG != null) {
            containerBG.setAlpha((int) (alpha * 255));
        }
        ViewCompat.setAlpha(mOtherViewLayout, alpha);
    }

    public interface IGetCurrentViewAdapter {
        View getCurrentView();
    }
}
