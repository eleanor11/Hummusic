package com.hummusic;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hummusic.functions.UserInfo;
import com.hummusic.functions.upload.OnStateListener;
import com.hummusic.functions.upload.UploadUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hummusic.fragments.SigninFragment.fa;

/**
 * Created by Eleanor on 2016/6/19.
 */

public class UploadActivity extends BaseActivity{

    private ArrayList<UserInfo> users = null;
    private Boolean finished = false;
    private ImageView backButton = null;

    private ImageView upload0;
    private ImageView uploading0;

    private List<Map<String, Object>> mData;
    private ListView myList;
    private MyAdapter myAdapter;

    private File recordFile = null;
    private String noteDetail;
    private Handler handler = new Handler(){
        @Override
        public void  handleMessage(Message msg) {
            Bundle b = msg.getData();
            Toast.makeText(UploadActivity.this, b.getString(Constants.MSG), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_users);

        recordFile = (File) getIntent().getExtras().get("file");
        noteDetail =  getIntent().getBundleExtra("bundle").getString(Constants.DETAIL);

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
                UploadActivity.this.finish();
            }
        });

        mData = getData();
        myList = (ListView) findViewById(R.id.usersListView);
        myList.setItemsCanFocus(true);
        myAdapter = new MyAdapter(getApplicationContext());
        myList.setAdapter(myAdapter);

        //TODO search
    }


    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (users == null) return null;

        int size = users.size();
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < size; i++) {
            UserInfo user = users.get(i);
            map.put("name", user.getName());
            list.add(map);
        }
        return list;
    }

    public class MyItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> L, View v, int position, long id) {
            HashMap<String, Object> item = (HashMap<String, Object>) myList.getItemAtPosition(position);
            //TODO enter dialogue
        }
    }

    public final class ViewHolder {

        public TextView name;
        public ImageView image;
        public ImageView upload;
        public ImageView uploading;
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

                convertView = mInflater.inflate(R.layout.user_item_2, parent,false);
                holder.name = (TextView) convertView.findViewById(R.id.item_userName);
                holder.image = (ImageView) convertView.findViewById(R.id.item_userImage);
                holder.upload = (ImageView) convertView.findViewById(R.id.item_userUpload);
                holder.uploading = (ImageView) convertView.findViewById(R.id.item_userUpload1);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (holder != null) {
                upload0 = holder.upload;
                uploading0 = holder.uploading;

                holder.name.setText((String) mData.get(position).get("name"));
                HashMap<String, Object> item = (HashMap<String, Object>) getItem(position);
                final boolean isuploading = false;
                holder.upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        holder.upload.setVisibility(View.INVISIBLE);
//                        holder.uploading.setVisibility(View.VISIBLE);
                        //upload0.setVisibility(View.INVISIBLE);
                        //uploading0.setVisibility(View.VISIBLE);
                        //TODO upload
                        //new Thread(uploadThread).start();
                        new Thread(uploadMscoreThread).start();
                    }
                });

                holder.uploading.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        holder.uploading.setVisibility(View.INVISIBLE);
//                        holder.upload.setVisibility(View.VISIBLE);
                        upload0.setVisibility(View.VISIBLE);
                        uploading0.setVisibility(View.INVISIBLE);
                        //TODO stop uploading

                    }
                });
            }

            return convertView;
        }
    }

    private String uploadFileServerUrl;
    private OnStateListener uploadFileStateListener;

    private class UpLoadFile extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... patameters) {
            return UploadUtil.uploadFile(recordFile, uploadFileServerUrl);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                if (uploadFileStateListener != null) {
                    uploadFileStateListener.onState(-2, "fail");
                }
                return;
            }
            else {
                if (uploadFileStateListener != null) {
                    uploadFileStateListener.onState(0, "success");;
                }
            }
        }
    }

    Runnable get = new Runnable() {
        @Override
        public void run() {
            users = fa.getTeachers();
            finished = true;
        }
    };

    Runnable uploadThread = new Runnable() {
        @Override
        public void run() {
            new UpLoadFile().execute();
        }
    };

    Runnable uploadMscoreThread = new Runnable() {
        @Override
        public void run() {
            int result = fa.createMScore(fa.getUserInfo().getEmail(), noteDetail);
            String text = result==1?"upload success":"upload fail";
            Bundle bundle = new Bundle();
            bundle.putString(Constants.MSG,text);
            Message msg = new Message();
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
    };

}
