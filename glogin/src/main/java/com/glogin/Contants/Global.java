package com.glogin.Contants;

/**
 * Created by Administrator on 7/15/2016.
 */
public class Global {
    public static final String URL_BASE = "https://auth.garena.com";

    public static final String URL_LOGIN_POPUP = URL_BASE + "/oauth/login?";
    public static final String URL_INSPECT_TOKEN = URL_BASE + "/oauth/token/inspect";
    public static final String URL_GET_USER_INFO = URL_BASE + "/oauth/user/info/get";
    public static final String URL_EXCHANGE_TOKEN = URL_BASE + "/oauth/token";
    public static final String URL_LOGOUT = URL_BASE + "/oauth/logout";

    public static final String LOGIN_FORM = "%sclient_id=%s&redirect_uri=%s&response_type=token&locale=%s";

    public static final String PARAM_GTOKEN = "access_token";
    public static final String PARAM_GRANT_TYPE = "grant_type";
    public static final String PARAM_CODE = "code";
    public static final String PARAM_REDIRECT_URI = "redirect_uri";
    public static final String PARAM_CLIENT_ID = "client_id";
    public static final String PARAM_CLIENT_SECRECT = "client_secret";
    public static final String PARAM_TOKEN = "token";

    public static final String UNKNOWN = "Unknown";

    public static final String ERROR_JSON_PARSE = "json parse error";
    public static final String ERROR_IO = "input output error";
    public static final String ERROR_TOKEN_EXPIRED = "token expired";

    public static final String DEFAULT_AVA_URL = "http://cdn.garenanow.com/webmain/static/images/avatars/default.jpg";

}
