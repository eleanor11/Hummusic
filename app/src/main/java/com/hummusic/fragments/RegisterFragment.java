package com.hummusic.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hummusic.Constants;
import com.hummusic.R;
import com.hummusic.widgets.TagSwitchListener;

import static com.hummusic.fragments.SigninFragment.fa;

/**
 * Created by bluemaple on 2016/6/26.
 */
public class RegisterFragment extends BaseFragment {

    private boolean[] isFill = {false,false,false};
    private TextView registerButton = null;
    private TextView startButton = null;

    private ImageView studentImage = null;
    private ImageView teacherImage = null;
    private View mRootView = null;

    //FunctionAccessor fa = new FunctionImpl();
    private int isTeacher;
    private TagSwitchListener mTagSwitchListener;
    @Override
    public void setTagSwitchListener(TagSwitchListener tagSwitchListener){
        this.mTagSwitchListener = tagSwitchListener;
    }
    @Override
    public boolean onBackPressed(){return true;}

    @Override
    public void hideOrShowContent(int hideOrShow){
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        mRootView =inflater.inflate(R.layout.layout_register,container,false);
        initWidgets();
        return mRootView;
    }

    private void enterChooseUserType(){
        mRootView.findViewById(R.id.register_layout_1).setVisibility(View.INVISIBLE);
        mRootView.findViewById(R.id.register_layout_2).setVisibility(View.VISIBLE);
    }

    private void enterUploadUserPhoto(int type){
        mRootView.findViewById(R.id.register_layout_2).setVisibility(View.INVISIBLE);
        mRootView.findViewById(R.id.register_layout_3).setVisibility(View.VISIBLE);
        isTeacher = type;
    }
    private boolean isFill(){
        return isFill[0]&&isFill[1]&&isFill[2];
    }

    private void initWidgets(){
        TextView signinText= (TextView)mRootView.findViewById(R.id.signinTextView);
        signinText.getBackground().setAlpha(0x4d);
        signinText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTagSwitchListener.addOrShowFragment(Constants.SIGNIN_INDEX);
            }
        });
        registerButton = (TextView)mRootView.findViewById(R.id.registerBTextView);
        registerButton.getBackground().setAlpha(0x4d);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFill()) enterChooseUserType();
            }
        });
        startButton = (TextView)mRootView.findViewById(R.id.startTextView);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("test", isTeacher + "");
                new Thread(register).start();
                if(isTeacher == Constants.STUDENT_TYPE)
                    mTagSwitchListener.enterMainActivity();
                else
                    mTagSwitchListener.enterMainActivityTeacher();
            }
        });

        studentImage = (ImageView)mRootView.findViewById(R.id.studentImage);
        studentImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        studentImage.setBackgroundResource(R.drawable.bg_radix_student_onfocus);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(event.getX()>view.getWidth() || event.getX()<0 || event.getY()<0 || event.getY()>view.getHeight())
                            studentImage.setBackgroundResource(R.drawable.bg_radix_student);
                        else
                            enterUploadUserPhoto(Constants.STUDENT_TYPE);
                        break;
                }
                return true;
            }
        });

        teacherImage = (ImageView)mRootView.findViewById(R.id.teacherImage);
        teacherImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        teacherImage.setBackgroundResource(R.drawable.bg_radix_teacher_onfocus);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (event.getX() > view.getWidth() || event.getX() < 0 || event.getY() < 0 || event.getY() > view.getHeight())
                            teacherImage.setBackgroundResource(R.drawable.bg_radix_teacher);
                        else
                            enterUploadUserPhoto(Constants.TEACHER_TYPE);
                        break;
                }
                return true;
            }
        });

        EditText nameText = (EditText) mRootView.findViewById(R.id.nameTextView);
        nameText.setOnFocusChangeListener(new FocusListener());
        nameText.addTextChangedListener(new FillTextWatcher(nameText, 0));
        EditText emailText = (EditText) mRootView.findViewById(R.id.emailTextView);
        emailText.setOnFocusChangeListener(new FocusListener());
        emailText.addTextChangedListener(new FillTextWatcher(emailText, 1));
        EditText pwText = (EditText) mRootView.findViewById(R.id.pwTextView);
        pwText.setOnFocusChangeListener(new FocusListener());
        pwText.addTextChangedListener(new FillTextWatcher(pwText, 2));

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
            if(isFill()) registerButton.setBackgroundResource(R.drawable.corner_view_opa_50);
            else registerButton.setBackgroundResource(R.drawable.corner_view_opa_30);
        }
    }

    Runnable register = new Runnable() {
        @Override
        public void run() {
            TextView nameText = (TextView) mRootView.findViewById(R.id.nameTextView);
            TextView emailText = (TextView) mRootView.findViewById(R.id.emailTextView);
            TextView pwText = (TextView) mRootView.findViewById(R.id.pwTextView);
            Log.i("test", "1");
            if (fa.register(emailText.getText().toString(), pwText.getText().toString(), isTeacher) == 1) {
                fa.setUserInfo(emailText.getText().toString(), nameText.getText().toString());
            }
        }
    };
}
