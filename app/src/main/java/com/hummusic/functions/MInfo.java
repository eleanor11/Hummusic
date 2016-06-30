package com.hummusic.functions;

import com.hummusic.functions.midi.Note;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Eleanor on 2016/6/17.
 */

public class MInfo {

    private String mid;
    private String createTime;
    private String creator;
    private String detail;

    public MInfo() {
        mid = "";
        createTime = (new Timestamp(System.currentTimeMillis())).toString();
        creator = "";
        detail = "";
    }

    public MInfo(String mid, String creator, String detail) {
        this.mid = mid;
        this.creator = creator;
        createTime = (new Timestamp(System.currentTimeMillis())).toString();
        this.detail = detail;
    }

    public MInfo(String mid, String creator, String time, String detail) {
        this.mid = mid;
        this.creator = creator;
        this.createTime = time;
        this.detail = detail;
    }

    public String getMid() {
        return mid;
    }
    public String getCreateTime() {
        return createTime;
    }
    public String getCreator() {
        return creator;
    }
    public String getDetail() {
        return detail;
    }

    public ArrayList<Note> getNotes() {
        return null;
    }

}
