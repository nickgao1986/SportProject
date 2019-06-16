package com.nick.apps.pregnancy11.mypedometer.fragment.adapter;

import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.nick.apps.pregnancy11.mypedometer.fragment.model.GuideImageModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.imooc.sport.R;

import java.util.List;

public class GuideItemAdapter extends BaseQuickAdapter<GuideImageModel, BaseViewHolder> implements BaseQuickAdapter.OnItemClickListener {


    public GuideItemAdapter(int layoutResId, @Nullable List<GuideImageModel> data) {
        super(layoutResId, data);
        setOnItemClickListener(this);
    }

    @Override
    protected void convert(BaseViewHolder helper, GuideImageModel item) {
        SimpleDraweeView pic = (SimpleDraweeView) helper.itemView.findViewById(R.id.ivPhoto);
        TextView name = (TextView)helper.itemView.findViewById(R.id.name);
        pic.setImageResource(item.resource);
        name.setText(item.name);
    }


    public int dip2px(int size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, mContext.getResources()
                .getDisplayMetrics());
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        View view1 = view.findViewById(R.id.ll_choose_pic);
        if(view1.getVisibility() == View.VISIBLE) {
            view1.setVisibility(View.GONE);
        }else{
            view1.setVisibility(View.VISIBLE);
        }
    }
}
