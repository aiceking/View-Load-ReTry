package com.wxystatic.loadretrylibrary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruffian.library.RTextView;

import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by static on 2017/12/5/005.
 */

public class LoadReTryHelp {
    private static LoadReTryHelp loadReTryHelp;
    private HashMap<Activity,View> hashMap_activity_loadView;
    private HashMap<Fragment,View> hashMap_fragment_loadView;
    private HashMap<Activity,Boolean> hashMap_activity_toolbar,hashMap_activity_isFirstLoad,hashMap_activity_isSuccess;
    private HashMap<Fragment,Boolean> hashMap_fragment_toolbar,hashMap_fragment_isFirstLoad,hashMap_fragment_isSuccess;
    private LoadRetryConfig loadRetryConfig;
    private LoadReTryHelp(){
        hashMap_activity_loadView=new HashMap<>();
        hashMap_fragment_loadView=new HashMap<>();
        
        hashMap_activity_toolbar=new HashMap<>();
        hashMap_activity_isFirstLoad=new HashMap<>();
        hashMap_activity_isSuccess=new HashMap<>();

        hashMap_fragment_toolbar=new HashMap<>();
        hashMap_fragment_isFirstLoad=new HashMap<>();
        hashMap_fragment_isSuccess=new HashMap<>();
    }
    public void setLoadRetryConfig(LoadRetryConfig loadRetryConfig) {
        this.loadRetryConfig = loadRetryConfig;
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
    public void loadRetry(Fragment fragment,View root,@ColorRes int backgroundColor){

    }
    public void loadRetry(Activity activity,final LoadRetryListener loadRetryListener){
        if (!hashMap_activity_toolbar.containsKey(activity)) {
            hashMap_activity_toolbar.put(activity, false);
            hashMap_activity_isFirstLoad.put(activity,true);
            hashMap_activity_isSuccess.put(activity,false);
             ViewGroup mRoot= (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
             //判断是否有ToolBar
            isHaveToolbar(mRoot,activity);
            if (hashMap_activity_toolbar.get(activity)) {
                View loadView = LayoutInflater.from(activity).inflate(R.layout.loadretry_view, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.setMargins(0, getActionBarHeight(activity), 0, 0);
                loadView.setLayoutParams(lp);
                LinearLayout loadretry_parent=(LinearLayout)loadView.findViewById(R.id.loadretry_parent);
                GifImageView gifImageView=(GifImageView)loadView.findViewById(R.id.loadretry_gifview);
                TextView tv_error=(TextView)loadView.findViewById(R.id.loadretry_tv_error);
                RTextView tv_retry=(RTextView)loadView.findViewById(R.id.loadretry_tv_retry);
                tv_retry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadRetryListener.reTry();
                    }
                });
                if (loadRetryConfig!=null){
                    if (loadRetryConfig.getToolBarHeight()!=0){
                        lp.setMargins(0, dip2px(activity,loadRetryConfig.getToolBarHeight()), 0, 0);
                    }
                    if (loadRetryConfig.getBackground()!=0){
                        loadretry_parent.setBackgroundColor(activity.getResources().getColor(loadRetryConfig.getBackground()));
                    }
                    if (loadRetryConfig.getBtnNormal()!=0&&loadRetryConfig.getBtnPressed()!=0){
                        tv_retry.setBackgroundColorNormal(activity.getResources().getColor(loadRetryConfig.getBtnNormal()));
                        tv_retry.setBackgroundColorPressed(activity.getResources().getColor(loadRetryConfig.getBtnPressed()));
                    }
                    if (loadRetryConfig.getBtnRadius()!=0){
                        tv_retry.setCornerRadius(loadRetryConfig.getBtnRadius());
                    }
                    if (loadRetryConfig.getBtnTextColor()!=0){
                        tv_retry.setTextColor(activity.getResources().getColor(loadRetryConfig.getBtnTextColor()));
                    }
                    if (!TextUtils.isEmpty(loadRetryConfig.getBtnText())){
                        tv_retry.setText(loadRetryConfig.getBtnText());
                    }
                    if (loadRetryConfig.getErrorTextColor()!=0){
                        tv_error.setTextColor(activity.getResources().getColor(loadRetryConfig.getErrorTextColor()));
                    }
                    if (!TextUtils.isEmpty(loadRetryConfig.getErrorText())){
                        tv_error.setText(loadRetryConfig.getErrorText());
                    }
                }
            }else{

            }
        }
    }
    public void onLoadSuccess(Activity activity){

    }
    public void onLoadFailed(Activity activity){

    }
    public void clearLoadReTry(Activity activity){

    }

    public void onLoadSuccess(Fragment fragment){

    }
    public void onLoadFailed(Fragment fragment){

    }
    public void clearLoadReTry(Fragment fragment){

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
            hashMap_activity_toolbar.remove(activity);
            hashMap_activity_toolbar.put(activity, true);
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
    public static int dip2px(Activity activity, float dpValue) {
        final float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
