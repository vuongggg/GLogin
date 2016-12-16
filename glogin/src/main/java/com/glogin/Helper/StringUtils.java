package com.glogin.Helper;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class StringUtils {
    public static List<NameValuePair> getNamePair(HashMap param){
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(param.size());
        Iterator<HashMap.Entry<String, String>> it = param.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry<String, String> entry = it.next();
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return nameValuePairs;
    }

    public static String getStringParam(List<NameValuePair> pairs){
        return URLEncodedUtils.format(pairs, "utf-8");
    }
}
