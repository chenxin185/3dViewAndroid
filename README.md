# 3dViewAndroid
## note: 
    这是一个简易的可以旋转的自定义ViewGruop，可以自定义Interpolate来做变速旋转。

## usage:
### 引用:
    首先在项目的 build.gradle->allproject->repositories中加入:
        maven { url 'https://jitpack.io' }
        例子：
        allprojects {
            repositories {
            maven { url 'https://jitpack.io' }
        }
    }
### 使用方法
    在layout文件中声明：
    <com.xingmeng.chenxin.my3drotateview.My3dRotateView
        app:ovalHeight="150dp"
        app:ovalWidth="400dp"
        app:childWidth="60dp"
        app:childHeight="60dp"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        >
        <ImageView
            android:src="@mipmap/ic_launcher"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
        <ImageView
            android:src="@mipmap/ic_launcher"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
        <ImageView
            android:src="@mipmap/ic_launcher"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
        <ImageView
            android:src="@mipmap/ic_launcher"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
        <ImageView
            android:src="@mipmap/ic_launcher"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
        <ImageView
            android:src="@mipmap/ic_launcher"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
        <ImageView
            android:src="@mipmap/ic_launcher"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
    </com.xingmeng.chenxin.my3drotateview.My3dRotateView>
    
