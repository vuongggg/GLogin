package com.glogin.Utity;

import com.glogin.Contants.Global;

import org.json.JSONObject;

/**
 * Created by Minh vuong on 13/12/2016.
 */

public class BaseUserInfo {
    protected static final int GENDER_UNKNOWN = 0;
    protected static final int GENDER_MALE = 1;
    protected static final int GENDER_FEMALE = 2;
    
    private int platform;
    private int uid;
    private String openId;
    private String nickname;
    private String username;
    private String iconUrl;
    private int gender;

    public BaseUserInfo () {

    }

    public BaseUserInfo (JSONObject jsonObject) {
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
        try {
            nickname = jsonObject.getString("nickname");
        } catch (Exception e) {
            nickname = Global.UNKNOWN;
        }
        try {
            username = jsonObject.getString("username");
        } catch (Exception e) {
            username = Global.UNKNOWN;
        }
        try {
            iconUrl = jsonObject.getString("icon");
        } catch (Exception e) {
            iconUrl = Global.DEFAULT_AVA_URL;
        }
        try {
            gender = jsonObject.getInt("gender");
        } catch (Exception e) {
            gender = GENDER_UNKNOWN;
        }
    }

    protected int getPlatform () {
        return platform;
    }

    protected void setPlatform (int platform) {
        this.platform = platform;
    }

    protected int getUid () {
        return uid;
    }

    protected void setUid (int uid) {
        this.uid = uid;
    }

    protected String getOpenId () {
        return openId;
    }

    protected void setOpenId (String openId) {
        this.openId = openId;
    }

    protected String getNickname () {
        return nickname;
    }

    protected void setNickname (String nickname) {
        this.nickname = nickname;
    }

    protected String getUsername () {
        return username;
    }

    protected void setUsername (String username) {
        this.username = username;
    }

    protected String getIconUrl () {
        return iconUrl;
    }

    protected void setIconUrl (String iconUrl) {
        this.iconUrl = iconUrl;
    }

    protected int getGender () {
        return gender;
    }

    protected void setGender (int gender) {
        this.gender = gender;
    }
}
