package com.nick.apps.pregnancy11.mypedometer.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.imooc.sport.R;

import java.util.List;


/**
 * Created by gaoyoujian on 2017/4/27.
 */

public class PedoMoreAllClassfiyAdapter extends BaseAdapter {

    private List<String> mFragmentTitleList;

    private LayoutInflater inflater;

    private Context context;

    private int size = 0;

    public PedoMoreAllClassfiyAdapter(Context context, List<String> mFragmentTitleList) {
        this.inflater = LayoutInflater.from(context);
        this.mFragmentTitleList = mFragmentTitleList;
        this.size = mFragmentTitleList.size();
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.size;
    }

    @Override
    public Object getItem(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = mFragmentTitleList.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.pedo_item_home_all_tab, null);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(name);

        return convertView;
    }

    class ViewHolder {
        public TextView tvName;
    }
}