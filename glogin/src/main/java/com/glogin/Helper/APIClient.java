package com.glogin.Helper;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

/**
 * Created by Administrator on 7/15/2016.
 */
public class APIClient {
    private static APIClient instance;
    private Context mContext;

    public APIClient (Context context) {
        mContext = context;
    }

    public static APIClient getInstance (Context context) {
        if (instance == null) {
            instance = new APIClient(context);
        }
        return instance;
    }


    public JSONObject postPath (String url, HashMap param) throws IOException, JSONException, SocketTimeoutException {
        String urlParameters = StringUtils.getStringParam(StringUtils.getNamePair(param));
        CsHTTPConnection cscon = new CsHTTPConnection(mContext, url);
        cscon.setPostMethod(true);
        cscon.addParameter(urlParameters);
        JSONObject jo = cscon.finish();
        if (jo != null) {
            Log.d("JSON", jo.toString());
        }
        return jo;
//        controller.processReceiver(jo,cmd);
    }

    public JSONObject getPath (String url, HashMap param) throws IOException, JSONException, SocketTimeoutException {
        String urlParameters = StringUtils.getStringParam(StringUtils.getNamePair(param));
        url = url + "?" + urlParameters;
        Log.d("URLREQUEST", "Sending 'GET' request to URL : " + url);
        CsHTTPConnection cscon = new CsHTTPConnection(mContext, url);
        JSONObject jo = cscon.finish();
        if (jo != null) {
            Log.d("JSON", jo.toString());
        }
//        controller.processReceiver(jo,cmd);
        return jo;
    }

    public interface APIDelegate {
        public void querySuccess (JSONObject response);

        public void queryFailed (String error);
    }
}
