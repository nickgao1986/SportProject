package com.nick.apps.pregnancy11.mypedometer.fragment.view;

import android.view.View;


public class ClickProxy implements View.OnClickListener {

    private View.OnClickListener origin;
    private long lastClick = 0;
    private long times = 500;

    public ClickProxy(View.OnClickListener origin) {
        this.origin = origin;
    }

    @Override
    public void onClick(View v) {
        if (System.currentTimeMillis() > this.lastClick) {
            if (System.currentTimeMillis() - this.lastClick >= this.times) {
                this.origin.onClick(v);
                this.lastClick = System.currentTimeMillis();
            }
        } else {
            this.origin.onClick(v);
            this.lastClick = System.currentTimeMillis();
        }
    }
}