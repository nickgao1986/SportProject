package com.nick.apps.pregnancy11.mypedometer.fragment.model;

import android.widget.ImageView;



public class ImageLoadParams {

    /**
     * 默认holder
     */
    public int defaultholder;
    /**
     * 失败holder
     */
    public int failholder;
    public int retryholder;
    public int bgholder;
    public int roundBgColor;
    public int width;
    public int height;
    public int radius;
    //禁止修改url
    public boolean forbidenModifyUrl;
    //如果 需要不同弧度的四个圆角 请设置这个


    public ImageView.ScaleType scaleType;
    /**
     * 是否圆形gt
     */
    public boolean round;
    public int loadMode = LOAD_MODE_NORMAL;
    public static final int LOAD_MODE_NORMAL = 0;
    //@Deprecated
    //public static final int LOAD_MODE_LOCAL = 1;

    /**
     * 是否允许图片重新点击实现加载
     */
    public boolean enableRetry;

    public boolean anim = false;

    //是否渐变
    public boolean isFade=true;

    public Object tag;

    public boolean isOrigin = false;

}
