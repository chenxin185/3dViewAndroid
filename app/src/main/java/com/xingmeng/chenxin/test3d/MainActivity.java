package com.xingmeng.chenxin.test3d;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.xingmeng.chenxin.my3drotateview.My3dRotateView;

public class MainActivity extends AppCompatActivity {
    My3dRotateView my3dView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
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

    }
}
