package com.fachrur_122.rxdabinmodule;

import android.content.Context;

/**
 * Created by fachrur_122 on 17/05/2016.
 *
 */
public class Application extends android.app.Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        Application.context = getApplicationContext();
    }

    public static Context getAppContext() { return Application.context; }

}
