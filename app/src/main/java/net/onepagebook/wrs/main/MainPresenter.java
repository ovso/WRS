package net.onepagebook.wrs.main;

import android.content.Context;

/**
 * Created by ovso on 2016. 1. 2..
 */
public interface MainPresenter {
    void onCreate();
    void onPageSelected(Context context, int position);
    interface View{
        void onInit();
    }
}
