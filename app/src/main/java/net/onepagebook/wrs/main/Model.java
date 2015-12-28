package net.onepagebook.wrs.main;

import android.content.Context;

import net.onepagebook.wrs.app.MyApplication;
import net.onepagebook.wrs.common.Log;

public class Model {
    public void setWRSText(Context context, String text) {
        ((MyApplication)context.getApplicationContext().getApplicationContext()).setWRSText(text);
    }
    public String getWRSText(Context context) {
        return ((MyApplication)context.getApplicationContext().getApplicationContext()).getWRSText();
    }
    public void setOnceTextLength(Context context, int length) {
        ((MyApplication)context.getApplicationContext()).setOnceTextLength(length);
    }
    public void getOnceTextLength(Context context){
        ((MyApplication)context.getApplicationContext()).getOnceTextLength();
    }
    public void setTextSplit(Context context, String[] texts) {
        ((MyApplication)context.getApplicationContext()).setTextSplit(texts);
    }
    public void doTextSplit(Context context) {
        String fullText = getWRSText(context);
        int fullTextLength = fullText.length();
        Log.d("문장길이 = " + fullTextLength);

        int onceLength = 20;

        Log.d("fullTextLength/onceLength = " + (fullTextLength/onceLength));
        Log.d("fullTextLength%onceLength = " + (fullTextLength%onceLength));

        int textArrayLength = fullTextLength%onceLength==0?(fullTextLength/onceLength):((fullTextLength/onceLength)+1);

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
}
