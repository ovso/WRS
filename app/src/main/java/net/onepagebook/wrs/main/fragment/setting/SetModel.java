package net.onepagebook.wrs.main.fragment.setting;

import android.content.Context;
import android.content.Intent;
import android.widget.SeekBar;

import net.onepagebook.wrs.app.MyApplication;
import net.onepagebook.wrs.common.Log;
import net.onepagebook.wrs.main.Model;
import net.onpagebook.wrs.R;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class SetModel extends Model {

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

    public String readTextFile(String filePath) {
        String charsetName = getDetectedCharset(filePath);

        String text = null;
        try {
            //File file = context.getFileStreamPath(filePath);
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            Reader in = new InputStreamReader(fis, charsetName);
            int size = fis.available();
            char[] buffer = new char[size];
            in.read(buffer);
            in.close();

            text = new String(buffer);
            Log.d("text=" + text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return text;
    }

    public String getDetectedCharset(String fileName) {
        byte[] buf = new byte[4096];
        FileInputStream fis;
        String encoding;

        try {
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        // (1)
        UniversalDetector detector = new UniversalDetector(null);
        // (2)
        int nread;
        try {
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // (3)
        detector.dataEnd();
        // (4)
        encoding = detector.getDetectedCharset();
        if (encoding != null) {
            Log.d("Detected encoding = " + encoding);
        } else {
            Log.d("No encoding detected.");
        }
        // (5)
        detector.reset();

        return encoding;
    }
    public void onProgressChangedTime(Context context, int progress, SetPresenter.View view) {
        String[] times = context.getResources().getStringArray(R.array.time_array);
        //100 : progress = 20 : x
        int index = (progress * times.length / 100)>(times.length-1)?(times.length-1):(progress * times.length / 100);
        int time = (int) (Double.parseDouble(times[index])*1000);
        ((MyApplication)context.getApplicationContext()).getScheduleManager().setTextShowTime(time);
        view.setTextShowTime(times[index] + "초");
    }

    public void onProgressChangedInterval(Context context, int progress, SetPresenter.View view) {
        String[] times = context.getResources().getStringArray(R.array.time_array);
        //100 : progress = 20 : x
        int index = (progress * times.length / 100)>(times.length-1)?(times.length-1):(progress * times.length / 100);
        int interval = (int) (Double.parseDouble(times[index])*1000);
        ((MyApplication)context.getApplicationContext()).getScheduleManager().setTextShowInterval(interval);
        view.setTextShowInterval(times[index] + "초");
    }

    public void onProgressChangedSize(Context context, int progress, SetPresenter.View view) {
        String[] sizes = context.getResources().getStringArray(R.array.text_size_array);
        //100 : progress = 20 : x
        int index = (progress * sizes.length / 100)>(sizes.length-1)?(sizes.length-1):(progress * sizes.length / 100);
        int textSize = Integer.parseInt(sizes[index]);
        ((MyApplication)context.getApplicationContext()).getScheduleManager().setTextSize(textSize);
        view.setTextSize(sizes[index] + "SP");
    }

    public void onProgressChangedOnceLength(Context context, int progress, SetPresenter.View view) {
        String[] onceLength = context.getResources().getStringArray(R.array.text_once_length_array);
        //100 : progress = 20 : x
        int index = (progress * onceLength.length / 100)>(onceLength.length-1)?(onceLength.length-1):(progress * onceLength.length / 100);
        int textOnceLength = Integer.parseInt(onceLength[index]);
        ((MyApplication)context.getApplicationContext()).getScheduleManager().setTextOnceLength(textOnceLength);

        view.setTextOnceLength(onceLength[index] + "자");
    }

    public void setWRSText(Context context, String text) {
        ((MyApplication)context.getApplicationContext()).getScheduleManager().setWRSText(text);
    }
    public String getWRSText(Context context) {
        return ((MyApplication)context.getApplicationContext()).getScheduleManager().getWRSText();
    }
    public void setTextSplit(Context context, String[] texts) {
        ((MyApplication)context.getApplicationContext()).getScheduleManager().setWRSTextSplit(texts);
        ((MyApplication)context.getApplicationContext()).getScheduleManager().setTextArrayLastPosition(texts.length - 1);
        ((MyApplication)context.getApplicationContext()).getScheduleManager().setTextArraySize(texts.length);
        ((MyApplication)context.getApplicationContext()).getScheduleManager().setTraningTime();
    }
    public void setFileName(Context context, String name) {
        ((MyApplication)context.getApplicationContext()).getScheduleManager().setFileName(name);
    }
    public String getFileName(Context context) {
        return ((MyApplication)context.getApplicationContext()).getScheduleManager().getFileName();
    }
    public void doTextSplit(Context context) {
        String fullText = getWRSText(context);
        int fullTextLength = fullText.trim().length();
        Log.d("문장길이 = " + fullTextLength);

        int onceLength = ((MyApplication)context.getApplicationContext()).getScheduleManager().getTextOnceLength();
        Log.d("한번에 표시될 길이 = " + onceLength);

        Log.d("fullTextLength/onceLength = " + (fullTextLength/onceLength));
        Log.d("fullTextLength%onceLength = " + (fullTextLength % onceLength));

        int textArrayLength = fullTextLength%onceLength==0?(fullTextLength/onceLength):((fullTextLength/onceLength)+1);
        Log.d("배열 갯수=" + textArrayLength);
        String[] textArray = new String[textArrayLength];
        Log.d("textArray.length = " + textArray.length);

        for (int i = 0; i < textArray.length; i++) {
            int start = i*onceLength;
            int end = start + onceLength;
            if(i == textArray.length-1) {
                end = start + (fullTextLength%onceLength);
            }
            textArray[i] = fullText.subSequence(start, end).toString();
        }

        setTextSplit(context, textArray);
    }
    public void onStopTrackingTouchOnceLength(SeekBar seekBar) {

        if(((MyApplication)seekBar.getContext().getApplicationContext()).getScheduleManager().getWRSText() != null &&
                !((MyApplication)seekBar.getContext().getApplicationContext()).getScheduleManager().getWRSText().equals("")) {
            doTextSplit(seekBar.getContext());
        }
        ((MyApplication)seekBar.getContext().getApplicationContext()).getScheduleManager().setProgressTextOnceLength(seekBar.getProgress());
    }
    public void onStopTrackingTouchTextSize(SeekBar seekBar) {
        Log.d("progress = " + seekBar.getProgress());
        ((MyApplication)seekBar.getContext().getApplicationContext()).getScheduleManager().setProgressTextSize(seekBar.getProgress());
    }
    public void onStopTrackingTouchTextTime(SeekBar seekBar) {
        ((MyApplication)seekBar.getContext().getApplicationContext()).getScheduleManager().setProgressShowTime(seekBar.getProgress());
    }
    public void onStopTrackingTouchTextInterval(SeekBar seekBar) {
        ((MyApplication)seekBar.getContext().getApplicationContext()).getScheduleManager().setProgressShowInterval(seekBar.getProgress());
    }
}