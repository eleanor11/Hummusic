package com.hummusic.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.hummusic.Constants;
import com.hummusic.DialogueActivity;
import com.hummusic.R;
import com.hummusic.UploadActivity;
import com.hummusic.functions.utils.PreferenceManager;
import com.hummusic.widgets.RefreshAdapter;
import com.hummusic.widgets.TagSwitchListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bluemaple on 2016/6/25.
 */
public class StaffListFragment extends BaseFragment {

    private List<Map<String, Object>> list;
    private ListView lv;
    private RefreshAdapter adapter;
    private View mRootView = null;
    private ImageView menuButton = null;
    private Activity mActivity;

    private RefreshAdapter.ViewHolder nextHolder;
    private ImageView profileButton = null;
    private View menuLayout = null;
    private PreferenceManager mPerferenceManager;

    private TagSwitchListener mTagSwitchListener;
    public void resetPlayerIcon(int length,RefreshAdapter.ViewHolder holder){
        this.nextHolder = holder;
        Message message = new Message();
        message.what = 0;
        Bundle bundle = new Bundle();
        bundle.putInt("length", length);
        message.setData(bundle);
        handler.sendMessage(message);
    }
    public void changeProgress(int length,int progress,RefreshAdapter.ViewHolder holder){
        this.nextHolder = holder;
        Message message = new Message();
        message.what = 1;
        Bundle bundle = new Bundle();
        bundle.putInt("length", length);
        bundle.putInt("progress",progress);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    public Handler handler = new Handler(){
        @Override
        public synchronized void handleMessage(Message msg) {
            if(msg.what==1) {
                Bundle b = msg.getData();
                nextHolder.changeProgressAction(b.getInt("length"), b.getInt("progress"));
            }
            else if(msg.what==0)
                nextHolder.resetAction(msg.getData().getInt("length"));

        }
    };

    @Override
    public void setTagSwitchListener(TagSwitchListener tagSwitchListener){
        this.mTagSwitchListener = tagSwitchListener;
    }
    @Override
    public boolean onBackPressed(){
        mTagSwitchListener.addOrShowFragment(Constants.RECORD_INDEX);
        return false;
    }
    @Override
    public void hideOrShowContent(int hideOrShow){
        initListView();
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.layout_listview,container,false);
        mActivity = this.getActivity();
        menuLayout = mRootView.findViewById(R.id.menuLayout);
        Log.i("aicitel", "oncreate");

        profileButton = (ImageView) mRootView.findViewById(R.id.top_profileImageView);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mPerferenceManager.setIndexNull();
                mTagSwitchListener.viewProfile();
            }
        });

        menuButton = (ImageView) mRootView.findViewById(R.id.top_menuImageView);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menuLayout.getVisibility() == View.VISIBLE)
                    menuLayout.setVisibility(View.INVISIBLE);
                else
                    menuLayout.setVisibility(View.VISIBLE);
            }
        });
        mRootView.findViewById(R.id.listTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.notifyDataSetChanged();
                menuLayout.setVisibility(View.INVISIBLE);
            }
        });
        mRootView.findViewById(R.id.msgTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity.getBaseContext(), DialogueActivity.class));
                menuLayout.setVisibility(View.INVISIBLE);
            }
        });
        mRootView.findViewById(R.id.top_micImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mPerferenceManager = new PreferenceManager(this.getActivity().getBaseContext());
        //initListView();
        return mRootView;
    }

    public void refresh(){
        mPerferenceManager.getAllNotes(list);
        adapter.notifyDataSetChanged();
    }
    public void deleteMusicScore(String index){
        mPerferenceManager.deleteMusicScore(index);
        refresh();
    }

    public void initListView(){
        lv = (ListView) mRootView.findViewById(R.id.sourceList);
        list = new ArrayList<>();
        mPerferenceManager.getAllNotes(list);
        adapter = new RefreshAdapter(list, this.getActivity().getBaseContext(),this);
        lv.setAdapter(adapter);

    }
    public void upload(String musicScore){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DETAIL, musicScore);
        intent.putExtra("bundle", bundle);
        intent.setClass(getActivity().getBaseContext(), UploadActivity.class);
        startActivity(intent);


    }

}
