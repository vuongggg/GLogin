package com.glogin;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import com.glogin.AsyncTasks.ExchangeTokenTask;
import com.glogin.AsyncTasks.GetUserInfoTask;
import com.glogin.AsyncTasks.InspectTokenTask;
import com.glogin.Contants.Global;
import com.glogin.Helper.NetworkUtils;
import com.glogin.Utity.AccessToken;
import com.glogin.Utity.UserInfo;
import com.glogin.View.PopupLoginGarena;

/**
 * Created by Minh vuong on 09/12/2016.
 */

public class GLoginActivity extends Activity {
    public static final String G_TOKEN = "token";
    public static final String CONFIG = "config";

    private final int REQUEST_CODE_GLOGIN = 1;

    private Config config;
    private PopupLoginGarena dialog;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        setTheme(R.style.Theme_Transparent);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_login);
        config = (Config) getIntent().getSerializableExtra(CONFIG);
        //loginG();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GLOGIN) {
            if (data == null || resultCode == RESULT_CANCELED) {
                endWithNoToken(Global.UNKNOWN);
                return;
            }
            final String code = data.getStringExtra("gg_token_value");
            if(resultCode == Activity.RESULT_OK) {
                exchangeToken(code);
            }
        }
    }

    public void loginG () {
        final String urlLogin = Global.URL_LOGIN_POPUP;
        final String appid = config.getGarenaLoginAppId();
        final String appkey = config.getGarenaLoginAppKey();
        final String redirectURL = config.getGarenaLoginRedirect();
        final String location = config.getGarenaLoginLocation();
        final String url = String.format(Global.LOGIN_FORM, urlLogin, appid, redirectURL, location);
        if (appid == null) {
            throw new RuntimeException("Garena login app id not found");
        }
        if (appkey == null) {
            throw new RuntimeException("Garena login app key not found");
        }
        if (redirectURL == null) {
            throw new RuntimeException("Garena login redirect url not found");
        }
        if (location == null) {
            throw new RuntimeException("Garena login location not found");
        }

        if(!NetworkUtils.isPackageInstalled(this, "com.garena.gas")) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog = new PopupLoginGarena(GLoginActivity.this);
                    dialog.setListener(gplusListener);
                    dialog.setUrlQuery(urlLogin);
                    dialog.setUrlRedirect(redirectURL);
                    dialog.callLogin(url);
                    dialog.show();
                }
            });
        } else {
            Intent loginIntent = new Intent();
            loginIntent.putExtra("gg_application_key", appkey);
            loginIntent.putExtra("gg_application_id", appid);
            loginIntent.putExtra("gg_app_redirect_url", redirectURL);
            loginIntent.setComponent(new ComponentName("com.garena.gas", "com.garena.gameauth.GPProxyAuthActivity"));
            this.startActivityForResult(loginIntent, REQUEST_CODE_GLOGIN);
        }
    }

    private void exchangeToken (String authCode) {
        ExchangeTokenTask exchangeTokenTask = new ExchangeTokenTask(this);
        exchangeTokenTask.setOnTaskExecutedListener(new ExchangeTokenTask.OnExchangeTokenTaskExecutedListener() {
            @Override
            public void onSuccess (AccessToken token) {
                getUserInfo(token);
            }

            @Override
            public void onError (String err) {
                endWithNoToken(err);
            }
        });
        exchangeTokenTask.execute(authCode, config.getGarenaLoginRedirect(), config.getGarenaLoginAppId(), config.getGarenaLoginAppKey());
    }

    private void inspectToken (String accessToken) {
        InspectTokenTask inspectTokenTask = new InspectTokenTask(this);
        inspectTokenTask.setOnTaskExecutedListener(new InspectTokenTask.OnInspectTokenTaskExecutedListener() {
            @Override
            public void onSuccess (AccessToken token) {
                getUserInfo(token);
            }

            @Override
            public void onError (String err) {
                endWithNoToken(err);
            }
        });
        inspectTokenTask.execute(accessToken);
    }

    private void getUserInfo (final AccessToken token) {
        GetUserInfoTask getUserInfoTask = new GetUserInfoTask(this);
        getUserInfoTask.setOnTaskExecutedListener(new GetUserInfoTask.OnTaskExecutedListener() {
            @Override
            public void onSuccess (UserInfo userInfo) {
                saveToken(token);
                if (GLogin.getInstance().getLoginCallback() != null)
                    GLogin.getInstance().getLoginCallback().onSuccess(userInfo);
                finish();
            }

            @Override
            public void onError (String err) {
                if (GLogin.getInstance().getLoginCallback() != null)
                    GLogin.getInstance().getLoginCallback().onGetUserInfoError(err);
                finish();
            }
        });
        getUserInfoTask.execute(token.getToken());
    }

    private void saveToken (AccessToken token) {
        GLogin.getInstance().setToken(token);
    }

    private void endWithNoToken (String err) {
        if (GLogin.getInstance().getLoginCallback() != null)
            GLogin.getInstance().getLoginCallback().onGetTokenError(err);
        finish();
    }

    private PopupLoginGarena.LoginInterface gplusListener = new PopupLoginGarena.LoginInterface() {
        @Override
        public void loginSuccess(final String token) {
            inspectToken(token);
        }

        @Override
        public void loginFailed(String message) {
            endWithNoToken(message);
        }

        @Override
        public void loginLoaded(String url) {}
    };

    @Override
    protected void onDestroy () {
        GLogin.getInstance().clearCallback();
        super.onDestroy();
    }
}
