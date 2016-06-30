package com.hummusic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static com.hummusic.fragments.SigninFragment.fa;

/**
 * Created by Eleanor on 2016/6/18.
 */

public class ProfileActivity extends BaseActivity {

    private TextView nameText = null;
    private TextView emailText = null;

    private TextView resetButton = null;
    private TextView logoutButton = null;
    private ImageView backButton = null;

    //FunctionAccessor fa = new FunctionImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile);
        initWidgets();
    }

    private void initWidgets(){

        nameText = (TextView) findViewById(R.id.nameTextView);
        emailText = (TextView) findViewById(R.id.emailTextView);
        if (fa.isLogin()) {
            nameText.setText(fa.getUserInfo().getName());
            emailText.setText(fa.getUserInfo().getEmail());
        }

        resetButton = (TextView) findViewById(R.id.resetTextView);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, ResetActivity.class));
            }
        });

        logoutButton = (TextView) findViewById(R.id.logoutTextView);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fa.logout();
                startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
            }
        });

        backButton = (ImageView) findViewById(R.id.backImageView);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileActivity.this.finish();
            }
        });


    }

}
