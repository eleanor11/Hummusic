package com.hummusic;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hummusic.functions.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;

import static com.hummusic.fragments.SigninFragment.fa;

/**
 * Created by Eleanor on 2016/6/19.
 */

public class ShowTeachersActivity extends BaseActivity{

    private ArrayList<UserInfo> users = null;
    private Boolean finished = false;
    private ImageView backButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_users);

        new Thread(get).start();

        while (!finished) {
        }

        initWidgets();

    }

    private void initWidgets(){
        backButton = (ImageView) findViewById(R.id.backImageView);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowTeachersActivity.this.finish();
            }
        });

        setListView();

        //TODO search
    }


    private void setListView() {
        if (users == null) return;

        ListView list = (ListView) findViewById(R.id.usersListView);

        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        Log.i("test", users.size()+"");
        for(int i = 0; i < users.size(); i++) {
            UserInfo user = users.get(i);
            HashMap<String, String> map = new HashMap<String, String>();
            Log.i("test", (user == null) + "");
            map.put("item_userName", user.getName());
            map.put("item_userNum", user.getMScoreSize() + "");
            mylist.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(
                this,
                mylist,
                R.layout.user_item_1,
                new String[] {"item_userName", "item_userNum"},
                new int[] {R.id.item_userName, R.id.item_userNum});

        list.setAdapter(simpleAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO enter dialogue
            }
        });
    }

    Runnable get = new Runnable() {
        @Override
        public void run() {
            users = fa.getTeachers();
            finished = true;
        }
    };
}
