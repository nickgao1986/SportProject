package com.nick.apps.pregnancy11.mypedometer.fragment.util;

import android.content.Context;

public class PedometerUtils {


    /**
     * 卡路里计算公式，单位是卡 steps*0.7计算是距离，1.036是系数
     * @param context
     * @param steps
     * @return
     */
    public static int getCalorieByStepRealValue(Context context, long steps) {
        return (int)(steps * 0.7f * 55 * 1.036f)/ 1000;
    }

    public static float getDistance(long steps) {
        return steps * 0.7f;
    }

    /**
     * 公里计算公式
     * @param steps
     * @return
     */
    public static String getDistanceByStep(long steps) {
        String distanceText;
        float distance = steps * 0.7f / 1000;
        if(distance > 0.1) {
            distanceText = String.format("%.1f", distance);
        } else if (distance < 0.1 && distance > 0){
            distanceText = String.format("%.2f", distance);
        } else if(steps > 0){
            distanceText = "0.01";
        }else{
            distanceText = "0";
        }
        return distanceText;
    }
}
