package net.onepagebook.wrs.main.fragment.wrs;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import net.onepagebook.wrs.common.Log;
import net.onepagebook.wrs.main.fragment.setting.SetPresenter;
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
        mWRSView.setTextSize(size);
        //Log.d("size="+size);
    }
    public int getTextSize() {
        return mTextSize;
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
        //String[] textArray = mWRSTextSplit;
        if(mWRSText == null || mWRSText.length() < 1) {
            mWRSView.changePage(PAGE_STATE.SET);
            mWRSView.showToast(mContext.getString(R.string.toast_empty_file));
        } else {
            mWRSState = WRS_STATE.PLAY;
            mTextDisplayTimeHandler.sendEmptyMessage(0);
            mTraningTimerHandler.sendEmptyMessage(0);
            mWRSView.showPauseButton();
        }
    }

    public void stop() {
        Log.d("stop");
        mWRSState = WRS_STATE.STOP;
        mWRSView.showPlayButton();
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

        mFixTimeStringBuilder = new StringBuilder();
        mFixTimeStringBuilder.append(replaceIntToString(hour));
        mFixTimeStringBuilder.append(":");
        mFixTimeStringBuilder.append(replaceIntToString(realMinutes));
        mFixTimeStringBuilder.append(":");
        mFixTimeStringBuilder.append(replaceIntToString(realSeconds));

        Log.d(mFixTimeStringBuilder.toString());
        mWRSView.showTraningTimeFix(mFixTimeStringBuilder.toString());
        //mWRSView.showTraningTimer(mFixTimeStringBuilder.toString());

    }
    private StringBuilder mFixTimeStringBuilder=null;
    public StringBuilder getFixTimerStringBuilder() {
        return mFixTimeStringBuilder;
    }
    public StringBuilder getTimerStringBuilder() {
        return mTimerStringBuilder;
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

        mTimerStringBuilder = new StringBuilder();
        mTimerStringBuilder.append(replaceIntToString(hour));
        mTimerStringBuilder.append(":");
        mTimerStringBuilder.append(replaceIntToString(realMinutes));
        mTimerStringBuilder.append(":");
        mTimerStringBuilder.append(replaceIntToString(realSeconds));

        //Log.d(builder.toString());
        mWRSView.showTraningTimer(mTimerStringBuilder.toString());
    }
    private StringBuilder mTimerStringBuilder=null;
    private String replaceIntToString(int num) {
        if(num > 9) {
            return String.valueOf(num);
        } else {
            return "0"+num;
        }
    }
    private String mFileName;
    public void setFileName(String name) {
        mFileName = name;
        mSetView.showFileName(name);
    }
    public String getFileName() {
        return mFileName;
    }
    public void pause() {
        Log.d("pause");
        mWRSState = WRS_STATE.PAUSE;
        textArrayCurrentPosition --;
        if(textArrayCurrentPosition < 0) {
            textArrayCurrentPosition = 0;
        }
        mWRSView.showPlayButton();

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
            if(mWRSText != null) {
                String text = textArray[textArrayCurrentPosition];
                mWRSView.showText(text);

                mSkipButtonHandler.sendEmptyMessageDelayed(0, textDisplayTime);
            } else {
                mWRSView.changePage(PAGE_STATE.SET);
                mWRSView.showToast(mContext.getString(R.string.toast_empty_file));
            }
        }
    }
    public void repeat() {
        int nowPosition = textArrayCurrentPosition>textArrayLastPosition?textArrayLastPosition-1:textArrayCurrentPosition;

        String[] textArray = mWRSTextSplit;
        if(mWRSText != null) {
            String text = textArray[nowPosition];
            Log.d("nowPosition="+nowPosition);
            Log.d("text = " + text);
            mWRSView.showText(text);
            mSkipButtonHandler.sendEmptyMessageDelayed(0, textDisplayTime);
        } else {
            mWRSView.showToast(mContext.getString(R.string.toast_empty_file));
            mWRSView.changePage(PAGE_STATE.SET);
        }
    }
    public void hold() {
        int nowPosition = textArrayCurrentPosition>textArrayLastPosition?textArrayLastPosition-1:textArrayCurrentPosition;

        String[] textArray = mWRSTextSplit;
        if(mWRSText != null) {
            String text = textArray[nowPosition];
            mWRSView.showText(text);
            //mSkipButtonHandler.sendEmptyMessageDelayed(0, textDisplayTime);
        } else {
            mWRSView.showToast(mContext.getString(R.string.toast_empty_file));
            mWRSView.changePage(PAGE_STATE.SET);
        }
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
            if(mWRSText != null) {
                String text = textArray[textArrayCurrentPosition];
                mWRSView.showText(text);

                mSkipButtonHandler.sendEmptyMessageDelayed(0, textDisplayTime);
            } else {
                mWRSView.showToast(mContext.getString(R.string.toast_empty_file));
                mWRSView.changePage(PAGE_STATE.SET);
            }
        }
    }
    public boolean isLoop = false;
    public void loop() {
        isLoop =! isLoop;
        if(isLoop) {
            mWRSView.showLoopOn();
        } else {
            mWRSView.showLoopOff();
        }
    }
    public void reset() {
        Log.d("reset");
/*
        textDisplayTime = 100;
        textDisplayinterval = 0;
        mTextSize = 15;
        mTextOnceLength = 5;
        .setTextSize(5);
*/
//        progressTextOnceLength = 0;
//        progressShowInterval = 0;
//        progressShowTime = 0;
//        progressTextSize = 0;
        stop();
        setProgressShowInterval(0);
        setProgressShowTime(0);
        setProgressTextSize(0);
        setProgressTextOnceLength(0);

        mWRSText = null;
        mFileName = null;
        mSetView.showFileName(mFileName);

        mFixTimeStringBuilder = null;
        mTimerStringBuilder = null;
        mWRSView.showTraningTimer("00:00:00");
        mWRSView.showTraningTimeFix("00:00:00");

        mWRSView.changePage(PAGE_STATE.SET);
    }
    private Handler mSkipButtonHandler;

    public int getProgressShowTime() {
        return progressShowTime;
    }

    public void setProgressShowTime(int progressShowTime) {
        this.progressShowTime = progressShowTime;
        mSetView.setSeekBarShowTime(progressShowTime);
    }

    public int getProgressShowInterval() {
        return progressShowInterval;
    }

    public void setProgressShowInterval(int progressShowInterval) {
        this.progressShowInterval = progressShowInterval;
        mSetView.setSeekBarShowInterval(progressShowInterval);
    }

    public int getProgressTextSize() {
        return progressTextSize;
    }

    public void setProgressTextSize(int progressTextSize) {
        this.progressTextSize = progressTextSize;
        mSetView.setSeekBarTextSize(progressTextSize);
    }

    public int getProgressTextOnceLength() {
        return progressTextOnceLength;
    }

    public void setProgressTextOnceLength(int progressTextOnceLength) {
        this.progressTextOnceLength = progressTextOnceLength;
        mSetView.setSeekBarTextOnceLength(progressTextOnceLength);
    }

    private class SkipButtonHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            mWRSView.clearText();
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

    private WRSPresenter.View mWRSView;
    public void setWRSView(WRSPresenter.View view) {
        mWRSView = view;
    }
    private SetPresenter.View mSetView;
    public void setSetView(SetPresenter.View view) {
        mSetView = view;
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
                mWRSView.showText(textArray[textArrayCurrentPosition]);
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
            mWRSView.clearText();
        }
    }

    private int progressShowTime = 0;
    private int progressShowInterval = 0;
    private int progressTextSize = 0;
    private int progressTextOnceLength = 0;
}