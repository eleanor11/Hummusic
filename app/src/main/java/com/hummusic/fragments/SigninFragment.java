package com.hummusic.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hummusic.Constants;
import com.hummusic.R;
import com.hummusic.functions.FunctionAccessor;
import com.hummusic.functions.FunctionImpl;
import com.hummusic.widgets.TagSwitchListener;

/**
 * Created by bluemaple on 2016/6/26.
 */
public class SigninFragment extends BaseFragment {

    private boolean[] isFill = {false,false};
    private TextView signinButton = null;
    private View mRootView = null;
    private TagSwitchListener mTagSwitchListener;

    public static FunctionAccessor fa = new FunctionImpl();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.layout_signin,container,false);
        initWidgets();
        return mRootView;
    }
    @Override
    public void setTagSwitchListener(TagSwitchListener tagSwitchListener){
        this.mTagSwitchListener = tagSwitchListener;
    }
    @Override
    public void hideOrShowContent(int hideOrShow){
    }
    @Override
    public boolean onBackPressed(){return true;}

    private boolean isFill(){
        return isFill[0]&&isFill[1];
    }

    private void enterMainActivity(int type){
        if(type==Constants.STUDENT_TYPE)
            mTagSwitchListener.enterMainActivity();
        else
            mTagSwitchListener.enterMainActivityTeacher();
    }

    private void initWidgets(){
        TextView registerText = (TextView)mRootView.findViewById(R.id.registerTextView);
        registerText.getBackground().setAlpha(0x4d);
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTagSwitchListener.addOrShowFragment(Constants.REGISTER_INDEX);
                //enterMainActivity();
            }
        });
        signinButton = (TextView)mRootView.findViewById(R.id.signinBTextView);
        signinButton.getBackground().setAlpha(0x4d);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(signin).start();
            }
        });

        EditText emailText = (EditText) mRootView.findViewById(R.id.emailTextView);
        emailText.setOnFocusChangeListener(new FocusListener());
        emailText.addTextChangedListener(new FillTextWatcher(emailText, 0));
        EditText pwText = (EditText)mRootView. findViewById(R.id.pwTextView);
        pwText.setOnFocusChangeListener(new FocusListener());
        pwText.addTextChangedListener(new FillTextWatcher(pwText, 1));

    }

    class FocusListener implements View.OnFocusChangeListener{
        Boolean mIsChanged = false;
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus && !mIsChanged) {
                ((EditText) v).setText("");
                mIsChanged = true;
            }
            if(hasFocus) v.setBackgroundResource(R.drawable.corner_view_opa_50);
            else  v.setBackgroundResource(R.drawable.corner_view_opa_30);
        }
    }

    class FillTextWatcher implements TextWatcher {
        EditText mEditText = null;
        int isFullPos = -1;
        public FillTextWatcher(EditText editText,int pos){
            this.mEditText = editText;
            this.isFullPos = pos;
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            isFill[isFullPos]= mEditText.getText().length()>0;
            if(isFill()) signinButton.setBackgroundResource(R.drawable.corner_view_opa_50);
            else signinButton.setBackgroundResource(R.drawable.corner_view_opa_30);
        }
    }

    Runnable signin = new Runnable() {
        @Override
        public void run() {
            TextView emailText = (TextView) mRootView.findViewById(R.id.emailTextView);
            TextView pwText = (TextView) mRootView.findViewById(R.id.pwTextView);
            if (fa.login(emailText.getText().toString(), pwText.getText().toString()) == 1) {
                Log.i("Signin", "Success");
                enterMainActivity(fa.getUserInfo().isTeacher()?Constants.TEACHER_TYPE:Constants.STUDENT_TYPE);
            }
            else{
                mTagSwitchListener.sendToastSign("login fail");
            }
        }
    };

}
