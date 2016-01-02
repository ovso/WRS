package net.onepagebook.wrs.main.fragment.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.SeekBar;

import net.onepagebook.wrs.app.MyApplication;
import net.onepagebook.wrs.common.Log;
import net.onepagebook.wrs.main.fragment.wrs.WRSScheduleManager;
import net.onpagebook.wrs.R;

public class SetPresenterImpl implements SetPresenter {
    SetPresenter.View mView;
    SetModel mModel;
    WRSScheduleManager mScheduleManager;
    public SetPresenterImpl(SetPresenter.View view) {
        mView = view;
        mModel = new SetModel();
    }

    @Override
    public void onActivityCreate(Context context) {
        mScheduleManager = ((MyApplication)context.getApplicationContext()).getScheduleManager();
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
                        String fullFilePath;
                        Log.d("path=" + path);
                        if(path.contains(":")) { // == if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH)
                            String fileName = path.split(":")[1];
                            fullFilePath = environmentDirectory + "/" + fileName;
                        } else {
                            fullFilePath = path;
                        }

                        mModel.setWRSText(context, mModel.readTextFile(fullFilePath));
                        mModel.doTextSplit(context);
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress) {
        Context context = seekBar.getContext();
        int id = seekBar.getId();
        if(id == R.id.seek_text_show_time) {
            mModel.onProgressChangedTime(context, progress, mView);
        } else if(id == R.id.seek_text_show_interval) {
            mModel.onProgressChangedInterval(context, progress, mView);
        } else if(id == R.id.seek_text_size) {
            mModel.onProgressChangedSize(context, progress, mView);
        } else if(id == R.id.seek_text_once_length) {
            mModel.onProgressChangedOnceLength(context, progress, mView);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(seekBar.getId() == R.id.seek_text_once_length) {
            mModel.onStopTrackingTouchOnceLength(seekBar.getContext());
        }
    }
}
