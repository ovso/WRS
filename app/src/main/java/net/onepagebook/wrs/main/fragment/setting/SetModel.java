package net.onepagebook.wrs.main.fragment.setting;

import android.content.Intent;

import net.onepagebook.wrs.common.Log;
import net.onepagebook.wrs.main.Model;

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
}