package net.onepagebook.wrs.app;

import android.app.Application;

import net.onepagebook.wrs.common.Log;
import net.onepagebook.wrs.main.fragment.wrs.WRSScheduleManager;

public class MyApplication extends Application {
    private String mWRSText;
    public void setWRSText(String text) {
        mWRSText = text;
        Log.d("text="+text);
    }
    public String getWRSText() {
        return mWRSText;
    }
    private int mOnceTextLength = 10;
    public void setOnceTextLength(int length) {
        mOnceTextLength = length;
    }
    public int getOnceTextLength() {
        return mOnceTextLength;
    }
    private String[] mTexts;
    public void setTextSplit(String[] texts) {
        mTexts = texts;
        mScheduleManager.setTextArrayLastPosition(texts.length - 1);
    }
    public String[] getTextSplit() {
        return mTexts;
    }
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