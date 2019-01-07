package com.aiceking.view_load_retry.fragment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aiceking.view_load_retry.R;
import com.aiceking.view_load_retry.fragment.adapter.TestAdapter;
import com.aiceking.view_load_retry.loadretryadapter.forfragment.LoadAdapterForFragment;
import com.aiceking.view_load_retry.loadretryadapter.forfragment.NetErrorAdapterForFragment;
import com.aiceking.viewloadretrylibrary.listener.LoadRetryListener;
import com.aiceking.viewloadretrylibrary.manager.LoadRetryManager;
import com.android.lazyfragmentlibrary.LazyBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TestFragment extends LazyBaseFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private AppCompatActivity activity;
    private View contentView;
    private List<String> stringList;
    private TestAdapter testAdapter;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @Override
    protected View setFragmentView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup) {
        contentView = layoutInflater.inflate(R.layout.fragment_test, viewGroup, false);
        ButterKnife.bind(this, contentView);
        stringList=new ArrayList<>();
        loadRetryRecycleview();
        testAdapter=new TestAdapter(R.layout.recycle_test,stringList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(testAdapter);
        return contentView;
    }
    private void loadRetryRecycleview() {
        LoadRetryManager.getInstance().register(recycler, new LoadRetryListener() {
            @Override
            public void load() {
                doSomethingOne(false);
            }

            @Override
            public void reTry() {
                doSomethingOne(true);
            }
        });
    }
    @Override
    protected void loadData() {
        LoadRetryManager.getInstance().load(recycler, LoadAdapterForFragment.class);

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
                if (value == 0) {
                    LoadRetryManager.getInstance().onSuccess(recycler);
                    for (int i=0;i<20;i++){
                        stringList.add("测试数据："+(i+1));
                    }
                    testAdapter.notifyDataSetChanged();
                } else {
                    LoadRetryManager.getInstance().onFailed(recycler, NetErrorAdapterForFragment.class, "请检查网络连接");
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

    @Override
    protected void stopLoadData() {


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LoadRetryManager.getInstance().unRegister(recycler);
    }
}
