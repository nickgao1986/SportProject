package com.nick.apps.pregnancy11.mypedometer.fragment.adapter;

import android.util.TypedValue;
import androidx.annotation.Nullable;
import com.nick.apps.pregnancy11.mypedometer.fragment.model.ReplyItem;
import com.nick.apps.pregnancy11.mypedometer.fragment.view.PedoPraiseButton;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.imooc.sport.R;

import java.util.List;

public class DetailItemAdapter extends BaseQuickAdapter<ReplyItem, BaseViewHolder> {


    public DetailItemAdapter(int layoutResId, @Nullable List<ReplyItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReplyItem item) {
        PedoPraiseButton btn_praise = (PedoPraiseButton)helper.itemView.findViewById(R.id.btn_praise);
        btn_praise.setOnPraiseButtonListener(new PedoPraiseButton.OnPraiseButtonClickListener() {
            @Override
            public boolean onClick(boolean isPraised) {
                return true;
            }
        });
    }


    public int dip2px(int size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, mContext.getResources()
                .getDisplayMetrics());
    }

}
