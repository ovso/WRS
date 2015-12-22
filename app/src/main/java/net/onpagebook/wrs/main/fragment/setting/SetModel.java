package net.onpagebook.wrs.main.fragment.setting;

import android.content.Intent;

/**
 * Created by jaeho_oh on 2015-12-22.
 */
public class SetModel {

    private final static int REQUEST_CODE_FILE_CHOOSER = 0X11;
    public Intent getFileChooserIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        return Intent.createChooser(intent, "파일 선택");
    }
    public int getFileChooserRequestCode() {
        return REQUEST_CODE_FILE_CHOOSER;
    }
}
