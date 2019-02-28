package com.aiceking.view_load_retry.application;

import android.app.Application;

import com.aiceking.view_load_retry.loadretryadapter.foractivity.LoadAdapterForActivity;
import com.aiceking.view_load_retry.loadretryadapter.forfragment.LoadAdapterForFragment;
import com.aiceking.view_load_retry.loadretryadapter.fortablayout.LoadAdapterForTabLayout;
import com.aiceking.view_load_retry.loadretryadapter.foractivity.NetErrorAdapterForActivity;
import com.aiceking.view_load_retry.loadretryadapter.forfragment.NetErrorAdapterForFragment;
import com.aiceking.view_load_retry.loadretryadapter.forimageview.NetErrorAdapterForImageView;
import com.aiceking.view_load_retry.loadretryadapter.forimageview.LoadAdapterForImageView;
import com.aiceking.view_load_retry.loadretryadapter.fortablayout.NetErrorAdapterForTabLayout;
import com.aiceking.viewloadretrylibrary.manager.LoadRetryManager;
import com.squareup.leakcanary.LeakCanary;
/**测试提交*/
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        LoadRetryManager.getInstance().addAdapter(new LoadAdapterForImageView());
        LoadRetryManager.getInstance().addAdapter(new NetErrorAdapterForImageView());
        LoadRetryManager.getInstance().addAdapter(new LoadAdapterForTabLayout());
        LoadRetryManager.getInstance().addAdapter(new NetErrorAdapterForTabLayout());
        LoadRetryManager.getInstance().addAdapter(new LoadAdapterForFragment());
        LoadRetryManager.getInstance().addAdapter(new NetErrorAdapterForFragment());
        LoadRetryManager.getInstance().addAdapter(new LoadAdapterForActivity());
        LoadRetryManager.getInstance().addAdapter(new NetErrorAdapterForActivity());
    }
}
