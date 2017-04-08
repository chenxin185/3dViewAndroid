# 3dViewAndroid
## note: 
    这是一个简易的可以旋转的自定义ViewGruop，可以自定义Interpolate来做变速旋转。
## demo

<imag src="https://github.com/chenxin185/3dViewAndroid/blob/master/gif/gif1.gif" width="200" height="360" />
<imag src="https://github.com/chenxin185/3dViewAndroid/blob/master/gif/gif2.gif" width="200" height="360" />

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
    然后app的build.gradle->dependencies中加入:
        compile 'com.github.chenxin185:3dViewAndroid:1.0.2'
### 使用方法
#### 在layout文件中声明：
     <com.xingmeng.chenxin.my3drotateview.My3dRotateView
        android:id="@+id/mView"
        android:layout_width="match_parent"
        android:layout_height="300dp"
        app:childHeight="50dp"
        app:childWidth="50dp"
        app:margin_ovalHeight="100dp"
        app:margin_ovalWidth="50dp">

        <ImageView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:src="@mipmap/ic_launcher" />

        <ImageView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:src="@mipmap/ic_launcher" />

        <ImageView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:src="@mipmap/ic_launcher" />

        <ImageView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:src="@mipmap/ic_launcher" />
        
    </com.xingmeng.chenxin.my3drotateview.My3dRotateView>
#### 在Activity中：
        my3dView = (My3dRotateView) findViewById(R.id.mView);
        //Item的点击事件
        my3dView.setItemClickListener(new My3dRotateView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, boolean isFirst) {
                Log.e("infoo","onItemClick position = "+position);
            }
        });
        //自定义的插值器，返回的是每一帧改变的角度
        my3dView.setInterpolate(new My3dRotateView.My3dInterpolate() {
            @Override
            public double getInterpolation(float timing) {
                return 0;
            }
        });
