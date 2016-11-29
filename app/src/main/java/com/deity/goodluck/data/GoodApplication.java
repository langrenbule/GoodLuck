package com.deity.goodluck.data;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.client.android.BuildConfig;

/**
 * Created by Deity on 2016/11/29.
 */

public class GoodApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(getApplicationContext());
    }
}
