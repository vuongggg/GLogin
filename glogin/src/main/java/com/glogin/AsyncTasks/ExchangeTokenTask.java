package com.glogin.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.glogin.Helper.APIClient;
import com.glogin.Contants.Global;
import com.glogin.Utity.AccessToken;
import com.glogin.View.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Minh vuong on 13/12/2016.
 */

public class ExchangeTokenTask extends AsyncTask<String, String, AccessToken> {
    private Context context;
    private OnExchangeTokenTaskExecutedListener onTaskExecutedListener;

    public ExchangeTokenTask (Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute () {
        LoadingDialog.showLoadingDialog(context);
    }

    @Override
    protected AccessToken doInBackground (String... param) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Global.PARAM_GRANT_TYPE, "authorization_code");
        params.put(Global.PARAM_CODE, param[0]);
        params.put(Global.PARAM_REDIRECT_URI, param[1]);
        params.put(Global.PARAM_CLIENT_ID, param[2]);
        params.put(Global.PARAM_CLIENT_SECRECT, param[3]);
        try {
            JSONObject jsonObject = APIClient.getInstance(context).postPath(Global.URL_EXCHANGE_TOKEN, params);
            if (jsonObject == null) {
                return null;
            }
            if (jsonObject.has("error")) {
                publishProgress(jsonObject.getString("error"));
                return null;
            }
            return new AccessToken(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
            publishProgress(Global.ERROR_IO);
        } catch (JSONException e) {
            e.printStackTrace();
            publishProgress(Global.ERROR_JSON_PARSE);
        }

        return null;
    }

    @Override
    protected void onProgressUpdate (String... values) {
        if (onTaskExecutedListener != null) {
            onTaskExecutedListener.onError(values[0]);
        }
    }

    @Override
    protected void onPostExecute (AccessToken token) {
        LoadingDialog.hideLoadingDialog();
        if (token != null && onTaskExecutedListener != null) {
            onTaskExecutedListener.onSuccess(token);
        }
    }

    public void setOnTaskExecutedListener (OnExchangeTokenTaskExecutedListener listener) {
        onTaskExecutedListener = listener;
    }

    public interface OnExchangeTokenTaskExecutedListener {
        void onSuccess (AccessToken token);
        void onError (String err);
    }
}
