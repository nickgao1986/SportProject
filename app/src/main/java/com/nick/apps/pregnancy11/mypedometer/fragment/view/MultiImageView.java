package com.nick.apps.pregnancy11.mypedometer.fragment.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nick.apps.pregnancy11.mypedometer.fragment.util.ScreenUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.imooc.sport.R;

import java.util.List;

public class MultiImageView extends LinearLayout {
    private static final int DEFAULT_IMAGE_COUNT = 3;
    private RelativeLayout[] mRelativeLayouts;
    private SimpleDraweeView[] mLoaderImageViews;
    private TextView[] textViews;
    private Context mContext;
    private int mCount = DEFAULT_IMAGE_COUNT;

    public MultiImageView(Context context) {
        super(context);
        init(context, DEFAULT_IMAGE_COUNT);
    }

    public MultiImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, DEFAULT_IMAGE_COUNT);
    }

    /**
     * 初始化控件
     */
    private void init(Context context, int count) {
        if (count <= 0) {
            throw new RuntimeException("MultiImageView must be greater than 0");
        }
        int space = ScreenUtil.dip2px(context, 5);
        this.mCount = count;
        this.mContext = context;
        removeAllViews();
        mRelativeLayouts = new RelativeLayout[count];
        mLoaderImageViews = new SimpleDraweeView[count];
        textViews = new TextView[count];
        for (int i = 0; i <= count - 1; i++) {
            mRelativeLayouts[i] = new RelativeLayout(mContext);

            mLoaderImageViews[i] = new SimpleDraweeView(mContext);
            RelativeLayout.LayoutParams ivLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mRelativeLayouts[i].addView(mLoaderImageViews[i], ivLayoutParams);

            textViews[i] = new TextView(context);
            RelativeLayout.LayoutParams lpTextView = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lpTextView.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lpTextView.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lpTextView.rightMargin = space;
            lpTextView.bottomMargin = space;
            textViews[i].setGravity(Gravity.CENTER);
            textViews[i].setTextSize(10);
            textViews[i].setTextColor(context.getResources().getColor(R.color.white));
            textViews[i].setPadding(space, 0, space, ScreenUtil.dip2px(context, 1));
            mRelativeLayouts[i].addView(textViews[i], lpTextView);

            addView(mRelativeLayouts[i]);
        }
    }

    /**
     * 设置图片的张数, 重新初始化count个LoaderImageView
     *
     * @param count
     */
    private void setImageCount(int count) {
        init(mContext, count);
    }

    /**
     * 显示图片
     *
     * @param width              每张图宽度 px
     * @param height             每张图高度 px
     * @param spaceDpValue       两张图之间的间隔 dp
     */
    public void displayImage(List<String> list, int width, int height, int spaceDpValue) {
        if (list == null || list.size() == 0) {
            setVisibility(View.GONE);
            return;
        }


        for (int i = 0; i <= mCount - 1; i++) {
            RelativeLayout rl = mRelativeLayouts[i];
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rl.getLayoutParams();
            params.height = height;
            params.width = width;
            if (i != 0) {
                params.leftMargin = ScreenUtil.dip2px(mContext, spaceDpValue);
            }
            rl.requestLayout();
            rl.setVisibility(View.GONE);
        }
        int len = list.size();
        for (int i = 0; i < len; i++) {
            String imageUrl = list.get(i);
            if (i > mCount - 1) {
                //最多只展示mLoaderImageViews.length张图
                break;
            }

            if (TextUtils.isEmpty(imageUrl)) {
                continue;
            }

            mRelativeLayouts[i].setVisibility(View.VISIBLE);
            mLoaderImageViews[i].setImageURI(imageUrl);
        }
    }

    public static class DisplayImageModel {
        public String imageUrl;
    }
}
