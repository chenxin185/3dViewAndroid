# 3dViewAndroid
## note: 
    这是一个简易的可以旋转的自定义ViewGruop，可以自定义Interpolate来做变速旋转。
## demo，第一个是匀速的，第二个是变速的
![image](https://github.com/chenxin185/3dViewAndroid/blob/master/gif/gif1.gif) 
![image](https://github.com/chenxin185/3dViewAndroid/blob/master/gif/gif2.gif) 

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
 ### 属性和方法：
     childHeight：子view的高度</br>
     childWidth：子view的宽度</br>
     ovalHeight：旋转轨迹的高度（旋转的轨迹就是一个椭圆，这里的宽高就是椭圆的宽高）</br>
     ovalWidth：旋转轨迹的宽度</br>
     margin_ovalWidth：旋转轨迹的宽度与父容器左右两边的margin，设置了这个属性，上面的ovalWidth无效</br>
     margin_ovalHeight：旋转轨迹的高度与父容器上下两边的margin，设置了这个属性，上面的ovalHeight无效</br>
     setItemClickListener（）：设置子view的点击事件</br>
     setInterpolate（）：设置速度插值器，来控制速度的变化</br>
