package com.hummusic.functions.wav;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by Eleanor on 2016/6/18.
 */

public class WavPlayer extends MediaPlayer {

    /**
     * Set WAV file for playback
     */
    public WavPlayer(String filepath) {
        try {
            setAudioStreamType(AudioManager.STREAM_MUSIC);
            setDataSource(filepath);
            prepare();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Start Playback
     */
    public void playWav() {

        start();
    }

    /**
     * Stop player and release resources
     */
    public void stopWav() {

        if (this != null) {
            try {
                stop();
                this.closeWav();
            } catch (Exception ex) {
                Log.v("Player", "Cannot stop...");
            }

        }


    }

    /**
     * Close player and release resources
     */
    public void closeWav() throws Exception {
//        UIUtils.log("Closing Midi");
        if (isPlaying() || isLooping()) {
            stop();
        }
        release();
    }
}
