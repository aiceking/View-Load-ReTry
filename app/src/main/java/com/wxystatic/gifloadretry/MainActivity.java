package com.wxystatic.gifloadretry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.wxystatic.loadretrylibrary.LoadReTryHelp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_theme)
    Button btnTheme;
    @BindView(R.id.btn_self_toolbar)
    Button btnSelfToolbar;
    @BindView(R.id.btn_no_toolbar)
    Button btnNoToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_theme, R.id.btn_self_toolbar, R.id.btn_no_toolbar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_theme:
                startActivity(new Intent(this,ThemeToolBarActivity.class));
                break;
            case R.id.btn_self_toolbar:
                startActivity(new Intent(this,SelfToolBarActivity.class));
                break;
            case R.id.btn_no_toolbar:
                startActivity(new Intent(this,NoToolBarActivity.class));

                break;
        }
    }
}

