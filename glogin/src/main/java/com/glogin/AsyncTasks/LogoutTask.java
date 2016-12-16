package com.glogin.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.glogin.Helper.APIClient;
import com.glogin.Contants.Global;
import com.glogin.View.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Minh vuong on 14/12/2016.
 */

public class LogoutTask extends AsyncTask<String, String, Boolean> {
    private Context context;
    private OnLogoutTaskExecutedListener onTaskExecutedListener;

    public LogoutTask (Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute () {
        LoadingDialog.showLoadingDialog(context);
    }

    @Override
    protected Boolean doInBackground (String... token) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Global.PARAM_GTOKEN, token[0]);
        try {
            JSONObject jsonObject = APIClient.getInstance(context).getPath(Global.URL_LOGOUT, params);
            if (jsonObject == null) {
                publishProgress(Global.UNKNOWN);
                return false;
            }
            if (jsonObject.has("error")) {
                publishProgress(jsonObject.getString("error"));
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            publishProgress(Global.ERROR_IO);
        } catch (JSONException e) {
            e.printStackTrace();
            publishProgress(Global.ERROR_JSON_PARSE);
        }

        return false;
    }

    @Override
    protected void onProgressUpdate (String... values) {
        if (onTaskExecutedListener != null) {
            onTaskExecutedListener.onError(values[0]);
        }
    }

    @Override
    protected void onPostExecute (Boolean success) {
        LoadingDialog.hideLoadingDialog();
        if (success && onTaskExecutedListener != null) {
            onTaskExecutedListener.onSuccess();
        }
    }

    public void setOnTaskExecutedListener (OnLogoutTaskExecutedListener listener) {
        onTaskExecutedListener = listener;
    }

    public interface OnLogoutTaskExecutedListener {
        void onSuccess ();
        void onError (String err);
    }
}
