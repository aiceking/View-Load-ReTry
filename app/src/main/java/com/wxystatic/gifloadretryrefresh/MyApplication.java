package com.wxystatic.gifloadretryrefresh;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.wxystatic.loadretrylibrary.LoadReTryRefreshManager;
import com.wxystatic.loadretrylibrary.LoadRetryRefreshConfig;

/**
 * Created by static on 2017/12/6/006.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        LoadRetryRefreshConfig config=new LoadRetryRefreshConfig();
        config.setBackgroundColor(R.color.white);
        config.setBtnNormalColor(R.color.blue_normal);
        config.setBtnPressedColor(R.color.blue_press);
//        config.setBtnBorderColor(R.color.oringe_normal);
        config.setBtnRadius(10f);
        config.setBtnText("点击重新加载");
        config.setLoadText("测试加载2秒钟...");
        config.setBtnTextColor(R.color.white);
        config.setLoadAndErrorTextColor(R.color.gray);
        config.setGif(R.drawable.zhufaner);

        LoadReTryRefreshManager.getInstance().setLoadRetryRefreshConfig(config);
    }
}
