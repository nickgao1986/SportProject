package com.nick.apps.pregnancy11.mypedometer.fragment.util;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.imooc.sport.SportApplication;
import com.imooc.sport.database.AppDatabase;

import java.util.ArrayList;

public class BarHelper {

    public static final int XAXIS_LABEL_COUNT_TOTAL = 14;
    public static final long DAY = 24 * 60 * 60 * 1000;


    public static void calChat2(BarChart chart2) {

        chart2.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart2.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart2.setPinchZoom(false);

        chart2.setDrawBarShadow(false);
        chart2.setDrawGridBackground(false);

        XAxis xAxis = chart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis yAxis = chart2.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);       //y轴的数值显示在外侧
        yAxis.setAxisMinimum(0f); ////为这个轴设置一个自定义的最小值。如果设置,这个值不会自动根据所提供的数据计算
        yAxis.setLabelCount(5, false);
        yAxis.setAxisMaximum(100f);
        chart2.getAxisRight().setEnabled(false);
        chart2.getAxisLeft().setDrawGridLines(false);

        initData(chart2,xAxis);

        chart2.getLegend().setEnabled(false);
        chart2.setScaleYEnabled(false);
        chart2.setScaleXEnabled(false);

    }


    private static void initData(BarChart chart2,  XAxis xAxis) {
        ArrayList<BarEntry> values = new ArrayList<>();
        final ArrayList<String> alxLabel = new ArrayList<>();
        final long curTime = System.currentTimeMillis();
        for (int i = XAXIS_LABEL_COUNT_TOTAL - 1; i >= 0; i--) {
            long startTime = (curTime - DAY * i);
            String startTimeMMdd = CalendarUtil.formatMMdd(startTime);
            alxLabel.add(startTimeMMdd);
            float value = 0f;
            String day = CalendarUtil.formatYYYYMMdd(startTime);
            long stepSum = AppDatabase.Companion.get(SportApplication.context).StepDao()
                    .findByDate(day).getStep();
            value = PedometerUtils.getCalorieByStepRealValue(SportApplication.context,stepSum);

            values.add(new BarEntry(XAXIS_LABEL_COUNT_TOTAL - i, value));
        }

        BarDataSet set1;
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                int x = (int) v;

                if (alxLabel.size() > x) {
                    return alxLabel.get((int) (v));
                } else {
                    return CalendarUtil.formatMMdd(curTime);
                }
            }
        });

        if (chart2.getData() != null &&
                chart2.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart2.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart2.getData().notifyDataChanged();
            chart2.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "热量");
            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set1.setDrawValues(false);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            chart2.setData(data);
            chart2.setFitBars(true);
        }

        chart2.invalidate();
    }

}
