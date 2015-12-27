package net.onepagebook.wrs.app;

import android.app.Application;
import android.content.Context;

import net.onepagebook.wrs.common.Log;

/**
 * Created by ovso on 2015. 12. 27..
 */
public class MyApplication extends Application {
    private String mWRSText;
    public void setWRSText(String text) {
        mWRSText = text;
        Log.d("text="+text);
    }
    public String getWRSText() {
        return mWRSText;
    }
}
