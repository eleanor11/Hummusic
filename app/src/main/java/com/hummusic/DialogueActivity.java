package com.hummusic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.media.AudioFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hummusic.fragments.RecordFragment;
import com.hummusic.fragments.StaffListFragment;
import com.hummusic.functions.MInfo;
import com.hummusic.functions.SettingManager;
import com.hummusic.functions.UserInfo;
import com.hummusic.functions.converters.Pcm2WavConverter;
import com.hummusic.functions.converters.Wav2MidiConverter;
import com.hummusic.functions.midi.MidiPlayer;
import com.hummusic.functions.raw.Recorder;
import com.hummusic.functions.upload.FileHelper;
import com.hummusic.functions.upload.OnStateListener;
import com.hummusic.functions.upload.UploadUtil;
import com.hummusic.functions.wav.WavPlayer;
import com.hummusic.widgets.MusicPlayer;
import com.hummusic.widgets.StaffSurfaceView;
import com.hummusic.widgets.StaffView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.hummusic.fragments.SigninFragment.fa;

/**
 * Created by Eleanor on 2016/6/23.
 */

public class DialogueActivity extends BaseActivity {

    private TextView nameText = null;
    private ImageView backButton = null;

    private ImageView recordButton = null;
    private int recordStage = Constants.RECORD_STAGE_UNBEGIN;

    private List<Map<String, Object>> mData;
    private ListView myList;
    private MyAdapter myAdapter;

    String mScore = "";
    String fileName = "";

    private UserInfo userInfo = null;
    private ArrayList<String> mInfos = null;
    private String mid = "";
    private MInfo mInfo = null;
    private Boolean finished = false;

    private SurfaceHolder staffHolder;
    private Canvas canvas;

    public Recorder recorderInstance;
    private Activity activity = this;
    private WavPlayer wavPlayer;
    private MidiPlayer midiPlayer;
    private Wav2MidiConverter wav2midiconverter;
    private Pcm2WavConverter pcm2wavconverter;
    private String pcmfilename = Constants.basefiledir + "/" + Constants.pcmfilenamefile;
    private String wavfilename = Constants.basefiledir + "/" + Constants.wavfilenamefile;
    private String midinotefilename = Constants.basefiledir + "/" + Constants.midinotefilename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialogue);

        new Thread(get).start();

        while (!finished) {

        }

        initWidgets();

        new Thread(download).start();
    }

    private void initWidgets() {

        nameText = (TextView) findViewById(R.id.nameTextView);
        nameText.setText(userInfo.getName());

        backButton = (ImageView)findViewById(R.id.backImageView);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogueActivity.this.finish();
            }
        });

        recordButton = (ImageView)findViewById(R.id.dialogueRecordImageView);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (recordStage) {
                    case Constants.RECORD_STAGE_UNBEGIN:
                        recordStage = Constants.RECORD_STAGE_RECORDING;
                        startRecord();
                        break;
                    case Constants.RECORD_STAGE_RECORDING:
                        recordStage = Constants.RECORD_STAGE_UNBEGIN;
                        break;
                }
            }
        });
        setListView();
    }
    private void setListView() {

        mData = getData();
        myList = (ListView) findViewById(R.id.dialogueListView);
        myList.setItemsCanFocus(true);
        myAdapter = new MyAdapter(getApplicationContext());
        myList.setAdapter(myAdapter);

        ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String, String>>();
        int size = 0;
        for (int i = 0; i < size; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            String fileName = "";
            map.put("position", i % 2 + "");       //0:left, 1:right
            map.put("fileName", fileName);
            myList.add(map);
        }
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int size = userInfo.getMScoreSize();
        for (int i = 0; i < size; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("position", "0");
            //get mscore
            finished = false;
            mid = mInfos.get(i);
            Log.d("minfo", mid);
            new Thread(getMscore).start();
            while (!finished) {}
            map.put("mScore", mInfo.getDetail());
            map.put("fileName", "");
            list.add(map);

//            map = new HashMap<String, Object>();
//            map.put("position", "1");
//            map.put("mScore", "");
//            map.put("fileName", userInfo.getEmail() + "_" + i);
//            list.add(map);
        }

//        //test
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("position", "0");
//        mid = "8a108c4e559c962c01559c962c3f0000";
//        finished = false;
//        new Thread(getMscore).start();
//        while (!finished) {}
//        map.put("mScore", "");
//        map.put("fileName", "");
//        list.add(map);
//
//        map = new HashMap<String, Object>();
//        map.put("position", "1");
//        map.put("mScore", "");
//        map.put("fileName", "");
//        list.add(map);

        return list;
    }

    public final class ViewHolder implements MusicPlayer{
        public ImageView userLeft;
        public ImageView userRight;
        public ImageView dialogueLeft;
        public ImageView dialogueRight;
        public ImageView playAudio;
        public ImageView playScore;
        public StaffSurfaceView surfaceView = null;

        public void resetPlayIcon(int length){

//            mFragment.resetPlayerIcon(length,this);
        }
        public void resetAction(int length){
            playScore.setVisibility(View.VISIBLE);
//            coverLayout.setVisibility(View.INVISIBLE);
//            timeText.setText(length+"\"");
//            progressNodeImage.setX(progressNodeImage.getX() - progressBarImage.getWidth());
        }
        public void changeProgress(int length,int progress){
//            mFragment.changeProgress(length,progress, this);
        }
        public void changeProgressAction(int length,int progress){
            //TODO
//            int offset = progressBarImage.getWidth()/length;
//            timeText.setText(progress+"\"");
//            progressNodeImage.setX(progressNodeImage.getX() + offset);
        }


    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private StaffListFragment mFragment = null;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mData.get(position);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.dialogue_item, null);

                String mScore = mData.get(position).get("mScore").toString();
                final String[] tmp = mScore.split("---");
                //test
//                final String mScore1 = "-178956965,-178956965,5-6,5-6,4-4,6-3,6-3,6-3,6-3,6-3,6-3,6-3,6-3,6-3,6-3,2-4,2-4,1-4,1-4,1-4,1-4,1-4,";

                holder.userLeft = (ImageView) convertView.findViewById(R.id.userImageViewLeft);
                holder.userRight = (ImageView) convertView.findViewById(R.id.userImageViewRight);
                holder.dialogueLeft = (ImageView) convertView.findViewById(R.id.dialogueImageViewLeft);
                holder.dialogueRight = (ImageView) convertView.findViewById(R.id.dialogueImageViewRight);
                holder.playAudio = (ImageView) convertView.findViewById(R.id.dialoguePlayAudio);
                holder.playScore = (ImageView) convertView.findViewById(R.id.dialoguePlayScore);
                holder.surfaceView = (StaffSurfaceView) convertView.findViewById(R.id.staffView);

                holder.playScore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("test", "play button left clicked");
                        if (RecordFragment.midiPlayer != null) {
                        } else {
                            RecordFragment.midiPlayer = new MidiPlayer(Constants.basefiledir + "/" + Constants.midinotefilename);
                        }
                        RecordFragment.midiPlayer.setMusicPlayer(new MusicPlayer() {
                            @Override
                            public void resetPlayIcon(int length) {

                            }

                            @Override
                            public void changeProgress(int length, int progress) {

                            }
                        });
                        if (RecordFragment.midiPlayer.getIsPaused() == Constants.PAUSED) {
                            RecordFragment.midiPlayer.setIsPaused(Constants.UNPAUSED);
                        }
                        else {
                            RecordFragment.PlayMidi playTask = new RecordFragment.PlayMidi();
                            playTask.setNoteString(tmp[1]);
//                            playTask.setNoteString(mScore1);
                            playTask.execute();
                        }

                    }
                });

                holder.playAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("test", "play button right clicked");
//                        if (RecordFragment.midiPlayer.getIsPaused() == Constants.PAUSED) {
//                            RecordFragment.midiPlayer.setIsPaused(Constants.UNPAUSED);
//                        }
//                        else {
//                            RecordFragment.PlayMidi playTask = new RecordFragment.PlayMidi();
////                            playTask.setNoteString(tmp[1]);
//                            playTask.setNoteString(mScore1);
//                            playTask.execute();
//                        }
//
//                        RecordFragment.midiPlayer.setMusicPlayer(holder);
                    }
                });


                mScore = mData.get(position).get("mScore").toString();
                fileName = mData.get(position).get("fileName").toString();

                if (mData.get(position).get("position").toString().contentEquals("0")) {
                    Log.d("test", "1");
                    holder.userLeft.setVisibility(View.VISIBLE);
                    holder.dialogueLeft.setVisibility(View.VISIBLE);
                    holder.playScore.setVisibility(View.VISIBLE);

                    holder.userRight.setVisibility(View.INVISIBLE);
                    holder.dialogueRight.setVisibility(View.INVISIBLE);
                    holder.playAudio.setVisibility(View.INVISIBLE);

                    holder.surfaceView.setStaffView(new StaffView(convertView.getContext(), tmp[1]));
//                    holder.surfaceView.setStaffView(new StaffView(convertView.getContext(), mScore1));
                    holder.surfaceView.reDraw();
                    if (holder.surfaceView == null) {
                        Log.d("test", "null");
                    }
                    staffHolder = holder.surfaceView.getHolder();
                    try {
                        if (staffHolder != null) {
                            canvas = staffHolder.lockCanvas();
                            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
                            StaffView view = new StaffView(getApplicationContext(), mData.get(position).get("mScore").toString());
                            view.onDraw(canvas);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (canvas != null && staffHolder!=null)
                            staffHolder.unlockCanvasAndPost(canvas);
                    }

                }
                else if (mData.get(position).get("position").toString().contentEquals("1")) {
                    holder.userLeft.setVisibility(View.INVISIBLE);
                    holder.dialogueLeft.setVisibility(View.INVISIBLE);
                    holder.playScore.setVisibility(View.INVISIBLE);
                    holder.surfaceView.setVisibility(View.INVISIBLE);

                    holder.userRight.setVisibility(View.VISIBLE);
                    holder.dialogueRight.setVisibility(View.VISIBLE);
                    holder.playAudio.setVisibility(View.VISIBLE);
                }

            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            return convertView;
        }

    }

    private void startRecord(){

        RecordWav r = new RecordWav();
        r.execute();

        new Thread(upload).start();

    }

    public static void sendHandlerMessage(Handler handler, int message_type) {
        Message msg1 = handler.obtainMessage();
        Bundle b = new Bundle();
        b.putInt("message_type", message_type);
        msg1.setData(b);
        handler.sendMessage(msg1);
    }
    public Handler handler = new Handler() {

        @Override
        public synchronized void handleMessage(Message msg) {
            Bundle b = msg.getData();
//            if (progDialog != null) {
//                progDialog.dismiss();
//            }
//            mLyrics.setText("Note, Volume, Time\n");
//            if (notes != null) {
//                mLyrics.setText(mLyrics.getText() + notes);
//            }
        }
    };



    private class RecordWav extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            recorderInstance = new Recorder(
                    SettingManager.getBitsPerSample(activity),
                    SettingManager.getRecFreq(activity),
                    SettingManager.getRecChannel(activity));


            Thread th = new Thread(recorderInstance);
            recorderInstance.setFileName(new File(Constants.basefiledir + "/" + Constants.pcmfilenamefile));
            th.start();
            recorderInstance.setRecording(true);
            synchronized (this) {
                try {
                    while (recordStage == Constants.RECORD_STAGE_RECORDING) {
                        this.wait(1);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            recorderInstance.setRecording(false);
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendHandlerMessage(handler, RESULT_OK);

            return null;
        }

        @Override
        protected void onPostExecute(Void test) {
            //convert pcm to midi
//            ProcessWav s = new ProcessWav();
//            s.execute();
//
//            initWavPlayer(Constants.basefiledir + "/" + Constants.wavfilenamefile);
//            initMidiPlayer(Constants.basefiledir + "/" + Constants.midinotefilename);
        }
    }
    private class ProcessWav extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            //Converting PCM to Midi
            Log.v("Process Recording", "COnverting PCM to Midi");
            convertPCM2Midi();

            //Populating Midi Notes
            Log.v("Process Recording", "Populate Midi Notes");
//            populateMidiNotes();
            sendHandlerMessage(handler, RESULT_OK);

            return null;
        }

        @Override
        protected void onPostExecute(Void test) {
        }
    }
    private class PlayMidi extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

            //UIUtils.log("Starting event listener thread for track: " + track);
            //TODO change parameter
            midiPlayer.playMidi(fa.getMScore().getNotes());

            sendHandlerMessage(handler, RESULT_OK);
            return null;
        }

        @Override
        protected void onPostExecute(Void test) {
        }
    }

    public void convertPCM2Midi() {
        Log.v("Process Recording", "Converting PCM to WAV");
        this.setPcm2WavParams();

        Log.v("Process Recording", "Converting WAV to MIDI");
        this.setWav2MidiParams();
        wav2midiconverter.wav2midiNotes();
    }
    private void setPcm2WavParams() {
        int bitsPerSampleInt = SettingManager.getBitsPerSample(activity);
        int bitsPerSample = 16;
        switch (bitsPerSampleInt) {
            case AudioFormat.ENCODING_DEFAULT:
                bitsPerSample = 16;
                break;
            case AudioFormat.ENCODING_PCM_8BIT:
                bitsPerSample = 8;
                break;
            case AudioFormat.ENCODING_PCM_16BIT:
                bitsPerSample = 16;
                break;

        }
        pcm2wavconverter.setBitPerSample(bitsPerSample);
        pcm2wavconverter.setChannels(SettingManager.getRecChannel(activity));
        pcm2wavconverter.setSamplerate(SettingManager.getRecFreq(activity));
        pcm2wavconverter.convertPcm2wav();

    }
    private void setWav2MidiParams() {
        int bitsPerSampleInt = SettingManager.getBitsPerSample(activity);
        int bitsPerSample = 16;
        switch (bitsPerSampleInt) {
            case AudioFormat.ENCODING_DEFAULT:
                bitsPerSample = 16;
                break;
            case AudioFormat.ENCODING_PCM_8BIT:
                bitsPerSample = 8;
                break;
            case AudioFormat.ENCODING_PCM_16BIT:
                bitsPerSample = 16;
                break;

        }

        wav2midiconverter.setBitspersample(bitsPerSample);
        wav2midiconverter.setChannels(SettingManager.getRecChannel(activity));
        wav2midiconverter.setSamplerate(SettingManager.getRecFreq(activity));

        //Set Engine Params
        wav2midiconverter.setBuffer_size(SettingManager.getBufferSize(activity));
        wav2midiconverter.setOverlap_size(SettingManager.getOverlapSize(activity));
        wav2midiconverter.setSilence(SettingManager.getSilence(activity));
        wav2midiconverter.setThreshold(SettingManager.getThreshold(activity));
        wav2midiconverter.setType_onset(SettingManager.getOnsetType(activity));
        wav2midiconverter.setType_onset2(SettingManager.getOnsetType2(activity));
        wav2midiconverter.setType_pitch(SettingManager.getPitchType(activity));
        if (SettingManager.getAveraging(activity)) {
            wav2midiconverter.setAveraging((float) 1.0);
        } else {
            wav2midiconverter.setAveraging((float) 0);
        }



    }
    public void initWavPlayer(String filepath) {
        if (wavPlayer != null) {
            if (wavPlayer.isPlaying() || wavPlayer.isLooping()) {
                wavPlayer.stop();
            }
        } else {
            wavPlayer = new WavPlayer(filepath);
        }
    }
    public void initMidiPlayer(String filepath) {
        if (midiPlayer != null) {
        } else {
            midiPlayer = new MidiPlayer(filepath);
        }

    }


    private Context context;
    private String downloadFileServerUrl = "http://115.28.14.137:8080/Hummusic/DownloadFile?filename=";
    private OnStateListener downloadFileFileStateListener;
    private String uploadFileServerUrl = "http://115.28.14.137:8080/Hummusic/UploadFile?";
    private OnStateListener uploadFileStateListener;

    private class UpLoadRecordFile extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... patameters) {
            return UploadUtil.uploadFile(recorderInstance.getFileName(), uploadFileServerUrl);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                if (uploadFileStateListener != null) {
                    uploadFileStateListener.onState(-1, "fail");
                }
                return;
            }
            else {
                if (uploadFileStateListener != null) {
                    uploadFileStateListener.onState(0, "success");;
                }
            }
        }
    }
    private class DownloadRecordFile extends AsyncTask<String, Integer, File> {
        @Override
        protected File doInBackground(String... parameters) {
            // TODO Auto-generated method stub11
            try {
                String filename = new DateFormat().format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".amr";
                return FileHelper.DownloadFromUrlToData(downloadFileServerUrl + parameters[0], filename, context);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(File result) {

            if (result == null || !result.exists() || result.length() == 0) {
                if (downloadFileFileStateListener != null) {
                    downloadFileFileStateListener.onState(-1, "下载文件失败");
                    return;
                }
            }

        }
    }
    Runnable upload = new Runnable() {
        @Override
        public void run() {
            new UpLoadRecordFile().execute();
        }
    };
    Runnable download = new Runnable() {
        @Override
        public void run() {
            new DownloadRecordFile().execute();
        }
    };

    Runnable get = new Runnable() {
        @Override
        public void run() {
            userInfo = fa.getUserInfo((String) getIntent().getExtras().get("email"));
            if (userInfo != null) {
                mInfos = userInfo.getMScores();
            }
            finished = true;
        }
    };

    Runnable getMscore = new Runnable() {
        @Override
        public void run() {
            mInfo = fa.getMScore(mid);
            finished = true;
        }
    };

}
