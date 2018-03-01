package com.basepictureoptionslib.android;

import android.app.Application;


public class PictureOptionsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppCommon.APPLICATION_CONTEXT = getApplicationContext();
    }
}
