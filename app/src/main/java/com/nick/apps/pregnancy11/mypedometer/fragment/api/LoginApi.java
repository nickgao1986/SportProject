package com.nick.apps.pregnancy11.mypedometer.fragment.api;


import com.nick.apps.pregnancy11.mypedometer.fragment.util.LogUtil;
import com.http.api.ApiUtil;
import com.http.api.Url;
import org.json.JSONObject;

public class LoginApi extends ApiUtil {

    private String userName;
    private String password;

    public LoginApi(String userName, String password) {
        this.userName = userName;
        this.password = password;
        addParam("phone","18559298167");
        addParam("password","123");
    }

    @Override
    protected String getUrl() {
        return Url.HOST_URL1 + "/api/v1/login";
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {
        JSONObject obj = (JSONObject)jsonObject.opt("data");
        String token = obj.optString("token");
        LogUtil.d("TAG","<<<<token="+token);
    }


}
