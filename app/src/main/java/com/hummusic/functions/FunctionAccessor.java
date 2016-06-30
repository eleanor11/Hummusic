package com.hummusic.functions;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Eleanor on 2016/6/15.
 */

public interface FunctionAccessor {

    /*
    * User info
    * */

    /*
    * input: email, password, isTeacher
    * output:
    *   1:success
    *   -1:fail
    * */
    int register(String email, String pwd, int isTeacher);

    /*
    * input: email, password
    * output:
    *   1:success
    *   -1:fail
    * */
    int login(String email, String pwd);

    int logout();

    /*
    * input: email, password
    * output: 
    *   1: success
    *   -1: fail
    */
    int resetPwd(String email, String password);

    /*
    * input: email, name
    * output:
    *   1:success
    *   -1:fail
    * */
    int setUserInfo(String email, String name);

    Boolean isLogin();
    /*
    * output: current user
    * */
    UserInfo getUserInfo();
    /*
    * input: email
    * output: user
    * */
    UserInfo getUserInfo(String email);
    /*
    * output: student list
    * */
    ArrayList<UserInfo> getStudents();
    /*
    * output: teacher list
    */
    ArrayList<UserInfo> getTeachers();

    /*
    * Midi info
    * */

    /*
    * output: current mscore
    * */
    MInfo getMScore();

    /*
    * input: mid
    * output: mscore
    * */
    MInfo getMScore(String mid);

    /*
    * input: email, detail
    * output:
    *   1:success
    *   -1:fail
    * */
    int createMScore(String email, String detail);


    int uploadFile();
    int downloadFile();

}
