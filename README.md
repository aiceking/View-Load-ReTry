# View-Load-ReTry

[![](https://jitpack.io/v/NoEndToLF/View-Load-ReTry.svg)](https://jitpack.io/#NoEndToLF/View-Load-ReTry)

**View-Load-ReTry**：这个加载框架有点不一样，针对View进行加载，侧重点在灵活，哪里需要加载哪里
 
- **原理** ：找到需要加载的View,放入FrameLayout（包含自定义的各种情况的加载反馈View），再把FrameLayout放回需要加载View的Parent中 ,然后根据需求调用显示加载或者异常View。
- **功能** ：只要当前需要加载View有Parent就可以实现加载反馈（仅不支持复用型的View场景），同一页面支持N个View的加载，彼此互不影响。
- **封装** ：全局配置封装，与业务解耦，一个入口控制全部的加载反馈页面。

-------------------
# 示例，Demo只添加了一种Error反馈页面做演示，使用时可按需自定义多种Error反馈页面
| 常规使用      |Activity+Fragment多View加载  |
| :--------:| :--------:|  
|![normal](https://github.com/NoEndToLF/View-Load-ReTry/blob/master/img/normal.gif)| ![fix](https://raw.githubusercontent.com/NoEndToLF/View-Load-ReTry/master/img/fixed.gif)| 
 <br />

# 使用   
* [初步配置](#初步配置)
    * [引入](#引入)
    * [自定义](#自定义加载状态页面Adapter,继承BaseLoadRetryAdapter，下面各方法都是按需被调用，取决于你主动设置的显示哪个Adapter)
    * [初始化](#示例代码：建议在 Application的 onCreate中进行初始化,有多少个Adapter就添加多少个，这里统一了入口是方便管理)
* [在Activity中使用](#在-activit中使用)
    * [布局](#布局中请在-toolbar下的需要加载的内容最外层套一层-framelayout为何需要这样做如)
    * [代码（勿遗漏第4步，防止内存泄漏）](#代码中)
         * [1、注册](#1注册一般在-oncreate中调用)
         * [2、开始加载](#2开始加载无需判断是初次加载还是刷新已自动进行判断只需要在想要加载或刷新的地方直接调用加载失败重试加载已封装到-button事件中)
         * [3、加载结果回调](#3加载结果回调在你的请求成功和失败的回调中加入加载结果回调)
         * [4、解除绑定](#4解除绑定可以直接写在-baseactivity的-ondestory方法中会自动判断然后进行解绑)
* [在Fragment中使用](#在-fragment中使用)
    * [布局](#布局中同-activity中使用一致请在-toolbar下的需要加载的内容最外层套一层-framelayout为何需要这样做)
    * [代码（勿遗漏第4步，防止内存泄漏）](#代码中)
         * [1、注册](#1注册一般在-oncreate中调用)
         * [2、开始加载](#2开始加载无需判断是初次加载还是刷新已自动进行判断只需要在想要加载或刷新的地方直接调用加载失败重试加载已封装到-button事件中)
         * [3、加载结果回调](#3加载结果回调在你的请求成功和失败的回调中加入加载结果回调)
         * [4、解除绑定](#4解除绑定可以直接写在-basefragment的-ondestroyview方法中会自动判断然后进行解绑)
* [反馈与建议](#反馈与建议)

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
	        implementation 'com.github.NoEndToLF:View-Load-ReTry:2.0.1'
	}
## 自定义加载状态页面Adapter,继承BaseLoadRetryAdapter，下面各方法都是按需被调用，取决于你主动设置的显示哪个Adapter。
| 方法      |参数  | 作用  |
| :-------- | :--------| :--: |
| onLoadStart| View  |  显示这个加载状态页面前开始前调用，用于你自定义页面中控件的初始化,此View为当前显示的加载页面View，以下方法中的View都是。   |
| getCoverViewLayoutId  | return R.layout |  加载页面的布局Layout   |
| onFalied|    View,Ogject |  加载失败的回调（会在你主动调用错误页面对应的那个Adapter里调用），Object可以是任意的对象，方便你显示加载错误的原因|
| onSuccess|    View |  加载成功的回调（会在你设置的Load状态那个Adapter里调用），这里可以做一些加载动画的停止操作，另需要手动让View.Gone，暴露在这里是方便各位添加加载页面消失的动画|
## 示例代码：建议在 Application的 onCreate中进行初始化,有多少个Adapter就添加多少个，这里统一了入口是方便管理。
``` java
LoadRetryManager.getInstance().addAdapter(new xxxxxx());
LoadRetryManager.getInstance().addAdapter(new xxxxxx());.......
LoadReTryRefreshManager.getInstance().setLoadRetryRefreshConfig(config);
```
# 在 Activit中使用

## 布局中，请在 Toolbar下的需要加载的内容最外层套一层 FrameLayout（[为何需要这样做](#为何必须在布局中套一层-framelayout)），如：
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
| :-------- | :--------| :--|
| register| Activity，LoadRetryRefreshListener |  注册   |
| startLoad| Activity |  开始加载   |
| unRegister|    Activity  |  解除绑定|
| onLoadSuccess|    Activity，ShowRefreshViewListener  |  关闭加载View和刷新时的Dialog、下拉刷新等|
| onLoadFailed|    Activity，String，ShowRefreshViewListener|  显示加载失败原因，关闭加载View和刷新时的Dialog、下拉刷新等|
  
    
### 1、注册，一般在 onCreate中调用
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
### 2、开始加载，无需判断是初次加载还是刷新，已自动进行判断，只需要在想要加载或刷新的地方直接调用（加载失败重试加载已封装到 Button事件中）
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
### 4、解除绑定，可以直接写在 BaseActivity的 onDestory方法中，会自动判断然后进行解绑
``` java
Override
    protected void onDestroy() {
        super.onDestroy();
        LoadReTryRefreshManager.getInstance().unRegister(this);
    }
```
# 在 Fragment中使用
## 布局中，同 [Activity](#布局中请在-toolbar下的需要加载的内容最外层套一层-framelayout为何需要这样做如)中使用一致，请在 Toolbar下的需要加载的内容最外层套一层 FrameLayout（[为何需要这样做](#为何必须在布局中套一层-framelayout)）
## 代码中
### 方法简介
| 方法      |参数  | 作用  |
| :-------- | :--------| :-- |
| register| Fragment，View，LoadRetryRefreshListener |  注册<br />(View为Fragment在onCreateView中返回的View)   |
| startLoad| Fragment|  开始加载   |
| unRegister|    Fragment|  解除绑定|
| onLoadSuccess|    Fragment，ShowRefreshViewListener  |  关闭加载View和刷新时的Dialog、下拉刷新等|
| onLoadFailed|    Fragment，String，ShowRefreshViewListener|  显示加载失败原因，关闭加载View和刷新时的Dialog、下拉刷新等|
### 1、注册，一般在 onCreateView中调用
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
### 2、开始加载，无需判断是初次加载还是刷新，已自动进行判断，只需要在想要加载或刷新的地方直接调用（加载失败重试加载已封装到 Button事件中）
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
### 4、解除绑定，可以直接写在 BaseFragment的 onDestroyView方法中，会自动判断然后进行解绑
``` java
@Override
    public void onDestroyView() {
        super.onDestroyView();
        LoadReTryRefreshManager.getInstance().unRegister(this);
    }
```

# 为何要造这个看起来重复的轮子
目前好多开源的加载反馈框架大多是针对Activity和Fragment的，原理都是从根上替换加载布局，但是有个缺点，反馈布局的作用域太大了，不够灵活，现在闲的蛋疼造这个轮子也是为了灵活性，比如说Sample中的同一个页面要加载3块内容的时候，这个轮子的优势就显示出来了，而且原View具有的基本特性加载反馈页面依然包含。

# 反馈与建议
- 邮箱：<static_wxy@foxmail.com>

# License
```
Copyright (c) [2018] [static]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
---------
