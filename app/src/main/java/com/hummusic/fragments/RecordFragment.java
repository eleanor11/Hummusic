package com.hummusic.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hummusic.Constants;
import com.hummusic.R;
import com.hummusic.ShowTeachersActivity;
import com.hummusic.functions.midi.MidiPlayer;
import com.hummusic.functions.raw.Recorder;
import com.hummusic.functions.utils.FileInstaller;
import com.hummusic.functions.utils.NoteParser;
import com.hummusic.functions.utils.PreferenceManager;
import com.hummusic.widgets.GifView;
import com.hummusic.widgets.MusicPlayer;
import com.hummusic.widgets.StaffView;
import com.hummusic.widgets.TagSwitchListener;
import com.hummusic.widgets.Yin;

/**
 * Created by bluemaple on 2016/6/26.
 */
public class RecordFragment extends BaseFragment implements MusicPlayer{

    private ImageView recordButton = null;
    private TextView mainHint = null;
    private int recordStage = Constants.RECORD_STAGE_UNBEGIN;

    private Recorder recorderInstance;
    private SurfaceHolder staffHolder;
    private Canvas canvas;

    private Activity mActivity;

    private View mRootView = null;
    private EditText nameEditText = null;
    private SurfaceView surfaceView = null;
    private ImageView profileButton = null;
    private ImageView menuButton = null;
    private ImageView addButton = null;
    private ImageView progressNode = null;
    private ImageView progressBar = null;
    private TextView cancelButton = null;
    private TextView timeText = null;
    private View menuLayout = null;
    private GifView recordGif = null;
    public static String noteString = "";
    public static String noteIndex = "";
    private float _pitch;
    private AudioRecord _audioRecord;

    private PreferenceManager mPerferenceManager;
    private FragmentManager mFragmentManager;
    private BaseFragment staffListFragment;

    private boolean cancelFlag = false;

    public static MidiPlayer midiPlayer;

    private TagSwitchListener mTagSwitchListener;

    @Override
    public void changeProgress(int length,int progress){
        Message message = new Message();
        message.what = 1;
        Bundle bundle = new Bundle();
        bundle.putInt("length",length);
        bundle.putInt("progress",progress);
        message.setData(bundle);
        handler.sendMessage(message);
    }
    public void changeProgressAction(int length,int progress){
        int offset = progressBar.getWidth()/length;
        timeText.setText(progress+"\"");
        progressNode.setX(progressNode.getX() + offset);
    }
    @Override
    public void resetPlayIcon(int length){
        Message message = new Message();
        message.what = 0;
        Bundle bundle = new Bundle();
        bundle.putInt("length", length);
        message.setData(bundle);
        handler.sendMessage(message);
    }
    public void resetAction(int length){
        mRootView.findViewById(R.id.playImageView).setVisibility(View.VISIBLE);
        mRootView.findViewById(R.id.pauImageView).setVisibility(View.INVISIBLE);
        timeText.setText(length + "\"");
        progressNode.setX(progressNode.getX() - progressBar.getWidth());
    }

    @Override
    public void setTagSwitchListener(TagSwitchListener tagSwitchListener){
        this.mTagSwitchListener = tagSwitchListener;
    }
    @Override
    public boolean onBackPressed(){
        if(recordStage == Constants.RECORD_STAGE_PREVIEW){
            onCancelPressed();
            return false;
        }
        return true;
    }
    @Override
    public void hideOrShowContent(int hideOrShow){
        if(hideOrShow==Constants.HIDE) {
            surfaceView.setVisibility(View.INVISIBLE);
        }
        else {
            surfaceView.setVisibility(View.VISIBLE);
            enterRecord();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        Log.i("aicitel", "oncreate");
        //TODO
        mRootView = inflater.inflate(R.layout.layout_record, container, false);
        mActivity = getActivity();
        nameEditText =  (EditText)mRootView.findViewById(R.id.songnameEditText);
        menuLayout = mRootView.findViewById(R.id.menuLayout);
        mRootView.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!noteIndex.equals("")){
                    saveMusicScoreLocal(noteString,noteIndex);
                    noteIndex = "";
                }
                else
                    saveMusicScoreLocal(noteString);
                enterListView();
            }
        });

        //TODO action of cancel
        recordButton = (ImageView)mRootView.findViewById(R.id.recordImageView);
        mainHint = (TextView)mRootView.findViewById(R.id.hintTextView);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (recordStage) {
                    case Constants.RECORD_STAGE_UNBEGIN:
                        recordStage = Constants.RECORD_STAGE_RECORDING;
                        //recordButton.setImageResource(R.drawable.main_stop);
                        mainHint.setText(R.string.main_hint2);
                        recordButton.setVisibility(View.INVISIBLE);
                        recordGif.setVisibility(View.VISIBLE);
                        startRecord();
                        break;
                    case Constants.RECORD_STAGE_FINISH:
                        recordStage = Constants.RECORD_STAGE_FINISH;
                        startShift();
                        break;
                }
            }
        });
        surfaceView = (SurfaceView)mRootView.findViewById(R.id.staffSurface);
        mRootView.findViewById(R.id.playImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //midiPlayer.playMidi(NoteParser.parseString2Array(noteString));
                if (midiPlayer.getIsPaused() == Constants.PAUSED) {
                    midiPlayer.setIsPaused(Constants.UNPAUSED);
                } else {
                    PlayMidi playTask = new PlayMidi();
                    playTask.setNoteString(noteString);
                    playTask.execute();
                }
                mRootView.findViewById(R.id.playImageView).setVisibility(View.INVISIBLE);
                mRootView.findViewById(R.id.pauImageView).setVisibility(View.VISIBLE);
            }
        });

        mRootView.findViewById(R.id.pauImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                midiPlayer.setIsPaused(Constants.PAUSED);
                mRootView.findViewById(R.id.playImageView).setVisibility(View.VISIBLE);
                mRootView.findViewById(R.id.pauImageView).setVisibility(View.INVISIBLE);
            }
        });
        mRootView.findViewById(R.id.listTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterListView();
                menuLayout.setVisibility(View.INVISIBLE);
            }
        });
        mRootView.findViewById(R.id.msgTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity.getBaseContext(), ShowTeachersActivity.class));
                menuLayout.setVisibility(View.INVISIBLE);
            }
        });
        nameEditText.setOnFocusChangeListener(new FocusListener());
        progressNode = (ImageView)mRootView.findViewById(R.id.progressNodeImage);
        progressBar = (ImageView)mRootView.findViewById(R.id.progressImage);
        profileButton = (ImageView) mRootView.findViewById(R.id.profileImageView);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTagSwitchListener.viewProfile();
                //startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

        addButton = (ImageView) mRootView.findViewById(R.id.addImageView);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterRecord();
            }
        });

        menuButton = (ImageView) mRootView.findViewById(R.id.menuImageView);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menuLayout.getVisibility() == View.VISIBLE)
                    menuLayout.setVisibility(View.INVISIBLE);
                else
                    menuLayout.setVisibility(View.VISIBLE);
            }
        });
        mRootView.findViewById(R.id.top_menuImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO menu
                onIconsPressed(Constants.ENTER_MENU_INDEX);
            }
        });
        mRootView.findViewById(R.id.top_micImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onIconsPressed(Constants.ENTER_MIC_INDEX);
            }
        });
        mRootView.findViewById(R.id.top_profileImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onIconsPressed(Constants.ENTER_PROFILE_INDEX);
                //startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

        mPerferenceManager = new PreferenceManager(this.getActivity().getBaseContext());
        recordGif = (GifView) mRootView.findViewById(R.id.recordGifView);
        recordGif.setMovieResource(R.drawable.music_rotate);
        recordGif.setVisibility(View.INVISIBLE);
        recordGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordStage = Constants.RECORD_STAGE_FINISH;
                recordButton.setImageResource(R.drawable.shift_rotate);
                mainHint.setText(R.string.main_hint3);
                recordButton.setVisibility(View.VISIBLE);
                recordGif.setVisibility(View.INVISIBLE);
                stopRecord();
            }
        });

        mRootView.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelPressed();
            }
        });
        timeText =(TextView) mRootView.findViewById(R.id.timeTextView);

        staffListFragment = new StaffListFragment();
        mFragmentManager = getFragmentManager();
        mFragmentManager.beginTransaction().add(R.id.listViewContainer,staffListFragment).hide(staffListFragment).commit();
        initMidiPlayer(Constants.basefiledir + "/" + Constants.midinotefilename);
        //staffListFragment.setTagSwitchListener(this);
        Init s = new Init();
        s.execute();
        return mRootView;
    }

    private void startRecord(){

        /*
        RecordWav r = new RecordWav();
        r.execute();
        */
        int lbufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT) * 8;
        if(_audioRecord==null)
            _audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, lbufferSize);
        else
            _audioRecord.startRecording();


        if (_audioRecord.getState() == AudioRecord.STATE_INITIALIZED)
        {
            _audioRecord.setPositionNotificationPeriod(44100 / 2); // should make sure the buffer is a multiple of this

            _audioRecord.setRecordPositionUpdateListener(
                    new AudioRecord.OnRecordPositionUpdateListener()
                    {
                        public void onPeriodicNotification(AudioRecord recorder)
                        {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    //String lout = String.format("%f Hz", _pitch);
                                    //_txtFrequency.setText(lout);

                                    //String lout = String.format("%f Hz", _pitch);
                                    double lhalfSteps = getHalfStepsFromA4((float) _pitch);
                                    String lout = halfStepsToString(lhalfSteps);
                                    //_txtNote.setText(_txtNote.getText()+" "+lout);
                                    noteString += (lout + ",");
                                }
                            });
                        }
                        public void onMarkerReached(AudioRecord recorder)
                        {
                            Log.d("aicitel", "onMarkerReached");
                        }
                    }
            );

            _audioRecord.startRecording();

            Thread lrecorder = new Thread(new Runnable()
            {
                public void run()
                {
                    int lcounter = 0;
                    while (_audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED)
                    {
                        final short[] lshortArray = new short[Constants.ReadBufferSize/2];

                        _audioRecord.read(lshortArray, 0, Constants.ReadBufferSize/2);

                        if (lcounter == 0)
                        {
                            Yin lyin = new Yin(_audioRecord.getSampleRate());
                            double linstantaneousPitch = lyin.getPitch(lshortArray);
                            if (linstantaneousPitch >= 0)
                            {
                                _pitch = (float)((2.0 * _pitch + linstantaneousPitch) / 3.0);
                            }
                        }

                        lcounter = (lcounter + 1) % 5;
                    }
                }
            });

            lrecorder.start();
        }
        else{
            Toast.makeText(this.getActivity().getBaseContext(), "fail", Toast.LENGTH_SHORT).show();
        }
    }
    private String halfStepsToString(double aHalfStepsFromA4)
    {
        String retval = "";

        int lintHalfStepsFromC0 = (int)aHalfStepsFromA4 + 57;

        int loctave = (lintHalfStepsFromC0 / 12);

        int lnoteInOctave = lintHalfStepsFromC0 % 12;

        switch (lnoteInOctave)
        {
            case 0: retval += "1-"; break;
            case 1: retval += "1-"; break;
            case 2: retval += "2-"; break;
            case 3: retval += "2-"; break;
            case 4: retval += "3-"; break;
            case 5: retval += "4-"; break;
            case 6: retval += "4-"; break;
            case 7: retval += "5-"; break;
            case 8: retval += "5-"; break;
            case 9: retval += "6-"; break;
            case 10: retval += "6-"; break;
            case 11: retval += "7-"; break;
        }

        retval = String.format("%s%d", retval, loctave);

        return retval;
    }


    private double getHalfStepsFromA4(float aFrequency)
    {
        return 12 * (log(aFrequency / 440.0, 2) );
    }

    private static double log(double x, double base)
    {
        return (Math.log(x) / Math.log(base));
    }

    private void stopRecord(){
        _audioRecord.stop();
    }
    private void startShift(){
        //TODO start shift
        //TODO mult thread and callback
        enterPreview();
        ((TextView)mRootView.findViewById(R.id.staffText)).setText(noteString);
        SurfaceView staffView = (SurfaceView)mRootView.findViewById(R.id.staffSurface);
        this.staffHolder = staffView.getHolder();
        //staffView.setZOrderOnTop(true);      // 这句不能少
        //staffHolder.setFormat(PixelFormat.TRANSPARENT);
        drawView();
    }

    public void drawView() {
        try {
            if (staffHolder != null) {
                canvas = staffHolder.lockCanvas();
                canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
                StaffView view = new StaffView(this.getActivity(),noteString);
                timeText.setText(noteString.split(",").length+"\"");
                view.onDraw(canvas);

            }
            else{
                Log.i("aicitel","?????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null && staffHolder!=null)
                staffHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void initMidiPlayer(String filepath) {
        if (midiPlayer != null) {
        } else {
            midiPlayer = new MidiPlayer(filepath);
        }
        midiPlayer.setMusicPlayer(this);
    }

    private void enterRecord() {
        mRootView.findViewById(R.id.main_layout_record).setVisibility(View.VISIBLE);
        mRootView.findViewById(R.id.main_layout_viewContent).setVisibility(View.INVISIBLE);
        recordStage = Constants.RECORD_STAGE_UNBEGIN;
        recordButton.setImageResource(R.drawable.music_rotate_1);
        mainHint.setText(R.string.main_hint1);
    }

    private void enterPreview(){
        mRootView.findViewById(R.id.main_layout_record).setVisibility(View.INVISIBLE);
        mRootView.findViewById(R.id.main_layout_viewContent).setVisibility(View.VISIBLE);
        recordStage = Constants.RECORD_STAGE_PREVIEW;
    }

    private void enterListView(){
        //startActivity(new Intent(MainActivity.this,StaffListFragment.class));
        //mFragmentManager.beginTransaction().show(staffListFragment).commit();
        noteString="";
        mTagSwitchListener.addOrShowFragment(Constants.LISTVIEW_INDEX);
    }

    private void saveMusicScoreLocal(String note){
        mPerferenceManager.saveMusicScoreLocal(note, nameEditText.getText().toString());
    }
    private void saveMusicScoreLocal(String note,String index){
        mPerferenceManager.saveMusicScoreLocal(note, nameEditText.getText().toString(), index);
    }

    public void onIconsPressed(final int type){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());  //先得到构造器
        builder.setTitle("未完成编辑"); //设置标题
        builder.setMessage("是否确认退出?"); //设置内容
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                switch (type){
                    case Constants.ENTER_PROFILE_INDEX:
                        mTagSwitchListener.viewProfile();
                        break;
                    case Constants.ENTER_MENU_INDEX:
                        enterListView();
                        break;
                    case Constants.ENTER_MIC_INDEX:
                        cancelRecord();
                        break;
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public boolean onCancelPressed(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());  //先得到构造器
        builder.setTitle("未完成编辑"); //设置标题
        builder.setMessage("是否确认退出?"); //设置内容
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                cancelRecord();
                cancelFlag = true;
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
        return cancelFlag;
    }

    private void cancelRecord(){
        noteString = "";
        enterRecord();
    }


    /*
    * something about record and play
    * */
    public static void sendHandlerMessage(Handler handler, int message_type) {
        Message msg1 = handler.obtainMessage();
        msg1.what=-1;
        Bundle b = new Bundle();
        b.putInt("message_type", message_type);
        msg1.setData(b);
        handler.sendMessage(msg1);
    }
    public Handler handler = new Handler() {

        @Override
        public synchronized void handleMessage(Message msg) {
            if(msg.what==1) {
                Bundle b = msg.getData();
                changeProgressAction(b.getInt("length"),b.getInt("progress"));
            }
            else if(msg.what==0)
                resetAction(msg.getData().getInt("length"));
        }
    };

    private class Init extends AsyncTask<Void, Void, Void> {
        private void loadNativeLibs() {
            String libLocation = "/data/data/com.atm.android.audiotomidi/lib" + "/" + "libatm.so";
            try {
                System.load(libLocation);
            } catch (Exception ex) {
                Log.e("JNIExample", "failed to load native library: " + ex);
            }

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            //Install libs
            //loadNativeLibs();
            //Install FIles
            FileInstaller.installFiles(mActivity);

            //Init ATM Converters
            //pcm2wavconverter = new Pcm2WavConverter(pcmfilename, wavfilename);

            //For now notes are retrieved via JNI method
            //wav2midiconverter = new Wav2MidiConverter(wavfilename);

            return null;
        }

        @Override
        protected void onPostExecute(Void test) {
            sendHandlerMessage(handler, Activity.RESULT_OK);
        }
    }
    public static class PlayMidi extends AsyncTask<Void, Void, Void> {
        private String noteString;
        public void setNoteString(String noteString){
            this.noteString = noteString;
        }
        @Override
        protected Void doInBackground(Void... arg0) {

            midiPlayer.playMidi(NoteParser.parseString2ArrayWithTime(this.noteString));
            //sendHandlerMessage(handler, Activity.RESULT_OK);
            return null;
        }

        @Override
        protected void onPostExecute(Void test) {
        }
    }

    class FocusListener implements View.OnFocusChangeListener{
        Boolean mIsChanged = false;
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus && !mIsChanged) {
                ((EditText) v).setText("");
                mIsChanged = true;
            }
        }
    }


}
