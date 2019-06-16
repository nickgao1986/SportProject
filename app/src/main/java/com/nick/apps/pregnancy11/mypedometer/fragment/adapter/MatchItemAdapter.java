package com.nick.apps.pregnancy11.mypedometer.fragment.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.nick.apps.pregnancy11.mypedometer.fragment.model.MatchItem;
import com.nick.apps.pregnancy11.mypedometer.fragment.view.CollectButton;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.imooc.sport.R;

import java.util.List;

public class MatchItemAdapter extends BaseQuickAdapter<MatchItem, BaseViewHolder> {

    private Context mContext;
    public MatchItemAdapter(int layoutResId, @Nullable List<MatchItem> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, final MatchItem item) {

        SimpleDraweeView iv_match = (SimpleDraweeView)helper.itemView.findViewById(R.id.iv_match);
        iv_match.setImageURI(item.image);

        TextView title = (TextView)helper.itemView.findViewById(R.id.title);
        TextView username = (TextView)helper.itemView.findViewById(R.id.username);

        title.setText(item.title);
        username.setText(item.create_name);

        final CollectButton collectButton = (CollectButton) helper.itemView.findViewById(R.id.collect);
        if(item.upvote_count >= 1) {
            collectButton.setCollectState(true);
        }else{
            collectButton.setCollectState(false);
            collectButton.setOnCollectButtonClickListener(new CollectButton.OnCollectButtonClickListener() {
                @Override
                public boolean onClick(boolean var1) {
                    collectRequest(collectButton,item.id);
                    return true;
                }
            });
        }


    }

    private void collectRequest(final CollectButton collectButton, final int newsId) {
//        new NewsFavApi(String.valueOf(newsId)).post(PregnancyApplication.getContext(), new ApiListener() {
//            @Override
//            public void success(ApiBase api) {
//
//            }
//
//            @Override
//            public void failure(ApiBase api) {
//                collectButton.setCollectState(false);
//            }
//        });
    }

    public int dip2px(int size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, mContext.getResources()
                .getDisplayMetrics());
    }

}
