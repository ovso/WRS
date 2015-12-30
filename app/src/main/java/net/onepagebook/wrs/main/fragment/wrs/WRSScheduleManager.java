package net.onepagebook.wrs.main.fragment.wrs;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import net.onepagebook.wrs.app.MyApplication;
import net.onepagebook.wrs.common.Log;

public class WRSScheduleManager {
    private int textDisplayTime = 100;
    private int textDisplayinterval = 100;
    private int textArrayLastPosition = 0;
    private int textArrayCurrentPosition = 0;

    private Context mContext;
    private TextDisplayTimeHandler mTextDisplayTimeHandler;
    private TextDisplayIntervalHandler mTextDisplayIntervalHandler;
    public WRSScheduleManager(Context context) {
        mContext = context;

        mTextDisplayTimeHandler = new TextDisplayTimeHandler();
        mTextDisplayIntervalHandler = new TextDisplayIntervalHandler();
    }
    public void setTextArrayLastPosition(int textArrayLastPosition) {
        this.textArrayLastPosition = textArrayLastPosition;
    }
    public void play() {
        Log.d("play");
        mWRSState = WRS_STATE.PLAY;
        mTextDisplayTimeHandler.sendEmptyMessage(0);
        mView.showPauseButton();
    }

    public void stop() {
        Log.d("stop");
        mWRSState = WRS_STATE.STOP;
        mView.showPlayButton();
        textArrayCurrentPosition = 0;
    }
    public void pause() {
        Log.d("pause");
        mWRSState = WRS_STATE.PAUSE;
        textArrayCurrentPosition --;
        mView.showPlayButton();
    }
    private WRSPresenter.View mView;
    public void setView(WRSPresenter.View view) {
        mView = view;
    }
    private boolean isLastPosition() {
        return textArrayCurrentPosition > textArrayLastPosition;
    }
    private class TextDisplayTimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            Log.d("msg.what="+msg.what);
            if(isLastPosition()){
                stop();
                //mView.showText("종료?");
                //mView.showToast("종료?");
                Log.d("END");
                return;
            }

            if(mWRSState == WRS_STATE.PLAY) {
                String[] textArray = ((MyApplication) mContext.getApplicationContext()).getTextSplit();
                Log.d("textArrayCurrentPosition="+textArrayCurrentPosition);
                mView.showText(textArray[textArrayCurrentPosition]);
                mTextDisplayIntervalHandler.sendEmptyMessageDelayed(0, textDisplayTime);
                textArrayCurrentPosition ++;
            }else if(mWRSState == WRS_STATE.PAUSE) {

            }else if(mWRSState == WRS_STATE.STOP) {

            } else {

            }
        }
    }
    public enum WRS_STATE {PLAY, PAUSE, STOP};
    private WRS_STATE mWRSState = WRS_STATE.STOP;
    public WRS_STATE getWRSState() {
        return mWRSState;
    }
    private class TextDisplayIntervalHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);

            if(mWRSState == WRS_STATE.PLAY) {
                mTextDisplayTimeHandler.sendEmptyMessageDelayed(0, textDisplayinterval);
            }else if(mWRSState == WRS_STATE.PAUSE) {

            } else if(mWRSState == WRS_STATE.STOP) {

            } else {

            }
            mView.clearText();
        }
    }
}