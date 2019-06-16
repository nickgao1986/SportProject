package com.nick.apps.pregnancy11.mypedometer.fragment.util;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.imooc.sport.SportApplication;
import com.imooc.sport.database.AppDatabase;
import java.util.ArrayList;

public class LinearLineHelper {

    public static float mXaxisLabelCount = 8;
    public static final int XAXIS_LABEL_COUNT_TOTAL = 20;
    public static final long DAY = 24 * 60 * 60 * 1000;

    public static final int HANDLE_MILES = 1;
    public static final int HANDLE_STEPS = 2;


    public static void cal(LineChart chart, int mode) {
        {
            chart.setBackgroundColor(Color.WHITE);

            chart.getDescription().setEnabled(false);

            chart.setTouchEnabled(true);

            chart.setDrawGridBackground(false);


            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);

            chart.setPinchZoom(true);
        }

        XAxis xAxis;
        {
            xAxis = chart.getXAxis();
            xAxis.setYOffset(6f);
            xAxis.setXOffset(30f);
            // vertical grid lines
            xAxis.setDrawGridLines(true);
            xAxis.enableAxisLineDashedLine(10f, 0, 0);
            xAxis.setGridColor(Color.parseColor("#F5F5F5"));
            xAxis.setGridLineWidth(1);
            xAxis.setTextColor(Color.parseColor("#FF666666"));
            xAxis.setDrawAxisLine(true);
            xAxis.setAxisLineColor(Color.parseColor("#DDDDDD"));
            xAxis.setAxisLineWidth(1f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            xAxis.setLabelCount((int) mXaxisLabelCount, false);
            xAxis.setTextSize(10);
            xAxis.setTextColor(Color.parseColor("#FF666666"));
            xAxis.setAxisMinimum(0f);
            xAxis.setAxisMaximum(XAXIS_LABEL_COUNT_TOTAL + 0.1f);
        }
        final ArrayList<String> alxLabel = new ArrayList<>();


        YAxis yAxis;
        {
            yAxis = chart.getAxisLeft();
            chart.getAxisRight().setEnabled(false);
            yAxis.disableAxisLineDashedLine();
            yAxis.setDrawGridLines(false);
            yAxis.setDrawAxisLine(true);
            yAxis.setAxisLineColor(Color.parseColor("#DDDDDD"));
            yAxis.setTextColor(Color.parseColor("#FF666666"));
            yAxis.setAxisLineWidth(1);
            // 描述y坐标有几个坐标点
            yAxis.setLabelCount(5, false);
            xAxis.setTextSize(11);

            if (mode == HANDLE_STEPS) {
                yAxis.setAxisMaximum(1000f);
                // axis rangef
                yAxis.setAxisMinimum(0f);
            } else if (mode == HANDLE_MILES){
                yAxis.setAxisMaximum(2000f);
                // axis range
                yAxis.setAxisMinimum(0f);
            }

        }


        final long curTime = System.currentTimeMillis();
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = XAXIS_LABEL_COUNT_TOTAL - 1; i >= 0; i--) {
            long startTime = (curTime - DAY * i);
            String startTimeMMdd = CalendarUtil.formatMMdd(startTime);
            alxLabel.add(startTimeMMdd);
            float value = 0f;
            String day = CalendarUtil.formatYYYYMMdd(startTime);
            if (mode == HANDLE_STEPS) {
                value = AppDatabase.Companion.get(SportApplication.context).StepDao()
                        .findByDate(day).getStep();

            }else if(mode == HANDLE_MILES){
//                long stepSum = AppDatabase.Companion.getDatabase(SportApplication.getContext()).StepDao()
//                        .findByDate(day).step;
                long stepSum = 0;
                value = PedometerUtils.getDistance(stepSum);
            }
            Entry entry = new Entry(XAXIS_LABEL_COUNT_TOTAL - i, value, null);
            values.add(entry);

        }

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

        String title = (mode == HANDLE_MILES ? "里程(米)" : "步数");
        setData(chart, values, title);
        float scale = (XAXIS_LABEL_COUNT_TOTAL + 1) / mXaxisLabelCount;
        Matrix m = new Matrix();
        m.postScale(scale, 1f);//两个参数分别是x,y轴的缩放比例。例如：将x轴的数据放大为之前的3倍
        chart.getViewPortHandler().refresh(m, chart, false);//将图表动画显示之前进行缩放
        chart.setScaleYEnabled(false);
        chart.setScaleXEnabled(false);

        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
    }


    private static void setData(final LineChart chart, ArrayList values, String title) {
        LineDataSet set1;
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            if (set1 != null) {
                set1.setValues(values);
                set1.notifyDataSetChanged();
            }
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, title);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setDrawIcons(false);
            // draw dashed line
            set1.enableDashedLine(30f, 15f, 5f);
            // black lines and points
            set1.setColor(Color.BLUE);
            set1.setCircleColor(Color.GREEN);
            // line thickness and point size
            set1.setLineWidth(1.5f);
            set1.setCircleRadius(4f);
            set1.setCircleHoleRadius(2.5F);
            // draw points as solid circles
            set1.setDrawCircleHole(true);
            // customize legend entry
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            // text size of values
            set1.setValueTextSize(9f);
            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setHighlightEnabled(false);
            // set the filled area
            set1.setDrawFilled(false);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            set1.setCubicIntensity(0.01f);

            set1.setFillColor(Color.BLACK);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            chart.setData(data);
        }
    }

}
