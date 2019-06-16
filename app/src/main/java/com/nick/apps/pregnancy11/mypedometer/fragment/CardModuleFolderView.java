package com.nick.apps.pregnancy11.mypedometer.fragment;

import android.content.Context;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.nick.apps.pregnancy11.mypedometer.fragment.util.ScreenUtil;
import com.nick.apps.pregnancy11.mypedometer.fragment.view.ClickProxy;
import com.imooc.sport.R;


public class CardModuleFolderView extends RelativeLayout implements View.OnClickListener {

    private static final String TAG = CardModuleFolderView.class.getSimpleName();
    private static final int DEF_MAX_LINE_ALL = 5;
    private static final int DEF_APPEND_LENGTH = 35;

    private TextView mTvContent;
    private TextView mTvButton;
    private int mRealWidth = 0;
    private int mAppendLength;
    private int mMaxLine = DEF_MAX_LINE_ALL;
    private String mContentValue;
    private boolean isFolderOpened;
    private OnCardFolderClickListener mListener;
    
    public CardModuleFolderView(Context context) {
        this(context, null);
    }

    public CardModuleFolderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardModuleFolderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.pedo_feeds_card_folder_view, this);
        mAppendLength = ScreenUtil.dip2px(context, DEF_APPEND_LENGTH);
        mTvContent = (TextView) findViewById(R.id.cms_feeds_card_folder_content);
        mTvContent.setOnClickListener(new ClickProxy(this));
        mTvButton = (TextView)findViewById(R.id.cms_feeds_card_folder_button);
        mTvButton.setOnClickListener(new ClickProxy(this));
    }

    /**
     * 设置字体颜色
     * @param color
     */
    public void setContentTextColor(int color) {
        mTvContent.setTextColor(getContext().getResources().getColor(color));
        invalidate();
    }

    /**
     * 设置字体大小
     * @param size
     */
    public void setContentTextSize(int size) {
        mTvContent.setTextSize(size);
        invalidate();
    }

    /**
     * 设置展开/收起字体大小
     * @param size
     */
    public void setButtonTextSize(int size) {
        mTvButton.setTextSize(size);
        invalidate();
    }

    /**
     * 设置收起时文字行数
     * @param mDefaultMaxLine
     */
    public void setDefaultMaxLine(int mDefaultMaxLine) {
        this.mMaxLine = mDefaultMaxLine;
        invalidate();
    }

    /**
     * 是否显示展开收起按钮
     * @return
     */
    public boolean isShowOpenButton(){
        return mTvButton.getVisibility() == VISIBLE;
    }

    /**
     * 设置控件真实宽度,不设置默认展示全部
     * @param realWidth
     * @return
     */
    public CardModuleFolderView setRealWidth(int realWidth) {
        this.mRealWidth = realWidth;
        return this;
    }

    /**
     * 展示文本
     * @param contentValue
     */
    public void display(boolean isFolderOpened, String contentValue) {
        this.isFolderOpened = isFolderOpened;
        this.mContentValue = contentValue;
        if (TextUtils.isEmpty(contentValue)) {
            mTvContent.setVisibility(GONE);
            mTvButton.setVisibility(GONE);
        } else if (mRealWidth <= 0) {
            mTvContent.setVisibility(VISIBLE);
            mTvContent.setMaxLines(Integer.MAX_VALUE);
            mTvContent.setText(contentValue);
            mTvButton.setVisibility(GONE);
        } else if (isFolderOpened) {
            mTvContent.setMaxLines(Integer.MAX_VALUE);
            dealOpenText(contentValue);
        } else {
            mTvContent.setMaxLines(mMaxLine);
            dealCloseText(contentValue);
        }
        mTvContent.setMaxLines(isFolderOpened ? Integer.MAX_VALUE : mMaxLine);
        mTvButton.setText(isFolderOpened ? R.string.cms_card_content_close : R.string.cms_card_content_open);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cms_feeds_card_folder_button || v.getId() == R.id.cms_feeds_card_folder_content) {
            if (!TextUtils.isEmpty(mContentValue)) {
                isFolderOpened = !isFolderOpened;
                display(isFolderOpened, mContentValue);
                if (mListener != null) {
                    mListener.onFolderClick(CardModuleFolderView.this, isFolderOpened, mContentValue);
                }
            }
        }
    }
    
    public void setOnCardFolderClickListener(OnCardFolderClickListener listener) {
        this.mListener = listener;
    }
    
    public interface OnCardFolderClickListener {
        /**
         * 折叠按钮点击
         * @param view
         * @param isFolderOpened
         * @param contentValue
         */
        void onFolderClick(CardModuleFolderView view, boolean isFolderOpened, String contentValue);
    }
    
    /**
     * 处理折叠情况文案
     * @param contentValue
     */
    private void dealCloseText(String contentValue) {
        try {
            StaticLayout layout = new StaticLayout(contentValue, mTvContent.getPaint(), mRealWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
            final int lineCount = layout.getLineCount();
            if (lineCount <= mMaxLine) {
                mTvContent.setVisibility(VISIBLE);
                mTvContent.setText(contentValue);
                mTvButton.setVisibility(GONE);
            } else {
                final int start = layout.getLineStart(mMaxLine - 1);
                final float lastRealWidth = mRealWidth - mAppendLength;
                final CharSequence preRealValue = contentValue.subSequence(0, start);
                final CharSequence lastContentValue = contentValue.subSequence(start, contentValue.length());
                final CharSequence lastRealValue = TextUtils.ellipsize(lastContentValue, mTvContent.getPaint(), lastRealWidth, TextUtils.TruncateAt.END, false, null);
                mTvButton.setVisibility(VISIBLE);
                mTvContent.setVisibility(VISIBLE);
                mTvContent.setText(preRealValue);
                mTvContent.append(lastRealValue);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理展开情况文案
     * @param contentValue
     */
    private void dealOpenText(String contentValue) {
        try {
            StaticLayout layout = new StaticLayout(contentValue, mTvContent.getPaint(), mRealWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
            final int lineCount = layout.getLineCount();
            if (lineCount <= mMaxLine) {
                mTvContent.setVisibility(VISIBLE);
                mTvContent.setText(contentValue);
                mTvButton.setVisibility(GONE);
            } else {
                final int start = layout.getLineStart(layout.getLineCount() - 1);
                final float lastRealWidth = mRealWidth - mAppendLength;
                final CharSequence lastContentValue = contentValue.subSequence(start, contentValue.length());
                final float lastContentLength = mTvContent.getPaint().measureText(lastContentValue.toString());
                mTvButton.setVisibility(VISIBLE);
                mTvContent.setVisibility(VISIBLE);
                mTvContent.setText(contentValue);
                if (lastContentLength > lastRealWidth) {
                    mTvContent.append("\n");
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
