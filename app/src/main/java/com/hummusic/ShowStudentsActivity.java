package com.hummusic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hummusic.functions.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hummusic.fragments.SigninFragment.fa;

/**
 * Created by Eleanor on 2016/6/19.
 */

public class ShowStudentsActivity extends BaseActivity{

    private ArrayList<UserInfo> users = null;
    private Boolean finished = false;
    private ImageView backButton = null;
    private TextView refreshButton = null;

    private List<Map<String, Object>> mData;
    private ListView myList;
    private MyAdapter myAdapter;

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
                ShowStudentsActivity.this.finish();
            }
        });

        refreshButton = (TextView) findViewById(R.id.refreshTextView);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        setListView();

        //TODO search
    }
    private void setListView() {

        mData = getData();
        myList = (ListView) findViewById(R.id.usersListView);
        myList.setItemsCanFocus(true);
        myAdapter = new MyAdapter(getApplicationContext());
        myList.setAdapter(myAdapter);
        myList.setOnItemClickListener(new MyItemClickListener());
    }
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (users == null) return null;

        int size = users.size();
        for (int i = 0; i < size; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            UserInfo user = users.get(i);
            map.put("name", user.getName());
            map.put("num", user.getMScoreSize() + "");
            map.put("email", user.getEmail());
            list.add(map);
        }
        return list;
    }

    public class MyItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> L, View v, int position, long id) {
            HashMap<String, Object> item = (HashMap<String, Object>) myList.getItemAtPosition(position);
            //TODO enter dialogue
            Intent intent = new Intent(ShowStudentsActivity.this, DialogueActivity.class);
            intent.putExtra("email", (String) mData.get(position).get("email"));
            startActivity(intent);

        }
    }

    public final class ViewHolder {

        public TextView name;
        public TextView num;
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mData.get(position);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.user_item_1, null);
                holder.name = (TextView) convertView.findViewById(R.id.item_userName);
                holder.num = (TextView) convertView.findViewById(R.id.item_userNum);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (holder != null) {
                holder.name.setText((String) mData.get(position).get("name"));
                holder.num.setText((String) mData.get(position).get("num"));
//                Log.d("test", mData.get(position).get("name").toString());
//                Log.d("test", mData.get(position).get("num").toString());
            }

            return convertView;
        }
    }
    Runnable get = new Runnable() {
        @Override
        public void run() {
            users = fa.getStudents();
            for (int i = 0; i < users.size(); i++) {
                Log.d("students", users.get(i).getEmail() + users.get(i).getMScores().size());
            }
            finished = true;
        }
    };

}
