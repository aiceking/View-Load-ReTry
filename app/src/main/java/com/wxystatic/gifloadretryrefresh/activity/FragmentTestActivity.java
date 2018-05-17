package com.wxystatic.gifloadretryrefresh.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wxystatic.gifloadretryrefresh.R;
import com.wxystatic.gifloadretryrefresh.fragment.TestFailedFragment;
import com.wxystatic.gifloadretryrefresh.fragment.TestSuccessFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentTestActivity extends AppCompatActivity {
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private List<Fragment> list_fragments;
    private List<String> list_titles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment_gf);
        ButterKnife.bind(this);
        
        initData();
        initVpTb();
    }

    private void initVpTb() {
        viewpager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        tablayout.setupWithViewPager(viewpager);
    }

    private void initData() {
        list_titles=new ArrayList<>();
        list_titles.add("测试一");
        list_titles.add("测试二");
        list_fragments=new ArrayList<>();
        for (String title:list_titles){
            tablayout.addTab(tablayout.newTab().setText(title));
        }
        list_fragments.add(new TestFailedFragment());
        list_fragments.add(new TestSuccessFragment());
    }
    public class MyAdapter extends FragmentPagerAdapter {

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
}
