package com.hummusic.widgets;

/**
 * Created by bluemaple on 2016/6/26.
 */
public interface TagSwitchListener {
    void addOrShowFragment(int index);
    void enterMainActivity();
    void enterMainActivityTeacher();
    void viewProfile();
    void sendToastSign(String sign);

}
