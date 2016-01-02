package net.onepagebook.wrs.main.fragment.wrs;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import net.onepagebook.wrs.app.MyApplication;
import net.onepagebook.wrs.common.Log;
import net.onpagebook.wrs.R;

public class WRSScheduleManager {
    private int textDisplayTime = 100;
    private int textDisplayinterval = 100;
    private int textArrayLastPosition = 0;
    private int textArrayCurrentPosition = 0;

    private Context mContext;
    private TextDisplayTimeHandler mTextDisplayTimeHandler;
    private TextDisplayIntervalHandler mTextDisplayIntervalHandler;
    private TraningTimerHandler mTraningTimerHandler;
    public WRSScheduleManager(Context context) {
        mContext = context;

        mTextDisplayTimeHandler = new TextDisplayTimeHandler();
        mTextDisplayIntervalHandler = new TextDisplayIntervalHandler();
        mSkipButtonHandler = new SkipButtonHandler();
        mTraningTimerHandler = new TraningTimerHandler();
    }
    public void setTextArrayLastPosition(int textArrayLastPosition) {
        this.textArrayLastPosition = textArrayLastPosition;
    }
    private int mTextArraySize = 0;
    public void setTextArraySize(int size) {
        mTextArraySize = size;
    }
    private int mTextSize = 15;
    public void setTextSize(int size) {
        mTextSize = size;
        mView.setTextSize(size);
        //Log.d("size="+size);
    }
    public void setTextShowTime(int time) {
        textDisplayTime = time;
    }
    public int getTextShowTime() {
        return textDisplayTime;
    }
    public void setTextShowInterval(int millis) {
        textDisplayinterval = millis;
    }
    public int getTextShowInterval() {
        return textDisplayinterval;
    }
    private int mTextOnceLength = 5;
    public void setTextOnceLength(int length) {
        mTextOnceLength = length;
    }
    private String mWRSText;
    public void setWRSText(String text) {
        mWRSText = text;
    }
    public String getWRSText() {
        return mWRSText;
    }
    private String[] mWRSTextSplit;
    public void setWRSTextSplit(String[] texts){
        mWRSTextSplit = texts;
    }

    public int getTextOnceLength() {
        return mTextOnceLength;
    }
    public void play() {
        Log.d("play");
        String[] textArray = mWRSTextSplit;
        if(textArray == null || textArray.length < 1) {
            mView.changePage(PAGE_STATE.SET);
            mView.showToast(mContext.getString(R.string.toast_empty_file));
        } else {
            mWRSState = WRS_STATE.PLAY;
            mTextDisplayTimeHandler.sendEmptyMessage(0);
            mTraningTimerHandler.sendEmptyMessage(0);
            mView.showPauseButton();
        }
    }

    public void stop() {
        Log.d("stop");
        mWRSState = WRS_STATE.STOP;
        mView.showPlayButton();
        textArrayCurrentPosition = 0;

        mTraningTimerHandler.sendEmptyMessage(-1);
    }
    public void setTraningTime() {

        int totalTime = ((textDisplayTime+textDisplayinterval)*mTextArraySize); // millis
        Log.d("totalTime="+((textDisplayTime+textDisplayinterval)*mTextArraySize));
        int seconds = totalTime/1000;
        int realSeconds = (seconds>59)?(seconds%60):seconds;
        int minutes = (seconds / 60);
        int realMinutes = (minutes>59)?(minutes%60):minutes;
        int hour = minutes / 60;

        StringBuilder builder = new StringBuilder();
        builder.append(replaceIntToString(hour));
        builder.append(":");
        builder.append(replaceIntToString(realMinutes));
        builder.append(":");
        builder.append(replaceIntToString(realSeconds));

        Log.d(builder.toString());
        mView.showTraningTimeFix(builder.toString());
        mView.showTraningTimer(builder.toString());

    }
    public void setTraningTimer() {
        int totalTime = ((textDisplayTime+textDisplayinterval)*(mTextArraySize))-((textDisplayTime+textDisplayinterval)*(mTextArraySize-textArrayCurrentPosition)); // millis
        //int totalTime = (textDisplayTime+textDisplayinterval)*(mTextArraySize-textArrayCurrentPosition);
        //Log.d("totalTime="+((textDisplayTime+textDisplayinterval)*mTextArraySize));
        int seconds = totalTime/1000;
        int realSeconds = (seconds>59)?(seconds%60):seconds;
        int minutes = (seconds / 60);
        int realMinutes = (minutes>59)?(minutes%60):minutes;
        int hour = minutes / 60;

        StringBuilder builder = new StringBuilder();
        builder.append(replaceIntToString(hour));
        builder.append(":");
        builder.append(replaceIntToString(realMinutes));
        builder.append(":");
        builder.append(replaceIntToString(realSeconds));

        //Log.d(builder.toString());
        mView.showTraningTimer(builder.toString());
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

        mTraningTimerHandler.sendEmptyMessage(-1);
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
            String[] textArray = mWRSTextSplit;
            String text = textArray[textArrayCurrentPosition];
            mView.showText(text);

            mSkipButtonHandler.sendEmptyMessageDelayed(0, textDisplayTime);
        }
    }
    public void repeat() {
        int nowPosition = textArrayCurrentPosition>textArrayLastPosition?textArrayLastPosition-1:textArrayCurrentPosition;

        String[] textArray = mWRSTextSplit;
        String text = textArray[nowPosition];
        Log.d("nowPosition="+nowPosition);
        Log.d("text = " + text);
        mView.showText(text);
        mSkipButtonHandler.sendEmptyMessageDelayed(0, textDisplayTime);
    }
    public void hold() {
        int nowPosition = textArrayCurrentPosition>textArrayLastPosition?textArrayLastPosition-1:textArrayCurrentPosition;

        String[] textArray = mWRSTextSplit;
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
            String[] textArray = mWRSTextSplit;
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
    private class TraningTimerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            setTraningTimer();
            if(msg.what != -1) {
                this.sendEmptyMessageDelayed(0, 500);
            }
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
                String[] textArray = mWRSTextSplit;
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
    public enum PAGE_STATE {WRS, SET};
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