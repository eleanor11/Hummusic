package com.hummusic;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hummusic.fragments.BaseFragment;
import com.hummusic.fragments.RegisterFragment;
import com.hummusic.fragments.SigninFragment;
import com.hummusic.widgets.TagSwitchListener;

/**
 * Created by bluemaple on 2016/6/16.
 */
public class LoginActivity extends BaseActivity implements TagSwitchListener {

    private FragmentManager mFragmentManager = null;
    private BaseFragment signinFragment = null;
    private BaseFragment registerFragment = null;
    public static int REGISTER_INDEX = 0;
    public static int SIGNIN_INDEX = 1;

    private Handler handler = new Handler(){
        @Override
        public void  handleMessage(Message msg) {
            Bundle b = msg.getData();
            Toast.makeText(LoginActivity.this, b.getString(Constants.MSG), Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_logining);
        mFragmentManager = this.getFragmentManager();
        signinFragment = new SigninFragment();
        signinFragment.setTagSwitchListener(this);
        registerFragment = new RegisterFragment();
        registerFragment.setTagSwitchListener(this);
        mFragmentManager.beginTransaction().add(R.id.signin_fragment_container,signinFragment).hide(signinFragment).commit();
        mFragmentManager.beginTransaction().add(R.id.register_fragment_container, registerFragment).commit();
    }
    @Override
    public void addOrShowFragment(int index){
        BaseFragment showFragment = index==REGISTER_INDEX?registerFragment:signinFragment;
        BaseFragment hideFragment = index!=REGISTER_INDEX?registerFragment:signinFragment;
        mFragmentManager.beginTransaction().hide(hideFragment).show(showFragment).commit();
    }
    @Override
    public void viewProfile(){
    }
    @Override
    public void enterMainActivity(){
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
    public void enterMainActivityTeacher(){
        startActivity(new Intent(LoginActivity.this, ShowStudentsActivity.class));
    }
    @Override
    public void onBackPressed(){
        System.exit(0);
    }
}
