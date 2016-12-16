package com.glogin.Helper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 7/29/2016.
 */
public class NetworkUtils {
    public static boolean isNetworkAvailable (Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isReachable (String ip) {
        try {
//            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            Process p1 = Runtime.getRuntime().exec("ping -c 1 " + ip);
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isPackageInstalled (Context mContext, String packagename) {
        PackageManager packageManager = mContext.getPackageManager();
        try {
            packageManager.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
