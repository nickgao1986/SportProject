package com.nick.apps.pregnancy11.mypedometer.fragment.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


public class PedoUtils {

    private static String TAG = PedoUtils.class.getSimpleName();

    /**
     * 隐藏输入法键盘
     *
     * @param context
     * @param editText
     */
    public static void hideInputMethod(Context context, EditText editText) {
        if (editText != null) {
            try {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            } catch (Exception e) {
                LogUtil.e(TAG, "hideInputMethod e[" + e + "]");
            }
        }
    }

    /**
     * 隐藏键盘
     *
     * @param context
     */
    public static void hideInputMethod(Activity context) {
        try {
            ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            LogUtil.e(TAG, "hideSoftInputKeyboard e[" + e + "]");
        }
    }

    /**
     * 显示输入框
     *
     * @param editText
     */
    public static void showSoftKeyboardAt(EditText editText) {
        if (editText == null) return;
        try {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            InputMethodManager m = (InputMethodManager) editText.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
