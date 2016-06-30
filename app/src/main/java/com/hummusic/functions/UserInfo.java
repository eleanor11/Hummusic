package com.hummusic.functions;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Eleanor on 2016/6/17.
 */

public class UserInfo {
    private String email;
    private String name;
    private String password;
    private int isTeacher;
    private ArrayList<String> mScores;

    public UserInfo() {
        email = "";
        name = "";
        password = "";
        isTeacher = 0;
        mScores = new ArrayList<String>();
    }

    public UserInfo(String email, String name, int isTeacher, String mScore) {
        this.email = email;
        this.name = name;
        this.isTeacher = isTeacher;

        mScore = mScore + '-';
        Log.d("stuednes", mScore);
        mScores = new ArrayList<String>();
        int begin = 0;
        int idx = mScore.indexOf('-');
        Log.d("stuednes", idx + "");
        while (idx > 0) {
            mScores.add(mScore.substring(begin, idx));
            begin = idx + 1;
            idx = mScore.indexOf('-', begin);
            Log.d("stuednes", idx + "");
        }

    }

    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public Boolean isTeacher() {
        return (isTeacher == 1);
    }
    public ArrayList<String> getMScores() {
        return mScores;
    }

    public String getMScoreStr() {
        String str = "";
        for (int i = 0; i < mScores.size(); i++) {
            str += mScores.get(i) + "-";
        }
        return str;
    }
    public int getMScoreSize() {
        return mScores.size();
    }
    public void addMScore(String mScore) {
        mScores.add(mScore);
    }

}
