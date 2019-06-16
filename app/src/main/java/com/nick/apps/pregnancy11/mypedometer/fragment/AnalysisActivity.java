package com.nick.apps.pregnancy11.mypedometer.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.imooc.sport.R;
import com.imooc.sport.base.BaseFragment;
import com.nick.apps.pregnancy11.mypedometer.fragment.util.*;
import com.nick.apps.pregnancy11.mypedometer.fragment.view.ButtonSelectView;
import com.nick.apps.pregnancy11.mypedometer.fragment.view.PedoAnalysisPopup;

public class AnalysisActivity extends BaseFragment implements ButtonSelectView.onButtonSelectClickListener{

    ButtonSelectView analysis_run_miles_layout;
    ButtonSelectView analysis_step_layout;
    ButtonSelectView analysis_calorie_layout;
    ButtonSelectView analysis_weight_layout;
    ButtonSelectView[] mArray;
    private BarChart chart2;
    private LineChart chart;
    private ImageView mRightButton;
    private boolean mIsOpen = false;
    private PedoAnalysisPopup mRecordPopup;
    private TextView label1;
    private TextView tv_distance;
    private TextView label2;
    private TextView tv_distance1;

    @Override
    public int getContentView() {
        return R.layout.pedo_weight_fragment_layout;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        switchTab(0);
    }

    private void initView(View view) {
        chart = (LineChart)view.findViewById(R.id.chart1);
        chart2 = (BarChart)view.findViewById(R.id.chart2);
        analysis_run_miles_layout = (ButtonSelectView) view.findViewById(R.id.analysis_run_miles_layout);
        analysis_step_layout = (ButtonSelectView) view.findViewById(R.id.analysis_step_layout);
        analysis_calorie_layout = (ButtonSelectView) view.findViewById(R.id.analysis_calorie_layout);
        analysis_weight_layout = (ButtonSelectView) view.findViewById(R.id.analysis_weight_layout);
        mArray = new ButtonSelectView[4];
        mArray[0] = analysis_run_miles_layout;
        mArray[1] = analysis_step_layout;
        mArray[2] = analysis_calorie_layout;
        mArray[3] = analysis_weight_layout;
        analysis_run_miles_layout.setListenter(this);
        analysis_step_layout.setListenter(this);
        analysis_calorie_layout.setListenter(this);
        analysis_weight_layout.setListenter(this);

        label1 = (TextView)view.findViewById(R.id.label1);
        label2 = (TextView)view.findViewById(R.id.label2);
        tv_distance = (TextView)view.findViewById(R.id.tv_distance);
        tv_distance1 = (TextView)view.findViewById(R.id.tv_distance1);

        TextView tv_title = (TextView)view.findViewById(R.id.tv_title);
        tv_title.setText("分析");

        mRightButton = (ImageView)view.findViewById(R.id.btn_right);
        mRightButton.setVisibility(View.VISIBLE);
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!mIsOpen) {
                    rotatePlusButton(mContext,true);
                    mRecordPopup = new PedoAnalysisPopup(mContext);
                    mRecordPopup.bindData(null);
                    mRecordPopup.showAsDropDown(mRightButton, 0, ScreenUtil.dip2px(mContext, 6));
                    mRecordPopup.setOnStepPopClickListener(new PedoAnalysisPopup.OnStepPopClickListener() {
                        @Override
                        public void onStepPopClick(View view, int showMode) {
                            if(showMode == StepConstants.MODE_SET_TARGET) {
                                ToastUtil.show(mContext,"set target");
                            }else if(showMode == StepConstants.MODE_SET_WEIGHT) {
                                ToastUtil.show(mContext,"set weight");
                            }
                        }
                    });
                    mRecordPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            rotatePlusButton(mContext,false);
                            mIsOpen = false;
                        }
                    });
                    mIsOpen = true;
                }else{
                    mRecordPopup.dismiss();
                    rotatePlusButton(mContext,false);
                    mIsOpen = false;
                }
            }
        });
    }

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

    private void setSelect(int index) {
        for (int i=0; i < mArray.length; i++) {
            ButtonSelectView view = mArray[i];
            if(i == index) {
                view.setSelect(true);
            }else{
                view.setSelect(false);
            }
        }
    }

    private void switchTab(int index) {
         if(index == 0) {
            chart.setVisibility(View.VISIBLE);
            chart2.setVisibility(View.GONE);
            LinearLineHelper.cal(chart,LinearLineHelper.HANDLE_MILES);
            label1.setText("每日里程(米)");
            label2.setText("7天总里程(米)");
        }else  if(index == 1) {
            chart.setVisibility(View.VISIBLE);
            chart2.setVisibility(View.GONE);
            LinearLineHelper.cal(chart,LinearLineHelper.HANDLE_STEPS);
             label1.setText("今日步数");
             label2.setText("7天总步数");
        }
        else if(index == 2) {
            chart.setVisibility(View.GONE);
            chart2.setVisibility(View.VISIBLE);
            BarHelper.calChat2(chart2);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.analysis_run_miles_layout) {
            setSelect(0);
            switchTab(0);
        }else if(view.getId() == R.id.analysis_step_layout) {
            setSelect(1);
            switchTab(1);
        }else if(view.getId() == R.id.analysis_calorie_layout) {
            setSelect(2);
            switchTab(2);
        }else if(view.getId() == R.id.analysis_weight_layout) {
            setSelect(3);
            switchTab(3);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }






}
