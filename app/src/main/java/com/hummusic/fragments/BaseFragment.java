package com.hummusic.fragments;

import android.app.Fragment;

import com.hummusic.widgets.TagSwitchListener;

/**
 * Created by bluemaple on 2016/6/26.
 */
public abstract class BaseFragment extends Fragment {
    /**
     * @param tagSwitchListener
     * set fragment tag switch listener (activity in this context)
     */
    abstract public void setTagSwitchListener(TagSwitchListener tagSwitchListener);

    /**
     * @return boolean which means activity could exit or not
     * logical action in fragment
     */
    abstract public boolean onBackPressed();

    /**
     * magic method
     * if do not make surface in record invisible, surface in listview will be shaded
     * //TODO solve it
     */
    abstract public void hideOrShowContent(int hideOrShow);
}
