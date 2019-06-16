package com.nick.apps.pregnancy11.mypedometer.fragment.decoration;

import android.graphics.Rect;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import com.nick.apps.pregnancy11.mypedometer.fragment.util.LogUtil;


public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        //outRect.left = space;
        outRect.bottom = space;
        outRect.top = space;
        //outRect.right = space;
        int position = parent.getChildLayoutPosition(view);
        LogUtil.d("TAG","<<<<position="+position);
        if (parent.getChildLayoutPosition(view) %2==0) {
            outRect.left = space;
            outRect.right = space*2;
        }else{
            outRect.left = space*2;
            outRect.right = space;
        }
    }

}