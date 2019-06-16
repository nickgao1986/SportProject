package com.nick.apps.pregnancy11.mypedometer.fragment.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.imooc.sport.R;

public class CollectButton extends LinearLayout {
    private boolean mIsCollected = false;
    private ImageView ivNotCollect;
    private ImageView ivCollect;
    private CollectButton.OnCollectButtonClickListener mListener;

    public CollectButton(Context context) {
        super(context);
        this.init(context);
    }

    public CollectButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    private void init(Context context) {
//        this.setGravity(Gravity.CENTER);
        this.setGravity(17);
        View contentView = LayoutInflater.from(context).inflate(R.layout.pedo_collect_button, this, true);
        this.ivNotCollect = (ImageView)contentView.findViewById(R.id.iv_no_collect);
        this.ivNotCollect.setVisibility(View.VISIBLE);
        this.ivCollect = (ImageView)contentView.findViewById(R.id.iv_collect);
        this.ivCollect.setVisibility(View.GONE);
        this.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (CollectButton.this.mListener != null) {
                    boolean result = CollectButton.this.mListener.onClick(!CollectButton.this.mIsCollected);
                    if (result) {
                        CollectButton.this.setCollectState(!CollectButton.this.mIsCollected);
                        CollectButton.this.startCollectAnimation(CollectButton.this.mIsCollected);
                    }
                }

            }
        });
    }

    private void startCollectAnimation(boolean isCollected) {
        this.stopAnimation();
        ImageView ivShow;
        ImageView ivHide;
        if (isCollected) {
            ivShow = this.ivCollect;
            ivHide = this.ivNotCollect;
        } else {
            ivShow = this.ivNotCollect;
            ivHide = this.ivCollect;
        }

        AnimationSet hideAnimationSet = new AnimationSet(false);
        hideAnimationSet.setFillAfter(true);
        ScaleAnimation s1 = new ScaleAnimation(1.0F, 0.0F, 1.0F, 0.0F, 1, 0.5F, 1, 0.5F);
        s1.setDuration(240L);
        s1.setInterpolator(new AccelerateDecelerateInterpolator());
        hideAnimationSet.addAnimation(s1);
        AlphaAnimation a1 = new AlphaAnimation(1.0F, 0.0F);
        a1.setDuration(240L);
        a1.setInterpolator(new DecelerateInterpolator());
        hideAnimationSet.addAnimation(a1);
        AnimationSet showAnimatorSet = new AnimationSet(false);
        ScaleAnimation s2 = new ScaleAnimation(0.0F, 1.0F, 0.0F, 1.0F, 1, 0.5F, 1, 0.5F);
        s2.setDuration(240L);
        showAnimatorSet.addAnimation(s2);
        AlphaAnimation a2 = new AlphaAnimation(0.0F, 1.0F);
        a2.setDuration(240L);
        a2.setInterpolator(new AccelerateInterpolator());
        showAnimatorSet.addAnimation(a2);
        ivHide.setVisibility(0);
        ivHide.startAnimation(hideAnimationSet);
        ivShow.setVisibility(0);
        ivShow.startAnimation(showAnimatorSet);
    }

    public void setCollectState(boolean isCollected) {
        this.mIsCollected = isCollected;
        this.stopAnimation();
        if (isCollected) {
            this.ivCollect.setVisibility(View.VISIBLE);
            this.ivNotCollect.setVisibility(View.GONE);
        } else {
            this.ivCollect.setVisibility(View.GONE);
            this.ivNotCollect.setVisibility(View.VISIBLE);
        }

    }

    public void setOnCollectButtonClickListener(CollectButton.OnCollectButtonClickListener listener) {
        this.mListener = listener;
    }

    public boolean isCollected() {
        return this.mIsCollected;
    }

    public void stopAnimation() {
        this.ivCollect.clearAnimation();
        this.ivNotCollect.clearAnimation();
    }

    public void setCollectDrawables(int unCollectResId, int collectResId) {
        this.ivCollect.setImageResource(collectResId);
        this.ivNotCollect.setImageResource(unCollectResId);
    }

    public interface OnCollectButtonClickListener {
        boolean onClick(boolean var1);
    }
}
