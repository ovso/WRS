package net.onepagebook.wrs.main.fragment.wrs;

import android.content.Context;
import android.view.View;

/**
 * Created by jaeho_oh on 2015-12-22.
 */
public interface WRSPresenter {
    void onActivityCreate(Context context);
    void onClick(android.view.View view);
    interface View{
        void onInit();
        void play(String content);
        void showText(String text);
        void clearText();
        void showToast(String msg);
        void showPlayButton();
        void showPauseButton();
    }
}
