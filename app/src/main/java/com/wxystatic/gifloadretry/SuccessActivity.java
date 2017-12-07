package com.wxystatic.gifloadretry;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.dialoglibrary.UsefulDialogHelp;
import com.ruffian.library.RTextView;
import com.wxystatic.loadretrylibrary.LoadReTryHelp;
import com.wxystatic.loadretrylibrary.LoadRetryListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SuccessActivity extends AppCompatActivity implements LoadRetryListener {

    @BindView(R.id.loadretry_tv_retry_success)
    RTextView loadretryTvRetrySuccess;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.linear_back)
    LinearLayout linearBack;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    @Override
    public void toDoAndreTry() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Thread.sleep(3000);

                    emitter.onNext(1);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                Toast.makeText(SuccessActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                tvContent.setText(getResources().getString(R.string.large_text));
                LoadReTryHelp.getInstance().onLoadSuccess(SuccessActivity.this);
                UsefulDialogHelp.getInstance().closeSmallLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });


    }

    @Override
    public void showReLoadView() {
        UsefulDialogHelp.getInstance().showSmallLoadingDialog(this, true);
    }


    /**
     * 模拟请求成功
     */
    @OnClick(R.id.loadretry_tv_retry_success)
    public void onLoadretryTvRetrySuccessClicked() {
        LoadReTryHelp.getInstance().loadRetry(this, this);
    }



    @OnClick({R.id.linear_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linear_back:
                finish();
                break;

        }
    }
    @Override
    protected void onDestroy() {
        LoadReTryHelp.getInstance().clearLoadReTry(this);
        super.onDestroy();
    }
}
