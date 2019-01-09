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
    * [自定义](#自定义加载状态页面-Adapter继承-BaseLoadRetryAdapter下面各方法都是按需被调用取决于你主动设置的显示哪个Adapter)
    * [初始化](#示例代码Demo中normal用法建议在-Application的-onCreate中进行初始化有多少个Adapter就添加多少个这里统一了入口是方便管理)
* [使用](#最常规用法针对一个View针对多个View和针对一个View用法一致流程都是针对某个View的)
    * [1、注册](#1注册一般在-oncreate中调用)
    * [2、开始加载](#2开始加载)
    * [3、加载结果回调](#3加载结果回调在你的请求成功和失败的回调中加入加载结果回调)
    * [4、解除绑定](#4解除绑定)
* [为何要造这个看起来重复的轮子](#为何要造这个看起来重复的轮子)
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
## 自定义加载状态页面 Adapter,继承 BaseLoadRetryAdapter，下面各方法都是按需被调用，取决于你主动设置的显示哪个Adapter。
| 方法      |参数  | 作用  |
| :-------- | :--------| :--: |
| onLoadStart| View  |  显示这个加载状态页面前开始前调用，用于你自定义页面中控件的初始化,此View为当前显示的加载页面View，以下方法中的View都是。   |
| getCoverViewLayoutId  | return R.layout |  加载页面的布局Layout   |
| onFalied|    View,Ogject |  加载失败的回调（会在你主动调用错误页面对应的那个Adapter里调用），Object可以是任意的对象，方便你显示加载错误的原因|
| onSuccess|    View |  加载成功的回调（会在你设置的Load状态那个Adapter里调用），这里可以做一些加载动画的停止操作，另需要手动让View.Gone，暴露在这里是方便各位添加加载页面消失的动画|
  
自定义Adapter示例（Demo中normal用法）
``` java
public class LoadAdapterForActivity extends BaseLoadRetryAdapter{
    @Override
    public void onLoadStart(View view) {
        ((TextView)view.findViewById(R.id.tv_text)).setText("加载中 ...");
    }
    @Override
    public void onFalied(View view, Object object) {
    }
/**这里在加载完成的时候做了一个淡出动画*/
    @Override
    public void onSuccess(View view) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
        alphaAnimation.setDuration(500);
        view.startAnimation(alphaAnimation);
        view.setVisibility(View.GONE);
    }

    @Override
    public int getCoverViewLayoutId() {
        return R.layout.load_activity;
    }
}
```
``` java
/**默认给加载失败页面添加了点击事件，回调到retry方法，开始重试，重试时显示加载中Adapter对应的页面*/
public class NetErrorAdapterForActivity extends BaseLoadRetryAdapter{
    @Override
    public void onLoadStart(View view) {
        ((TextView)view.findViewById(R.id.tv_retry)).setText("点击重新加载");
    }

    @Override
    public void onFalied(View view, Object object) {
        ((ImageView) view.findViewById(R.id.iv_head)).setImageResource(R.mipmap.timeout);
        ((TextView)view.findViewById(R.id.tv_text)).setText((String)object);
        ((TextView)view.findViewById(R.id.tv_retry)).setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(View view) {
      
    }

    @Override
    public int getCoverViewLayoutId() {
        return R.layout.retry_activity;
    }
}
```
## 示例代码(Demo中normal用法)：建议在 Application的 onCreate中进行初始化,有多少个Adapter就添加多少个，这里统一了入口是方便管理。
``` java
LoadRetryManager.getInstance().addAdapter(new LoadAdapterForActivity());
LoadRetryManager.getInstance().addAdapter(new NetErrorAdapterForActivity());.......
```
# 最常规用法，针对一个View（针对多个View和针对一个View用法一致,流程都是针对某个View的）

| 方法      |参数  | 作用  |
| :-------- | :--------| :--|
| register| View，LoadRetryRefreshListener |  注册   |
| startLoad| View,Class<? extends BaseLoadRetryAdapter> |  开始加载,加载中类Adapter.Calss   |
| unRegister|    View  |  解除绑定|
| unRegister|    List <View>  |  解除多个View的绑定|
| onSuccess   |    View    |  加载成功，会调用View对应加载中Adapter的onSuccess方法|
| onFailed    |View，Class<? extends BaseLoadRetryAdapter>，Object|  加载失败时显示加载失败类Adapter.Class对应的页面，并调用该Adapter的onFalied()方法，Object为加载失败原因|
  
    
### 1、注册，一般在 onCreate中调用
``` java
LoadRetryManager.getInstance().register(view, new LoadRetryListener() {
            @Override
            public void load() {
                //执行你的网络请求
               //dosomething();
            }

            @Override
            public void reTry() {
                //执行你的重试请求
                //dosomethingRetry();
            }
        });       
```
### 2、开始加载
``` java
LoadRetryManager.getInstance().load(view, LoadAdapterForActivity.class);     
```
### 3、加载结果回调，在你的请求成功和失败的回调中加入加载结果回调
``` java
            @Override
            public void onSuccess(Integer value) {
            //加载成功你要做的事.....
            
                //加载结果回调
                LoadRetryManager.getInstance().onSuccess(view);
            }

            @Override
            public void onFailed(Throwable e) {
            //加载失败你要做的事.....
            
                //加载结果回调
             LoadRetryManager.getInstance().onFailed(view, NetErrorAdapterForActivity.class, "请检查网络连接");

            }        
```
### 4、解除绑定
``` java
//在Activity中
Override
    protected void onDestroy() {
        super.onDestroy();
        LoadRetryManager.getInstance().unRegister(view);

	//加载多个View时建议在BaseActivity中封装方法，维护一个List<View>,每注册一个加载View就Add一次，解绑时方便操作
        LoadRetryManager.getInstance().unRegister(list); 
    }
```
``` java
//在Fragment中
@Override
    public void onDestroyView() {
        super.onDestroyView();
        LoadRetryManager.getInstance().unRegister(view);

	//加载多个View时建议在BaseFragment中封装方法，维护一个List<View>,每注册一个加载View就Add一次，解绑时方便操作
        LoadRetryManager.getInstance().unRegister(list); 
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
