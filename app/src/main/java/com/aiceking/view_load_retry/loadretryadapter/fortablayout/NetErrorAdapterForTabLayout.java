package com.aiceking.view_load_retry.loadretryadapter.fortablayout;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiceking.view_load_retry.R;
import com.aiceking.viewloadretrylibrary.adapter.BaseLoadRetryAdapter;

public class NetErrorAdapterForTabLayout extends BaseLoadRetryAdapter{
    @Override
    public void onLoadStart(View view) {
    }

    @Override
    public void onFalied(View view, Object object) {
        ((TextView)view.findViewById(R.id.tv_text)).setText((String)object+"，点击重新加载");
    }

    @Override
    public void onSuccess(View view) {

    }

    @Override
    public int getCoverViewLayoutId() {
        return R.layout.retry_tablayout;
    }
}
