package com.hummusic.functions.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bluemaple on 2016/6/25.
 */
public class PreferenceManager {
    public static final String perferenceTag = "hummusic";
    public static final String noteIndex = "index";
    public static final String splitNode = "---";
    public static final String TITLE_STRING = "title";
    public static final String CONTENT_STRING = "content";
    public static final String INDEX_STRING = "index";
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    public PreferenceManager(Context context){
        this.mContext = context;
        this.mSharedPreferences = mContext.getSharedPreferences(perferenceTag, Activity.MODE_PRIVATE);
    }
    public String getAllNotes(){
        String notes = "";
        String[] indexs = mSharedPreferences.getString(noteIndex, "").split(",");
        for(String index:indexs){
            notes+=mSharedPreferences.getString(index, "");
        }
        return notes;
    }
    public void getAllNotes(List<Map<String, Object>> list){
        String[] indexs = mSharedPreferences.getString(noteIndex, "null").split(",");
        Log.i("aicitel", "notes index " + mSharedPreferences.getString(noteIndex, ""));
        list.clear();
        for(String index:indexs){
            if(index.equals("null")||index.equals(""))
                continue;
            Log.i("aicitel", index + " ??? " + mSharedPreferences.getString(index, ""));
            String[] noteWithName = mSharedPreferences.getString(index,"" ).split(splitNode);
            try {
                Map<String,Object>map = new HashMap<>();
                map.put(INDEX_STRING,index);
                map.put(TITLE_STRING,noteWithName[0]);
                map.put(CONTENT_STRING,noteWithName[1]);
                list.add(map);
            }
            catch (Exception e){
                Log.e("aicitel","error in noteWithName");
                e.printStackTrace();
            }
        }
        /*
        if(mSharedPreferences.getString(noteIndex, "null").equals("null")){
            Map<String,Object>map = new HashMap<>();
            map.put(TITLE_STRING,"default");
            map.put(CONTENT_STRING,"1-3,2-3,3-3,4-3,5-3,6-3,7-3,1-4,2-4,3-4,4-4,5-4,6-4,7-4");
            list.add(map);
        }*/
    }
    public void saveMusicScoreLocal(String note,String noteName){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String nextId = nextId();
        Log.i("aicitel","nextid "+nextId);
        if(noteName.equals(""))
            noteName = "unnamed";
        editor.putString(nextId, noteName+splitNode+note);
        Log.i("aicitel", "nextid " + nextId + "," + note);
        editor.putString(noteIndex, (getIndexs().equals("") ? "" : (getIndexs() + ",")) + nextId);
        Log.i("aicitel", "noteIndex " + getIndexs() + "," + nextId);
        editor.commit();
    }
    public void  saveMusicScoreLocal(String note,String noteName,String index){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(index, noteName + splitNode + note);
        editor.commit();
    }
    public void deleteMusicScore(String index){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        //String nextId = nextId();
        //editor.putString(nextId,note);
        String indexs = getIndexs();
        String[] index_splits = indexs.split("," + index + ",");
        if(index_splits.length!=1)
            indexs = index_splits[0]+","+index_splits[1];
        else {
            if(indexs.substring(0,index.length()+1).equals(index+",")){
                indexs =  indexs.substring(index.length()+1,indexs.length());
            }
            else if(indexs.substring(indexs.length()-index.length(),indexs.length()).equals("," + index)){
                indexs =  indexs.substring(0, indexs.length() - index.length());
            }
        }
        editor.putString(noteIndex,indexs);
        editor.putString(index,null);
        editor.commit();
    }

    private String getIndexs(){
        return mSharedPreferences.getString(noteIndex, "");
    }
    public String nextId(){
        String noteIndexString = mSharedPreferences.getString(noteIndex, "");
        if(noteIndexString.equals(""))
            return "0";
        else {
            String[] indexs = noteIndexString.split(",");
            return (Integer.parseInt(indexs[indexs.length - 1]) + 1) + "";
        }
    }
    public void setIndexNull(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        //String nextId = nextId();
        //editor.putString(nextId,note);
        editor.putString(noteIndex,null);
        editor.putString("0",null);
        editor.putString("1",null);
        editor.commit();
    }
}
