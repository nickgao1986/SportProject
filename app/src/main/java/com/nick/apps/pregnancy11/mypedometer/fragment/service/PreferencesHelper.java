package com.nick.apps.pregnancy11.mypedometer.fragment.service;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * @ClassName: PreferencesHelper
 * @Description: (公用类，用于缓存一些key——value类型的数据)
 */

public class PreferencesHelper {

    private static final String TAG = "PreferencesHelper";

    public static final String APP_SHARD = "today_step_share_prefs";

    // 上一次计步器的步数
    public static final String LAST_SENSOR_TIME = "last_sensor_time";
    // 步数补偿数值，每次传感器返回的步数-offset=当前步数
    public static final String STEP_OFFSET = "step_offset";
    // 当天，用来判断是否跨天
    public static final String STEP_TODAY = "step_today";
    // 清除步数
    public static final String CLEAN_STEP = "clean_step";
    // 当前步数
    public static final String CURR_STEP = "curr_step";
    //手机关机监听
    public static final String SHUTDOWN = "shutdown";
    //系统运行时间
    public static final String ELAPSED_REALTIMEl = "elapsed_realtime";


    public static final String PEDOMETER_NOTIFICATION_ENABLED = "pedometer_notification_enabled";

    public static final String PEDOMETER_NOTIFICATION_ENABLED_SERVICE = "pedometer_notification_enabled_service";

    public static final String SPORTS_TARGET_FOR_SERVICE = "sports_target_for_service";


    /**
     * Get SharedPreferences
     */
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SHARD, Context.MODE_PRIVATE);
    }

    public static void setLastSensorStep(Context context, float lastSensorStep){
        getSharedPreferences(context).edit().putFloat(LAST_SENSOR_TIME,lastSensorStep).commit();
    }

    public static float getLastSensorStep(Context context){
        return getSharedPreferences(context).getFloat(LAST_SENSOR_TIME,0.0f);
    }

    public static void setStepOffset(Context context, float stepOffset){
        getSharedPreferences(context).edit().putFloat(STEP_OFFSET,stepOffset).commit();
    }

    public static float getStepOffset(Context context){
        return getSharedPreferences(context).getFloat(STEP_OFFSET,0.0f);
    }

    public static void setStepToday(Context context, String stepToday){
        getSharedPreferences(context).edit().putString(STEP_TODAY,stepToday).commit();
    }

    public static String getStepToday(Context context){
        return getSharedPreferences(context).getString(STEP_TODAY,"");
    }


    public static void setSportsTargetForService(Context context, int target){
        getSharedPreferences(context).edit().putInt(SPORTS_TARGET_FOR_SERVICE,target).commit();
    }

    public static int getSportTargetForService(Context context){
        return getSharedPreferences(context).getInt(SPORTS_TARGET_FOR_SERVICE,200);
    }

    /**
     * true清除步数从0开始，false否
     * @param context
     * @param cleanStep
     */
    public static void setCleanStep(Context context, boolean cleanStep){
        getSharedPreferences(context).edit().putBoolean(CLEAN_STEP,cleanStep).commit();
    }

    /**
     * true 清除步数，false否
     * @param context
     * @return
     */
    public static boolean getCleanStep(Context context){
        return getSharedPreferences(context).getBoolean(CLEAN_STEP,true);
    }


    public static void setPedometerNotificationEnable(Context context, boolean flag){
        getSharedPreferences(context).edit().putBoolean(PEDOMETER_NOTIFICATION_ENABLED,flag).commit();
    }

    public static boolean getPedometerNotificationEnable(Context context){
        return getSharedPreferences(context).getBoolean(PEDOMETER_NOTIFICATION_ENABLED, true);
    }


    public static void setCurrentStep(Context context, float currStep){
        getSharedPreferences(context).edit().putFloat(CURR_STEP,currStep).commit();
    }

    public static float getCurrentStep(Context context){
        return getSharedPreferences(context).getFloat(CURR_STEP,0.0f);
    }

    public static void setShutdown(Context context, boolean shutdown){
        getSharedPreferences(context).edit().putBoolean(SHUTDOWN,shutdown).commit();
    }

    public static boolean getShutdown(Context context){
        return getSharedPreferences(context).getBoolean(SHUTDOWN, false);
    }

    public static void setElapsedRealtime(Context context, long elapsedRealtime){
        getSharedPreferences(context).edit().putLong(ELAPSED_REALTIMEl,elapsedRealtime).commit();
    }

    public static long getElapsedRealtime(Context context){
        return getSharedPreferences(context).getLong(ELAPSED_REALTIMEl, 0L);
    }



    public static void setPedometerNotificationEnableForService(Context context, boolean flag){
        getSharedPreferences(context).edit().putBoolean(PEDOMETER_NOTIFICATION_ENABLED_SERVICE,flag).commit();
    }

    public static boolean getPedometerNotificationEnableForService(Context context){
        return true;
    }
}
