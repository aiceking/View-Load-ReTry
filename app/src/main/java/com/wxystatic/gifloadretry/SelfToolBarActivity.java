package com.wxystatic.gifloadretry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wxystatic.loadretrylibrary.LoadReTryHelp;

public class SelfToolBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_tool_bar);
        LoadReTryHelp.getInstance().loadRetry(this);

    }
}
