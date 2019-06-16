package com.nick.apps.pregnancy11.mypedometer.fragment.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.http.api.ApiListener;
import com.http.api.ApiUtil;
import com.imooc.sport.R;
import com.imooc.sport.activity.GlideImageLoader;
import com.imooc.sport.activity.VideoDetailActivity;
import com.imooc.sport.base.BaseFragment;
import com.nick.apps.pregnancy11.mypedometer.fragment.adapter.MatchItemAdapter;
import com.nick.apps.pregnancy11.mypedometer.fragment.api.CircleApi;
import com.nick.apps.pregnancy11.mypedometer.fragment.decoration.SpaceItemDecoration;
import com.nick.apps.pregnancy11.mypedometer.fragment.util.LogUtil;
import com.nick.apps.pregnancy11.mypedometer.fragment.util.ScreenUtil;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;


public class CircleActivity extends BaseFragment {

    private RecyclerView mRecyclerView;
    private Banner mBanner;
    MatchItemAdapter mMatchItemAdapter;
    private int mNextRequestPage = 1;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static final int PAGE_SIZE = 5;


    @Override
    public int getContentView() {
        return R.layout.match_fragment_layout;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handleRecycleView(view);

    }


    private void handleCircleApi() {
        mNextRequestPage = 1;
        mMatchItemAdapter.setEnableLoadMore(false);
        new CircleApi(mNextRequestPage).get(mContext, new ApiListener() {
            @Override
            public void success(ApiUtil api) {
                CircleApi circleApi = (CircleApi)api;
                setData(true, circleApi.mList);
                mMatchItemAdapter.setEnableLoadMore(true);
                mSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void failure(ApiUtil api) {
                Toast.makeText(mContext, "network error", Toast.LENGTH_LONG).show();
                mMatchItemAdapter.setEnableLoadMore(true);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadMore() {
        LogUtil.d("TAG","<<<<loadmore");
        new CircleApi(mNextRequestPage).get(mContext, new ApiListener() {
            @Override
            public void success(ApiUtil api) {
                CircleApi circleApi = (CircleApi)api;
                boolean isRefresh =mNextRequestPage ==1;
                setData(isRefresh, circleApi.mList);
            }

            @Override
            public void failure(ApiUtil api) {
                mMatchItemAdapter.loadMoreFail();
                Toast.makeText(mContext, "network error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setData(boolean isRefresh, List data) {
        mNextRequestPage++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mMatchItemAdapter.setNewData(data);
        } else {
            if (size > 0) {
                mMatchItemAdapter.addData(data);
            }
        }
        if (size < PAGE_SIZE) {
            //第一页如果不够一页就不显示没有更多数据布局
            mMatchItemAdapter.loadMoreEnd(isRefresh);
            Toast.makeText(mContext, "no more data", Toast.LENGTH_SHORT).show();
        } else {
            mMatchItemAdapter.loadMoreComplete();
        }
    }

    private void setBanner() {
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        List<String> images = new ArrayList<>();
        images.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=4128213686,1490868641&fm=26&gp=0.jpg");
        images.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2544063423,3641037535&fm=26&gp=0.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559789963231&di=77b9c839ade44c826a7c90c2e5c81b99&imgtype=0&src=http%3A%2F%2F01img.mopimg.cn%2Fmobile%2F20180108%2F20180108074316_9650ed8640892cad200b4ea822a6f519_2.jpeg");

        //设置图片集合
        mBanner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }
    private void initRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handleCircleApi();
            }
        });
    }


    private void handleRecycleView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        TextView tv_title = (TextView)view.findViewById(R.id.tv_title);
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeLayout);
        tv_title.setText("赛事");
        GridLayoutManager layoutManager = new GridLayoutManager(mContext,2);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 2;
            }
        });

        mMatchItemAdapter = new MatchItemAdapter(R.layout.pedo_match_adapter_item,null);
        mMatchItemAdapter.setLoadMoreView(new CustomLoadMoreView());

        mRecyclerView.setAdapter(mMatchItemAdapter);
        View headerView = mContext.getLayoutInflater().inflate(R.layout.match_header_layout, (ViewGroup) mRecyclerView.getParent(), false);
        mMatchItemAdapter.setEnableLoadMore(true);
        mMatchItemAdapter.addHeaderView(headerView);

        mMatchItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        },mRecyclerView);
        mMatchItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(mContext, VideoDetailActivity.class));
            }
        });
        mBanner = (Banner) headerView.findViewById(R.id.banner);
        setBanner();

        mRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtil.dip2px(mContext,6)));
        initRefreshLayout();
        mSwipeRefreshLayout.setRefreshing(true);
        handleCircleApi();
//        List<MatchItem> list = new ArrayList<>();
//        for (int i=0;i<12;i++)
//            list.add(new MatchItem(array[i]));
      //  MatchItemAdapter adapter = new MatchItemAdapter(R.layout.pedo_match_adapter_item,list);
//        mRecyclerView.setAdapter(adapter);
//        View headerView = mContext.getLayoutInflater().inflate(R.layout.match_header_layout, (ViewGroup) mRecyclerView.getParent(), false);
//
//        adapter.addHeaderView(headerView);
//        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                startActivity(new Intent(mContext, VideoDetailActivity.class));
//            }
//        });

    }

    @Override
    public void onResume() {
        super.onResume();
//        ((BottomActivity)mContext).showTitle();
//        ((BottomActivity)mContext).setTitle("赛事");
    }
}
