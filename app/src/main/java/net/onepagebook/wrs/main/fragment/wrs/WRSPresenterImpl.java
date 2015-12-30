package net.onepagebook.wrs.main.fragment.wrs;

import android.content.Context;

import net.onepagebook.wrs.app.MyApplication;
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
        mScheduleManager.setView(mView);

        mView.onInit();
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
        } else {

        }
    }
}
