package com.hummusic;

import android.os.Bundle;

import com.hummusic.widgets.GifView;

/**
 * Created by bluemaple on 2016/6/26.
 */
public class testGif extends BaseActivity{
    private GifView gif1, gif2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_test);
        gif1 = (GifView) findViewById(R.id.gif1);
        // 设置背景gif图片资源
        gif1.setMovieResource(R.drawable.rotate);
        // 设置暂停
        // gif2.setPaused(true);

    }
}
