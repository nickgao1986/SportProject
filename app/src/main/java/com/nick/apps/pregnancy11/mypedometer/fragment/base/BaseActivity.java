package com.nick.apps.pregnancy11.mypedometer.fragment.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import com.imooc.sport.R;

public abstract class BaseActivity extends AppCompatActivity implements PedoActionBar.ActionBarListener {

    /**
     * 上下文
     */
    protected Activity mContext;

    /**
     * 主布局
     */
    protected ViewGroup mBaseFragment;
    /**
     * 内容布局
     */
    private ViewGroup mContentLayout;

    protected PedoActionBar mActionBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mBaseFragment = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.pedo_activity_base, null);
        mContentLayout = ((RelativeLayout) mBaseFragment.findViewById(R.id.body));
        // 当取到的title为null或者为0 则不显示actionbar
        ViewStub stub = (ViewStub) mBaseFragment.findViewById(R.id.actionbar_title);
        if (getTitleString() != null && !getTitleString().equals(0)) {
            mActionBar = (PedoActionBar) stub.inflate();
            mActionBar.setTitle(getTitleString());
            // 左键监听
            mActionBar.getLeftButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLeftButtonClick();
                }
            });
            // 右键监听
            mActionBar.getRightButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRightButtonClick();
                }
            });

            initLeftButton(mActionBar.getLeftButton());
            initRightButton(mActionBar.getRightButton());
        }

        setContentView(getContentView());
        onViewCreated();
    }



    @Override
    public void initLeftButton(Button leftButton) {

    }

    @Override
    public void initRightButton(Button rightButton) {

    }

    public void onViewCreated() {
    }

    @Override
    public void setContentView(View view) {
        if (view != null) {
            mBaseFragment.removeView(mContentLayout);
            mBaseFragment.addView(view, mContentLayout.getLayoutParams());
            mContentLayout = (ViewGroup) view;
        }
        super.setContentView(mBaseFragment);
    }


    /**
     * 重写此方法，实现设置新布局时可以同时带入actionbar
     *
     * @param layoutResID
     */
    @Override
    public void setContentView(int layoutResID) {
        if (layoutResID != 0) {
            View view = LayoutInflater.from(this).inflate(layoutResID, null);
            setContentView(view);
        } else {
            setContentView(null);
        }
    }


}
