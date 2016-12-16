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
 * Created by Minh vuong on 14/12/2016.
 */

public class InspectTokenTask extends AsyncTask<String, String, AccessToken> {
    private Context context;
    private OnInspectTokenTaskExecutedListener onTaskExecutedListener;

    public InspectTokenTask (Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute () {
        LoadingDialog.showLoadingDialog(context);
    }

    @Override
    protected AccessToken doInBackground (String... token) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Global.PARAM_TOKEN, token[0]);
        try {
            JSONObject jsonObject = APIClient.getInstance(context).getPath(Global.URL_INSPECT_TOKEN, params);
            if (jsonObject == null) {
                return null;
            }
            if (jsonObject.has("error")) {
                publishProgress(jsonObject.getString("error"));
                return null;
            }
            AccessToken accessToken = new AccessToken(jsonObject);
            accessToken.setToken(token[0]);
            return accessToken;
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

    public void setOnTaskExecutedListener (OnInspectTokenTaskExecutedListener listener) {
        onTaskExecutedListener = listener;
    }

    public interface OnInspectTokenTaskExecutedListener {
        void onSuccess (AccessToken token);
        void onError (String err);
    }
}
