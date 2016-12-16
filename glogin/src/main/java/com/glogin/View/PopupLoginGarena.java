package com.glogin.View;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.glogin.R;


/**
 * Created by Administrator on 7/7/2016.
 */
public class PopupLoginGarena extends Dialog {
    private static String TAG = "LoginDialog";

    private WebView mWebView;
    private ImageView mCloseImg;
    private Context mContext;

    private LoginInterface listener;

    private String urlQuery;
    private String urlRedirect;

    public PopupLoginGarena (Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public PopupLoginGarena (Context context, int themeResId) {
        super(context, themeResId);
    }

    protected PopupLoginGarena (Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void initView () {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_popup);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        mWebView = (WebView) findViewById(R.id.popup_webview);
        mCloseImg = (ImageView) findViewById(R.id.popup_close_img);
        mCloseImg.setOnClickListener(closeOnClick);
        this.setCancelable(false);
        initWebView();
    }

    private void initWebView () {
        mWebView.clearCache(true);
        mWebView.clearHistory();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebViewClient(webChromeClient);
    }

    public void callLogin (String urlLogin) {
        mWebView.loadUrl(urlLogin);
    }

    private boolean checkForToken (String url) {
        if (url != null && urlRedirect != null && url.contains(urlRedirect)) {
            return true;
        }
        return false;
    }

    public LoginInterface getListener () {
        return listener;
    }

    public void setListener (LoginInterface listener) {
        this.listener = listener;
    }

    public String getUrlRedirect () {
        return urlRedirect;
    }

    public void setUrlRedirect (String urlRedirect) {
        this.urlRedirect = urlRedirect;
    }

    public String getUrlQuery () {
        return urlQuery;
    }

    public void setUrlQuery (String urlQuery) {
        this.urlQuery = urlQuery;
    }

    public void clearCookies () {
        CookieSyncManager.createInstance(mContext);
        CookieManager.getInstance().removeAllCookie();
    }

    private View.OnClickListener closeOnClick = new View.OnClickListener() {
        @Override
        public void onClick (View v) {
            mWebView.stopLoading();
            listener.loginFailed(mContext.getResources().getString(R.string.logincancel_message));
            dismiss();
        }
    };

    private WebViewClient webChromeClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading (WebView view, String url) {
            Log.d(TAG, "url webview = " + url);
            if (url != null && !url.equals("") && url.contains(urlRedirect) && url.contains("access_token")) {
                String token = url.replace(urlRedirect, "").replace("?access_token=", "");
                if (listener != null) {
                    listener.loginSuccess(token);
                }
                return false;
            }
            view.loadUrl(url);
            return true;
        }

        public void onPageFinished (WebView view, String url) {
            Log.d(TAG, "finish loaded = " + url);
            if (listener != null) {
                listener.loginLoaded(url);
            }
        }

        public void onReceivedError (WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(mContext, description, Toast.LENGTH_SHORT).show();
        }
    };

    public interface LoginInterface {
        void loginSuccess (String token);

        void loginFailed (String message);

        void loginLoaded (String url);
    }
}
