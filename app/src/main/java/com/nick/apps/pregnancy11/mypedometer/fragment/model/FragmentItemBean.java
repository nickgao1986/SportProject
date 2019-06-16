package com.nick.apps.pregnancy11.mypedometer.fragment.model;


import androidx.annotation.NonNull;
import org.json.JSONObject;

import java.util.List;

public class FragmentItemBean {

    public List<String> images;
    public String title;
    public String forum_name;
    public int total_review;
    public int type;



    public static FragmentItemBean parse(@NonNull JSONObject json) {
        FragmentItemBean categoryGroupBean = new FragmentItemBean();
        return categoryGroupBean;
    }

}
