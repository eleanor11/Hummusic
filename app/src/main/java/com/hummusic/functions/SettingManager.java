package com.hummusic.functions;

/**
 * Created by Eleanor on 2016/6/17.
 */


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.hummusic.Constants;


public class SettingManager extends PreferenceActivity {

    public static int getBitsPerSample(Activity activity) {
        int recFormat = AudioFormat.ENCODING_DEFAULT;
        int recFormatIndex = SettingManager.getRecFormat(activity);
        switch (recFormatIndex) {
            case 0:
                //ENCODING DEFAULT fails for some devices using 16 Bit as default
                recFormat = AudioFormat.ENCODING_PCM_16BIT;
                break;
            case 1:
                recFormat = AudioFormat.ENCODING_PCM_8BIT;
                break;
            case 2:
                recFormat = AudioFormat.ENCODING_PCM_16BIT;
                break;
        }
        return recFormat;
    }

    public static boolean getAveraging(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getBoolean("averagingPref", true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent data = new Intent();
        setResult(Constants.SETTINGS_RETURN_CODE, data);
        addPrefs();
    }

    public static int getSynthInstrument(Activity activity) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return Integer.parseInt(prefs.getString("synthInstrumentPref", "65"));

    }

    public static int getRecFreq(Activity activity) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return Integer.parseInt(prefs.getString("recFreqPref", "11025"));

    }

    public static int getRecFormat(Activity activity) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return Integer.parseInt(prefs.getString("recFormatPref", "0"));

    }

    public static int getRecChannel(Activity activity) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return Integer.parseInt(prefs.getString("recChanPref", "1"));

    }

    public static int getOverlapSize(Activity activity) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return Integer.parseInt(prefs.getString("overlapSizePref", "256"));

    }

    public static int getBufferSize(Activity activity) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return Integer.parseInt(prefs.getString("bufferSizePref", "128"));

    }

    public static String getPitchType(Activity activity) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("pitchTypePref", "yinfft");

    }

    public static String getOnsetType(Activity activity) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("onsetTypePref", "kl");

    }

    public static String getOnsetType2(Activity activity) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("onsetType2Pref", "complex");

    }

    public static float getThreshold(Activity activity) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return Float.parseFloat(prefs.getString("thresholdPref", "0.30"));

    }

    public static float getSilence(Activity activity) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return Float.parseFloat(prefs.getString("silencePref", "-90.0"));

    }

    public static boolean getShowNoteLetter(Activity activity) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getBoolean("showNoteLettersOnPref", true);
    }

    public void addPrefs() {
        //addPreferencesFromResource(R.xml.settings);
    }
}

