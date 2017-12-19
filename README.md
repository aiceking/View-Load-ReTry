# Gif-Load-ReTry-Refresh

[![](https://jitpack.io/v/NoEndToLF/Gif-Load-ReTry-Refresh.svg)](https://jitpack.io/#NoEndToLF/Gif-Load-ReTry-Refresh)

**Gif-Load-ReTry-Refresh**：只需要一张Gif图，一行代码支持初次加载，重试加载，加载后再次刷新
 
- **原理** ：遍历View树，在Framelayout中动态插入和移除加载布局，与生命周期绑定，避免内存泄漏；
- **功能** ：目前支持在Activity，Fragment中使用（支持任何方式实现的沉浸式状态栏和透明状态栏）;
- **封装** ：接口化调用，支持MVP结构中使用。

-------------------
# 演示项目
 在Activity中加载成功，然后再次加载刷新在Activity中加载失败，然后重试加载，加载成功后刷新加载
 ![activity_success](https://github.com/NoEndToLF/Gif-Load-ReTry-Refresh/blob/master/imgs/activity_success.gif?raw=true)![activity_success](https://github.com/NoEndToLF/Gif-Load-ReTry-Refresh/blob/master/imgs/activity_failed.gif?raw=true)  
   
  <br />在Fragment中加载成功，然后再次加载刷新在Fragment中加载失败，然后重试加载，加载成功后刷新加载
 <br />![fragment_success]( https://github.com/NoEndToLF/Gif-Load-ReTry-Refresh/blob/master/imgs/fragment_success.gif?raw=true)![fragment_failed]( https://github.com/NoEndToLF/Gif-Load-ReTry-Refresh/blob/master/imgs/fragment_failed.gif?raw=true)
   
* [初步配置](#初步配置)
    * [引入](#引入)
    * [配置属性](#配置属性)
    * [示例代码](#示例代码)
* [在Activity中使用](#在Activity中使用)
    * [1、注册，一般在onCreate中调用](#1-注册-一般在onCreate中调用)
    * [2、开始加载，无需判断是初次加载还是加载完后刷新，已自动进行判断，初次加载和刷新都调用该方法](#2-开始加载-无需判断是初次加载还是加载完后刷新-已自动进行判断-初次加载和刷新都调用该方法)
    * [3、加载结果回调，在你的请求成功和失败的回调中加入加载结果回调](#3-加载结果回调-在你的请求成功和失败的回调中加入加载结果回调)
# 初步配置
## 引入
Step 1. Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        compile 'com.github.NoEndToLF:Gif-Load-ReTry-Refresh:1.1.0'
	}
## 配置属性
| 方法      |参数  | 作用  |
| :-------- | :--------| :--: |
| setGif| R.drawable.* |  加载页面的Gif图   |
| setBackgroundColor  | R.color.* |  加载页面整体背景颜色   |
| setBtnNormalColor|    R.color.* |  加载页面按钮未按下时的颜色|
| setBtnPressedColor|    R.color.* |  加载页面按钮按下时的颜色|
| setBtnBorderColor|    R.color.* |  加载页面按钮边框的颜色|
| setBtnTextColor|    R.color.* |  加载页面按钮文字的颜色|
| setBtnRadius|    Float |  加载页面按钮的圆角弧度|
| setBtnText|    String |  加载页面按钮的显示文字|
| setLoadText|    String | 正在加载中的提示文字|
| setLoadAndErrorTextColor|    R.color.*  | 加载页面的提示文字和加载失败提示文字的颜色|

## 示例代码
``` java
LoadRetryRefreshConfig config=new LoadRetryRefreshConfig();
        config.setBackgroundColor(R.color.white);
        config.setBtnNormalColor(R.color.blue_normal);
        config.setBtnPressedColor(R.color.blue_press);
        config.setBtnBorderColor(R.color.oringe_normal);
        config.setBtnRadius(10f);
        config.setBtnText("点击重新加载");
        config.setLoadText("测试加载2秒钟...");
        config.setBtnTextColor(R.color.white);
        config.setLoadAndErrorTextColor(R.color.gray);
        config.setGif(R.drawable.zhufaner);
        LoadReTryRefreshManager.getInstance().setLoadRetryRefreshConfig(config);
```
# 在<span id="activity">Activity</span>中使用

## 布局中，请在Toolbar下的需要加载的内容最外层套一层FrameLayout（[为何需要这样做](#reason)），如：
``` java
<LinearLayout android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:orientation="vertical"
    xmlns:android="http://schemas.android.com/apk/res/android">
    <android.support.v7.widget.Toolbar
        app:contentInsetStart="0dp"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:gravity="center_vertical"
        android:id="@+id/toolbar"
        android:background="@color/color_toolbar"
        >
        <FrameLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent">
            
            **********************************
            **********************************
            你的内容布局，如Linearlayout等
            **********************************
            **********************************
            
             </FrameLayout>
</LinearLayout>
```
## 代码中
### 方法简介
| 方法      |参数  | 作用  |
| :-------- | :--------| :--: |
| register| Activity，LoadRetryRefreshListener |  注册   |
| startLoad| Activity |  开始加载   |
| unRegister|    Activity  |  解除绑定|
| onLoadSuccess|    Activity，ShowRefreshViewListener  |  关闭加载View和刷新时的Dialog、下拉刷新等<br />(自动判断)|
| onLoadFailed|    Activity，ShowRefreshViewListener|  解除关闭加载View和刷新时的Dialog、下拉刷新等<br />(自动判断)定|
  
    
### 1、注册，一般在onCreate中调用
``` java
LoadReTryRefreshManager.getInstance().register(this, new LoadRetryRefreshListener() {
            @Override
            public void loadAndRetry() {
            //执行你的网络请求
            //dosomething();
            }

            @Override
            public void showRefreshView() {
            //显示你刷新时的加载View，如Dialog，下拉刷新等
                
            }
        });           
```
### 2、开始加载，无需判断是初次加载还是加载完后刷新，已自动进行判断，初次加载和刷新都调用该方法
``` java
LoadReTryRefreshManager.getInstance().startLoad(this);          
```
### 3、加载结果回调，在你的请求成功和失败的回调中加入加载结果回调
``` java
            @Override
            public void onSuccess(Integer value) {
            //加载成功你要做的事.....
            
                //加载结果回调
                LoadReTryRefreshManager.getInstance().onLoadSuccess(FailedActivity.this, 
                new ShowRefreshViewListener() {
                    @Override
                    public void colseRefreshView() {
                     //关闭你的刷新View,如Dialog，下拉刷新等  
                    }
                });
            }

            @Override
            public void onFailed(Throwable e) {
            //加载失败你要做的事.....
            
                //加载结果回调
                LoadReTryRefreshManager.getInstance().onLoadFailed(FailedActivity.this, 
                e.getMessage(), new ShowRefreshViewListener() {
                    @Override
                    public void colseRefreshView() {
                       //关闭你的刷新View,如Dialog，下拉刷新等
                    }
                });
            }        
```
### 4、解除绑定，可以直接写在BaseActivity的onDestory方法中，会自动判断然后进行解绑
``` java
Override
    protected void onDestroy() {
        super.onDestroy();
        LoadReTryRefreshManager.getInstance().unRegister(this);
    }
```
# 在Fragment中使用
## 同[Activity](#activity)中使用一致，请在Toolbar下的需要加载的内容最外层套一层FrameLayout（[为何需要这样做](#reason)）
## 代码中
### 方法简介
| 方法      |参数  | 作用  |
| :-------- | :--------| :--: |
| register| Fragment，View，LoadRetryRefreshListener |  注册<br />(View为Fragment在onCreateView中返回的View)   |
| startLoad| Fragment|  开始加载   |
| unRegister|    Fragment|  解除绑定|
| onLoadSuccess|    Activity，ShowRefreshViewListener  |  关闭加载View和刷新时的Dialog、下拉刷新等<br />(自动判断)|
| onLoadFailed|    Activity，ShowRefreshViewListener|  解除关闭加载View和刷新时的Dialog、下拉刷新等<br />(自动判断)定|
### 1、注册，一般在onCreateView中调用
``` java
LoadReTryRefreshManager.getInstance().register(this, contentView,new LoadRetryRefreshListener() {
            @Override
            public void loadAndRetry() {
            //执行你的网络请求
            //dosomething();
            }

            @Override
            public void showRefreshView() {
            //显示你刷新时的加载View，如Dialog，下拉刷新等
                
            }
        });           
```
### 2、始加载，无需判断是初次加载还是加载完后刷新，已自动进行判断，初次加载和刷新都调用该方法
``` java
LoadReTryRefreshManager.getInstance().startLoad(this);          
```
### 3、加载结果回调，在你的请求成功和失败的回调中加入加载结果回调
``` java
            @Override
            public void onSuccess(Integer value) {
            //加载成功你要做的事.....
            
                //加载结果回调
                LoadReTryRefreshManager.getInstance().onLoadSuccess(FailedActivity.this, 
                new ShowRefreshViewListener() {
                    @Override
                    public void colseRefreshView() {
                     //关闭你的刷新View,如Dialog，下拉刷新等  
                    }
                });
            }

            @Override
            public void onFailed(Throwable e) {
            //加载失败你要做的事.....
            
                //加载结果回调
                LoadReTryRefreshManager.getInstance().onLoadFailed(FailedActivity.this, 
                e.getMessage(), new ShowRefreshViewListener() {
                    @Override
                    public void colseRefreshView() {
                       //关闭你的刷新View,如Dialog，下拉刷新等
                    }
                });
            }        
```
### 4、解除绑定，可以直接写在BaseFragment的onDestroyView方法中，会自动判断然后进行解绑
``` java
@Override
    public void onDestroyView() {
        super.onDestroyView();
        LoadReTryRefreshManager.getInstance().unRegister(this);
    }
```

# <span id="reason">为何必须在布局中套一层FrameLayout</span>
目前为了在4.4，5.0，6.0，7.0及以上的版本中实现沉浸式状态栏或者是透明式状态栏，实现方式主要在低版本中有所不同，有的是给Toolbar加一个PaddingTop来留出StatusBar的高度，有的是设置全屏StatusBar透明，然后再动态插入一个大小一致的View来占位，达到设置状态栏颜色的目的，因此，如果单纯的在DecorView中来插入加载布局，难以控制加载页面的MarginTop，可能会遮盖到Toolbar，因此，退而求其次，在布局中需要加载的部分包一层FrameLayout，再通过递归View树来找到需要添加加载布局的地方，进行动态插入。（当然如果有更好的想法，强烈欢迎Issues）


# 反馈与建议
- 邮箱：<static_wxy@foxmail.com>

---------