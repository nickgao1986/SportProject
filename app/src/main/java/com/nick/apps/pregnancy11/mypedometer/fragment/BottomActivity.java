package com.nick.apps.pregnancy11.mypedometer.fragment;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import androidx.annotation.NonNull;
import com.imooc.sport.R;
import com.imooc.sport.database.StepDao;
import com.imooc.sport.fragment.MeActivity;
import com.imooc.sport.fragment.MyPedometerActivity;
import com.nick.apps.pregnancy11.mypedometer.fragment.base.BaseActivity;
import com.nick.apps.pregnancy11.mypedometer.fragment.service.PedometerStepService;
import com.nick.apps.pregnancy11.mypedometer.fragment.util.LogUtil;
import com.nick.apps.pregnancy11.mypedometer.fragment.view.CircleActivity;
import com.nick.apps.pregnancy11.mypedometer.fragment.view.PedoAnalysisPopup;
import com.nick.sport.ISportStepInterface;

import java.lang.ref.WeakReference;

public class BottomActivity extends BaseActivity {

    private Fragment[] fragments;
    private CircleActivity mCircleActivity;
    private  AnalysisActivity mAnalysisActivity;
    private MyPedometerActivity mMyPedometerActivity;
    private MeActivity mMeActivity;
    private int lastfragment;//用于记录上个选择的Fragment
    private Button mRightButton;
    private boolean mIsOpen = false;
    private PedoAnalysisPopup mRecordPopup;
    public ISportStepInterface iSportStepInterface;
    private static Handler mDelayHandler = null;
    private static final String TAG = "[MyPedometerActivity]";
    private static final int REFRESH_STEP_WHAT = 0;
    public long mFootSteps;
    public StepDao mStepDao;
    /**
     * 循环取当前时刻的步数中间的间隔时间
     */
    private static final long TIME_INTERVAL_REFRESH = 1500;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    if(lastfragment!=0)
                    {
                        switchFragment(lastfragment,0);
                        lastfragment=0;

                    }
                    return true;
                case R.id.list:
                    if(lastfragment!=1)
                    {
                        switchFragment(lastfragment,1);
                        lastfragment=1;

                    }                    return true;
                case R.id.form:
                    if(lastfragment!=2)
                    {
                        switchFragment(lastfragment,2);
                        lastfragment=2;

                    }
                    return true;
                case R.id.form1:
                    if(lastfragment!=3)
                    {
                        switchFragment(lastfragment,3);
                        lastfragment=3;

                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCircleActivity = new CircleActivity();
        mAnalysisActivity = new AnalysisActivity();
        mMeActivity = new MeActivity();
        mMyPedometerActivity = new MyPedometerActivity();
        fragments = new Fragment[]{mMyPedometerActivity,mAnalysisActivity,mCircleActivity,mMeActivity};
        lastfragment=0;

        BottomNavigationView navView = (BottomNavigationView)findViewById(R.id.nav_view);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainview,mMyPedometerActivity).show(mMyPedometerActivity).commit();
         BottomNavigationViewHelper.disableShiftMode(navView);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mDelayHandler = new Handler(new TodayStepCounterCall(this));
        try{
            bindService();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        initDataBase();
    }

    private void initDataBase() {

    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iSportStepInterface = ISportStepInterface.Stub.asInterface(service);
            try {
                mFootSteps = iSportStepInterface.getCurrentTimeSportStep();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d(TAG,"======onServiceDisconnected");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mServiceConnection != null) {
            mContext.unbindService(mServiceConnection);
        }

        if(mDelayHandler != null) {
            mDelayHandler.removeCallbacksAndMessages(null);
            mDelayHandler = null;
        }
    }

    private void bindService() {
        //开启计步Service，这个service是不同进程的，同时绑定Activity进行aidl通信 ,计步器的数据都从这里获取
        Intent intent = new Intent(mContext, PedometerStepService.class);
        mContext.startService(intent);
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

    }



    private void switchFragment(int lastfragment,int index)
    {
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
        if(fragments[index].isAdded()==false)
        {
            transaction.add(R.id.mainview,fragments[index]);


        }
        transaction.show(fragments[index]).commitAllowingStateLoss();


    }

//    @Override
//    public void initRightButton(Button rightButton) {
//        super.initRightButton(rightButton);
//        mRightButton = rightButton;
//        rightButton.setVisibility(View.VISIBLE);
//        rightButton.setBackgroundResource(R.drawable.actionbar_add_bg);
//    }
//
//
//    @Override
//    public void onRightButtonClick() {
//        super.onRightButtonClick();
//
//        if(!mIsOpen) {
//            rotatePlusButton(mContext,true);
//            mRecordPopup = new PedoAnalysisPopup(mContext);
//            mRecordPopup.bindData(null);
//            mRecordPopup.showAsDropDown(mRightButton, 0, ScreenUtil.dip2px(mContext, 6));
//            mRecordPopup.setOnStepPopClickListener(new PedoAnalysisPopup.OnStepPopClickListener() {
//                @Override
//                public void onStepPopClick(View view, int showMode) {
//                    if(showMode == StepConstants.MODE_SET_TARGET) {
//                        ToastUtil.show(mContext,"set target");
//                    }else if(showMode == StepConstants.MODE_SET_WEIGHT) {
//                        ToastUtil.show(mContext,"set weight");
//                    }
//                }
//            });
//            mRecordPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                    rotatePlusButton(mContext,false);
//                    mIsOpen = false;
//                }
//            });
//            mIsOpen = true;
//        }else{
//            mRecordPopup.dismiss();
//            rotatePlusButton(mContext,false);
//            mIsOpen = false;
//        }
//
//
//    }

    public void rotatePlusButton(Context context, boolean isUp) {
        ObjectAnimator rotate = null;
        if(isUp) {
            rotate = ObjectAnimator.ofFloat(mRightButton, "rotation", 0f, 45f).setDuration(100);
        }else{
            rotate = ObjectAnimator.ofFloat(mRightButton, "rotation", 45f, 0f).setDuration(100);
        }
        rotate.setInterpolator(new BounceInterpolator());
        rotate.start();

    }

//    @Override
//    public void initLeftButton(Button leftButton) {
//        leftButton.setVisibility(View.GONE);
//    }

    @Override
    public Object getTitleString() {
        return null;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_bottom;
    }

    @Override
    public void onLeftButtonClick() {

    }

    @Override
    public void onRightButtonClick() {

    }

    static class TodayStepCounterCall implements Handler.Callback {

        WeakReference<BottomActivity> mWeakReference;

        public TodayStepCounterCall(BottomActivity activity)
        {
            mWeakReference=new WeakReference<BottomActivity>(activity);
        }

        @Override
        public boolean handleMessage(Message msg) {

            final BottomActivity activity=mWeakReference.get();
            if(activity == null) {
                LogUtil.d(TAG,"=====handleMessage activity==null");
                return false;
            }
            switch (msg.what) {
                case REFRESH_STEP_WHAT: {
                    //每隔500毫秒获取一次计步数据刷新UI
                    if (null != activity.iSportStepInterface) {
                        int step = 0;
                        try {
                            step = activity.iSportStepInterface.getCurrentTimeSportStep();
//                            if (activity.mMyPedometerActivity.mFootSteps != step) {
//                                //activity.updateTodaySteps();
//
//                                activity.mMyPedometerActivity.mFootSteps = step;
//                                activity.mMyPedometerActivity.updateDataWhenSensorChanged();
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH);

                    break;
                }
                default:
                    break;
            }
            return false;
        }
    }

}
