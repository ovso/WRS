package net.onepagebook.wrs.app;

import android.app.Application;

import net.onepagebook.wrs.main.fragment.wrs.WRSScheduleManager;

public class MyApplication extends Application {
    private WRSScheduleManager mScheduleManager;
    @Override
    public void onCreate() {
        super.onCreate();
        mScheduleManager = new WRSScheduleManager(getApplicationContext());
    }
    public WRSScheduleManager getScheduleManager() {
        return mScheduleManager;
    }
}