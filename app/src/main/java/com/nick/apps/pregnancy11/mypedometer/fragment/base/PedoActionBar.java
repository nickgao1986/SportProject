package com.nick.apps.pregnancy11.mypedometer.fragment.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.imooc.sport.R;

public class PedoActionBar extends RelativeLayout {


    /**
     * 标题
     */
    private TextView mTvTitle;
    /**
     * 左键
     */
    private Button mBtnLeft;

    /**
     * 右键
     */
    private Button mBtnRight;

    public PedoActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mBtnLeft = (Button) findViewById(R.id.btn_left);
        mBtnRight = (Button) findViewById(R.id.btn_right);
    }

    /**
     * 获取左键
     *
     * @return
     */
    public Button getLeftButton() {
        return mBtnLeft;
    }

    /**
     * 获取右键
     *
     * @return
     */
    public Button getRightButton() {
        return mBtnRight;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(Object title) {
        if(title instanceof Integer){
            mTvTitle.setText((Integer)title);
        }else if(title instanceof String){
            mTvTitle.setText((String)title);
        }
    }


    /**
     * 事件监听
     */
    public interface ActionBarListener {
        /**
         * 获取title, 返回null或0则不加载标题栏，动态设置标题栏时可返回""
         */
        Object getTitleString();


        int getContentView();

        /**
         * 左键事件
         */
        void onLeftButtonClick();

        /**
         * 右键事件
         */
        void onRightButtonClick();

        /**
         * 设置左键
         */
        void initLeftButton(Button leftButton);

        /**
         * 设置右键
         */
        void initRightButton(Button rightButton);
    }

}
