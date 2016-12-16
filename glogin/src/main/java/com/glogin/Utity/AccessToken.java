package com.glogin.Utity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Minh vuong on 13/12/2016.
 */

public class AccessToken {
    private String token;
    private long createTime;
    private long expiryTime;
    private ArrayList<String> scope;
    private int appId;
    private int platform;
    private int uid;
    private String openId;

    public AccessToken () {

    }

    public AccessToken (JSONObject jsonObject) {
        try {
            token = jsonObject.getString("access_token");
        } catch (JSONException e) {
            token = "";
        }

        try {
            createTime = jsonObject.getLong("create_time");
        } catch (JSONException e) {
            createTime = 0;
        }

        try {
            expiryTime = jsonObject.getLong("expiry_time");
        } catch (JSONException e) {
            expiryTime = 0;
        }
        try {
            scope = new ArrayList<>();
            JSONArray arrayScope = jsonObject.getJSONArray("scope");
            for (int i = 0; i < arrayScope.length(); i++) {
                scope.add(arrayScope.getString(i));
            }
        } catch (Exception e) {

        }

        try {
            appId = jsonObject.getInt("app_id");
        } catch (Exception e) {
            appId = -1;
        }

        try {
            platform = jsonObject.getInt("platform");
        } catch (Exception e) {
            platform = -1;
        }

        try {
            uid = jsonObject.getInt("uid");
        } catch (Exception e) {
            uid = -1;
        }

        try {
            openId = jsonObject.getString("open_id");
        } catch (Exception e) {
            openId = "";
        }
    }

    public String getToken () {
        return token;
    }

    public void setToken (String token) {
        this.token = token;
    }

    public long getCreateTime () {
        return createTime;
    }

    public void setCreateTime (long createTime) {
        this.createTime = createTime;
    }

    public long getExpiryTime () {
        return expiryTime;
    }

    public void setExpiryTime (long expiryTime) {
        this.expiryTime = expiryTime;
    }
}
