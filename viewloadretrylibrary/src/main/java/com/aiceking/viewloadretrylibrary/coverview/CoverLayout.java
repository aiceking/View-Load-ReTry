package com.aiceking.viewloadretrylibrary.coverview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.aiceking.viewloadretrylibrary.adapter.BaseLoadRetryAdapter;
import com.aiceking.viewloadretrylibrary.listener.LoadRetryListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CoverLayout extends FrameLayout{
    private LayoutInflater mInflater;
    private List<View> viewList;
    private HashMap<Integer,View> viewHashMap;
    private LoadRetryListener loadRetryListener;
    private int layout;
    private View contentView;

    public void setLoadAdapter(Class<? extends BaseLoadRetryAdapter> loadAdapter) {
        this.loadAdapter = loadAdapter;
    }

    public Class<? extends BaseLoadRetryAdapter> getLoadAdapter() {
        return loadAdapter;
    }

    private Class<? extends BaseLoadRetryAdapter> loadAdapter;
    private HashMap<Class<? extends BaseLoadRetryAdapter>,BaseLoadRetryAdapter> baseLoadRetryAdapterHashMap;
    public CoverLayout(@NonNull Context context) {
        this(context,null);
    }

    public CoverLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }
    public CoverLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        viewList=new ArrayList<>();
        viewHashMap=new HashMap<>();
    }
    public void setLoadRetryListener(LoadRetryListener loadRetryListener) {
        this.loadRetryListener = loadRetryListener;
    }
    public void setAdapters(HashMap<Class<? extends BaseLoadRetryAdapter>,BaseLoadRetryAdapter> baseLoadRetryAdapterHashMap){
        this.baseLoadRetryAdapterHashMap=baseLoadRetryAdapterHashMap;
    }
    public void addContentView(View view){
        this.contentView=view;
        viewList.add(view);
        addView(contentView);
    }
    public void showView(int layout){
        if (!viewHashMap.containsKey(layout)){
            View view=mInflater.inflate(layout,this,false);
            viewHashMap.put(layout,view);
            viewList.add(view);
            addView(view);
        }
        for (View view:viewList){
            view.setVisibility(View.GONE);
        }
        this.layout=layout;
        viewHashMap.get(layout).setVisibility(View.VISIBLE);
    }
    public void onSuccess(Class<? extends BaseLoadRetryAdapter> cla){
        baseLoadRetryAdapterHashMap.get(cla).onSuccess(getNowShowView());
        contentView.setVisibility(View.VISIBLE);
    }
    public void onFailed(final Class<? extends BaseLoadRetryAdapter> cla,Object object){
        showView(baseLoadRetryAdapterHashMap.get(cla).getCoverViewLayoutId());
        baseLoadRetryAdapterHashMap.get(cla).onLoadStart(getNowShowView());
        baseLoadRetryAdapterHashMap.get(cla).onFalied(getNowShowView(),object);
        getNowShowView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reTry();
            }
        });
    }
    public View getNowShowView(){
        return viewHashMap.get(layout);
    }
    public void reTry(){
        if (loadRetryListener!=null){
            showView(baseLoadRetryAdapterHashMap.get(loadAdapter).getCoverViewLayoutId());
            loadRetryListener.reTry();
        }
    }

}
