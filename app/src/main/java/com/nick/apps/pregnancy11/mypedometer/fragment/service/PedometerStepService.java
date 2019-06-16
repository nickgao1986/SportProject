package com.nick.apps.pregnancy11.mypedometer.fragment.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.*;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import androidx.annotation.Nullable;
import com.imooc.sport.R;
import com.imooc.sport.database.AppDatabase;
import com.imooc.sport.database.StepDao;
import com.nick.apps.pregnancy11.mypedometer.fragment.BottomActivity;
import com.nick.apps.pregnancy11.mypedometer.fragment.model.StepData;
import com.nick.apps.pregnancy11.mypedometer.fragment.util.CalendarUtil;
import com.nick.apps.pregnancy11.mypedometer.fragment.util.LogUtil;
import com.nick.sport.ISportStepInterface;


public class PedometerStepService extends Service implements Handler.Callback {

//    private ISportStepCounterDBHelper mTodayStepDBHelper;
    public static int sCurrentStep = 0;

    private static final int HANDLER_WHAT_SAVE_STEP = 10;
    private static final String TAG = "TodayStepService";


    private NotificationManager mNotificationManager;
    private static Notification mNotification;
    private NotificationCompat.Builder mBuilder;
    private StepDao mStepDao;

    /**
     * 传感器相关
     */
    private SensorManager sensorManager;
    private TodayStepCounter stepCounter;
    /**
     * 传感器的采样周期，这里使用SensorManager.SENSOR_DELAY_FASTEST，如果使用SENSOR_DELAY_UI会导致部分手机后台清理内存之后传感器不记步
     */
    private static final int SAMPLING_PERIOD_US = SensorManager.SENSOR_DELAY_FASTEST;
    private int mPreStep = -1;
    private SaveStepAsyncTask mSaveStepAsyncTask;
    public static final String EVENT_FROM_PEDOMETER = "event_from_pedometer";
    /**
     * 通知栏更新步数的时间
     */
    private static final int DURATION_UPDATE_NOTIFICATION = 10;
    private long mLastSavedStepTime = -1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder.asBinder();
    }

    /**
     * Sensor的回调
     */
    private OnStepCounterListener mOnStepCounterListener = new OnStepCounterListener() {
        @Override
        public void onChangeStepCounter(int step) {
            Log.d("TAG", "===onChangeStepCounter step=" + step + "pre=" + mPreStep);
            updateTodayStep(step);
        }

        @Override
        public void onStepCounterClean() {

            sCurrentStep = 0;
            updateNotification(0);
        }

    };

    @Override
    public void onCreate() {
        super.onCreate();
//        mTodayStepDBHelper = SportStepCountDbHelper.getInstance();
        mStepDao = AppDatabase.Companion.get(this).StepDao();
        sensorManager = (SensorManager) this
                .getSystemService(SENSOR_SERVICE);

        try {
            initPedometerNotification();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //注册传感器
        startStepDetector();
        //START_STICKY：表示Service运行的进程被Android系统强制杀掉之后，Android系统会将该Service依然设置为started状态（即运行状态）
        return START_STICKY;
    }


    private void startStepDetector() {
        try {
            addStepCounterListener();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addStepCounterListener() {
        if (null != stepCounter) {
            Log.e(TAG, "已经注册TYPE_STEP_COUNTER");
            sCurrentStep = stepCounter.getCurrentStep();
            updateNotification(sCurrentStep);
            return;
        }
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (null == countSensor) {
            return;
        }
        stepCounter = new TodayStepCounter(getApplicationContext(), mOnStepCounterListener);

        sensorManager.registerListener(stepCounter, countSensor, SAMPLING_PERIOD_US);
    }


    @Override
    public void onDestroy() {

        Intent intent = new Intent(this, PedometerStepService.class);
        startService(intent);

        if (mSaveStepAsyncTask != null) {
            mSaveStepAsyncTask.cancel(true);
        }
        super.onDestroy();
    }


    /**
     * 步数每次回调的方法
     *
     * @param currentStep
     */
    private void updateTodayStep(int currentStep) {
        long nowTime = System.currentTimeMillis();
        long inteval = (nowTime - mLastSavedStepTime) / 1000;
        sCurrentStep = currentStep;
        //大于10s
        if (currentStep < 50 || inteval > DURATION_UPDATE_NOTIFICATION) {
            LogUtil.d("TAG", "====== updateTodaySteps inteval=" + inteval);

            mLastSavedStepTime = nowTime;
            updateNotification(currentStep);
            saveStep(currentStep);
        } else {
            LogUtil.d("TAG", "====== not update");

        }
    }

    private void saveStep(int currentStep) {
        try {

            mSaveStepAsyncTask = new SaveStepAsyncTask();
            executor(mSaveStepAsyncTask);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case HANDLER_WHAT_SAVE_STEP: {

                mSaveStepAsyncTask = new SaveStepAsyncTask();
                executor(mSaveStepAsyncTask);
                break;
            }
            default:
                break;
        }
        return false;
    }


    private void saveDb() {
        StepData todayStepData = new StepData();
        todayStepData.setDate(CalendarUtil.getCurrentDate());
        todayStepData.setStep(sCurrentStep);
        //必须加这个
        todayStepData.setTarget(PreferencesHelper.getSportTargetForService(this));

        if (null != mStepDao) {
            mStepDao.insert(todayStepData);
        }
    }

    private void executor(AsyncTask<Void, Void, Void> task) {
        try {
            task.execute();
        } catch (Exception e) {
            LogUtil.e("TAG", "executor [" + e + "]");
            e.printStackTrace();
        }
    }


    private class SaveStepAsyncTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            saveDb();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }


    private final ISportStepInterface.Stub mIBinder = new ISportStepInterface.Stub() {

        @Override
        public int getCurrentTimeSportStep() throws RemoteException {
            return sCurrentStep;
        }

    };


    //***********************mNotification****************************/

    private void initPedometerNotification() {

        Intent resultIntent = new Intent(PedometerStepService.this,
                BottomActivity.class);
        //封装一个Intent
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        RemoteViews remoteView = setStepRemoteView(this, R.layout.pedo_step_count_notification);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setPriority(Notification.PRIORITY_DEFAULT);


        mBuilder.setContent(remoteView)
                .setContentIntent(resultPendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setOngoing(true);

        mNotification = mBuilder.build();

        mNotificationManager.notify(R.id.pedometer_notification, mNotification);
    }


    private RemoteViews setStepRemoteView(Context context, int layout) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), layout);
        LogUtil.d("TAG", "======setStepRemoteView remoteViews=" + remoteViews);

        remoteViews.setTextViewText(R.id.step, String.valueOf(sCurrentStep));
        return remoteViews;
    }


    private void updateNotification(int stepCount) {

        if (null == mNotification || null == mNotificationManager) {
            return;
        }
        try {
            mNotification.contentView.setTextViewText(R.id.step, String.valueOf(stepCount));
            mNotificationManager.notify(R.id.pedometer_notification, mNotification);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }



}
