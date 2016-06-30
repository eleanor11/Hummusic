package com.hummusic;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import static com.hummusic.fragments.SigninFragment.fa;

/**
 * Created by Eleanor on 2016/6/29.
 */

public class ResetActivity extends BaseActivity{
    private EditText nameText = null;
    private EditText pwText = null;

    private TextView saveButton = null;
    private ImageView backButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_reset);
        initWidgets();
    }

    private void initWidgets(){

        nameText = (EditText) findViewById(R.id.nameResetText);
        nameText.setOnFocusChangeListener(new FocusListener0());

        pwText = (EditText) findViewById(R.id.pwResetText);
        pwText.setOnFocusChangeListener(new FocusListener1());

        saveButton = (TextView) findViewById(R.id.saveTextView);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(reset).start();
            }
        });

        backButton = (ImageView) findViewById(R.id.backImageView);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetActivity.this.finish();
            }
        });

    }
    class FocusListener0 implements View.OnFocusChangeListener {
        Boolean mIsChanged = false;

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            Log.d("test", ((EditText) v).getText().toString());
            if (hasFocus && !mIsChanged) {
                ((EditText) v).setText("");
                mIsChanged = true;
            }
            else if (!hasFocus && ((EditText) v).getText().toString().contentEquals("")) {
                ((EditText) v).setText("NAME RESET");
                mIsChanged = false;
            }
            if (hasFocus) v.setBackgroundResource(R.drawable.corner_view_opa_50);
            else v.setBackgroundResource(R.drawable.corner_view_opa_30);
        }
    }

    class FocusListener1 implements View.OnFocusChangeListener {
        Boolean mIsChanged = false;

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            Log.d("test", ((EditText) v).getText().toString());
            if (hasFocus && !mIsChanged) {
                ((EditText) v).setText("");
                mIsChanged = true;
            }
            else if (!hasFocus && ((EditText) v).getText().toString().contentEquals("")) {
                ((EditText) v).setText("PASSWORD RESET");
                mIsChanged = false;
            }
            if (hasFocus) v.setBackgroundResource(R.drawable.corner_view_opa_50);
            else v.setBackgroundResource(R.drawable.corner_view_opa_30);
        }
    }


    Runnable reset = new Runnable() {
        @Override
        public void run() {
            String email = fa.getUserInfo().getEmail();
            String name = nameText.getText().toString();
            String pw = pwText.getText().toString();
            if (!name.contentEquals("NAME RESET") && !name.contentEquals("")) {
                fa.setUserInfo(email, name);
            }
            if (!pw.contentEquals("PASSWORD RESET") && !pw.contentEquals("")) {
                fa.resetPwd(email, pw);
            }
        }
    };

}

