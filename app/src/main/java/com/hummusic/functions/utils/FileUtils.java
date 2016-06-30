package com.hummusic.functions.utils;

import android.app.Activity;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by Eleanor on 2016/6/18.
 */

public class FileUtils {

    public static void modifyByte(Activity activity, InputStreamReader srcFile, String destFile, byte[] byteNums, byte[] byteContents) {
//        throw new UnsupportedOperationException("Not yet implemented");
//        UIUtils.log("Modifying byte: " + byteNum + " in sdcard file: " + srcFile);
        try {
            InputStreamReader is = srcFile; // open the input stream for reading
            OutputStream os = new FileOutputStream(destFile);
            char[] charBuf = new char[8092];
            byte[] buf = new byte[8092];
            int n;
            int totalBytes = 0;
            while ((n = is.read(charBuf)) > 0) {
                totalBytes += n;
//                UIUtils.log("Total Bytes read: " + totalBytes);
                buf = new String(charBuf).getBytes();
                for (int i = 0; i < byteNums.length; i++) {
                    if (byteNums[i] <= totalBytes) {
//                    UIUtils.log("Replacing byte: " + (totalBytes - n + byteNum ) + " contents with: " + byteContents);
                        buf[totalBytes - n + byteNums[i]] = byteContents[i];
                    }
                }
                os.write(buf, 0, n);
            }
//            UIUtils.log(ByteUtils.ByteArrayToString(buf));
            os.close();
            is.close();
        } catch (Exception ex) {
            Log.e("Installer0", "failed to modify file: " + ex);
        }
    }

    public static void modifyByte(Activity activity, File srcFile, String destFile, byte[] byteNums, byte[] byteContents) {
//        throw new UnsupportedOperationException("Not yet implemented");
//        UIUtils.log("Modifying byte: " + byteNum + " in sdcard file: " + srcFile);
        try {
            FileInputStream is = new FileInputStream(srcFile); // open the input stream for reading
            OutputStream os = new FileOutputStream(destFile);
            byte[] buf = new byte[8092];
            int n;
            int totalBytes = 0;
            while ((n = is.read(buf)) > 0) {
                totalBytes += n;
//                UIUtils.log("Total Bytes read: " + totalBytes);
                for (int i = 0; i < byteNums.length; i++) {
                    if (byteNums[i] <= totalBytes) {
//                    UIUtils.log("Replacing byte: " + (totalBytes - n + byteNum ) + " contents with: " + byteContents);
                        buf[totalBytes - n + byteNums[i]] = byteContents[i];
                    }
                }
                os.write(buf, 0, n);
            }
//            UIUtils.log(ByteUtils.ByteArrayToString(buf));
            os.close();
            is.close();
        } catch (Exception ex) {
            Log.e("Installer0", "failed to modify file: " + ex);
        }
    }

    public static void modifyByte(Activity activity, String srcFile, String destFile, int byteNum, byte byteContents) {
//        throw new UnsupportedOperationException("Not yet implemented");
//        UIUtils.log("Modifying byte: " + byteNum + " in Asset file: " + srcFile);
        try {
            AssetManager am = activity.getResources().getAssets(); // get the local asset manager
            InputStream is = am.open(srcFile); // open the input stream for reading
            OutputStream os = new FileOutputStream(destFile);
            byte[] buf = new byte[8092];
            int n;
            int totalBytes = 0;
            while ((n = is.read(buf)) > 0) {
                totalBytes += n;
//                UIUtils.log("Total Bytes read: " + totalBytes);
                if (byteNum <= totalBytes) {
//                    UIUtils.log("Replacing byte: " + (totalBytes - n + byteNum ) + " contents with: " + byteContents);
                    buf[totalBytes - n + byteNum] = byteContents;
                }
                os.write(buf, 0, n);
            }
//            UIUtils.log(ByteUtils.ByteArrayToString(buf));
            os.close();
            is.close();
        } catch (Exception ex) {
            Log.e("Installer1", "failed to modify file: " + ex);
        }
    }
}
