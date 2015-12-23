package net.onpagebook.wrs.main.fragment.setting;

import android.content.Context;
import android.content.Intent;

import net.onepagebook.wrs.common.Log;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by jaeho_oh on 2015-12-22.
 */
public class SetModel {

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

    public String readTextFile(Context context, String filePath) {
        String text = null;
        try {
            //File file = context.getFileStreamPath(filePath);
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            Reader in = new InputStreamReader(fis, "euc-kr");
            int size = fis.available();
            char[] buffer = new char[size];
            byte[] buf = new byte[size];
            in.read(buffer);
            in.close();

            text = new String(buffer);
            UniversalDetector detector = new UniversalDetector(null);
            int nread;
            while ((nread = fis.read(buf))>0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
            detector.dataEnd();
            Log.d("charset = " + detector.getDetectedCharset());
            Log.d("text="+text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return text;
    }
}
