package com.hummusic.functions.midi;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

import com.hummusic.Constants;
import com.hummusic.widgets.MusicPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Eleanor on 2016/6/17.
 */

public class MidiPlayer implements OnCompletionListener {

    private boolean quit = false;
    private int isPaused = -1;
    private String baseFilename = "";
    private MusicPlayer musicPlayer;

    public MidiPlayer(String baseFilename) {
        this.baseFilename = baseFilename;
    }
    public void setMusicPlayer(MusicPlayer musicPlayer){
        this.musicPlayer = musicPlayer;
    }

    public void setIsPaused(int isPaused){
        this.isPaused = isPaused;
    }

    public int getIsPaused(){
        return this.isPaused;
    }

    public synchronized  void playMidi(ArrayList<Note> notes) {
        if (notes == null) return;

        quit = false;

        ListIterator iterator = notes.listIterator();
        Note mynote = null;
        float totTime = 0;
        int previousNote = 0;
        int length = notes.size();
        int progress = 0;
        System.gc();
        synchronized (this) {
            while (iterator.hasNext() && !quit) {
                while(isPaused== Constants.PAUSED){
                    try{
                        Thread.sleep(100);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                progress++;
                musicPlayer.changeProgress(length,progress);
                mynote = (Note) iterator.next();
                if (mynote == null) {
                    return;
                }
                try {
                    Log.i("Player", "Playing note: " + mynote.note + ", time: " + mynote.time);
                    this.wait((int) ((mynote.time - totTime)));
                    totTime = mynote.time;
                }catch (InterruptedException e) {
                    Logger.getLogger(MidiPlayer.class.getName()).log(Level.SEVERE, null, e);
                }
//                if (previousNote != mynote.note) {
                    try{
                        playNote(mynote.note);
                    }catch (IOException e) {
                        Logger.getLogger(MidiPlayer.class.getName()).log(Level.SEVERE, null, e);

                    }
//                }
                previousNote = mynote.note;
            }
            this.isPaused = Constants.NEWBEGIN;
            musicPlayer.resetPlayIcon(length);
        }
    }
    public  void playNote(int note) throws IOException {
        MediaPlayer midiPlayer = new MediaPlayer();
        midiPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        midiPlayer.setDataSource(baseFilename + note + ".mid");
        midiPlayer.prepare();
        midiPlayer.setVolume(1, 1);
        midiPlayer.start();
    }

    public void onCompletion(MediaPlayer midiPlayer) {
        Log.i("Player", "Closing Midi");
        if (midiPlayer != null) {
            if (midiPlayer.isPlaying() || midiPlayer.isLooping()) {
                midiPlayer.stop();
            }
            midiPlayer.release();
        }
        midiPlayer = null;
    }
}
