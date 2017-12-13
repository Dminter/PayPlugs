package com.zncm.dminter.payplugs.lawnchair.zncm.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.zncm.dminter.payplugs.App;


public class SPHelper {


    public static String getSuggestIntentByPkName(String pkgName) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.getInstance().getApplicationContext());
        return sp.getString(pkgName, "");
    }

    public static void setSuggestIntentByPkName(String pkgName,String suggestIntent) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.getInstance().getApplicationContext());
        sp.edit().putString(pkgName, suggestIntent).commit();
    }


}
