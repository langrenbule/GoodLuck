package com.deity.goodluck;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.deity.goodluck.widget.WaterView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.luck_data)public WaterView luckData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        luckData.register();
//        luckData.setWaterColor(R.color.colorAccent);
//        luckData.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                luckData.unregister();
//            }
//        }, 10000);
    }
}
