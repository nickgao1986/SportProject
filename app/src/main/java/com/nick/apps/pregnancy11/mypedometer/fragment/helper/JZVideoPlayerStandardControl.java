package com.nick.apps.pregnancy11.mypedometer.fragment.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import cn.jzvd.JZVideoPlayerStandard;
import com.imooc.sport.R;


public class JZVideoPlayerStandardControl extends JZVideoPlayerStandard {

    public static onClickFullScreenListener onClickFullScreenListener;

    public JZVideoPlayerStandardControl(Context context) {
        super(context);
    }

    public JZVideoPlayerStandardControl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static void setOnClickFullScreenListener(onClickFullScreenListener listener) {
        onClickFullScreenListener = listener;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.fullscreen) {
            if (onClickFullScreenListener != null) {
                onClickFullScreenListener.onClickFullScreen();
            }
        }
    }

    public interface onClickFullScreenListener {
        void onClickFullScreen();
    }
}
