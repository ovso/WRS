package net.onepagebook.wrs.main.fragment.wrs;

/**
 * Created by jaeho_oh on 2015-12-22.
 */
public interface WRSPresenter {
    void onActivityCreate();
    void onClickPlay(android.view.View view);
    interface View{
        void onInit();
        void play(String content);
    }
}
