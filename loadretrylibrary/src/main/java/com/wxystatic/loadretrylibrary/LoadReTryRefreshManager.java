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

public class LoadReTryRefreshManager {
    private static LoadReTryRefreshManager loadReTryRefreshManager;
    private HashMap<Activity,LoadRetryRefreshListener> hashMap_activity_loadRetryListener;
    private HashMap<Activity,View> hashMap_activity_loadView;
    private HashMap<Activity,Boolean> hashMap_activity_isSuccess;

    private HashMap<Fragment,LoadRetryRefreshListener> hashMap_fragment_loadRetryListener;
    private HashMap<Fragment,View> hashMap_fragment_loadView;
    private HashMap<Fragment,FrameLayout> hashMap_fragment_parent_view;
    private HashMap<Fragment,Boolean> hashMap_fragment_isSuccess;
    private LoadRetryRefreshConfig loadRetryRefreshConfig;
    private LoadReTryRefreshManager(){
        hashMap_activity_loadRetryListener=new HashMap<>();
        hashMap_activity_loadView=new HashMap<>();
        hashMap_activity_isSuccess=new HashMap<>();

        hashMap_fragment_loadRetryListener=new HashMap<>();
        hashMap_fragment_loadView=new HashMap<>();
        hashMap_fragment_isSuccess=new HashMap<>();
        hashMap_fragment_parent_view=new HashMap<>();
    }
    public void setLoadRetryRefreshConfig(LoadRetryRefreshConfig loadRetryRefreshConfig) {
        this.loadRetryRefreshConfig = loadRetryRefreshConfig;
    }
    public static LoadReTryRefreshManager getInstance(){
        if (loadReTryRefreshManager ==null){
            synchronized (LoadReTryRefreshManager.class){
                if (loadReTryRefreshManager ==null){
                    loadReTryRefreshManager =new LoadReTryRefreshManager();
                }
            }
        }
        return loadReTryRefreshManager;
    }
    public void startLoad(Activity activity){
        if (hashMap_activity_loadRetryListener.containsKey(activity)){
            hashMap_activity_loadRetryListener.get(activity).loadAndRetry();
            if (hashMap_activity_isSuccess.get(activity)){
                hashMap_activity_loadRetryListener.get(activity).showRefreshView();
            }else{
                initLoadView(activity);
            }
        }
    }
    public void register(Activity activity,final LoadRetryRefreshListener loadRetryRefreshListener){
        if (!hashMap_activity_isSuccess.containsKey(activity)) {
            hashMap_activity_isSuccess.put(activity,false);
            hashMap_activity_loadRetryListener.put(activity, loadRetryRefreshListener);
             ViewGroup mRoot= (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
             ViewGroup mContentView=null;
             for (int i=0;i<((ViewGroup) mRoot.getChildAt(0)).getChildCount();i++){
                 View view=((ViewGroup) mRoot.getChildAt(0)).getChildAt(i);
                 if (view instanceof FrameLayout){
                     mContentView=(ViewGroup)view;
                     break;
                 }
             }
            View loadView = LayoutInflater.from(activity).inflate(R.layout.loadretry_view, null);
            if (mContentView!=null){
                mContentView.addView(loadView);
            }else{
                mRoot.addView(loadView);
            }
            hashMap_activity_loadView.put(activity,loadView);

            }
        }

    private void initLoadView(Activity activity) {
        View loadView=hashMap_activity_loadView.get(activity);
        LinearLayout loadretry_parent=(LinearLayout)loadView.findViewById(R.id.loadretry_parent);
        GifImageView gifImageView=(GifImageView)loadView.findViewById(R.id.loadretry_gifview);
        TextView tv_error=(TextView)loadView.findViewById(R.id.loadretry_tv_error);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(loadRetryRefreshConfig.getStartAnimTime());
        gifImageView.startAnimation(alphaAnimation);
        tv_error.startAnimation(alphaAnimation);
        gifImageView.setVisibility(View.VISIBLE);
        tv_error.setVisibility(View.VISIBLE);
        RTextView tv_retry=(RTextView)loadView.findViewById(R.id.loadretry_tv_retry);
        tv_retry.setVisibility(View.INVISIBLE);
        if (loadRetryRefreshConfig !=null){
            if (loadRetryRefreshConfig.getGif()!=0){
                setGifImageView(activity,gifImageView,false);
            }
            if (loadRetryRefreshConfig.getBackgroundColor()!=0){
                loadretry_parent.setBackgroundColor(activity.getResources().getColor(loadRetryRefreshConfig.getBackgroundColor()));
            }
            if (loadRetryRefreshConfig.getBtnBorderColor()!=0){
                tv_retry.setBorderColorNormal(activity.getResources().getColor(loadRetryRefreshConfig.getBtnBorderColor()));
                tv_retry.setBorderColorPressed(activity.getResources().getColor(loadRetryRefreshConfig.getBtnBorderColor()));
                tv_retry.setBorderWidthNormal(dip2px(activity,1));
                tv_retry.setBorderWidthPressed(dip2px(activity,1));
            }
            if (loadRetryRefreshConfig.getBtnNormalColor()!=0&& loadRetryRefreshConfig.getBtnPressedColor()!=0){
                tv_retry.setBackgroundColorNormal(activity.getResources().getColor(loadRetryRefreshConfig.getBtnNormalColor()));
                tv_retry.setBackgroundColorPressed(activity.getResources().getColor(loadRetryRefreshConfig.getBtnPressedColor()));
            }
            if (loadRetryRefreshConfig.getBtnRadius()!=null){
                tv_retry.setCornerRadius(loadRetryRefreshConfig.getBtnRadius());
            }
            if (loadRetryRefreshConfig.getBtnTextColor()!=0){
                tv_retry.setTextColor(activity.getResources().getColor(loadRetryRefreshConfig.getBtnTextColor()));
            }
            if (!TextUtils.isEmpty(loadRetryRefreshConfig.getBtnText())){
                tv_retry.setText(loadRetryRefreshConfig.getBtnText());
            }
            if (loadRetryRefreshConfig.getLoadAndErrorTextColor()!=0){
                tv_error.setTextColor(activity.getResources().getColor(loadRetryRefreshConfig.getLoadAndErrorTextColor()));
            }
            if (!TextUtils.isEmpty(loadRetryRefreshConfig.getLoadText())){
                tv_error.setText(loadRetryRefreshConfig.getLoadText());
            }
        }
    }

    public void onLoadSuccess(Activity activity,ShowRefreshViewListener showRefreshViewListener){
        if (hashMap_activity_loadView.containsKey(activity)){
            if (!hashMap_activity_isSuccess.get(activity)){
       hashMap_activity_isSuccess.remove(activity);
       hashMap_activity_isSuccess.put(activity,true);
       View loadView=hashMap_activity_loadView.get(activity);
            AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
            alphaAnimation.setDuration(loadRetryRefreshConfig.getEndAnimTime());
                loadView.startAnimation(alphaAnimation);
                loadView.setVisibility(View.GONE);
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
                    if (loadRetryRefreshConfig !=null){
                        if (!TextUtils.isEmpty(loadRetryRefreshConfig.getLoadText())){
                            tv_error.setText(loadRetryRefreshConfig.getLoadText());
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
            if (loadRetryRefreshConfig !=null){
                if (loadRetryRefreshConfig.getGif()!=0){
                    gifFromResource = new GifDrawable( activity.getResources(), loadRetryRefreshConfig.getGif());
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
        }
    }




    public void register(Fragment fragment,View rootView,final LoadRetryRefreshListener loadRetryRefreshListener){
        if (!hashMap_fragment_isSuccess.containsKey(fragment)) {
            hashMap_fragment_isSuccess.put(fragment,false);
            hashMap_fragment_loadRetryListener.put(fragment, loadRetryRefreshListener);
            ViewGroup mRoot= (ViewGroup) rootView;
            ViewGroup mContentView=null;
            for (int i=0;i<mRoot.getChildCount();i++){
                View view=mRoot.getChildAt(i);
                if (view instanceof FrameLayout){
                    mContentView=(ViewGroup)view;
                    break;
                }
            }
            View loadView = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.loadretry_view, null);
            if (mContentView!=null){
                mContentView.addView(loadView);
            }else{
                mRoot.addView(loadView);
            }
            hashMap_fragment_loadView.put(fragment,loadView);
        }
    }
    public void startLoad(Fragment fragment){
        if (hashMap_fragment_loadRetryListener.containsKey(fragment)){
            hashMap_fragment_loadRetryListener.get(fragment).loadAndRetry();
            if (hashMap_fragment_isSuccess.get(fragment)){
                hashMap_fragment_loadRetryListener.get(fragment).showRefreshView();
            }else{
                initLoadView(fragment);
            }
        }
    }
    private void initLoadView(Fragment fragment) {
        View loadView=hashMap_fragment_loadView.get(fragment);
        LinearLayout loadretry_parent=(LinearLayout)loadView.findViewById(R.id.loadretry_parent);
        GifImageView gifImageView=(GifImageView)loadView.findViewById(R.id.loadretry_gifview);
        TextView tv_error=(TextView)loadView.findViewById(R.id.loadretry_tv_error);
        RTextView tv_retry=(RTextView)loadView.findViewById(R.id.loadretry_tv_retry);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(loadRetryRefreshConfig.getStartAnimTime());
        gifImageView.startAnimation(alphaAnimation);
        tv_error.startAnimation(alphaAnimation);
        gifImageView.setVisibility(View.VISIBLE);
        tv_error.setVisibility(View.VISIBLE);
        tv_retry.setVisibility(View.INVISIBLE);
        if (loadRetryRefreshConfig !=null){
            if (loadRetryRefreshConfig.getGif()!=0){
                setGifImageView(fragment.getActivity(),gifImageView,false);
            }
            if (loadRetryRefreshConfig.getBackgroundColor()!=0){
                loadretry_parent.setBackgroundColor(fragment.getActivity().getResources().getColor(loadRetryRefreshConfig.getBackgroundColor()));
            }
            if (loadRetryRefreshConfig.getBtnBorderColor()!=0){
                tv_retry.setBorderColorNormal(fragment.getActivity().getResources().getColor(loadRetryRefreshConfig.getBtnBorderColor()));
                tv_retry.setBorderColorPressed(fragment.getActivity().getResources().getColor(loadRetryRefreshConfig.getBtnBorderColor()));
                tv_retry.setBorderWidthNormal(dip2px(fragment.getActivity(),1));
                tv_retry.setBorderWidthPressed(dip2px(fragment.getActivity(),1));
            }
            if (loadRetryRefreshConfig.getBtnNormalColor()!=0&& loadRetryRefreshConfig.getBtnPressedColor()!=0){
                tv_retry.setBackgroundColorNormal(fragment.getActivity().getResources().getColor(loadRetryRefreshConfig.getBtnNormalColor()));
                tv_retry.setBackgroundColorPressed(fragment.getActivity().getResources().getColor(loadRetryRefreshConfig.getBtnPressedColor()));
            }
            if (loadRetryRefreshConfig.getBtnRadius()!=null){
                tv_retry.setCornerRadius(loadRetryRefreshConfig.getBtnRadius());
            }
            if (loadRetryRefreshConfig.getBtnTextColor()!=0){
                tv_retry.setTextColor(fragment.getActivity().getResources().getColor(loadRetryRefreshConfig.getBtnTextColor()));
            }
            if (!TextUtils.isEmpty(loadRetryRefreshConfig.getBtnText())){
                tv_retry.setText(loadRetryRefreshConfig.getBtnText());
            }
            if (loadRetryRefreshConfig.getLoadAndErrorTextColor()!=0){
                tv_error.setTextColor(fragment.getActivity().getResources().getColor(loadRetryRefreshConfig.getLoadAndErrorTextColor()));
            }
            if (!TextUtils.isEmpty(loadRetryRefreshConfig.getLoadText())){
                tv_error.setText(loadRetryRefreshConfig.getLoadText());
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
                alphaAnimation.setDuration(loadRetryRefreshConfig.getEndAnimTime());
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
                    if (loadRetryRefreshConfig !=null){
                        if (!TextUtils.isEmpty(loadRetryRefreshConfig.getLoadText())){
                            tv_error.setText(loadRetryRefreshConfig.getLoadText());
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
        }
    }

    public  int dip2px(Activity activity, float dpValue) {
        final float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
