package com.aiceking.view_load_retry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.aiceking.view_load_retry.activity.LoadFragmentActivity;
import com.aiceking.view_load_retry.activity.NormalActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.btn_normal)
    Button btnNormal;
    @BindView(R.id.btn_fix)
    Button btnFix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }


    @OnClick({R.id.btn_normal, R.id.btn_fix})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_normal:
                startActivity(new Intent(this, NormalActivity.class));
                break;
            case R.id.btn_fix:
                startActivity(new Intent(this, LoadFragmentActivity.class));
                break;
        }
    }
}
