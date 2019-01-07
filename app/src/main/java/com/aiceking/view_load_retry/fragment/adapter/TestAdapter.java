package com.aiceking.view_load_retry.fragment.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.aiceking.view_load_retry.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class TestAdapter extends BaseQuickAdapter<String,BaseViewHolder>{
    public TestAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ((TextView)helper.getView(R.id.tv_text)).setText(item);
    }
}
