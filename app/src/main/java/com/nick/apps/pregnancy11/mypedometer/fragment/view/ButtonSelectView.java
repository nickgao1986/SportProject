package com.nick.apps.pregnancy11.mypedometer.fragment.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.imooc.sport.R;


public class ButtonSelectView extends LinearLayout {

    private String mText;
    private Drawable mDrawable;

    private TextView textView;
    private ImageView leftImg;

    private onButtonSelectClickListener mListenter;

    public ButtonSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.ButtonSelectView);

        mText = t.getString(R.styleable.ButtonSelectView_analysisContent);
        mDrawable = t.getDrawable(R.styleable.ButtonSelectView_iconTop);
        boolean isMeItem = t.getBoolean(R.styleable.ButtonSelectView_isMeItem,false);
        if(isMeItem) {
            inflate(context, R.layout.pedo_me_item_layout,this);
        }else{
            inflate(context, R.layout.pedo_weight_item_layout,this);
        }

        textView = (TextView)findViewById(R.id.content);
        leftImg = (ImageView)findViewById(R.id.icon);

        textView.setText(mText);
        leftImg.setImageDrawable(mDrawable);

        leftImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mListenter != null) {
                    mListenter.onClick(ButtonSelectView.this);
                }
            }
        });

    }

    public void setIcon(int resource) {
        leftImg.setImageResource(resource);
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setButtonState(String text, int resource) {
        setText(text);
        setIcon(resource);
    }

    public void enabled(boolean enable) {
        textView.setEnabled(enable);
    }

    public void setSelect(boolean select) {
        leftImg.setSelected(select);
    }

    public interface onButtonSelectClickListener{
        void onClick(View view);
    }


    public void setListenter(onButtonSelectClickListener listenter) {
        this.mListenter = listenter;
    }


}
