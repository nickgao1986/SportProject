package com.nick.apps.pregnancy11.mypedometer.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.nick.apps.pregnancy11.mypedometer.fragment.model.PhoneModel;
import com.imooc.sport.R;

import java.util.ArrayList;

public class LoginPhoneAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater li;
    private ArrayList<PhoneModel> dataList;

    public LoginPhoneAdapter(Context ctx, ArrayList<PhoneModel> dataList) {
        this.ctx = ctx;
        this.li = LayoutInflater.from(ctx);
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public PhoneModel getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.pedo_username_item_layout, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        return convertView;
    }

    class ViewHolder {

        public ViewHolder(View view) {

        }

    }


}