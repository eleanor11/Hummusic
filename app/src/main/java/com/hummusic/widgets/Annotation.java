package com.hummusic.widgets;

/**
 * Created by bluemaple on 2016/6/17.
 */
public class Annotation {

    private int type;
    private int note;
    private float time;
    private boolean isHollow;
    public Annotation(int note){
        this.note = note;
        this.time = 0.5f;
    }
    public Annotation(int note, float time){
        this.note = note;
        this.time = time;
    }
    public Annotation(int note, float time, boolean isHollow){
        this.note = note;
        this.time = time;
        this.isHollow = isHollow;
    }
    public boolean isHollow(){
        return this.isHollow;
    }
    public int getNote(){
        return this.note;
    }
    public int getType(){
        return this.type;
    }

    /**
     * Useless time should be reducted to type
     * @return
     */
    public float getTime(){
        return this.time;
    }
}
