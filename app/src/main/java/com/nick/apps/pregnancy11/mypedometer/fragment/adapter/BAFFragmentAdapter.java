package com.nick.apps.pregnancy11.mypedometer.fragment.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Arrays;
import java.util.List;

public class BAFFragmentAdapter<T extends Fragment> extends FragmentPagerAdapter {
    private List<T> mFragList;
    private List<String> mTitleList;

    public BAFFragmentAdapter(FragmentManager manager, T... fragments) {
        super(manager);
        this.mFragList = Arrays.asList(fragments);
    }

    public BAFFragmentAdapter(FragmentManager manager, List<T> mFragList) {
        super(manager);
        this.mFragList = mFragList;
    }

    public BAFFragmentAdapter(FragmentManager manager, List<T> mFragList, List<String> mTitleList) {
        super(manager);
        this.mFragList = mFragList;
        this.mTitleList = mTitleList;
    }

    public T getItem(int position) {
        return this.mFragList.get(position);
    }

    public int getCount() {
        return this.mFragList == null ? 0 : this.mFragList.size();
    }

    public CharSequence getPageTitle(int position) {
        return this.mTitleList != null ? (CharSequence)this.mTitleList.get(position) : super.getPageTitle(position);
    }
}
