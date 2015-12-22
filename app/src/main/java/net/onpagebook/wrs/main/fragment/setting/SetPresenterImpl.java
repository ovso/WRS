package net.onpagebook.wrs.main.fragment.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.view.View;

import net.onepagebook.wrs.common.Log;
import net.onpagebook.wrs.R;

import java.io.File;

/**
 * Created by jaeho_oh on 2015-12-22.
 */
public class SetPresenterImpl implements SetPresenter {
    SetPresenter.View mView;
    SetModel mModel;
    public SetPresenterImpl(SetPresenter.View view) {
        mView = view;
        mModel = new SetModel();
    }

    @Override
    public void onActivityCreate() {
        mView.onInit();
    }
    @Override
    public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == mModel.getFileChooserRequestCode()) {
                if(data != null) {
                    if(data.getData() != null) {
                        String environmentDirectory = Environment.getExternalStorageDirectory().toString();
                        Log.d("getPath=" + data.getData().getPath());
                        String path = data.getData().getPath();
                        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                            String fileName = path.split(":")[1];
                            String fullPath = environmentDirectory + "/" + fileName;
                            Log.d(fullPath);
                            File file = new File(fullPath);
                            Log.d(file.exists()+"");
                        } else {
                            File file = new File(data.getData().getPath());
                            Log.d(file.exists()+"kit");
                        }
                    }

                }
            }
        }
    }

    @Override
    public void onClick(android.view.View view) {
        if(view.getId() == R.id.button) {
            mView.navigateToActivity(mModel.getFileChooserIntent(), mModel.getFileChooserRequestCode());
        }
    }
}
