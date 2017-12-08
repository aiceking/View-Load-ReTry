package com.wxystatic.gifloadretry;

import android.app.Application;

import com.wxystatic.loadretrylibrary.LoadReTryManager;
import com.wxystatic.loadretrylibrary.LoadRetryConfig;

/**
 * Created by static on 2017/12/6/006.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        LoadRetryConfig config=new LoadRetryConfig();
        config.setBackgroundColor(R.color.white);
        config.setBtnNormalColor(R.color.blue_normal);
        config.setBtnPressedColor(R.color.blue_press);
//        config.setBtnBorderColor(R.color.oringe_normal);
        config.setBtnRadius(10f);
//        config.setToolBarHeight(48);
        config.setBtnText("点击重新加载");
        config.setLoadText("测试加载2秒钟...");
        config.setBtnTextColor(R.color.white);
        config.setLoadAndErrorTextColor(R.color.gray);
        config.setGif(R.drawable.zhufaner);

        LoadReTryManager.getInstance().setLoadRetryConfig(config);
    }
}
