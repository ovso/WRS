package net.onpagebook.wrs.main.fragment.wrs;

import android.view.View;

/**
 * Created by jaeho_oh on 2015-12-22.
 */
public interface WRSPresenter {
    void onActivityCreate();
    void onClickPlay(android.view.View view);
    interface View{
        void onInit();
    }
}
