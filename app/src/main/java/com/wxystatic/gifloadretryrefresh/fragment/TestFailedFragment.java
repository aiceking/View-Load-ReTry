package com.wxystatic.gifloadretryrefresh.fragment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.lazyfragmentlibrary.LazyBaseFragment;
import com.wxystatic.gifloadretryrefresh.R;
import com.wxystatic.loadretrylibrary.LoadReTryRefreshManager;
import com.wxystatic.loadretrylibrary.LoadRetryRefreshListener;
import com.wxystatic.loadretrylibrary.ShowRefreshViewListener;

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

/**
 * Created by radio on 2017/10/30.
 */

public class TestFailedFragment extends LazyBaseFragment implements LoadRetryRefreshListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.tv_success)
    TextView tvSuccess;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private AppCompatActivity activity;
    private boolean isFailed;
    private View contentView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @Override
    protected View setFragmentView(LayoutInflater inflater, @Nullable ViewGroup container) {
        contentView = inflater.inflate(R.layout.test_failed_fragment, container, false);
        ButterKnife.bind(this, contentView);
        activity.setSupportActionBar(toolbar);
        isFailed = true;
        tvTitle.setText("测试一");
        LoadReTryRefreshManager.getInstance().register(this, contentView, this);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAndRetry();
            }
        });
        return contentView;
    }

    @Override
    protected void loadData() {
        LoadReTryRefreshManager.getInstance().startLoad(this);
    }

    @Override
    protected void stopLoadData() {

    }


    @Override
    public void loadAndRetry() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Thread.sleep(2000);
                if (isFailed) {
                    emitter.onNext(1 / 0);
                } else {
                    emitter.onNext(1);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                if (!activity.isFinishing()){
                    Toast.makeText(activity, "加载成功", Toast.LENGTH_SHORT).show();
                tvName.setText(getResources().getString(R.string.large_text));
                LoadReTryRefreshManager.getInstance().onLoadSuccess(TestFailedFragment.this, new ShowRefreshViewListener() {
                    @Override
                    public void colseRefreshView() {
                        refreshLayout.setRefreshing(false);
                    }
                });
                }
            }

            @Override
            public void onError(Throwable e) {
                if (!activity.isFinishing()){
                    Toast.makeText(activity, "加载失败", Toast.LENGTH_SHORT).show();
                LoadReTryRefreshManager.getInstance().onLoadFailed(TestFailedFragment.this, e.getMessage(), new ShowRefreshViewListener() {
                    @Override
                    public void colseRefreshView() {
                        refreshLayout.setRefreshing(false);
                    }
                });
                }
            }

            @Override
            public void onComplete() {
            }
        });

    }

    @Override
    public void showRefreshView() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LoadReTryRefreshManager.getInstance().unRegister(this);
    }




    @OnClick({R.id.tv_refresh, R.id.tv_success})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_refresh:
                LoadReTryRefreshManager.getInstance().startLoad(this);
                break;
            case R.id.tv_success:
                isFailed=false;
                break;
        }
    }
}
