package com.aiceking.view_load_retry.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiceking.view_load_retry.R;
import com.aiceking.view_load_retry.fragment.adapter.TestAdapter;
import com.aiceking.view_load_retry.loadretryadapter.foractivity.LoadAdapterForActivity;
import com.aiceking.view_load_retry.loadretryadapter.foractivity.NetErrorAdapterForActivity;
import com.aiceking.viewloadretrylibrary.listener.LoadRetryListener;
import com.aiceking.viewloadretrylibrary.manager.LoadRetryManager;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

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

public class NormalActivity extends AppCompatActivity {

    @BindView(R.id.linear_back)
    LinearLayout linearBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.sw)
    SwipeRefreshLayout sw;
    private List<String> stringList;
    private TestAdapter testAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        ButterKnife.bind(this);
        ImmersionBar.with(this).statusBarColor(R.color.blue).fitsSystemWindows(true).init();

        tvName.setText("常规使用");
        stringList = new ArrayList<>();
        testAdapter = new TestAdapter(R.layout.recycle_test, stringList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(testAdapter);
        sw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doSomethingOne(false);
            }
        });
        loadRetryRecycleview();
    }

    private void loadRetryRecycleview() {
        LoadRetryManager.getInstance().register(sw, new LoadRetryListener() {
            @Override
            public void load() {
                doSomethingOne(false);
            }

            @Override
            public void reTry() {
                doSomethingOne(true);
            }
        });
        LoadRetryManager.getInstance().load(sw, LoadAdapterForActivity.class);

    }

    public void doSomethingOne(final boolean isSuccess) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Thread.sleep(3000);
                if (isSuccess) {
                    emitter.onNext(0);
                } else {
                    emitter.onNext(2);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                sw.setRefreshing(false);
                if (value == 0) {
                    LoadRetryManager.getInstance().onSuccess(sw);
                    for (int i = 0; i < 20; i++) {
                        stringList.add("测试数据：" + (i + 1));
                    }
                    testAdapter.notifyDataSetChanged();
                } else {
                    LoadRetryManager.getInstance().onFailed(sw, NetErrorAdapterForActivity.class, "请检查网络连接");
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    @OnClick(R.id.linear_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        LoadRetryManager.getInstance().unRegister(sw);
    }
}
