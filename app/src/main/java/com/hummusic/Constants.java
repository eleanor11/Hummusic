package com.hummusic;

import android.os.Environment;

/**
 * Created by bluemaple on 2016/6/16.
 */
public class Constants {

    public final static String MSG = "msg";

    public final static int REFRESH = 10;
    public final static int HIDE = 0;
    public final static int SHOW = 1;

    public final static int NEWBEGIN = -1;
    public final static int PAUSED = 0;
    public final static int UNPAUSED = 1;

    public final static int REGISTER_INDEX = 0;
    public final static int SIGNIN_INDEX = 1;

    public final static int ENTER_PROFILE_INDEX = 0;
    public final static int ENTER_MENU_INDEX = 1;
    public final static int ENTER_MIC_INDEX = 2;

    public final static int RECORD_INDEX = 0;
    public final static int LISTVIEW_INDEX = 1;

    public final static int STUDENT_TYPE = 0;
    public final static int TEACHER_TYPE = 1;

    public final static int RECORD_STAGE_UNBEGIN = 0;
    public final static int RECORD_STAGE_RECORDING = 1;
    public final static int RECORD_STAGE_FINISH = 2;
    public final static int RECORD_STAGE_PREVIEW = 3;

    public static String basefiledir = Environment.getExternalStorageDirectory() + "/hummusic";
    public static String midinotefilename = "midinote";
    public static String wavfilenamefile = "test.wav";
    public static String pcmfilenamefile = "test.pcm";
    public static String midfilenamefile = "test.mid";
    public static int SETTINGS_RETURN_CODE = 1000;
    public static int SETTINGS_REQUEST_CODE = 1001;

    public static String DETAIL = "detail";

    public static int ReadBufferSize = 1024;
}
