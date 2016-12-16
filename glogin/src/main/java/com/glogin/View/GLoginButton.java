package com.glogin.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glogin.Config;
import com.glogin.GLogin;
import com.glogin.Interface.LoginCallback;
import com.glogin.R;

/**
 * Created by Minh vuong on 13/12/2016.
 */

public class GLoginButton extends LinearLayout {
    public static final int FORMAT_ICON_ONLY = 1;
    public static final int FORMAT_TEXT_ONLY = 2;
    public static final int FORMAT_TEXT_WITH_ICON = 0;

    private ImageView imgGarenaIcon;
    private TextView txtLogin;
    private Config loginConfig;
    private LoginCallback loginCallback;

    public GLoginButton (Context context) {
        super(context);
        init(null);
    }

    public GLoginButton (Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GLoginButton (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GLoginButton (Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init (AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.btn_g_login, this);
        imgGarenaIcon = (ImageView) view.findViewById(R.id.img_gar_logo);
        txtLogin = (TextView) view.findViewById(R.id.txt_login);
        setClickable(true);

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.GLoginButton);
            int format = typedArray.getInt(R.styleable.GLoginButton_format, 0);
            setFormat(format);
        }
    }

    @Override
    public boolean performClick () {
        boolean b = super.performClick();
        if (loginConfig == null) {
            throw new RuntimeException("Login config not found");
        }
        else {
            GLogin.getInstance().loginG(getContext(), loginConfig, loginCallback);
        }

        return b;
    }

    public void setFormat (int format) {
        int padding = (int) getContext().getResources().getDimension(R.dimen.txt_login_padding);
        switch (format) {
            case FORMAT_ICON_ONLY:
                imgGarenaIcon.setVisibility(VISIBLE);
                txtLogin.setVisibility(GONE);
                break;
            case FORMAT_TEXT_ONLY:
                imgGarenaIcon.setVisibility(GONE);
                txtLogin.setVisibility(VISIBLE);
                txtLogin.setPadding(padding, 0, padding, 0);
                break;
            case FORMAT_TEXT_WITH_ICON:
                imgGarenaIcon.setVisibility(VISIBLE);
                txtLogin.setVisibility(VISIBLE);
                txtLogin.setPadding(0, 0, padding, 0);
                break;
        }
    }

    public void setLoginConfig (Config config) {
        loginConfig = config;
    }

    public void setLoginCallback (LoginCallback listener) {
        this.loginCallback = listener;
    }
}
