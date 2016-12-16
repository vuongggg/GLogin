package com.glogin;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.glogin.AsyncTasks.GetUserInfoTask;
import com.glogin.AsyncTasks.InspectTokenTask;
import com.glogin.AsyncTasks.LogoutTask;
import com.glogin.Contants.Global;
import com.glogin.Interface.GetUserInfoCallback;
import com.glogin.Interface.LoginCallback;
import com.glogin.Interface.LogoutCallback;
import com.glogin.Utity.AccessToken;
import com.glogin.Utity.UserInfo;

/**
 * Created by Minh vuong on 13/12/2016.
 */

public class GLogin {
    private static GLogin instance;
    private String token;
    private LoginCallback loginCallback;

    private GLogin () {
    }

    public static GLogin getInstance () {
        if (instance == null) {
            instance = new GLogin();
        }

        return instance;
    }

    public void loginG (@NonNull Context context,
                        @NonNull Config config,
                        @Nullable LoginCallback loginCallback) {
        if (token == null) {
            this.loginCallback = loginCallback;
            Intent loginIntent = new Intent(context, GLoginActivity.class);
            loginIntent.putExtra(GLoginActivity.CONFIG, config);
            context.startActivity(loginIntent);
        }
    }

    public void logoutG (@NonNull final Context context,
                        @NonNull final LogoutCallback logoutCallback) {
        if (token == null)
            return;
        LogoutTask logoutTask = new LogoutTask(context);
        logoutTask.setOnTaskExecutedListener(new LogoutTask.OnLogoutTaskExecutedListener() {
            @Override
            public void onSuccess () {
                new WebView(context).clearCache(true);
                removeAllCookie(context, new ValueCallback<Boolean>() {
                    @Override
                    public void onReceiveValue (Boolean value) {
                        if (true) {
                            clear();
                            logoutCallback.onSuccess();
                        }
                        else {
                            logoutCallback.onError("failed to clear cookie");
                        }
                    }
                });
            }

            @Override
            public void onError (String err) {
                logoutCallback.onError(err);
            }
        });
        logoutTask.execute(token);
    }

    @SuppressWarnings("deprecation")
    private void removeAllCookie (Context context, ValueCallback<Boolean> callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(callback);
            CookieManager.getInstance().flush();
        }
        else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    protected LoginCallback getLoginCallback () {
        return loginCallback;
    }

    protected void clear () {
        token = null;
    }

    public void getUserInfo (final Context context, final GetUserInfoCallback callback) {
        if (token != null) {
            InspectTokenTask inspectTokenTask = new InspectTokenTask(context);
            inspectTokenTask.setOnTaskExecutedListener(new InspectTokenTask.OnInspectTokenTaskExecutedListener() {
                @Override
                public void onSuccess (AccessToken token) {
                    GetUserInfoTask getUserInfoTask = new GetUserInfoTask(context);
                    getUserInfoTask.setOnTaskExecutedListener(new GetUserInfoTask.OnTaskExecutedListener() {
                        @Override
                        public void onSuccess (UserInfo userInfo) {
                            if (callback != null) {
                                callback.onSuccess(userInfo);
                            }
                        }

                        @Override
                        public void onError (String err) {
                            if (callback != null) {
                                callback.onError(err);
                            }
                        }
                    });
                    getUserInfoTask.execute(token.getToken());
                }

                @Override
                public void onError (String err) {
                    if (callback != null) {
                        callback.onError(err);
                    }
                }
            });
            inspectTokenTask.execute(token);
        }
        else {
            if (callback != null) {
                callback.onError(Global.ERROR_TOKEN_EXPIRED);
            }
        }
    }

    protected void setToken (AccessToken token) {
        this.token = token.getToken();
    }

    protected void clearCallback () {
        loginCallback = null;
    }
}
