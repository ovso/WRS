package net.onepagebook.wrs.main.fragment.setting;

import android.content.Context;
import android.content.Intent;

/**
 * Created by jaeho_oh on 2015-12-22.
 */
public interface SetPresenter {
    void onActivityCreate();
    void onActivityResult(Context context, int requestCode, int resultCode, Intent data);
    void onClick(android.view.View view);
    interface View {
        void onInit();
        void navigateToActivity(Intent intent, int requestCode);
    }
}
