package com.hummusic.widgets;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hummusic.Constants;
import com.hummusic.R;
import com.hummusic.fragments.RecordFragment;
import com.hummusic.fragments.StaffListFragment;
import com.hummusic.functions.utils.PreferenceManager;

import java.util.List;
import java.util.Map;

public class RefreshAdapter extends BaseAdapter{
    private StaffListFragment mFragment = null;
    private List<Map<String, Object>> mData;
    private Context context;
    private LayoutInflater mInflater;

    public RefreshAdapter(List<Map<String, Object>> data, Context context, StaffListFragment baseFragment) {
        //this.mStaff = staff;
        this.mData = data;
        this.context = context;
        this.mInflater =  LayoutInflater.from(context);
        this.mFragment = baseFragment;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView tv = new TextView(context.getApplicationContext());
        tv.setTextColor(Color.BLACK);

        final ViewHolder holder;
        if (convertView == null) {
            holder=new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder.choiceLayout = convertView.findViewById(R.id.choicesLayout);
            holder.coverLayout = convertView.findViewById(R.id.coverLayout);
            holder.staffHolder = ((StaffSurfaceView)convertView.findViewById(R.id.staffView));
            holder.staffHolder.setStaffView(new StaffView(convertView.getContext(), (String) (mData.get(position).get(PreferenceManager.CONTENT_STRING))));
            holder.title = (TextView)convertView.findViewById(R.id.item_title);
            holder.title.setText((String)(mData.get(position).get(PreferenceManager.TITLE_STRING)));
            holder.timeText = (TextView)convertView.findViewById(R.id.timeTextView);
            holder.uploadImage = (ImageView)convertView.findViewById(R.id.item_uploadImage);
            holder.uploadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String detailString =  (String) (mData.get(position).get(PreferenceManager.TITLE_STRING))
                                        + PreferenceManager.splitNode
                                        + (String) (mData.get(position).get(PreferenceManager.CONTENT_STRING));
                    mFragment.upload(detailString);
                }
            });
            holder.playImage = (ImageView)convertView.findViewById(R.id.item_playImage);
            holder.playImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.coverLayout.setVisibility(View.VISIBLE);
                    if(RecordFragment.midiPlayer.getIsPaused()== Constants.PAUSED) {
                        RecordFragment.midiPlayer.setIsPaused(Constants.UNPAUSED);
                    }
                    else{
                        RecordFragment.PlayMidi playTask = new RecordFragment.PlayMidi();
                        playTask.setNoteString((String)mData.get(position).get(PreferenceManager.CONTENT_STRING));
                        playTask.execute();
                    }
                    holder.playImage.setVisibility(View.INVISIBLE);
                    holder.pauseImage.setVisibility(View.VISIBLE);
                    RecordFragment.midiPlayer.setMusicPlayer(holder);
                }
            });
            holder.progressBarImage = (ImageView)convertView.findViewById(R.id.progressImage);
            holder.progressNodeImage = (ImageView)convertView.findViewById(R.id.progressNodeImage);
            holder.pauseImage = (ImageView)convertView.findViewById(R.id.item_pauseImage);
            holder.pauseImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RecordFragment.midiPlayer.setIsPaused(Constants.PAUSED);
                    holder.playImage.setVisibility(View.VISIBLE);
                    holder.pauseImage.setVisibility(View.INVISIBLE);
                }
            });
            holder.writeImage = (ImageView)convertView.findViewById(R.id.item_writeImage);
            holder.writeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFragment.onBackPressed();
                    RecordFragment.noteString = (String)mData.get(position).get(PreferenceManager.CONTENT_STRING);
                    RecordFragment.noteIndex = (String)mData.get(position).get(PreferenceManager.INDEX_STRING);
                    //TODO continue write
                }
            });
            holder.deleteImage = (ImageView)convertView.findViewById(R.id.deletButton);
            holder.deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.choiceLayout .findViewById(R.id.choicesLayout).setVisibility(View.VISIBLE);
                }
            });
            holder.yesChoice = convertView.findViewById(R.id.yesTextView);
            holder.yesChoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFragment.deleteMusicScore((String) mData.get(position).get(PreferenceManager.INDEX_STRING));
                    holder.choiceLayout .findViewById(R.id.choicesLayout).setVisibility(View.INVISIBLE);
                }
            });
            holder.noChoice = convertView.findViewById(R.id.noTextView);
            holder.noChoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.choiceLayout .findViewById(R.id.choicesLayout).setVisibility(View.INVISIBLE);
                }
            });
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.title.setText((String) mData.get(position).get(PreferenceManager.TITLE_STRING));
        holder.staffHolder.setStaffView(new StaffView(convertView.getContext(), (String) (mData.get(position).get(PreferenceManager.CONTENT_STRING))));
        holder.staffHolder.reDraw();
        holder.title.setTextSize(14);
        return convertView;
    }

    public final class ViewHolder implements MusicPlayer{
        public StaffSurfaceView staffHolder;
        public TextView title;
        public TextView timeText;
        public ImageView uploadImage;
        public ImageView playImage;
        public ImageView pauseImage;
        public ImageView writeImage;
        public ImageView deleteImage;
        public ImageView progressBarImage;
        public ImageView progressNodeImage;
        public View choiceLayout;
        public View coverLayout;
        public View yesChoice;
        public View noChoice;

        public void resetPlayIcon(int length){
            mFragment.resetPlayerIcon(length,this);
        }
        public void resetAction(int length){
            playImage.setVisibility(View.VISIBLE);
            pauseImage.setVisibility(View.INVISIBLE);
            coverLayout.setVisibility(View.INVISIBLE);
            timeText.setText(length+"\"");
            progressNodeImage.setX(progressNodeImage.getX() - progressBarImage.getWidth());
        }
        public void changeProgress(int length,int progress){
            mFragment.changeProgress(length,progress, this);
        }
        public void changeProgressAction(int length,int progress){
            //TODO
            int offset = progressBarImage.getWidth()/length;
            timeText.setText(progress+"\"");
            progressNodeImage.setX(progressNodeImage.getX() + offset);
        }
    }


}