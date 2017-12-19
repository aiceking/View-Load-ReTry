<<<<<<< HEAD
# Gif-Load-ReTry-Refresh

[![](https://jitpack.io/v/NoEndToLF/Gif-Load-ReTry-Refresh.svg)](https://jitpack.io/#NoEndToLF/Gif-Load-ReTry-Refresh)

**Gif-Load-ReTry-Refresh**：只需要一张Gif图，一行代码支持初次加载，重试加载，加载后再次刷新
 
- **原理** ：遍历View树，在Framelayout中动态插入和移除加载布局，与生命周期绑定，避免内存泄漏；
- **功能** ：目前支持在Activity，Fragment中使用（支持任何方式实现的沉浸式状态栏和透明状态栏）;
- **封装** ：接口化调用，支持MVP结构中使用。

-------------------
# 演示项目
 在Activity中加载成功，然后再次加载刷新在Activity中加载失败，然后重试加载，加载成功后刷新加载
 ![activity_success](https://github.com/NoEndToLF/Gif-Load-ReTry-Refresh/blob/master/imgs/activity_success.gif?raw=true)&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp![activity_success](https://github.com/NoEndToLF/Gif-Load-ReTry-Refresh/blob/master/imgs/activity_failed.gif?raw=true)  
   
  <br />在Fragment中加载成功，然后再次加载刷新在Fragment中加载失败，然后重试加载，加载成功后刷新加载
 <br />![fragment_success]( https://github.com/NoEndToLF/Gif-Load-ReTry-Refresh/blob/master/imgs/fragment_success.gif?raw=true)&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp![fragment_failed]( https://github.com/NoEndToLF/Gif-Load-ReTry-Refresh/blob/master/imgs/fragment_failed.gif?raw=true)

- [初步配置](#初步配置)
    - [引入 ](#引入)
    - [配置属性 ](#配置属性)
    - [示例代码 ](#示例代码，建议在Application中完成初始化配置)


## 初步配置
### 引入
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
### 配置属性
| 方法      |参数  | 作用  |
| :-------- | --------:| :--: |
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

### 示例代码，建议在Application中完成初始化配置
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
## 在<span id="activity">Activity</span>中使用

### 布局中，请在Toolbar下的需要加载的内容最外层套一层FrameLayout（[为何需要这样做](#reason)），如：
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
### 代码中
#### 方法简介
| 方法      |参数  | 作用  |
| :-------- | --------:| :--: |
| register| Activity，LoadRetryRefreshListener |  注册   |
| startLoad| Activity |  开始加载   |
| unRegister|    Activity  |  解除绑定|
| onLoadSuccess|    Activity，ShowRefreshViewListener  |  关闭加载View和刷新时的Dialog、下拉刷新等<br />(自动判断)|
| onLoadFailed|    Activity，ShowRefreshViewListener|  解除关闭加载View和刷新时的Dialog、下拉刷新等<br />(自动判断)定|
####1、注册，一般在onCreate中调用
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
####2、开始加载，无需判断是初次加载还是加载完后刷新，已自动进行判断，初次加载和刷新都调用该方法
``` java
LoadReTryRefreshManager.getInstance().startLoad(this);          
```
####3、加载结果回调，在你的请求成功和失败的回调中加入加载结果回调
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
####4、解除绑定，可以直接写在BaseActivity的onDestory方法中，会自动判断然后进行解绑
``` java
Override
    protected void onDestroy() {
        super.onDestroy();
        LoadReTryRefreshManager.getInstance().unRegister(this);
    }
```
##在Fragment中使用
### 同[Activity](#activity)中使用一致，请在Toolbar下的需要加载的内容最外层套一层FrameLayout（[为何需要这样做](#reason)）
### 代码中
#### 方法简介
| 方法      |参数  | 作用  |
| :-------- | --------:| :--: |
| register| Fragment，View，LoadRetryRefreshListener |  注册<br />(View为Fragment在onCreateView中返回的View)   |
| startLoad| Fragment|  开始加载   |
| unRegister|    Fragment|  解除绑定|
| onLoadSuccess|    Activity，ShowRefreshViewListener  |  关闭加载View和刷新时的Dialog、下拉刷新等<br />(自动判断)|
| onLoadFailed|    Activity，ShowRefreshViewListener|  解除关闭加载View和刷新时的Dialog、下拉刷新等<br />(自动判断)定|
####1、注册，一般在onCreateView中调用
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
####2、始加载，无需判断是初次加载还是加载完后刷新，已自动进行判断，初次加载和刷新都调用该方法
``` java
LoadReTryRefreshManager.getInstance().startLoad(this);          
```
####3、加载结果回调，在你的请求成功和失败的回调中加入加载结果回调
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
####4、解除绑定，可以直接写在BaseFragment的onDestroyView方法中，会自动判断然后进行解绑
``` java
@Override
    public void onDestroyView() {
        super.onDestroyView();
        LoadReTryRefreshManager.getInstance().unRegister(this);
    }
```




## <span id="reason">为何必须在布局中套一层FrameLayout</span>
目前为了在4.4，5.0，6.0，7.0及以上的版本中实现沉浸式状态栏或者是透明式状态栏，实现方式主要在低版本中有所不同，有的是给Toolbar加一个PaddingTop来留出StatusBar的高度，有的是设置全屏StatusBar透明，然后再动态插入一个大小一致的View来占位，达到设置状态栏颜色的目的，因此，如果单纯的在DecorView中来插入加载布局，难以控制加载页面的MarginTop，可能会遮盖到Toolbar，因此，退而求其次，在布局中需要加载的部分包一层FrameLayout，再通过递归View树来找到需要添加加载布局的地方，进行动态插入。（当然如果有更好的想法，强烈欢迎Issues）


## 反馈与建议
- 邮箱：<static_wxy@foxmail.com>

---------
=======
# 欢迎使用马克飞象

@(示例笔记本)[马克飞象|帮助|Markdown]

**马克飞象**是一款专为印象笔记（Evernote）打造的Markdown编辑器，通过精心的设计与技术实现，配合印象笔记强大的存储和同步功能，带来前所未有的书写体验。特点概述：
 
- **功能丰富** ：支持高亮代码块、*LaTeX* 公式、流程图，本地图片以及附件上传，甚至截图粘贴，工作学习好帮手；
- **得心应手** ：简洁高效的编辑器，提供[桌面客户端][1]以及[离线Chrome App][2]，支持移动端 Web；
- **深度整合** ：支持选择笔记本和添加标签，支持从印象笔记跳转编辑，轻松管理。

-------------------

[TOC]

## Markdown简介

> Markdown 是一种轻量级标记语言，它允许人们使用易读易写的纯文本格式编写文档，然后转换成格式丰富的HTML页面。    —— [维基百科](https://zh.wikipedia.org/wiki/Markdown)

正如您在阅读的这份文档，它使用简单的符号标识不同的标题，将某些文字标记为**粗体**或者*斜体*，创建一个[链接](http://www.example.com)或一个脚注[^demo]。下面列举了几个高级功能，更多语法请按`Ctrl + /`查看帮助。 

### 代码块
``` python
@requires_authorization
def somefunc(param1='', param2=0):
    '''A docstring'''
    if param1 > param2: # interesting
        print 'Greater'
    return (param2 - param1 + 1) or None
class SomeClass:
    pass
>>> message = '''interpreter
... prompt'''
```
### LaTeX 公式

可以创建行内公式，例如 $\Gamma(n) = (n-1)!\quad\forall n\in\mathbb N$。或者块级公式：

$$	x = \dfrac{-b \pm \sqrt{b^2 - 4ac}}{2a} $$

### 表格
| Item      |    Value | Qty  |
| :-------- | --------:| :--: |
| Computer  | 1600 USD |  5   |
| Phone     |   12 USD |  12  |
| Pipe      |    1 USD | 234  |

### 流程图
```flow
st=>start: Start
e=>end
op=>operation: My Operation
cond=>condition: Yes or No?

st->op->cond
cond(yes)->e
cond(no)->op
```

以及时序图:

```sequence
Alice->Bob: Hello Bob, how are you?
Note right of Bob: Bob thinks
Bob-->Alice: I am good thanks!
```

> **提示：**想了解更多，请查看**流程图**[语法][3]以及**时序图**[语法][4]。

### 复选框

使用 `- [ ]` 和 `- [x]` 语法可以创建复选框，实现 todo-list 等功能。例如：

- [x] 已完成事项
- [ ] 待办事项1
- [ ] 待办事项2

> **注意：**目前支持尚不完全，在印象笔记中勾选复选框是无效、不能同步的，所以必须在**马克飞象**中修改 Markdown 原文才可生效。下个版本将会全面支持。


## 印象笔记相关

### 笔记本和标签
**马克飞象**增加了`@(笔记本)[标签A|标签B]`语法, 以选择笔记本和添加标签。 **绑定账号后**， 输入`(`自动会出现笔记本列表，请从中选择。

### 笔记标题
**马克飞象**会自动使用文档内出现的第一个标题作为笔记标题。例如本文，就是第一行的 `欢迎使用马克飞象`。

### 快捷编辑
保存在印象笔记中的笔记，右上角会有一个红色的编辑按钮，点击后会回到**马克飞象**中打开并编辑该笔记。
>**注意：**目前用户在印象笔记中单方面做的任何修改，马克飞象是无法自动感知和更新的。所以请务必回到马克飞象编辑。

### 数据同步
**马克飞象**通过**将Markdown原文以隐藏内容保存在笔记中**的精妙设计，实现了对Markdown的存储和再次编辑。既解决了其他产品只是单向导出HTML的单薄，又规避了服务端存储Markdown带来的隐私安全问题。这样，服务端仅作为对印象笔记 API调用和数据转换之用。

 >**隐私声明：用户所有的笔记数据，均保存在印象笔记中。马克飞象不存储用户的任何笔记数据。**

### 离线存储
**马克飞象**使用浏览器离线存储将内容实时保存在本地，不必担心网络断掉或浏览器崩溃。为了节省空间和避免冲突，已同步至印象笔记并且不再修改的笔记将删除部分本地缓存，不过依然可以随时通过`文档管理`打开。

> **注意：**虽然浏览器存储大部分时候都比较可靠，但印象笔记作为专业云存储，更值得信赖。以防万一，**请务必经常及时同步到印象笔记**。

## 编辑器相关
### 设置
右侧系统菜单（快捷键`Ctrl + M`）的`设置`中，提供了界面字体、字号、自定义CSS、vim/emacs 键盘模式等高级选项。

### 快捷键

帮助    `Ctrl + /`
同步文档    `Ctrl + S`
创建文档    `Ctrl + Alt + N`
最大化编辑器    `Ctrl + Enter`
预览文档 `Ctrl + Alt + Enter`
文档管理    `Ctrl + O`
系统菜单    `Ctrl + M` 

加粗    `Ctrl + B`
插入图片    `Ctrl + G`
插入链接    `Ctrl + L`
提升标题    `Ctrl + H`

## 关于收费

**马克飞象**为新用户提供 10 天的试用期，试用期过后需要[续费](maxiang.info/vip.html)才能继续使用。未购买或者未及时续费，将不能同步新的笔记。之前保存过的笔记依然可以编辑。


## 反馈与建议
- 微博：[@马克飞象](http://weibo.com/u/2788354117)，[@GGock](http://weibo.com/ggock "开发者个人账号")
- 邮箱：<hustgock@gmail.com>

---------
感谢阅读这份帮助文档。请点击右上角，绑定印象笔记账号，开启全新的记录与分享体验吧。




[^demo]: 这是一个示例脚注。请查阅 [MultiMarkdown 文档](https://github.com/fletcher/MultiMarkdown/wiki/MultiMarkdown-Syntax-Guide#footnotes) 关于脚注的说明。 **限制：** 印象笔记的笔记内容使用 [ENML][5] 格式，基于 HTML，但是不支持某些标签和属性，例如id，这就导致`脚注`和`TOC`无法正常点击。


  [1]: http://maxiang.info/client_zh
  [2]: https://chrome.google.com/webstore/detail/kidnkfckhbdkfgbicccmdggmpgogehop
  [3]: http://adrai.github.io/flowchart.js/
  [4]: http://bramp.github.io/js-sequence-diagrams/
  [5]: https://dev.yinxiang.com/doc/articles/enml.php

>>>>>>> 9ab55177e98b4af5a3dd72eefb848f02bead8116
