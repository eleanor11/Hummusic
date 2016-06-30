package com.hummusic;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.hummusic.fragments.BaseFragment;
import com.hummusic.fragments.RecordFragment;
import com.hummusic.fragments.StaffListFragment;
import com.hummusic.widgets.TagSwitchListener;

public class MainActivity extends Activity implements TagSwitchListener {

    private FragmentManager mFragmentManager;
    private BaseFragment mFragment;
    private BaseFragment staffListFragment;
    private BaseFragment recordFragment;
    private Handler handler = new Handler(){
        @Override
        public void  handleMessage(Message msg) {
            Bundle b = msg.getData();
            Toast.makeText(MainActivity.this, b.getString(Constants.MSG), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.i("aicitel", "oncreate");
        setContentView(R.layout.layout_main);

        mFragmentManager = getFragmentManager();
        staffListFragment = new StaffListFragment();
        staffListFragment.setTagSwitchListener(this);
        mFragmentManager.beginTransaction().add(R.id.listViewContainer,staffListFragment).hide(staffListFragment).commit();
        recordFragment = new RecordFragment();
        recordFragment.setTagSwitchListener(this);
        mFragmentManager.beginTransaction().add(R.id.recordContainer,recordFragment).commit();
        mFragment = recordFragment;
    }

    @Override
    public void addOrShowFragment(int index){
        if(index==Constants.LISTVIEW_INDEX) {
            recordFragment.hideOrShowContent(Constants.HIDE);
            staffListFragment.hideOrShowContent(Constants.REFRESH);
        }
        else {
            recordFragment.hideOrShowContent(Constants.SHOW);
        }
        BaseFragment showFragment = index==Constants.RECORD_INDEX?recordFragment:staffListFragment;
        BaseFragment hideFragment = index!=Constants.RECORD_INDEX?recordFragment:staffListFragment;
        mFragment = showFragment;
        mFragmentManager.beginTransaction().hide(hideFragment).show(showFragment).commit();
    }
    @Override
    public void viewProfile(){
        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
    }
    @Override
    public void enterMainActivity(){
        //startActivity();
    }
    @Override
    public void enterMainActivityTeacher(){
    }
    @Override
    public void sendToastSign(String text){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MSG, text);
        Message msg = new Message();
        msg.setData(bundle);
        handler.sendMessage(msg);
    }


    @Override
    public void onBackPressed(){
        //TODO state change with action
        if(mFragment.onBackPressed()) {
            System.exit(0);
        }
    }

}
