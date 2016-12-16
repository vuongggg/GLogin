package com.glogin.Interface;

import com.glogin.Utity.UserInfo;

/**
 * Created by Minh vuong on 13/12/2016.
 */

public interface LoginCallback {
    void onSuccess (UserInfo userInfo);
    void onGetUserInfoError (String error);
    void onGetTokenError (String error);
}
