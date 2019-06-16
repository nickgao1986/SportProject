package com.nick.apps.pregnancy11.mypedometer.fragment.api;

import com.nick.apps.pregnancy11.mypedometer.fragment.model.MatchItem;
import com.http.api.ApiUtil;
import com.http.api.Url;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CircleApi extends ApiUtil {


    private int total;
    private int page_num;
    private int page;
    public List<MatchItem> mList = new ArrayList<>();

    public CircleApi(int page) {
        addParam("page",String.valueOf(page));
    }
    @Override
    protected String getUrl() {
        return Url.HOST_URL1 + "/api/v1/news";
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {
        JSONObject object = (JSONObject)jsonObject.opt("data");
        total = object.optInt("total");
        page_num = object.optInt("page_num");
        JSONArray array = (JSONArray)object.optJSONArray("list");
        for (int i=0;i<array.length();i++) {
            JSONObject obj = array.optJSONObject(i);
            MatchItem item = new MatchItem();
            item.image = obj.optString("image");
            item.create_name = obj.optString("create_name");
            item.id = obj.optInt("id");
            item.catid = obj.optInt("catid");
            item.title = obj.optString("title");
            item.upvote_count = obj.optInt("upvote_count");
            item.catname = obj.optString("catname");
            mList.add(item);
        }
    }
}
