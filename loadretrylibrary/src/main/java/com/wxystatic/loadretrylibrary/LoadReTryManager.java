package com.wxystatic.loadretrylibrary;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruffian.library.RTextView;

import java.io.IOException;
import java.util.HashMap;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by static on 2017/12/5/005.
 */

public class LoadReTryManager {
    private static LoadReTryManager loadReTryManager;
    private HashMap<Activity,LoadRetryListener> hashMap_activity_loadRetryListener;
    private HashMap<Activity,View> hashMap_activity_loadView;
    private HashMap<Activity,Boolean> hashMap_activity_toolbar,hashMap_activity_isSuccess;

    private HashMap<Fragment,LoadRetryListener> hashMap_fragment_loadRetryListener;
    private HashMap<Fragment,View> hashMap_fragment_loadView;
    private HashMap<Fragment,FrameLayout> hashMap_fragment_parent_view;
    private HashMap<Fragment,Boolean> hashMap_fragment_toolbar,hashMap_fragment_isSuccess;
    private LoadRetryConfig loadRetryConfig;
    private LoadReTryManager(){
        hashMap_activity_loadRetryListener=new HashMap<>();
        hashMap_activity_loadView=new HashMap<>();
        hashMap_activity_toolbar=new HashMap<>();
        hashMap_activity_isSuccess=new HashMap<>();

        hashMap_fragment_loadRetryListener=new HashMap<>();
        hashMap_fragment_loadView=new HashMap<>();
        hashMap_fragment_toolbar=new HashMap<>();
        hashMap_fragment_isSuccess=new HashMap<>();
        hashMap_fragment_parent_view=new HashMap<>();
    }
    public void setLoadRetryConfig(LoadRetryConfig loadRetryConfig) {
        this.loadRetryConfig = loadRetryConfig;
    }
    public static LoadReTryManager getInstance(){
        if (loadReTryManager ==null){
            synchronized (LoadReTryManager.class){
                if (loadReTryManager ==null){
                    loadReTryManager =new LoadReTryManager();
                }
            }
        }
        return loadReTryManager;
    }
    public void startLoad(Activity activity){
        if (hashMap_activity_loadRetryListener.containsKey(activity)){
            hashMap_activity_loadRetryListener.get(activity).loadAndRetry();
        }
    }
    public void register(Activity activity,final LoadRetryListener loadRetryListener){
        
        if (!hashMap_activity_toolbar.containsKey(activity)) {
            hashMap_activity_toolbar.put(activity, false);
            hashMap_activity_isSuccess.put(activity,false);
            hashMap_activity_loadRetryListener.put(activity,loadRetryListener);
             ViewGroup mRoot= (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
             //判断是否有ToolBar
            isHaveToolbar(mRoot,activity);
            View loadView = LayoutInflater.from(activity).inflate(R.layout.loadretry_view, null);
            mRoot.addView(loadView);
            hashMap_activity_loadView.put(activity,loadView);
            if (hashMap_activity_toolbar.get(activity)) {
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.setMargins(0, dip2px(activity,56), 0, 0);
                hashMap_activity_loadView.get(activity).setLayoutParams(lp);
            }
                initLoadView(activity);
            }else{
            hashMap_activity_loadRetryListener.remove(activity);
            hashMap_activity_loadRetryListener.put(activity,loadRetryListener);
            if (hashMap_activity_isSuccess.get(activity)){
                hashMap_activity_loadRetryListener.get(activity).showRefreshView();
            }else{
                initLoadView(activity);
            }
            }
        }

    private void initLoadView(Activity activity) {
        View loadView=hashMap_activity_loadView.get(activity);
        LinearLayout loadretry_parent=(LinearLayout)loadView.findViewById(R.id.loadretry_parent);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(500);
        loadretry_parent.startAnimation(alphaAnimation);
        GifImageView gifImageView=(GifImageView)loadView.findViewById(R.id.loadretry_gifview);
        TextView tv_error=(TextView)loadView.findViewById(R.id.loadretry_tv_error);
        RTextView tv_retry=(RTextView)loadView.findViewById(R.id.loadretry_tv_retry);
        tv_retry.setVisibility(View.INVISIBLE);
        if (loadRetryConfig!=null){
            if (loadRetryConfig.getGif()!=0){
                setGifImageView(activity,gifImageView,false);
            }
            if (loadRetryConfig.getToolBarHeight()!=0){
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.setMargins(0, dip2px(activity,loadRetryConfig.getToolBarHeight()), 0, 0);
                loadView.setLayoutParams(lp);
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
            if (loadRetryConfig.getLoadAndErrorTextColor()!=0){
                tv_error.setTextColor(activity.getResources().getColor(loadRetryConfig.getLoadAndErrorTextColor()));
            }
            if (!TextUtils.isEmpty(loadRetryConfig.getLoadText())){
                tv_error.setText(loadRetryConfig.getLoadText());
            }
        }
    }

    public void onLoadSuccess(Activity activity,ShowRefreshViewListener showRefreshViewListener){
        if (hashMap_activity_loadView.containsKey(activity)){
            if (!hashMap_activity_isSuccess.get(activity)){
       hashMap_activity_isSuccess.remove(activity);
       hashMap_activity_isSuccess.put(activity,true);
       View loadView=hashMap_activity_loadView.get(activity);
       LinearLayout loadretry_parent=(LinearLayout)loadView.findViewById(R.id.loadretry_parent);
            AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
            alphaAnimation.setDuration(500);
            loadretry_parent.startAnimation(alphaAnimation);
            loadretry_parent.setVisibility(View.GONE);
   }else{
                showRefreshViewListener.colseRefreshView();
            }
        }
    }
    public void onLoadFailed(final Activity activity, String errorText,ShowRefreshViewListener showRefreshViewListener){

        if (hashMap_activity_loadView.containsKey(activity)){
            if (!hashMap_activity_isSuccess.get(activity)){
            View loadView=hashMap_activity_loadView.get(activity);
            LinearLayout loadretry_parent=(LinearLayout)loadView.findViewById(R.id.loadretry_parent);
            final GifImageView gifImageView=(GifImageView)loadView.findViewById(R.id.loadretry_gifview);
            final TextView tv_error=(TextView)loadView.findViewById(R.id.loadretry_tv_error);
            final RTextView tv_retry=(RTextView)loadView.findViewById(R.id.loadretry_tv_retry);
            setGifImageView(activity,gifImageView,true);
            tv_error.setText(errorText);
            tv_retry.setVisibility(View.VISIBLE);
            tv_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (loadRetryConfig!=null){
                        if (!TextUtils.isEmpty(loadRetryConfig.getLoadText())){
                            tv_error.setText(loadRetryConfig.getLoadText());
                        }
                    }
                    setGifImageView(activity,gifImageView,false);
                    tv_retry.setVisibility(View.INVISIBLE);
                    hashMap_activity_loadRetryListener.get(activity).loadAndRetry();
                }
            });
        }else{
                showRefreshViewListener.colseRefreshView();
            }
        }
    }

    private void setGifImageView(Activity activity,GifImageView gifImageView, boolean b) {
        GifDrawable gifFromResource = null;
        try {
            if (loadRetryConfig!=null){
                if (loadRetryConfig.getGif()!=0){
                    gifFromResource = new GifDrawable( activity.getResources(), loadRetryConfig.getGif());
                    if (b){
                    gifFromResource.stop();
                    }
                    gifImageView.setImageDrawable(gifFromResource);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unRegister(Activity activity){
        if (hashMap_activity_loadRetryListener.containsKey(activity)){
        hashMap_activity_loadRetryListener.remove(activity);
          hashMap_activity_isSuccess.remove(activity);
          hashMap_activity_loadView.remove(activity);
          hashMap_activity_toolbar.remove(activity);
        }
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
    public void register(Fragment fragment,View rootView,final LoadRetryListener loadRetryListener){
        if (!hashMap_fragment_toolbar.containsKey(fragment)) {
            hashMap_fragment_toolbar.put(fragment, false);
            hashMap_fragment_isSuccess.put(fragment,false);
            hashMap_fragment_loadRetryListener.put(fragment,loadRetryListener);
            //判断是否有ToolBar
            isHaveToolbar(rootView,fragment);
            View loadView = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.loadretry_view, null);
            ((FrameLayout)rootView).addView(loadView);
            hashMap_fragment_loadView.put(fragment,loadView);
            if (hashMap_fragment_toolbar.get(fragment)) {
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.setMargins(0, dip2px(fragment.getActivity(),56), 0, 0);
                hashMap_fragment_loadView.get(fragment).setLayoutParams(lp);
            }
            initLoadView(fragment);
        }else{
            hashMap_fragment_loadRetryListener.remove(fragment);
            hashMap_fragment_loadRetryListener.put(fragment,loadRetryListener);
            if (hashMap_fragment_isSuccess.get(fragment)){
                hashMap_fragment_loadRetryListener.get(fragment).showRefreshView();
            }else{
                initLoadView(fragment);
            }
        }
    }
    public void startLoad(Fragment fragment){
        if (hashMap_fragment_loadRetryListener.containsKey(fragment)){
            hashMap_fragment_loadRetryListener.get(fragment).loadAndRetry();
        }
    }
    private void initLoadView(Fragment fragment) {
        View loadView=hashMap_fragment_loadView.get(fragment);
        LinearLayout loadretry_parent=(LinearLayout)loadView.findViewById(R.id.loadretry_parent);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(500);
        loadretry_parent.startAnimation(alphaAnimation);
        GifImageView gifImageView=(GifImageView)loadView.findViewById(R.id.loadretry_gifview);
        TextView tv_error=(TextView)loadView.findViewById(R.id.loadretry_tv_error);
        RTextView tv_retry=(RTextView)loadView.findViewById(R.id.loadretry_tv_retry);
        tv_retry.setVisibility(View.INVISIBLE);
        if (loadRetryConfig!=null){
            if (loadRetryConfig.getGif()!=0){
                setGifImageView(fragment.getActivity(),gifImageView,false);
            }
            if (loadRetryConfig.getToolBarHeight()!=0){
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.setMargins(0, dip2px(fragment.getActivity(),loadRetryConfig.getToolBarHeight()), 0, 0);
                loadView.setLayoutParams(lp);
            }
            if (loadRetryConfig.getBackground()!=0){
                loadretry_parent.setBackgroundColor(fragment.getActivity().getResources().getColor(loadRetryConfig.getBackground()));
            }
            if (loadRetryConfig.getBtnNormal()!=0&&loadRetryConfig.getBtnPressed()!=0){
                tv_retry.setBackgroundColorNormal(fragment.getActivity().getResources().getColor(loadRetryConfig.getBtnNormal()));
                tv_retry.setBackgroundColorPressed(fragment.getActivity().getResources().getColor(loadRetryConfig.getBtnPressed()));
            }
            if (loadRetryConfig.getBtnRadius()!=0){
                tv_retry.setCornerRadius(loadRetryConfig.getBtnRadius());
            }
            if (loadRetryConfig.getBtnTextColor()!=0){
                tv_retry.setTextColor(fragment.getActivity().getResources().getColor(loadRetryConfig.getBtnTextColor()));
            }
            if (!TextUtils.isEmpty(loadRetryConfig.getBtnText())){
                tv_retry.setText(loadRetryConfig.getBtnText());
            }
            if (loadRetryConfig.getLoadAndErrorTextColor()!=0){
                tv_error.setTextColor(fragment.getActivity().getResources().getColor(loadRetryConfig.getLoadAndErrorTextColor()));
            }
            if (!TextUtils.isEmpty(loadRetryConfig.getLoadText())){
                tv_error.setText(loadRetryConfig.getLoadText());
            }
        }
    }
    public void onLoadSuccess(Fragment fragment,ShowRefreshViewListener showRefreshViewListener){
        if (hashMap_fragment_loadView.containsKey(fragment)){
            if (!hashMap_fragment_isSuccess.get(fragment)){
                hashMap_fragment_isSuccess.remove(fragment);
                hashMap_fragment_isSuccess.put(fragment,true);
                View loadView=hashMap_fragment_loadView.get(fragment);
                LinearLayout loadretry_parent=(LinearLayout)loadView.findViewById(R.id.loadretry_parent);
                AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
                alphaAnimation.setDuration(500);
                loadretry_parent.startAnimation(alphaAnimation);
                loadretry_parent.setVisibility(View.GONE);
            }else{
                showRefreshViewListener.colseRefreshView();
            }
        }
    }
    public void onLoadFailed(final Fragment fragment,String errorText,ShowRefreshViewListener showRefreshViewListener){
        if (hashMap_fragment_loadView.containsKey(fragment)){
            if (!hashMap_fragment_isSuccess.get(fragment)){
                View loadView=hashMap_fragment_loadView.get(fragment);
            LinearLayout loadretry_parent=(LinearLayout)loadView.findViewById(R.id.loadretry_parent);
            final GifImageView gifImageView=(GifImageView)loadView.findViewById(R.id.loadretry_gifview);
            final TextView tv_error=(TextView)loadView.findViewById(R.id.loadretry_tv_error);
            final RTextView tv_retry=(RTextView)loadView.findViewById(R.id.loadretry_tv_retry);
            setGifImageView(fragment.getActivity(),gifImageView,true);
            tv_error.setText(errorText);
            tv_retry.setVisibility(View.VISIBLE);
            tv_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (loadRetryConfig!=null){
                        if (!TextUtils.isEmpty(loadRetryConfig.getLoadText())){
                            tv_error.setText(loadRetryConfig.getLoadText());
                        }
                    }
                    setGifImageView(fragment.getActivity(),gifImageView,false);
                    tv_retry.setVisibility(View.INVISIBLE);
                    hashMap_fragment_loadRetryListener.get(fragment).loadAndRetry();
                }
            });
        }else{
                showRefreshViewListener.colseRefreshView();
            }
        }
    }
    public void unRegister(Fragment fragment){
        if (hashMap_fragment_loadRetryListener.containsKey(fragment)){
            hashMap_fragment_loadRetryListener.remove(fragment);
            hashMap_fragment_isSuccess.remove(fragment);
            hashMap_fragment_loadView.remove(fragment);
            hashMap_fragment_toolbar.remove(fragment);
        }
    }
    private void isHaveToolbar(View view ,Fragment fragment) {
        if (view instanceof Toolbar) {
            hashMap_fragment_toolbar.remove(fragment);
            hashMap_fragment_toolbar.put(fragment, true);
            return;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                isHaveToolbar(child,fragment);
            }
        }
    }
    public static int dip2px(Activity activity, float dpValue) {
        final float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
