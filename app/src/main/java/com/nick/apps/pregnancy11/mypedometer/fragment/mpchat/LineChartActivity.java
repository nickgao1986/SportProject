package com.nick.apps.pregnancy11.mypedometer.fragment.mpchat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.imooc.sport.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * https://www.jianshu.com/p/d3696ce819da
 */
public class LineChartActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvDate;
    private ImageView ivDate;
    private LineChart chart;

    private static final String[] dates = new String[]{"今日", "本周", "本月"};
    private List<String> dateList = Arrays.asList(dates);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        initView();
    }

    private void initView() {
        tvDate = (TextView) findViewById(R.id.tv_date);
        ivDate = (ImageView) findViewById(R.id.iv_date);
        chart = (LineChart) findViewById(R.id.chart);

        ivDate.setColorFilter(Color.WHITE);
        tvDate.setOnClickListener(this);
        ivDate.setOnClickListener(this);

        ChartUtils.initChart(chart);
        ChartUtils.notifyDataSetChanged(chart, getData(), ChartUtils.dayValue);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date:
            case R.id.iv_date:
                break;

            default:
                break;
        }
    }

    private List<Entry> getData() {
        List<Entry> values = new ArrayList<>();
        values.add(new Entry(0, 15));
        values.add(new Entry(1, 15));
        values.add(new Entry(2, 15));
        values.add(new Entry(3, 20));
        values.add(new Entry(4, 25));
        values.add(new Entry(5, 20));
        values.add(new Entry(6, 20));
        return values;
    }
}
