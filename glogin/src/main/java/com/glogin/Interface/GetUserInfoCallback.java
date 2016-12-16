package com.glogin.Interface;

import com.glogin.Utity.UserInfo;

/**
 * Created by Minh vuong on 14/12/2016.
 */

public interface GetUserInfoCallback {
    void onSuccess (UserInfo userInfo);
    void onError (String error);

}
