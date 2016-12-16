package com.glogin;

import java.io.Serializable;

/**
 * Created by Minh vuong on 09/12/2016.
 */

public class Config implements Serializable {
    public static final String G_LOGIN_APP_ID = "garena login app id";
    public static final String G_LOGIN_APP_KEY = "garena login app key";
    public static final String G_LOGIN_REDIRECT = "garena login redirect";
    public static final String G_LOGIN_LOCATION = "garena login location";

    private String garenaLoginAppId;
    private String garenaLoginAppKey;
    private String garenaLoginRedirect;
    private String garenaLoginLocation;

    public Config (String appId, String appKey, String redirectUrl, String loginLocation) {
        this.garenaLoginAppId = appId;
        this.garenaLoginAppKey = appKey;
        this.garenaLoginRedirect = redirectUrl;
        this.garenaLoginLocation = loginLocation;
    }

    public String getGarenaLoginAppId () {
        return garenaLoginAppId;
    }

    public void setGarenaLoginAppId (String garenaLoginAppId) {
        this.garenaLoginAppId = garenaLoginAppId;
    }

    public String getGarenaLoginAppKey () {
        return garenaLoginAppKey;
    }

    public void setGarenaLoginAppKey (String garenaLoginAppKey) {
        this.garenaLoginAppKey = garenaLoginAppKey;
    }

    public String getGarenaLoginRedirect () {
        return garenaLoginRedirect;
    }

    public void setGarenaLoginRedirect (String garenaLoginRedirect) {
        this.garenaLoginRedirect = garenaLoginRedirect;
    }

    public String getGarenaLoginLocation () {
        return garenaLoginLocation;
    }

    public void setGarenaLoginLocation (String garenaLoginLocation) {
        this.garenaLoginLocation = garenaLoginLocation;
    }

}
