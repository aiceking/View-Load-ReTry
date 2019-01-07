package com.aiceking.view_load_retry.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiceking.view_load_retry.R;
import com.aiceking.view_load_retry.fragment.TestFragment;
import com.aiceking.view_load_retry.loadretryadapter.forimageview.LoadAdapterForImageView;
import com.aiceking.view_load_retry.loadretryadapter.fortablayout.LoadAdapterForTabLayout;
import com.aiceking.view_load_retry.loadretryadapter.forimageview.NetErrorAdapterForImageView;
import com.aiceking.view_load_retry.loadretryadapter.fortablayout.NetErrorAdapterForTabLayout;
import com.aiceking.view_load_retry.util.ToolBarUtil;
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

public class LoadFragmentActivity extends AppCompatActivity {

    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.linear_back)
    LinearLayout linearBack;
    private List<Fragment> list_fragments;
    private List<String> list_titles;
    private List<View> contentViewList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_fragment);
        ButterKnife.bind(this);
        contentViewList=new ArrayList<>();
        contentViewList.add(ivHead);
        contentViewList.add(tablayout);
        initToolBar();
        initData();
        initViewPagerAndTabLayout();
        loadRetryImageView();
        loadRetryTablayout();
    }

    private void loadRetryImageView() {
        LoadRetryManager.getInstance().register(ivHead, new LoadRetryListener() {
            @Override
            public void load() {
                doSomethingOne(false);
            }

            @Override
            public void reTry() {
                doSomethingOne(true);
            }
        });
        LoadRetryManager.getInstance().load(ivHead, LoadAdapterForImageView.class);
    }

    private void loadRetryTablayout() {
        LoadRetryManager.getInstance().register(tablayout, new LoadRetryListener() {
            @Override
            public void load() {
                doSomethingTwo(false);
            }

            @Override
            public void reTry() {
                doSomethingTwo(true);
            }
        });
        LoadRetryManager.getInstance().load(tablayout, LoadAdapterForTabLayout.class);
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
                    LoadRetryManager.getInstance().onSuccess(ivHead);
                } else {
                    LoadRetryManager.getInstance().onFailed(ivHead, NetErrorAdapterForImageView.class, "请检查网络连接");
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

    public void doSomethingTwo(final boolean isSuccess) {
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
                    LoadRetryManager.getInstance().onSuccess(tablayout);
                } else {
                    LoadRetryManager.getInstance().onFailed(tablayout, NetErrorAdapterForTabLayout.class, "请检查网络连接");
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

    private void initData() {
        list_titles = new ArrayList<>();
        list_fragments = new ArrayList<>();
        list_titles.add("测试一");
        list_titles.add("测试二");
        list_fragments.add(new TestFragment());
        list_fragments.add(new TestFragment());
        for (String title : list_titles) {
            tablayout.addTab(tablayout.newTab().setText(title));
        }
    }

    private void initViewPagerAndTabLayout() {
        vp.setAdapter(new MyAdapter(getSupportFragmentManager()));
        tablayout.setupWithViewPager(vp);
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        ToolBarUtil.setToolbarPaddingTop(toolbar, this);
        ImmersionBar.with(this).transparentStatusBar().init();
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                toolbar.setBackgroundColor(ToolBarUtil.changeAlpha(getResources().getColor(R.color.blue), Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange()));
                tvName.setTextColor(ToolBarUtil.changeAlpha(getResources().getColor(R.color.white), Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange()));
            }
        });
    }

    @OnClick(R.id.linear_back)
    public void onViewClicked() {
        finish();
    }

    public class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return list_titles.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return list_fragments.get(position);
        }

        @Override
        public int getCount() {
            return list_fragments.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        LoadRetryManager.getInstance().unRegister(contentViewList);
//        LoadRetryManager.getInstance().unRegister(ivHead);
//        LoadRetryManager.getInstance().unRegister(tablayout);
    }
}
