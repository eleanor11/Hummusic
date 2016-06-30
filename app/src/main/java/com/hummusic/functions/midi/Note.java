package com.hummusic.functions.midi;

/**
 * Created by Eleanor on 2016/6/17.
 */

public class Note {
    public int note;
    public int vel;
    public float time; //millisecs

    public Note(int note, int vel, float time) {
        this.note = note;
        this.vel = vel;
        this.time = time;

    }
}
