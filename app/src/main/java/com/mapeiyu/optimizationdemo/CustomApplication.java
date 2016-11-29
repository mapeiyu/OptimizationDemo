package com.mapeiyu.optimizationdemo;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mapeiyu on 16-11-28.
 */

public class CustomApplication extends Application {

    private List<Activity> list = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //// TODO: 16-11-28
    }

    private void add(Activity activity) {
        list.add(activity);
    }

    public static void addActivity(Activity activity) {
        ((CustomApplication)activity.getApplication()).add(activity);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        if (level >= ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            //// TODO: 16-11-28  
        }
    }
}
