package com.zncm.dminter.payplugs;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.Bugly;

import cn.bmob.v3.Bmob;


public class App extends Application {
    public Context ctx;
    public static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        this.ctx = this.getApplicationContext();
        instance = this;
        Bmob.initialize(this, "14b73b81487208dc6e4b58ec42fd7765");
        Bugly.init(getApplicationContext(), "b76d5eed70", false);
    }

    public static App getInstance() {
        return instance;
    }


}
