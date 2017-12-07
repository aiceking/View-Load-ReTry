package com.wxystatic.gifloadretry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.btn_activity_success)
    Button btnActivitySuccess;
    @BindView(R.id.btn_activity_failed)
    Button btnActivityFailed;
    @BindView(R.id.btn_fragment)
    Button btnFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btn_activity_success, R.id.btn_activity_failed, R.id.btn_fragment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_activity_success:
                startActivity(new Intent(this,SuccessActivity.class));
                break;
            case R.id.btn_activity_failed:
                startActivity(new Intent(this,FailedActivity.class));
                break;
            case R.id.btn_fragment:
                startActivity(new Intent(this,FragmentTestActivity.class));
                break;
        }
    }
}

