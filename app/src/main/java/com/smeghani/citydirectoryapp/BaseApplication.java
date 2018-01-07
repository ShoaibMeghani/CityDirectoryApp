package com.smeghani.citydirectoryapp;

import android.app.Application;

/**
 * Created by shoaibmeghani on 07/01/2018.
 */

public class BaseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        getDependencyManager().setApplicationContext(this);
    }

    public DependencyManager getDependencyManager(){
        return  DependencyManager.getInstance();
    }
}
