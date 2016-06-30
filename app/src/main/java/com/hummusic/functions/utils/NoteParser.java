package com.hummusic.functions.utils;

import com.hummusic.functions.midi.Note;

import java.util.ArrayList;

/**
 * Created by bluemaple on 2016/6/26.
 */
public class NoteParser {

    public static ArrayList<Note>parseString2ArrayWithTime(String noteString){
        ArrayList<Note>list = new ArrayList<>();
        String[] notes = noteString.split(",");
        int time = 0;
        for(String note:notes){
            String cntNote = note.split("-")[0];
            String lnoteInOctave = note.split("-")[1];
            if(cntNote.length()>0 && lnoteInOctave.length()>0) {
                int noteValue = Integer.parseInt(cntNote);
                int levelValue = Integer.parseInt(lnoteInOctave);
                list.add(new Note(noteValue+(levelValue-3)*7+60,80,time+500));
                time+=500;
            }
        }
        return list;
    }
    public static ArrayList<Note>parseString2Array(String noteString){
        ArrayList<Note>list = new ArrayList<>();
        String[] notes = noteString.split(",");
        for(String note:notes){
            String cntNote = note.split("-")[0];
            String lnoteInOctave = note.split("-")[1];
            if(cntNote.length()>0 && lnoteInOctave.length()>0) {
                int noteValue = Integer.parseInt(cntNote);
                int levelValue = Integer.parseInt(lnoteInOctave);
                list.add(new Note(noteValue+(levelValue-3)*7,80,500));
            }
        }
        return list;
    }

}
