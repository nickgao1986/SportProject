package com.nick.apps.pregnancy11.mypedometer.fragment.api;

import android.content.Context;
import com.nick.apps.pregnancy11.mypedometer.fragment.model.CategoryBean;
import com.http.api.ApiUtil;
import com.http.api.Url;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class CatApi extends ApiUtil {

    private Context mContext;
    public ArrayList<CategoryBean> mList;
    public Map<String, String> mCategoryTabs = new LinkedHashMap<>();

    public ArrayList<CategoryBean> getGuideList() {
        return mList;
    }

    public Map<String, String> getCategoryTabs() {
        return mCategoryTabs;
    }

    @Override
    protected String getUrl() {
        return Url.HOST_URL1 + "/api/v1/cat";
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {

        JSONArray list = jsonObject.optJSONArray("data");
        ArrayList<CategoryBean> categoryBeanArrayList = new ArrayList<>();
        for (int i=0;i<list.length();i++) {
            JSONObject item = list.optJSONObject(i);
            CategoryBean categoryBean = new CategoryBean();
            categoryBean.id = item.optString("catid");
            categoryBean.name = item.optString("catname");
            categoryBeanArrayList.add(categoryBean);
            mCategoryTabs.put(categoryBean.id, categoryBean.name);
        }
        mList = categoryBeanArrayList;
    }
}
