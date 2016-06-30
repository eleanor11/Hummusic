package com.hummusic.functions;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Eleanor on 2016/6/15.
 */

public class FunctionImpl implements FunctionAccessor {

    private String URLBase = "http://115.28.14.137:8080/Hummusic/";
    private UserInfo curUser = null;
    private MInfo curM = null;

    @Override
    public int register(String email, String pwd, int isTeacher) {

        int status = -1;

        try {
            URL url = new URL(URLBase + "UserReg");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setConnectTimeout(3000);

            String data = "userEmail=" + email + "&userPwd=" + pwd + "&isTeacher=" + isTeacher;

            Log.i("data", data);

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", data.length() + "");

            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());

//            conn.setConnectTimeout(3000);
//            conn.setRequestProperty("Accept", "application/json");
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setRequestMethod("POST");
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setUseCaches(false);
//            conn.connect();
//
//            JSONObject json = new JSONObject();
//            json.put("userEmail", email);
//            json.put("userPwd", pwd);
//            json.put("isTeacher", isTeacher);
//            String jsonstr = json.toString();

//            OutputStream out = conn.getOutputStream();
//            BufferedOutputStream bos = new BufferedOutputStream(out);
//            byte[] bytes = jsonstr.getBytes("UTF-8");
//            bos.write(bytes);
//            bos.flush();
//            out.close();
//            bos.close();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                while ((str = br.readLine()) != null) {
                    buffer.append(str);
                }
                in.close();
                br.close();
                JSONObject rjson = new JSONObject(buffer.toString());

//                Log.i("rjson", rjson+"");

                status = rjson.getInt("status");
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        if (status >= 0) {
            try {
                curUser = getUserInfo(email);
            }catch (Exception e) {
                e.printStackTrace();
            }

            return 1;
        }
        return status;
    }

    @Override
    public int login(String email, String pwd) {
        int status = -1;

        try {
            URL url = new URL(URLBase + "UserLogin");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setConnectTimeout(3000);

            String data = "userEmail=" + email + "&userPwd=" + pwd;

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", data.length() + "");

            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());

//            conn.setConnectTimeout(3000);
//            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//            conn.setRequestMethod("POST");
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.connect();
//
//            JSONObject json = new JSONObject();
//            json.put("userEmail", email);
//            json.put("userPwd", pwd);
//            String jsonstr = json.toString();
//
//            OutputStream out = conn.getOutputStream();
//            BufferedOutputStream bos = new BufferedOutputStream(out);
//            byte[] bytes = jsonstr.getBytes("UTF-8");
//            bos.write(bytes);
//            bos.flush();
//            out.close();
//            bos.close();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                while ((str = br.readLine()) != null) {
                    buffer.append(str);
                }
                in.close();
                br.close();
                JSONObject rjson = new JSONObject(buffer.toString());

                Log.i("rjson", rjson+"");
                status = rjson.getInt("status");

            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        if (status >= 0) {
            try {
                Log.i("test", "get user info");
                curUser = getUserInfo(email);
                Log.i("aicitel",curUser.getEmail()+"???");
                Log.i("testcur", (curUser == null) + "");
            }catch (Exception e) {
                e.printStackTrace();
            }

            return 1;
        }
        else return -1;
    }

    @Override
    public int logout() {
        curUser = null;
        return 1;
    }

    @Override
    public int resetPwd(String email, String password) {
                int status = -1;

        try {
            URL url = new URL(URLBase + "resetPwd");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setConnectTimeout(3000);

            String data = "userEmail=" + email + "&newPwd=" + password;

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", data.length() + "");

            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                while ((str = br.readLine()) != null) {
                    buffer.append(str);
                }
                in.close();
                br.close();
                JSONObject rjson = new JSONObject(buffer.toString());

                status = rjson.getInt("status");
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    @Override
    public int setUserInfo(String email, String name) {

        int status = -1;

        try {
            URL url = new URL(URLBase + "setUserInfo");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setConnectTimeout(3000);

            String data = "userEmail=" + email + "&userName=" + name;

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", data.length() + "");

            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());

//            conn.setConnectTimeout(3000);
//            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//            conn.setRequestMethod("POST");
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.connect();
//
//            JSONObject json = new JSONObject();
//            json.put("userEmail", email);
//            json.put("userName", name);
//            String jsonstr = json.toString();
//
//            OutputStream out = conn.getOutputStream();
//            BufferedOutputStream bos = new BufferedOutputStream(out);
//            byte[] bytes = jsonstr.getBytes("UTF-8");
//            bos.write(bytes);
//            bos.flush();
//            out.close();
//            bos.close();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                while ((str = br.readLine()) != null) {
                    buffer.append(str);
                }
                in.close();
                br.close();
                JSONObject rjson = new JSONObject(buffer.toString());

                status = rjson.getInt("status");
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    @Override
    public Boolean isLogin() {
//        Log.i("test", (curUser == null) + "");
        return (curUser != null);
    }
    @Override
    public UserInfo getUserInfo() {
       return curUser;
   }
    @Override
    public UserInfo getUserInfo(String email) {

        try{
            URL url = new URL(URLBase + "getUserInfo?userEmail=" + email);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();

//            Log.i("test", conn.getResponseCode() + "");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line = null;
                StringBuffer buffer = new StringBuffer();
                while((line=br.readLine())!=null){
                    buffer.append(line);
                }
                in.close();
                br.close();

                JSONObject rjson = new JSONObject(buffer.toString());
//                Log.i("test", rjson.toString());
                String name = "";
                if (!rjson.isNull("userName")) name = rjson.getString("userName");
                Integer isTeacher = 0;
                if (!rjson.isNull("isTeacher")) isTeacher = rjson.getInt("isTeacher");
                String mScore = "";
                if (!rjson.isNull("mScore")) mScore = rjson.getString("mScore");

//                if (email.contentEquals("zhangsan@qq.com")) mScore = "8a108c4e559c962c0155a0ae35250003";
                UserInfo userInfo = new UserInfo(email, name, isTeacher, mScore);
//                Log.i("test", (userInfo == null) + "");
                return userInfo;
            }
            else{
                Log.i("aicitel","not OK "+conn.getResponseCode());
            }

        } catch(Exception e) {
            Log.i("aicitel","here exe");
            e.printStackTrace();
        }

        return null;
    }
    @Override
    public ArrayList<UserInfo> getStudents() {
        ArrayList<UserInfo> students = new ArrayList<>();
        Log.i("test", "get students");
        try{
            URL url = new URL(URLBase + "getUsers?isTeacher=0");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line = null;
                StringBuffer buffer = new StringBuffer();
                while((line=br.readLine())!=null){
                    buffer.append(line);
                }
                in.close();
                br.close();

                JSONArray rjsons = new JSONArray(buffer.toString());

                for (int i = 0; i < rjsons.length(); i++) {
                    JSONObject json = rjsons.getJSONObject(i);
                    Log.d("jsons", json.toString());
                    String email = "";
                    if (!json.isNull("userEmail")) email = json.getString("userEmail");
                    String name = "";
                    if (!json.isNull("userName")) name = json.getString("userName");
                    Integer isTeacher = 0;
                    if (!json.isNull("isTeacher")) isTeacher = json.getInt("isTeacher");
                    String mScore = "";
                    if (!json.isNull("mScore")) mScore = json.getString("mScore");
//                    if (email.contentEquals("zhangsan@qq.com")) mScore = "8a108c4e559c962c0155a0ae35250003";
                    UserInfo userInfo = new UserInfo(email, name, isTeacher, mScore);
                    students.add(userInfo);
                }
                Log.i("test", "st"+students.size()+"");
                return students;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        Log.i("test", "stfail");
        return null;
    }

    @Override
    public ArrayList<UserInfo> getTeachers() {

        ArrayList<UserInfo> teachers = new ArrayList<>();
        try{
            URL url = new URL(URLBase + "getUsers?isTeacher=1");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line = null;
                StringBuffer buffer = new StringBuffer();
                while((line=br.readLine())!=null){
                    buffer.append(line);
                }
                in.close();
                br.close();

                JSONArray rjsons = new JSONArray(buffer.toString());

                for (int i = 0; i < rjsons.length(); i++) {
                    JSONObject json = rjsons.getJSONObject(i);
                    String email = "";
                    if (!json.isNull("userEmail")) email = json.getString("userEmail");
                    String name = "";
                    if (!json.isNull("userName")) name = json.getString("userName");
                    Integer isTeacher = 0;
                    if (!json.isNull("isTeacher")) isTeacher = json.getInt("isTeacher");
                    String mScore = "";
                    if (!json.isNull("mScore")) mScore = json.getString("mScore");
                    UserInfo userInfo = new UserInfo(email, name, isTeacher, mScore);
                    teachers.add(userInfo);
                }
                return teachers;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
    * Midi info
    * */
    @Override
    public MInfo getMScore() {
        return curM;
    }
    @Override
    public MInfo getMScore(String mid) {

        try{
            URL url = new URL(URLBase + "getMScore?mid=" + mid);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line = null;
                StringBuffer buffer = new StringBuffer();
                while((line=br.readLine())!=null){
                    buffer.append(line);
                }
                in.close();
                br.close();

                JSONObject rjson = new JSONObject(buffer.toString());

                Log.i("minfo", rjson.toString());

                String creator = rjson.getString("creator");
                if (creator == null) creator = "";
                String createTime = rjson.getString("createtime");
                if (createTime == null) createTime = "";
                String detail = rjson.getString("detail");
                if (detail == null) detail = "";
                MInfo mInfo = new MInfo(mid, creator, createTime, detail);
                curM = mInfo;
                return mInfo;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    @Override
    public int createMScore(String email, String detail) {

        int status = -1;
        String mid = "";
        String time = (new Timestamp(System.currentTimeMillis())).toString();

        try {
            URL url = new URL(URLBase + "createMScore");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setConnectTimeout(3000);

            String data = "creator=" + email + "&createTime=" + time + "&detail=" + detail;

            Log.i("data", data);

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", data.getBytes().length + "");

            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());

//            conn.setConnectTimeout(3000);
//            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//            conn.setRequestMethod("POST");
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.connect();
//
//            JSONObject json = new JSONObject();
//            json.put("userEmail", email);
//            json.put("createTime", time);
//            json.put("detail", detail);
//            String jsonstr = json.toString();
//
//            OutputStream out = conn.getOutputStream();
//            BufferedOutputStream bos = new BufferedOutputStream(out);
//            byte[] bytes = jsonstr.getBytes("UTF-8");
//            bos.write(bytes);
//            bos.flush();
//            out.close();
//            bos.close();

            int t = conn.getResponseCode();
            Log.i("aicitel create", t+"");
            if (t == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                while ((str = br.readLine()) != null) {
                    buffer.append(str);
                }
                in.close();
                br.close();
                JSONObject rjson = new JSONObject(buffer.toString());

                status = rjson.getInt("status");
                if (status == 1) {
                    mid = rjson.getString("mid");
                    Log.i("mid", mid);
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        curM = new MInfo(mid, email, time, detail);

        return status;
    }




    @Override
    public int uploadFile() {
        int status = -1;

        return status;
    }

    @Override
    public int downloadFile() {
        int status = -1;

        return status;
    }


}
