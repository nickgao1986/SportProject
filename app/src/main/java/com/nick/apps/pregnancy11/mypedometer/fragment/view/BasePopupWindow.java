package com.nick.apps.pregnancy11.mypedometer.fragment.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.imooc.sport.R;


public abstract class BasePopupWindow<T> extends PopupWindow implements View.OnClickListener {

    protected View mView;
    protected Activity mActivity;
    protected T mData;

    public BasePopupWindow(Activity activity) {
        super(activity);
        init(activity);
    }

    public final void bindData(@Nullable T data) {
        this.mData = data;
        bindData(mActivity, mData);
    }

    protected abstract void bindData(Activity activity, @Nullable T data);

    protected abstract @LayoutRes
    int getLayoutId();

    protected abstract void initView(@NonNull View view);

    protected void init(Activity activity) {
        try {
            this.mActivity = activity;
            this.mView = LayoutInflater.from(activity).inflate(getLayoutId(), null);
            if (mView != null) {
                this.initView(mView);
                this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                this.setOutsideTouchable(false);
                this.setAnimationStyle(R.style.PopupWindowAnimation);
                this.setBackgroundDrawable(new ColorDrawable(0x00000000));
                this.setFocusable(true);
                this.setContentView(mView);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 && mActivity.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    this.setAttachedInDecor(false);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (mView == null) {return;}
        try {
            parent = checkParent(parent);
            super.showAtLocation(parent, gravity, x, y);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showAsDropDown(View parent) {
        if (mView == null) {return;}
        try {
            parent = checkParent(parent);
            super.showAsDropDown(parent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (mView == null) {return;}
        try {
            anchor = checkParent(anchor);
            super.showAsDropDown(anchor, xoff, yoff);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (mView == null) {return;}
        try {
            anchor = checkParent(anchor);
            super.showAsDropDown(anchor, xoff, yoff, gravity);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private View checkParent(View view) {
        if (view != null) {return view;}
        try {
            return mActivity.getWindow().getDecorView();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

}