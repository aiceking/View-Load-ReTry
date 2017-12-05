package com.wxystatic.loadretrylibrary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by static on 2017/12/5/005.
 */

public class LoadReTryHelp {
    private static LoadReTryHelp loadReTryHelp;
    private HashMap<Activity,Boolean> hashMap;
    private Toolbar toolbar;
    private LoadReTryHelp(){
        hashMap=new HashMap<>();
    }
    public static LoadReTryHelp getInstance(){
        if (loadReTryHelp==null){
            synchronized (LoadReTryHelp.class){
                if (loadReTryHelp==null){
                    loadReTryHelp=new LoadReTryHelp();
                }
            }
        }
        return loadReTryHelp;
    }
    public void loadRetry(Activity activity){
        if (!hashMap.containsKey(activity)) {
            hashMap.put(activity, false);
             ViewGroup mRoot= (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
            isHaveToolbar(mRoot,activity);
            if (hashMap.get(activity)) {
                TextView tv = new TextView(activity);
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.setMargins(0, getActionBarHeight(activity), 0, 0);
                tv.setLayoutParams(lp);
                tv.setBackgroundColor(Color.RED);
                tv.setGravity(Gravity.CENTER);
                tv.setText("测试中...");
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(30);
                mRoot.addView(tv);
            }else{
                TextView tv = new TextView(activity);
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.setMargins(0, 50, 0, 0);
                tv.setLayoutParams(lp);
                tv.setBackgroundColor(Color.RED);
                tv.setGravity(Gravity.CENTER);
                tv.setText("测试中...");
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(30);
                mRoot.addView(tv);
            }
        }
    }
    @TargetApi(14)
    private int getActionBarHeight(Activity activity) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            TypedValue tv = new TypedValue();
            activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
            result = activity.getResources().getDimensionPixelSize(tv.resourceId);
        }
        return result;
    }
    private void isHaveToolbar(View view ,Activity activity) {
        if (view instanceof Toolbar) {
            hashMap.remove(activity);
            hashMap.put(activity, true);
            return;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                 isHaveToolbar(child,activity);
            }
        }
    }
}
