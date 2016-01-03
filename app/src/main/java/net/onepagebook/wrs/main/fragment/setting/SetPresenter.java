package net.onepagebook.wrs.main.fragment.setting;

import android.content.Context;
import android.content.Intent;
import android.widget.SeekBar;

/**
 * Created by jaeho_oh on 2015-12-22.
 */
public interface SetPresenter {
    void onActivityCreate(Context context);
    void onActivityResult(Context context, int requestCode, int resultCode, Intent data);
    void onClick(android.view.View view);
    void onProgressChanged(SeekBar seekBar, int progress);
    //void onStartTrackingTouch(SeekBar seekBar);
    void onStopTrackingTouch(SeekBar seekBar);
    interface View {
        void onInit();
        void navigateToActivity(Intent intent, int requestCode);
        void setTextShowTime(String text);
        void setTextShowInterval(String text);
        void setTextSize(String text);
        void setTextOnceLength(String text);
        void setSeekBarShowTime(int progress);
        void setSeekBarShowInterval(int progress);
        void setSeekBarTextSize(int progress);
        void setSeekBarTextOnceLength(int progress);
        void showFileName(String name);
    }
}
