package net.onepagebook.wrs.main;

import android.content.Context;

import net.onepagebook.wrs.app.MyApplication;

/**
 * Created by ovso on 2016. 1. 2..
 */
public class MainModel {

    public void stopWRS(Context context) {
        ((MyApplication)context.getApplicationContext()).getScheduleManager().stop();
    }
}
