package com.wxystatic.gifloadretry;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.dialoglibrary.UsefulDialogHelp;
import com.android.lazyfragmentlibrary.LazyBaseFragment;
import com.wxystatic.loadretrylibrary.LoadReTryHelp;
import com.wxystatic.loadretrylibrary.LoadRetryListener;

import butterknife.BindView;
import butterknife.ButterKnife;
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

public class TestFragment extends LazyBaseFragment implements LoadRetryListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private String title;
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
         contentView = inflater.inflate(R.layout.lazy_fragment_main, container, false);
        ButterKnife.bind(this, contentView);
        title = getArguments().getString("title");
        if (title.equals("测试1")){
            isFailed=true;
        }
        tvTitle.setText(title);
        activity.setSupportActionBar(toolbar);
        return contentView;
    }

    @Override
    protected void loadData() {
        LoadReTryHelp.getInstance().loadRetry(TestFragment.this, contentView, TestFragment.this);
    }

    @Override
    protected void stopLoadData() {

    }


    @Override
    public void loadAndRetry() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Thread.sleep(3000);
                 if (isFailed){
                     emitter.onNext(1/0);
                 }else{
                emitter.onNext(1);
                 }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                Toast.makeText(activity, "加载成功", Toast.LENGTH_SHORT).show();
                tvName.setText(getResources().getString(R.string.large_text));
                LoadReTryHelp.getInstance().onLoadSuccess(TestFragment.this);
                UsefulDialogHelp.getInstance().closeSmallLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(activity, "加载失败", Toast.LENGTH_SHORT).show();
                LoadReTryHelp.getInstance().onLoadFailed(TestFragment.this,e.getMessage());
                UsefulDialogHelp.getInstance().closeSmallLoadingDialog();
            }

            @Override
            public void onComplete() {
            }
        });

    }

    @Override
    public void showReLoadView() {
        UsefulDialogHelp.getInstance().showSmallLoadingDialog(activity, true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LoadReTryHelp.getInstance().clearLoadReTry(this);
    }


}
