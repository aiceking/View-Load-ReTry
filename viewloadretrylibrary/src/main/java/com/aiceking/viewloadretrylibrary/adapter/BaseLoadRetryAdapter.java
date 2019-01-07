package com.aiceking.viewloadretrylibrary.adapter;

import android.view.View;

import com.aiceking.viewloadretrylibrary.coverview.CoverLayout;

public abstract class BaseLoadRetryAdapter implements loadRtryAdapterInterface{

    public abstract void onLoadStart(View view);
    public abstract void onFalied(View view,Object object);
    public abstract void onSuccess(View view);
}
