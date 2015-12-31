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
        mSkipButtonHandler = new SkipButtonHandler();
    }
    public void setTextArrayLastPosition(int textArrayLastPosition) {
        this.textArrayLastPosition = textArrayLastPosition;
    }
    private int mTextArraySize = 0;
    public void setTextArraySize(int size) {
        mTextArraySize = size;
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

        int totalTime = ((textDisplayTime+textDisplayinterval)*mTextArraySize)*252*40; // millis
        Log.d("totalTime="+((textDisplayTime+textDisplayinterval)*mTextArraySize));
        int seconds = totalTime/1000;
        int minutes = seconds / 60;
        int hour = minutes / 60;

        StringBuilder builder = new StringBuilder();
        builder.append(replaceIntToString(hour));
        builder.append(":");
        builder.append(replaceIntToString((minutes>59)?(minutes%60):minutes));
        builder.append(":");
        builder.append(replaceIntToString((seconds>59)?(seconds%60):seconds));

        Log.d(builder.toString());
        //시
        //분
        //초



    }
    private String replaceIntToString(int num) {
        if(num > 9) {
            return String.valueOf(num);
        } else {
            return "0"+num;
        }
    }
    public void pause() {
        Log.d("pause");
        mWRSState = WRS_STATE.PAUSE;
        textArrayCurrentPosition --;
        mView.showPlayButton();
    }
    public void skipPrevious() {
        if(mWRSState == WRS_STATE.PLAY) {
            textArrayCurrentPosition --;

            if(textArrayCurrentPosition < 0) {
                textArrayCurrentPosition = textArrayLastPosition;
            }
        } else { // PAUSE, STOP, ..
            textArrayCurrentPosition --;
            if(textArrayCurrentPosition < 0) {
                textArrayCurrentPosition = textArrayLastPosition;
            }
            String[] textArray = ((MyApplication) mContext.getApplicationContext()).getTextSplit();
            String text = textArray[textArrayCurrentPosition];
            mView.showText(text);

            mSkipButtonHandler.sendEmptyMessageDelayed(0, textDisplayTime);
        }
    }
    public void repeat() {
        int nowPosition = textArrayCurrentPosition>textArrayLastPosition?textArrayLastPosition-1:textArrayLastPosition;

        String[] textArray = ((MyApplication) mContext.getApplicationContext()).getTextSplit();
        String text = textArray[nowPosition];
        mView.showText(text);
        mSkipButtonHandler.sendEmptyMessageDelayed(0, textDisplayTime);
    }
    public void hold() {
        int nowPosition = textArrayCurrentPosition>textArrayLastPosition?textArrayLastPosition-1:textArrayCurrentPosition;

        String[] textArray = ((MyApplication) mContext.getApplicationContext()).getTextSplit();
        String text = textArray[nowPosition];
        mView.showText(text);
        //mSkipButtonHandler.sendEmptyMessageDelayed(0, textDisplayTime);
    }
    public void skipNext() {
        if(mWRSState == WRS_STATE.PLAY) {
            textArrayCurrentPosition ++;

            if(textArrayCurrentPosition > textArrayLastPosition) {
                textArrayCurrentPosition = 0;
            }
        } else { // PAUSE, STOP, ..
            textArrayCurrentPosition ++;

            if(textArrayCurrentPosition > textArrayLastPosition) {
                textArrayCurrentPosition = 0;
            }
            String[] textArray = ((MyApplication) mContext.getApplicationContext()).getTextSplit();
            String text = textArray[textArrayCurrentPosition];
            mView.showText(text);

            mSkipButtonHandler.sendEmptyMessageDelayed(0, textDisplayTime);
        }
    }
    public boolean isLoop = false;
    public void loop() {
        isLoop =! isLoop;
        if(isLoop) {
            mView.showLoopOn();
        } else {
            mView.showLoopOff();
        }
    }
    public void reset() {
        Log.d("reset");
    }
    private Handler mSkipButtonHandler;

    private class SkipButtonHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            mView.clearText();
        }
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
            //Log.d("msg.what="+msg.what);
            if(isLastPosition()){
                if(isLoop) {
                   textArrayCurrentPosition = 0;
                } else {
                    stop();
                    Log.d("END");
                    return;
                }

            }

            if(mWRSState == WRS_STATE.PLAY) {
                String[] textArray = ((MyApplication) mContext.getApplicationContext()).getTextSplit();
                //Log.d("textArrayCurrentPosition="+textArrayCurrentPosition);
                mView.showText(textArray[textArrayCurrentPosition]);
                mTextDisplayIntervalHandler.sendEmptyMessageDelayed(0, textDisplayTime);
                textArrayCurrentPosition ++;
            }else if(mWRSState == WRS_STATE.PAUSE) {

            }else if(mWRSState == WRS_STATE.STOP) {

            } else {

            }
        }
    }
    public enum WRS_STATE {PLAY, PAUSE, STOP, PREVIOUS};
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