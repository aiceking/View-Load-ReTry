package com.aiceking.viewloadretrylibrary.manager;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aiceking.viewloadretrylibrary.adapter.BaseLoadRetryAdapter;
import com.aiceking.viewloadretrylibrary.coverview.CoverLayout;
import com.aiceking.viewloadretrylibrary.listener.LoadRetryListener;
import com.aiceking.viewloadretrylibrary.viewmodel.ContentViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

public class LoadRetryManager {
    private static LoadRetryManager loadRetryManager;
    private String Tag="LoadRetryManager";
    private HashMap<Class<? extends BaseLoadRetryAdapter>,BaseLoadRetryAdapter> baseLoadRetryAdapterHashMap;
    private HashMap<Integer,CoverLayout> coverLayoutHashMap;
    private HashMap<Integer,LoadRetryListener> loadRetryListenerHashMap;
    private HashMap<Integer,Boolean> viewLoadSuccess;
    public void addAdapter(BaseLoadRetryAdapter baseLoadRetryAdapter){
        baseLoadRetryAdapterHashMap.put(baseLoadRetryAdapter.getClass(),baseLoadRetryAdapter);
    }

    private LoadRetryManager(){
        baseLoadRetryAdapterHashMap=new HashMap<>();
        coverLayoutHashMap=new HashMap<>();
        loadRetryListenerHashMap=new HashMap<>();
        viewLoadSuccess=new HashMap<>();
    }
    public static LoadRetryManager getInstance(){
        if (loadRetryManager ==null){
            synchronized (LoadRetryManager.class){
                if (loadRetryManager ==null){
                    loadRetryManager =new LoadRetryManager();
                }
            }
        }
        return loadRetryManager;
    }
    public void register(final View view, final LoadRetryListener loadRetryListener) {
       if (!coverLayoutHashMap.containsKey(System.identityHashCode(view))){
           final CoverLayout coverLayout=new CoverLayout(view.getContext());
           coverLayout.setLoadRetryListener(loadRetryListener);
           viewLoadSuccess.put(System.identityHashCode(view),false);
           coverLayoutHashMap.put(System.identityHashCode(view),coverLayout);
           loadRetryListenerHashMap.put(System.identityHashCode(view),loadRetryListener);
       }
    }
    public void unRegister(final View view) {
        if (coverLayoutHashMap.containsKey(System.identityHashCode(view))){
            coverLayoutHashMap.remove(System.identityHashCode(view));
            viewLoadSuccess.remove(System.identityHashCode(view));
            loadRetryListenerHashMap.remove(System.identityHashCode(view));

        }
    }
    public void unRegister(final List<View> viewList) {
        if (viewList==null){
            Log.i(Tag,"View集合为空");
            return;
        }
        for (View view:viewList){
        if (coverLayoutHashMap.containsKey(System.identityHashCode(view))){
            coverLayoutHashMap.remove(System.identityHashCode(view));
            viewLoadSuccess.remove(System.identityHashCode(view));
            loadRetryListenerHashMap.remove(System.identityHashCode(view));

        }
        }
    }
    public void load(View view,Class<? extends BaseLoadRetryAdapter> cla){
        if (isRegister(view)){
                if (isHasAdapter(cla)) {
                    if (!viewLoadSuccess.get(System.identityHashCode(view))){
                    Cover(view, baseLoadRetryAdapterHashMap.get(cla).getCoverViewLayoutId());
                    coverLayoutHashMap.get(System.identityHashCode(view)).setLoadAdapter(cla);
                    baseLoadRetryAdapterHashMap.get(cla).onLoadStart(coverLayoutHashMap.get(System.identityHashCode(view)).getNowShowView());
                    loadRetryListenerHashMap.get(System.identityHashCode(view)).load();
                }
                }
        }
    }
    private boolean isHasAdapter(Class<? extends BaseLoadRetryAdapter> cla){
        boolean isHasAdapter=false;
        if (baseLoadRetryAdapterHashMap.containsKey(cla)){
            isHasAdapter=true;
        }else {
            Log.i(Tag,"未找到该adapter");
        }
        return isHasAdapter;
    }
    private boolean isRegister(View view){
        boolean isRegister=false;
        if (coverLayoutHashMap.containsKey(System.identityHashCode(view))){
            isRegister=true;
        }else {
            Log.i(Tag,"未进行register");
        }
        return isRegister;
    }
    public void onSuccess(View view){
        if (isRegister(view)){
            if (isHasAdapter(coverLayoutHashMap.get(System.identityHashCode(view)).getLoadAdapter())) {
                if (!viewLoadSuccess.get(System.identityHashCode(view))){
                viewLoadSuccess.put(System.identityHashCode(view),true);
                coverLayoutHashMap.get(System.identityHashCode(view)).onSuccess(coverLayoutHashMap.get(System.identityHashCode(view)).getLoadAdapter());
            }
            }
        }
    }
    public void onFailed(View view,Class<? extends BaseLoadRetryAdapter> cla,Object object){
        if (isRegister(view)){
            if (isHasAdapter(cla)) {
                if (!viewLoadSuccess.get(System.identityHashCode(view))){
                coverLayoutHashMap.get(System.identityHashCode(view)).onFailed(cla,object);
                }
        }
        }
    }
    public  void Cover(final View contentView, final int layout){
        ViewGroup mRoot = (ViewGroup) contentView.getParent();
        ViewGroup.LayoutParams lp;
        if (mRoot instanceof CoordinatorLayout){
            lp = (CoordinatorLayout.LayoutParams) contentView.getLayoutParams();
        }else if (mRoot instanceof CollapsingToolbarLayout){
            lp = (CollapsingToolbarLayout.LayoutParams) contentView.getLayoutParams();
        }else if (mRoot instanceof AppBarLayout){
            lp = (AppBarLayout.LayoutParams) contentView.getLayoutParams();
        }else if (mRoot instanceof NestedScrollView){
            lp = (NestedScrollView.LayoutParams) contentView.getLayoutParams();
        }else if (mRoot instanceof LinearLayout){
            lp = (LinearLayout.LayoutParams) contentView.getLayoutParams();
        }else if (mRoot instanceof RelativeLayout){
            lp = (RelativeLayout.LayoutParams) contentView.getLayoutParams();
        }else if (mRoot instanceof FrameLayout){
            lp = (FrameLayout.LayoutParams) contentView.getLayoutParams();
        }else {
            lp = (ViewGroup.LayoutParams) contentView.getLayoutParams();
        }
        int index=-1;
        for (int i = 0; i < mRoot.getChildCount(); i++) {
            if (contentView.getId() == mRoot.getChildAt(i).getId()) {
                index=i;
                break;
            }
        }
        if (index>=0){
            mRoot.removeView(contentView);
            mRoot.addView(coverLayoutHashMap.get(System.identityHashCode(contentView)),index,lp );
            coverLayoutHashMap.get(System.identityHashCode(contentView)).setAdapters(baseLoadRetryAdapterHashMap);
            coverLayoutHashMap.get(System.identityHashCode(contentView)).addContentView(contentView);
            coverLayoutHashMap.get(System.identityHashCode(contentView)).showView(layout);
        }
    }

}
