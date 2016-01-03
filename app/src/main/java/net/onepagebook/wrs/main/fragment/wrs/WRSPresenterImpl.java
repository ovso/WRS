package net.onepagebook.wrs.main.fragment.wrs;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import net.onepagebook.wrs.app.MyApplication;
import net.onepagebook.wrs.common.Log;
import net.onpagebook.wrs.R;

public class WRSPresenterImpl implements WRSPresenter {
    private WRSPresenter.View mView;
    private WRSModel mModel;

    public WRSPresenterImpl(WRSPresenter.View view) {
        mView = view;
        mModel = new WRSModel();
    }

    private WRSScheduleManager mScheduleManager;
    @Override
    public void onActivityCreate(Context context) {
        mScheduleManager = ((MyApplication)context.getApplicationContext()).getScheduleManager();
        mScheduleManager.setWRSView(mView);

        mView.onInit();
        Log.d("isLoop = " + mScheduleManager.isLoop);
        if(mScheduleManager.isLoop) {
            mView.showLoopOn();
        } else {
            mView.showLoopOff();
        }
        mView.showTraningTimeFix(mScheduleManager.getFixTimerStringBuilder()==null?"00:00:00":mScheduleManager.getFixTimerStringBuilder().toString());
        mView.showTraningTimer(mScheduleManager.getTimerStringBuilder()==null?"00:00:00":mScheduleManager.getTimerStringBuilder().toString());
    }

    @Override
    public void onClick(android.view.View view) {
        int id = view.getId();
        if(id == R.id.btn_play_pause) {
            if(mScheduleManager.getWRSState() == WRSScheduleManager.WRS_STATE.STOP) {
                mScheduleManager.play();
            } else if(mScheduleManager.getWRSState() == WRSScheduleManager.WRS_STATE.PLAY) {
                mScheduleManager.pause();
            } else if(mScheduleManager.getWRSState() == WRSScheduleManager.WRS_STATE.PAUSE) {
                mScheduleManager.play();
            }
        } else if(id ==R.id.btn_stop) {
            mScheduleManager.stop();
        } else if(id == R.id.btn_skip_previous) {
            mScheduleManager.skipPrevious();
        } else if(id == R.id.btn_skip_next) {
            mScheduleManager.skipNext();
        } else if(id == R.id.btn_current_repeat) {
            mScheduleManager.repeat();
        } else if(id == R.id.btn_current_hold) {
            mScheduleManager.hold();
        } else if(id == R.id.btn_loop) {
            mScheduleManager.loop();
        } else if(id == R.id.btn_reset) {
            mView.showResetDialog(new DialogInterfaceHandler());
            mScheduleManager.pause();
        }
    }
    private class DialogInterfaceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            mScheduleManager.reset();
        }
    }
}
