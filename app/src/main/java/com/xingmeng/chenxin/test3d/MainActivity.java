package com.xingmeng.chenxin.test3d;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {
    My3dView my3dView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        my3dView = (My3dView) findViewById(R.id.mView);
        my3dView.setItemClickListener(new My3dView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, boolean isFirst) {
                Log.e("infoo","onItemClick position = "+position);
            }
        });
        my3dView.setInterpolate(new My3dView.My3dInterpolate() {
            @Override
            public double getInterpolation(float timing) {
                return 2;
            }
        });
    }
}
