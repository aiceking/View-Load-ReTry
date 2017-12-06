package com.wxystatic.gifloadretry;

import android.app.Application;

import com.wxystatic.loadretrylibrary.LoadReTryHelp;
import com.wxystatic.loadretrylibrary.LoadRetryConfig;

/**
 * Created by static on 2017/12/6/006.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        LoadRetryConfig config=new LoadRetryConfig();
        config.setBackground(R.color.white);
        config.setBtnNormal(R.color.oringe_normal);
        config.setBtnPressed(R.color.oringe_press);
        config.setBtnRadius(4f);
        config.setBtnText("点击重新加载");
        config.setLoadText("加载中...");
        config.setBtnTextColor(R.color.black);
        config.setErrorTextColor(R.color.black);
        config.setGif(R.drawable.test);
        LoadReTryHelp.getInstance().setLoadRetryConfig(config);
    }
}
