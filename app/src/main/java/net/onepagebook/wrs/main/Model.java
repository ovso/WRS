package net.onepagebook.wrs.main;

import android.content.Context;

import net.onepagebook.wrs.app.MyApplication;

public class Model {
    public void setWRSText(Context context, String text) {
        ((MyApplication)context.getApplicationContext()).setWRSText(text);
    }
    public String getWRSText(Context context) {
        return ((MyApplication)context.getApplicationContext()).getWRSText();
    }
}
