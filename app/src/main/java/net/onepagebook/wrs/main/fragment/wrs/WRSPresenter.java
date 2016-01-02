package net.onepagebook.wrs.main.fragment.wrs;

import android.content.Context;
import android.os.Handler;
import android.view.View;

/**
 * Created by jaeho_oh on 2015-12-22.
 */
public interface WRSPresenter {
    void onActivityCreate(Context context);
    void onClick(android.view.View view);
    interface View{
        void onInit();
        void showText(String text);
        void clearText();
        void showToast(String msg);
        void showPlayButton();
        void showPauseButton();
        void showLoopOn();
        void showLoopOff();
        void showResetDialog(Handler handler);
        void showTraningTimer(String time);
        void showTraningTimeFix(String time);
        void changePage(WRSScheduleManager.PAGE_STATE state);
    }
}
