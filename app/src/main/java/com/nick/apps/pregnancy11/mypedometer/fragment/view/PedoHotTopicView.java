package com.nick.apps.pregnancy11.mypedometer.fragment.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.imooc.sport.R;


public class PedoHotTopicView extends LinearLayout {

    private TextView tv_name;
    private TextView tv_description;
    private TextView tv_attend;
    private ImageView iv_click_icon;

    private String name;
    private String description;
    private String attend;
    private Drawable clickUrl;

    public PedoHotTopicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.PedoHotTopicView);

        name = t.getString(R.styleable.PedoHotTopicView_hotTopicName);
        description  = t.getString(R.styleable.PedoHotTopicView_hotTopicDesc);
        attend  = t.getString(R.styleable.PedoHotTopicView_hotTopicAttend);
        clickUrl  = t.getDrawable(R.styleable.PedoHotTopicView_hotTopicUrl);

        inflate(context, R.layout.pedo_circle_hot_match_item_layout,this);

        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_description = (TextView)findViewById(R.id.tv_description);
        tv_attend = (TextView)findViewById(R.id.tv_attend);
        iv_click_icon = (ImageView)findViewById(R.id.iv_click_icon);

        tv_name.setText(name);
        tv_description.setText(description);
        tv_attend.setText(attend);
        iv_click_icon.setImageDrawable(clickUrl);
    }



}
