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
import com.wxystatic.loadretrylibrary.ShowReLoadViewListener;

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

public class FailedActivity extends AppCompatActivity implements LoadRetryListener {

    @BindView(R.id.linear_back)
    LinearLayout linearBack;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.loadretry_tv_retry)
    RTextView loadretryTvRetry;
    @BindView(R.id.loadretry_tv_retry_success)
    RTextView loadretryTvRetrySuccess;
    @BindView(R.id.loadretry_tv_retry_failed)
    RTextView loadretryTvRetryFailed;
    private boolean isSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failed);
        ButterKnife.bind(this);
    }

    @Override
    public void loadAndRetry() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Thread.sleep(3000);
                if (isSuccess) {
                    emitter.onNext(1);
                } else {
                    emitter.onNext(1 / 0);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                Toast.makeText(FailedActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                tvContent.setText(getResources().getString(R.string.large_text));
                LoadReTryHelp.getInstance().onLoadSuccess(FailedActivity.this, new ShowReLoadViewListener() {
                    @Override
                    public void colseReLoadView() {
                        Toast.makeText(FailedActivity.this, "2次加载成功", Toast.LENGTH_SHORT).show();
                        UsefulDialogHelp.getInstance().closeSmallLoadingDialog();
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(FailedActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                LoadReTryHelp.getInstance().onLoadFailed(FailedActivity.this, e.getMessage(), new ShowReLoadViewListener() {
                    @Override
                    public void colseReLoadView() {
                        Toast.makeText(FailedActivity.this, "2次加载失败", Toast.LENGTH_SHORT).show();
                        UsefulDialogHelp.getInstance().closeSmallLoadingDialog();
                    }
                });
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

    @Override
    protected void onDestroy() {
        LoadReTryHelp.getInstance().unRegister(this);
        super.onDestroy();
    }

    @OnClick({R.id.linear_back, R.id.loadretry_tv_retry, R.id.loadretry_tv_retry_success, R.id.loadretry_tv_retry_failed})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linear_back:
                finish();
                break;
            case R.id.loadretry_tv_retry:
                LoadReTryHelp.getInstance().register(this, this);
                LoadReTryHelp.getInstance().startLoad(this);
                break;
            case R.id.loadretry_tv_retry_success:
                isSuccess=true;
                break;
            case R.id.loadretry_tv_retry_failed:
                isSuccess=false;
                break;
        }
    }
}
