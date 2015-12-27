package net.onepagebook.wrs.main.fragment.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import net.onepagebook.wrs.common.Log;
import net.onpagebook.wrs.R;

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
                        String path = data.getData().getPath();
                        String fullFilePath = null;
                        Log.d("path=" + path);
                        if(path.indexOf(":") != -1) { // == if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH)
                            String fileName = path.split(":")[1];
                            fullFilePath = environmentDirectory + "/" + fileName;
                        } else {
                            fullFilePath = path;
                        }

                        mModel.setWRSText(context,mModel.readTextFile(fullFilePath));
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
