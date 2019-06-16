package com.nick.apps.pregnancy11.mypedometer.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.annotation.Nullable;
import com.imooc.sport.R;
import com.imooc.sport.base.BaseFragment;
import com.nick.apps.pregnancy11.mypedometer.fragment.adapter.BAFFragmentAdapter;
import com.nick.apps.pregnancy11.mypedometer.fragment.adapter.PedoMoreAllClassfiyAdapter;
import com.nick.apps.pregnancy11.mypedometer.fragment.view.TipView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.nick.apps.pregnancy11.mypedometer.fragment.util.ScreenUtil.dip2px;


public class PedoClassifyListFragment extends BaseFragment implements View.OnClickListener {

    private View mTabParentLayout;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TipView mTipView;
    private BAFFragmentAdapter mAdapter;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitleList = new ArrayList<>();
    private GridView categoryGridView;
    private RelativeLayout rlMoreClassify;
    private ImageView ivMoreClassify;
    public boolean bTopMenuShowed;
    private LinearLayout ll_special_category_content;
    private RelativeLayout linearMenu;
    private Animation animation_out;
    private Animation animation_in;

    @Override
    public void onResume() {
        super.onResume();
        if (mViewPager != null && mViewPager.getCurrentItem() < mFragmentList.size()) {
            Fragment fragment = mFragmentList.get(mViewPager.getCurrentItem());
            fragment.onHiddenChanged(false);
        }
    }


    @Override
    public int getContentView() {
        return R.layout.pedo_classify_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        mTabParentLayout = view.findViewById(R.id.tab_fragment_list_layout);
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_fragment_list_tabs);
        categoryGridView = (GridView) view.findViewById(R.id.special_gridview);
        ll_special_category_content = (LinearLayout)view.findViewById(R.id.ll_special_category_content);
        linearMenu = (RelativeLayout) ll_special_category_content.findViewById(R.id.rl_special_category_list);
        rlMoreClassify = (RelativeLayout)view.findViewById(R.id.rlMoreClassify);
        rlMoreClassify.setOnClickListener(this);
        ivMoreClassify = (ImageView)view.findViewById(R.id.ivMoreClassify);
        mViewPager = (ViewPager) view.findViewById(R.id.tab_fragment_list_viewpager);
        mTipView = (TipView) view.findViewById(R.id.multi_expert_list_tipview);
        mAdapter = new BAFFragmentAdapter(mContext.getSupportFragmentManager(), mFragmentList, mFragmentTitleList);
        mViewPager.setAdapter(mAdapter);
        mTipView.setClickListener(this);
        animation_in = AnimationUtils.loadAnimation(mContext, R.anim.menu_in);
        animation_out = AnimationUtils.loadAnimation(mContext, R.anim.menu_out);
        loadData();
    }

    public void reflex(final TabLayout tabLayout) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp90 = dip2px(tabLayout.getContext(), 90);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为tab宽度-textview宽度除以2  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        //params.width = width;
                        params.leftMargin = (tabView.getMeasuredWidth() - width) / 2;
                        params.rightMargin = (tabView.getMeasuredWidth() - width) / 2;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void loadData() {
        mTipView.setLoadingData(true);
//        new ChatCategoryListApi(mContext).get(mContext, new ApiListener() {
//            @Override
//            public void success(ApiBase api) {
//                if (mContext.isFinishing()) {
//                    return;
//                }
//                ChatCategoryListApi rsp = (ChatCategoryListApi) api;
//                if (rsp.getGuideList().size() == 0) {
//                    return;
//                }
//                mTipView.setLoadingData(false);
//                mTipView.hideView();
//                mTabParentLayout.setVisibility(View.VISIBLE);
//                Map<String, String> linkedHashMap = rsp.getCategoryTabs();
//                Set<Map.Entry<String, String>> dataSet = linkedHashMap.entrySet();
//
//                int tabTarget = 0;
//                for (Map.Entry<String, String> entry : dataSet) {
//                    mFragmentList.add(com.babytree.apps.pregnancy11.mypedometer.fragment.activity.PedoClassifyListFragment.newInstance(entry.getKey(), entry.getValue()));
//                    mFragmentTitleList.add(entry.getValue());
//                }
//                setGridView();
//                mAdapter.notifyDataSetChanged();
//                mTabLayout.setupWithViewPager(mViewPager);
//                mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
//                    @Override
//                    public void onTabReselected(TabLayout.Tab tab) {
//                        CharSequence title = tab.getText();
//                        showPopMenu();
//                        if (TextUtils.isEmpty(title)) return;
//                    }
//                });
//                reflex(mTabLayout);
//                mViewPager.setOffscreenPageLimit(dataSet.size());
//                mViewPager.setCurrentItem(tabTarget);
//            }
//
//            @Override
//            public void failure(ApiBase apiBase) {
//                if (mContext.isFinishing()) {
//                    return;
//                }
//                mTipView.setLoadingData(false);
//            }
//        });

    }



    private void setGridView() {
        PedoMoreAllClassfiyAdapter classfiyAdapter = new PedoMoreAllClassfiyAdapter(mContext, mFragmentTitleList);
        categoryGridView.setAdapter(classfiyAdapter);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.rlMoreClassify) {
            showPopMenu();
        }else{
            loadData();
        }
    }

    /**
     * 显示菜单 点击标题的时候
     */
    private void showPopMenu() {
        if (bTopMenuShowed) {
            return;
        }
        bTopMenuShowed = true;

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_special_category_content.getLayoutParams();
        int category_height = dip2px(mContext,40);
        layoutParams.topMargin = category_height;
        ll_special_category_content.requestLayout();
        //显示
        ll_special_category_content.setVisibility(View.VISIBLE);
        downAnimation();
        //动画
        linearMenu.startAnimation(animation_in);
        //响应
        ll_special_category_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击收缩
                hidePopMenu();
            }
        });
    }

    public void hidePopMenu() {
        if (!bTopMenuShowed) {
            return;
        }
        bTopMenuShowed = false;

        upAnimation();
        animation_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_special_category_content.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        linearMenu.startAnimation(animation_out);
    }


    /**
     * 按下箭头的动画
     */
    private void downAnimation() {
        ObjectAnimator.ofFloat(ivMoreClassify, "rotation", 0,45f).setDuration(500).start();
    }

    /**
     * 收回之后箭头的动画
     */
    private void upAnimation() {
        ObjectAnimator.ofFloat(ivMoreClassify, "rotation", 45f,90f).setDuration(500).start();
    }


}
