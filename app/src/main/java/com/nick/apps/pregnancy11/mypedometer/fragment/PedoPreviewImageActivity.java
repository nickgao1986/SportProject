package com.nick.apps.pregnancy11.mypedometer.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.imooc.sport.R;
import com.nick.apps.pregnancy11.mypedometer.fragment.base.BaseActivity;
import com.nick.apps.pregnancy11.mypedometer.fragment.model.ImageLoadParams;
import com.nick.apps.pregnancy11.mypedometer.fragment.model.PreviewImageModel;
import com.nick.apps.pregnancy11.mypedometer.fragment.util.ImageDragHelper;
import com.nick.apps.pregnancy11.mypedometer.fragment.util.ScreenUtil;
import com.nick.apps.pregnancy11.mypedometer.fragment.view.DragRelativeLayout;
import com.nick.apps.pregnancy11.mypedometer.fragment.view.LoadingSmallView;
import me.relex.photodraweeview.OnPhotoTapListener;
import me.relex.photodraweeview.PhotoDraweeView;

import java.util.ArrayList;
import java.util.List;

public class PedoPreviewImageActivity extends BaseActivity {

    private View mRootView;

    @Override
    public Object getTitleString() {
        return null;
    }

    @Override
    public int getContentView() {
        return R.layout.pedo_common_image_preview;
    }

    @Override
    public void onLeftButtonClick() {

    }

    @Override
    public void onRightButtonClick() {

    }

    private static final String TAG = "PreviewImageActivity";

    private static int mCurrentPosition;
    private static List<PreviewImageModel> listModel = new ArrayList<>();

    private ViewPager mViewPager;
    private PreviewImageAdapter mAdapter;
    private TextView indicatorTv;

    public static void enterActivity(Context context) {
        try {
            Intent intent = new Intent(context, PedoPreviewImageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        mRootView = findViewById(R.id.rootview);
        mViewPager = (ViewPager) findViewById(R.id.news_image_preview_vp);

        List<String> images = new ArrayList<>();
        // String[] images={"file:///android_asset/pedo_banner_icon1.png"};
        images.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=4128213686,1490868641&fm=26&gp=0.jpg");
        images.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2544063423,3641037535&fm=26&gp=0.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559789963231&di=77b9c839ade44c826a7c90c2e5c81b99&imgtype=0&src=http%3A%2F%2F01img.mopimg.cn%2Fmobile%2F20180108%2F20180108074316_9650ed8640892cad200b4ea822a6f519_2.jpeg");
        listModel.clear();
        for (String str:images) {
            PreviewImageModel model = new PreviewImageModel();
            model.strPathName = str;
            listModel.add(model);
        }

        mAdapter = new PreviewImageAdapter(this, listModel);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentPosition);
        indicatorTv = (TextView) findViewById(R.id.news_image_preview_indicator_tv);
        if (listModel.size() <= 1) {
            indicatorTv.setVisibility(View.GONE);
        } else {
            indicatorTv.setVisibility(View.VISIBLE);
        }
        updateText();
        setListener();
        DragRelativeLayout layout = (DragRelativeLayout) findViewById(R.id.rootview);
        layout.setOnMDragListener(new ImageDragHelper(this, mAdapter, mRootView, indicatorTv));
    }

    private void setListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                mCurrentPosition = i;
                updateText();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        mAdapter.setOnPreviewActionListener(new PreviewImageAdapter.OnPreviewActionListener() {
            @Override
            public void onItemClick(int position, String uri) {
                handleFinish();
            }

            @Override
            public void onItemLongClick(int position, String uri, Bitmap bitmap) {
                if (uri == null) {
                    return;
                }

            }
        });
    }

    private void updateText() {
        if (listModel.size() == 0)
            return;
        //设置1/3
        int index = mCurrentPosition + 1;
        String text = index + "/" + listModel.size();
        indicatorTv.setText(text);
    }


    @Override
    public void onBackPressed() {
        handleFinish();
    }

    private void handleFinish() {
        finish();
        overridePendingTransition(R.anim.activity_animation_none, R.anim.activity_preview_image_out);
    }



    private static class PreviewImageAdapter extends PagerAdapter implements ImageDragHelper.IGetCurrentViewAdapter {

        private Activity mContext;
        private List<PreviewImageModel> listModel;
        private View mCurrentView;
        private OnPreviewActionListener mListener;

        private PreviewImageAdapter(Activity context, List<PreviewImageModel> listModel) {
            this.mContext = context;
            this.listModel = listModel;
        }

        @Override
        public int getCount() {
            return listModel.size();
        }

        @Override
        public Object instantiateItem(final View paramView, final int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.pedo_image_preview_item, null);
            findView(position, view);
            ((ViewGroup) paramView).addView(view);
            return view;
        }

        private void findView(final int position, View view) {
            final PhotoDraweeView zoomImageView;
            final LoadingSmallView loadingView;
            zoomImageView = (PhotoDraweeView) view.findViewById(R.id.zoomImage);
            zoomImageView.setAllowParentInterceptOnEdge(true);
            loadingView = (LoadingSmallView) view.findViewById(R.id.loadingView);
            loadingView.hide();

            final PreviewImageModel imageModel = listModel.get(position);
            zoomImageView.setOnPhotoTapListener(new OnPhotoTapListener() {

                @Override
                public void onPhotoTap(View view, float x, float y) {
                    try {
                        if (mListener != null) {
                            mListener.onItemClick(position, imageModel.strUrl);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            //长按操纵
            zoomImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    try {
                        if (mListener != null) {
                            mListener.onItemLongClick(position, imageModel.strUrl, null);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return false;
                }
            });
            //加载本地图片
            final String strPathName = imageModel.strPathName;
            final String strUrl = imageModel.strUrl;

            loadingView.setStatus(LoadingSmallView.TYPE_LOADING);
            String url = strPathName;
            if (TextUtils.isEmpty(strPathName)) {
                url = strUrl;
            }
            loadPic(url, zoomImageView, loadingView);

            //设置标记
            view.setTag(position);
        }

        private void loadPic(final String strPathName, final PhotoDraweeView zoomImageView, final LoadingSmallView loadingView) {
            if (!TextUtils.isEmpty(strPathName)) {
                ImageLoadParams params = new ImageLoadParams();
                params.height = ScreenUtil.getScreenHeight(mContext);
                params.width = ScreenUtil.getScreenWidth(mContext);

                params.forbidenModifyUrl = true;
                params.anim = true;
                zoomImageView.setPhotoUri(Uri.parse(strPathName));
                setPathNameLoaded(strPathName);
                loadingView.hide();
            } else {
                loadingView.hide();
//                zoomImageView.setImageResource(R.drawable.apk_remind_noimage);
            }
        }

        //设置已经加载过
        private void setPathNameLoaded(String pathname) {
            try {
                if (TextUtils.isEmpty(pathname))
                    return;
                for (PreviewImageModel model : listModel) {
                    if (!TextUtils.isEmpty(model.strPathName) && model.strPathName.equals(pathname)) {
                        model.bLoaded = true;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void setOnPreviewActionListener(OnPreviewActionListener listener) {
            mListener = listener;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mCurrentView = (View) object;
        }

        @Override
        public View getCurrentView() {
            return mCurrentView;
        }

        private interface OnPreviewActionListener {

            void onItemClick(int position, String uri);

            void onItemLongClick(int position, String uri, Bitmap bitmap);
        }

        @Override
        public void destroyItem(View collection, int position, Object paramObject) {
            ((ViewGroup) collection).removeView((View) paramObject);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }
    }


}
