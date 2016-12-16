# GLogin

Support Garena login

# Usage

 1. Include the library as local library project or add the dependency in your build.gradle.
        
        dependencies {
            compile 'com.glogin:glogin:0.1'
        }
        
 2. Define GLoginActivity in your Manifest

        <activity android:name="com.glogin.GLoginActivity" />
        
 3. Include the PagerSlidingTabStrip widget in your layout. This should usually be placed
    above the `ViewPager` it represents.

        <com.glogin.View.GLoginButton
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/btn_glogin" />
            
 4. In your `onCreate` method (or `onCreateView` for a fragment), bind the
     widget to the `GLoginButton`.

         //init GLoginButton
         gLoginButton = (GLoginButton) findViewById(R.id.btn_glogin);
         
         // create new config object for login operation and bind it to our GLoginButton
         Config config = new Config(appid, appkey, redirectURL, location);
         gLoginButton.setLoginConfig(config);
         
         // add a callback for login operation
         gLoginButton.setLoginCallback(new LoginCallback() {
             @Override
                 public void onSuccess (UserInfo userInfo) {}

             @Override
                 public void onGetUserInfoError (String error) {}

             @Override
                 public void onGetTokenError (String error) {}
         });
         
 5. To logout, use `GLogin.getInstance().logoutG(Context, LogoutCallback)`
  
         GLogin.getInstance().logoutG(MainActivity.this, new LogoutCallback() {
                    @Override
                    public void onSuccess () {}

                    @Override
                    public void onError (String error) {}
                });
                
 6. To get user infomation after login, use `GLogin.getInstance().getUserInfo(Context, GetUserInfoCallback)`
  
         GLogin.getInstance().getUserInfo(MainActivity.this, new GetUserInfoCallback() {
                    @Override
                    public void onSuccess (UserInfo userInfo) {}

                    @Override
                    public void onError (String error) {}
                });
                
 7. *(Optional)* If you don't want to use GLoginButton, you can call `GLogin.getInstance().loginG()` directly
   
         GLogin.getInstance().loginG(context, config, loginCallback);
         
# Customization

 * `format` Display format of GLoginButton, include `'icon_only'`, `'text_only'`, `'text_with_icon'`
 
