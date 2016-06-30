package com.hummusic.functions.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.hummusic.Constants;
import com.hummusic.functions.SettingManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Eleanor on 2016/6/18.
 */

public class FileInstaller {
    public static void cloneMidiFileProg(Activity activity, String srcFile, String destFile, byte newProg) {
        FileUtils.modifyByte(activity, srcFile, destFile, 102, newProg);

    }

    public static void cloneMidiFileNotes(Activity activity, String srcFile, String destFile, byte newNote) {

        //TODO mi zhi file
        //File srcFileF = new File("/sdcard/midinote0.mid");
        File srcFileF = null;
        InputStream inputStream = null;
        try {
            inputStream = activity.getResources().getAssets().open("midinote0.mid");
            //File file = new File(filePath);
            srcFileF = new File("/sdcard/midinote0.mid");
            if(!srcFileF.exists()){
                //srcFileF.mkdirs();
                FileOutputStream fileOutputStream = new FileOutputStream("/sdcard/midinote0.mid");
                byte[] buffer = new byte[512];
                int count = 0;
                while((count = inputStream.read(buffer)) > 0){
                    fileOutputStream.write(buffer, 0 ,count);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        srcFileF = new File("/sdcard/midinote0.mid");
        byte[] bytes = {109, 114};
        byte[] byteContents = {newNote, newNote};
        FileUtils.modifyByte(activity, srcFileF, destFile, bytes, byteContents);
        //FileUtils.modifyByte(activity, srcFileFInputStream, destFile, bytes, byteContents);

    }

    public static void installFiles(Activity activity) {

        Log.v("Installer", "Installing files...");
        File tmpDir = new File(Constants.basefiledir);
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }

        //Install synth notes
        installSynthFiles(activity, (byte) SettingManager.getSynthInstrument(activity));

        //Install Demo WAV file
        installFile(activity, Constants.wavfilenamefile, Constants.basefiledir);

    }

    public static void installSynthFiles(Activity activity, byte progNum) {
//        UIUtils.log("Installing custom synth files...");
        String srcFile = Constants.midinotefilename + "0.mid";
//        FileUtils.installFile(activity, srcFile, VMidiConst.TMP_DIR);
        cloneMidiFileProg(activity, srcFile, Constants.basefiledir + "/" + srcFile, (byte) progNum);
        //Clone Notes for rest of files
        for (int i = 1; i <= 127; i++) {
//            UIUtils.log("Installing custom synth file: " + i);
            String destFile = Constants.midinotefilename + (i) + ".mid";
            cloneMidiFileNotes(activity, Constants.basefiledir + "/" + srcFile, Constants.basefiledir + "/" + destFile, (byte) i);
        }
    }


    public static void installFile(Context activity, String srcFile, String destDir) {
        try {
            AssetManager am = activity.getResources().getAssets(); // get the local asset manager
            InputStream is = am.open(srcFile); // open the input stream for reading
            File destDirF = new File(destDir);
            if (!destDirF.exists()) {
                destDirF.mkdir();
            }
            OutputStream os = new FileOutputStream(destDir + "/" + srcFile);
            byte[] buf = new byte[8092];
            int n;
            while ((n = is.read(buf)) > 0) {
                os.write(buf, 0, n);
            }
            os.close();
            is.close();
        } catch (Exception ex) {
            Log.e("Installer", "failed to install file: " + ex);
        }

    }

}
