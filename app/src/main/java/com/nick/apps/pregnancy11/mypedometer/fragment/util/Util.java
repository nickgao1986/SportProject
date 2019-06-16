package com.nick.apps.pregnancy11.mypedometer.fragment.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Collection;

public class Util {

    public static boolean isEmpty(Collection<?> objs) {
        return objs == null || objs.size() <= 0;
    }

    /**
     * 网络状态判断
     *
     * @param context
     * @return
     */
    public static boolean hasNetwork(Context context) {
        try {
            if (null == context || null == context.getApplicationContext()) {
                return false;
            }
            ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            if (manager == null) {
                return false;
            }
            NetworkInfo networkinfo = manager.getActiveNetworkInfo();
            if (networkinfo == null || !networkinfo.isAvailable()) {
                return false;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            LogUtil.e("TAG", "hasNetwork e[" + e + "]");
        }
        return true;
    }

}
