package com.nick.apps.pregnancy11.mypedometer.fragment.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.imooc.sport.R;


public class PedoTopicView extends LinearLayout {

    private TextView topic_position_tv;
    private TextView topic_position2_tv;
    private TextView topic_title_tv;

    private boolean isFire;
    private String index;
    private String mText;


    public PedoTopicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.PedoTopicView);

        mText = t.getString(R.styleable.PedoTopicView_topicContent);
        isFire = t.getBoolean(R.styleable.PedoTopicView_isFire,false);
        index  = t.getString(R.styleable.PedoTopicView_topicIndex);
        inflate(context, R.layout.pedo_item_topic_list,this);

        topic_position_tv = (TextView)findViewById(R.id.topic_position_tv);
        topic_position2_tv = (TextView)findViewById(R.id.topic_position2_tv);
        topic_title_tv = (TextView)findViewById(R.id.topic_title_tv);


        if(isFire) {
            topic_position_tv.setVisibility(View.VISIBLE);
            topic_position2_tv.setVisibility(View.GONE);
        }else{
            topic_position_tv.setVisibility(View.GONE);
            topic_position2_tv.setVisibility(View.VISIBLE);
            topic_position2_tv.setText(index);
        }
        topic_title_tv.setText(mText);

    }



}
