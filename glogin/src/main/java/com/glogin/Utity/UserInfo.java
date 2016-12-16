package com.glogin.Utity;

import org.json.JSONObject;

/**
 * Created by Minh vuong on 13/12/2016.
 */

public class UserInfo extends BaseUserInfo {

    public UserInfo () {
        super();
    }

    public UserInfo (JSONObject jsonObject) {
        super(jsonObject);
    }

    public String getNickname () {
        return super.getNickname();
    }

    public String getAvatarUrl () {
        return super.getIconUrl();
    }

}
