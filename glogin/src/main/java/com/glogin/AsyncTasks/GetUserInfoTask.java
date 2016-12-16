package com.glogin.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.glogin.Helper.APIClient;
import com.glogin.Contants.Global;
import com.glogin.Utity.UserInfo;
import com.glogin.View.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Minh vuong on 13/12/2016.
 */

public class GetUserInfoTask extends AsyncTask<String, String, UserInfo> {
    private Context context;
    private OnTaskExecutedListener onTaskExecutedListener;

    public GetUserInfoTask (Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute () {
        LoadingDialog.showLoadingDialog(context);
    }

    @Override
    protected UserInfo doInBackground (String... accessToken) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Global.PARAM_GTOKEN, accessToken[0]);
        try {
            JSONObject jsonObject = APIClient.getInstance(context).getPath(Global.URL_GET_USER_INFO, params);
            if (jsonObject == null) {
                return null;
            }
            if (jsonObject.has("error")) {
                publishProgress(jsonObject.getString("error"));
                return null;
            }
            return new UserInfo(jsonObject);
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
    protected void onPostExecute (UserInfo userInfo) {
        LoadingDialog.hideLoadingDialog();
        if (userInfo != null && onTaskExecutedListener != null) {
            onTaskExecutedListener.onSuccess(userInfo);
        }
    }

    public void setOnTaskExecutedListener (OnTaskExecutedListener listener) {
        onTaskExecutedListener = listener;
    }

    public interface OnTaskExecutedListener {
        void onSuccess (UserInfo userInfo);
        void onError (String err);
    }

}
