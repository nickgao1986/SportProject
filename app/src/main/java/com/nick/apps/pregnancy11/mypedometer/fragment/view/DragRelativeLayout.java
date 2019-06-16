package com.nick.apps.pregnancy11.mypedometer.fragment.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;


public class DragRelativeLayout extends RelativeLayout {

    private float mLastY;
    private boolean mIsBeingDragged;
    private int mTouchSlop;
    private OnMDragListener mDragListener;

    public DragRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public DragRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledPagingTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mDragListener != null && mDragListener.isAnimationRunning())
            return super.onInterceptTouchEvent(ev);
        if (mDragListener != null && mDragListener.getDragView() == null)
            return super.onInterceptTouchEvent(ev);
        final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
        float y = ev.getRawY();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mLastY = y;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final float yDiff = Math.abs(y - mLastY);
                if (yDiff > mTouchSlop) {
                    mIsBeingDragged = true;
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    mIsBeingDragged = false;
                }
                mLastY = y;
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                break;
        }
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float y = ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE: {
                float dy = y - mLastY;
                if (mDragListener != null) {
                    mDragListener.onDrag(dy);
                }
                mLastY = y;
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                if (mDragListener != null) {
                    mDragListener.onRelease();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setOnMDragListener(OnMDragListener DragListener) {
        this.mDragListener = DragListener;
    }

    public interface OnMDragListener {

        /**
         * 后退或者关闭的动画是否在运行
         *
         * @return
         */
        boolean isAnimationRunning();

        /**
         * 拖动
         *
         * @param dy
         */
        void onDrag(float dy);

        /**
         * 放开
         */
        void onRelease();

        /**
         * 获取拖动View
         *
         * @return
         */
        View getDragView();
    }
}
