package com.nick.apps.pregnancy11.mypedometer.fragment.api;

import com.http.api.ApiUtil;
import com.http.api.Url;
import org.json.JSONObject;

public class NewsFavApi extends ApiUtil {


    public NewsFavApi(String id) {
        addParam("id",id);
    }
    @Override
    protected String getUrl() {
        return Url.HOST_URL1 + "/api/v1/upvote";
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {


    }
}
