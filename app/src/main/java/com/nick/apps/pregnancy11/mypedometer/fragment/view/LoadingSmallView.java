package com.nick.apps.pregnancy11.mypedometer.fragment.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.imooc.sport.R;


public class LoadingSmallView extends LinearLayout {


    public RelativeLayout relativeLayout;
    public ImageView imageOut;//转圈圈

    public LoadingSmallView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(LinearLayout.VERTICAL);


        relativeLayout = new RelativeLayout(context);
        //加入滚动条
        //ProgressBar progressBar = new ProgressBar(context);
        imageOut = new ImageView(context);
        imageOut.setImageResource(R.drawable.loading);
        RelativeLayout.LayoutParams paramrl = new  RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        paramrl.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(imageOut, paramrl);

        LayoutParams paramRelative = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        paramRelative.gravity = Gravity.CENTER_HORIZONTAL;
        addView(relativeLayout,paramRelative);

    }
    private RotateAnimation getRotateAnimation(){
        RotateAnimation rotateAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(-1);
        LinearInterpolator li = new LinearInterpolator();
        rotateAnimation.setInterpolator(li);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        return rotateAnimation;
    }
    //加载中
    public static final int TYPE_LOADING=1;
    //无数据
    public static final int TYPE_NODATA=2;
    //无网络
    public static final int TYPE_NONETWORK=3;


    public void setStatus(int type){
        show();
        switch (type){
            case TYPE_LOADING:{

                relativeLayout.setVisibility(View.VISIBLE);
                imageOut.setVisibility(VISIBLE);
                if(imageOut.getAnimation() == null)
                    imageOut.startAnimation(getRotateAnimation());


                break;
            }
            case TYPE_NODATA:{
                relativeLayout.setVisibility(View.GONE);
                imageOut.clearAnimation();


                break;
            }
            case TYPE_NONETWORK:{

                relativeLayout.setVisibility(View.GONE);
                imageOut.clearAnimation();

                break;
            }
            default:
                break;
        }
    }


    /**
     * 显示
     */
    public void show(){
        setVisibility(View.VISIBLE);
    }
    /**
     * 隐藏
     */
    public void hide(){
        setVisibility(View.GONE);
        imageOut.clearAnimation();
        relativeLayout.setVisibility(View.GONE);
    }

}
