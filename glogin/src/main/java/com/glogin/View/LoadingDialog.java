package com.glogin.View;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Minh vuong on 13/12/2016.
 */

public class LoadingDialog {
    private static Dialog loadingDialog;

    public static void showLoadingDialog (Context context) {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(context);
        }

        if (loadingDialog.isShowing()) {
            return;
        }

        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    public static void hideLoadingDialog () {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    public static void setLoadingDialog (Dialog dialog) {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            throw new RuntimeException("Cannot set new loading dialog when current dialog is showing");
        }
        loadingDialog = dialog;
    }
}
