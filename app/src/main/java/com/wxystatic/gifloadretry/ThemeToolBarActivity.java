package com.wxystatic.gifloadretry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wxystatic.loadretrylibrary.LoadReTryHelp;

public class ThemeToolBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadretry_view);
        LoadReTryHelp.getInstance().loadRetry(this,R.color.white);

    }
}
