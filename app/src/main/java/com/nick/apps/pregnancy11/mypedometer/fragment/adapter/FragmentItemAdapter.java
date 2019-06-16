package com.nick.apps.pregnancy11.mypedometer.fragment.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.imooc.sport.R;
import com.imooc.sport.SportApplication;
import com.nick.apps.pregnancy11.mypedometer.fragment.model.FragmentItemBean;
import com.nick.apps.pregnancy11.mypedometer.fragment.util.ScreenUtil;
import com.nick.apps.pregnancy11.mypedometer.fragment.view.MultiImageView;

import java.util.ArrayList;
import java.util.List;

public class FragmentItemAdapter extends BaseQuickAdapter<FragmentItemBean, BaseViewHolder> {


    //图片显示的宽度
    private int imageWidth;
    private int mScreenWidth;
    private int mImageSize;
    private TextView title;
    public ImageView iv_image;
    private Context mContext;
    private int index;

    public FragmentItemAdapter(int layoutResId, @Nullable List<FragmentItemBean> data, Context context) {
        super(layoutResId, data);
        mContext = context;
        mScreenWidth = ScreenUtil.getScreenWidth(mContext.getApplicationContext());
        mImageSize = (mScreenWidth - ScreenUtil.dip2px(mContext, 20) - ScreenUtil.dip2px(mContext, 6 * 2)) / 3;
        this.imageWidth = (mScreenWidth - ScreenUtil.dip2px(SportApplication.context, 24)
                - ScreenUtil.dip2px(SportApplication.context, 3 * 2)) / 3;

    }

    @Override
    protected void convert(BaseViewHolder helper, FragmentItemBean model) {

        MultiImageView ivMultiImage = (MultiImageView) helper.itemView.findViewById(R.id.iv_multi_image);
        SimpleDraweeView iv_image = (SimpleDraweeView)helper.itemView.findViewById(R.id.iv_image);
        ivMultiImage.setVisibility(View.VISIBLE);
        model.images = new ArrayList<>();

        if(index%2 == 0) {
            model.images.add("http://psfzl2l3l.bkt.clouddn.com/11.png");
            model.images.add("http://psfzl2l3l.bkt.clouddn.com/12.png");
            model.images.add("http://psfzl2l3l.bkt.clouddn.com/13.png");
        }else{
            model.images.add("http://psfzl2l3l.bkt.clouddn.com/14.png");
        }
        index++;
        if (model.images.size() > 3) {
            model.images = model.images.subList(0, 3);
        }
        int height = (int) ((double) imageWidth / 1.5);
        if(model.images.size() <= 1) {
            iv_image.setVisibility(View.VISIBLE);
            ivMultiImage.setVisibility(View.GONE);
            iv_image.setImageURI(model.images.get(0));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)iv_image.getLayoutParams();
            layoutParams.height = height;//扁图的比列是3:2
            layoutParams.width = imageWidth;
            layoutParams.rightMargin = ScreenUtil.dip2px(mContext, 12);
            iv_image.setLayoutParams(layoutParams);

        }else{
            iv_image.setVisibility(View.GONE);
            ivMultiImage.setVisibility(View.VISIBLE);
            ivMultiImage.displayImage(model.images, imageWidth, height, 6);

        }

    }


    public void bind(final FragmentItemBean model) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int dip2px(int size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, mContext.getResources()
                .getDisplayMetrics());
    }

}
