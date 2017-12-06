package com.wxystatic.gifloadretry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wxystatic.loadretrylibrary.LoadReTryHelp;
import com.wxystatic.loadretrylibrary.LoadRetryListener;

public class NoToolBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_tool_bar);
        LoadReTryHelp.getInstance().loadRetry(this, new LoadRetryListener() {
            @Override
            public void toDoAndreTry() {

            }
            @Override
            public void showReLoadView() {

            }
        });

    }
}
