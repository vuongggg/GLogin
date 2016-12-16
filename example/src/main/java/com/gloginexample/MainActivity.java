package com.gloginexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.glogin.Config;
import com.glogin.GLogin;
import com.glogin.Interface.LoginCallback;
import com.glogin.Interface.LogoutCallback;
import com.glogin.Utity.UserInfo;
import com.glogin.View.GLoginButton;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private GLoginButton gLoginButton;
    private Button btnLogout;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String appid = getResources().getString(R.string.garena_login_appid);
        String appkey = getResources().getString(R.string.garena_login_appkey);
        String redirectURL = getResources().getString(R.string.garena_login_redirect);
        String location = getResources().getString(R.string.garena_login_location);
        Config config = new Config(appid, appkey, redirectURL, location);
        gLoginButton = (GLoginButton) findViewById(R.id.btn_glogin);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        textView = (TextView) findViewById(R.id.txt);
        gLoginButton.setLoginConfig(config);
        gLoginButton.setLoginCallback(new LoginCallback() {
            @Override
            public void onSuccess (UserInfo userInfo) {
                textView.setText("Hello " + userInfo.getNickname());
                Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                gLoginButton.setVisibility(View.GONE);
                btnLogout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onGetUserInfoError (String error) {
                Toast.makeText(MainActivity.this, "Login error: " + error, Toast.LENGTH_SHORT).show();
                gLoginButton.setVisibility(View.VISIBLE);
                btnLogout.setVisibility(View.GONE);
            }

            @Override
            public void onGetTokenError (String error) {
                Toast.makeText(MainActivity.this, "Login error: " + error, Toast.LENGTH_SHORT).show();
                gLoginButton.setVisibility(View.VISIBLE);
                btnLogout.setVisibility(View.GONE);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                GLogin.getInstance().logoutG(MainActivity.this, new LogoutCallback() {
                    @Override
                    public void onSuccess () {
                        textView.setText("");
                        Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                        gLoginButton.setVisibility(View.VISIBLE);
                        btnLogout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError (String error) {
                        Toast.makeText(MainActivity.this, "Log out error: " + error, Toast.LENGTH_SHORT).show();
                        gLoginButton.setVisibility(View.GONE);
                        btnLogout.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
}
