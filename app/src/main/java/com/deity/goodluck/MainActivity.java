package com.deity.goodluck;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.deity.goodluck.adapter.HeaderViewRecyclerAdapter;
import com.deity.goodluck.adapter.LotteryTicketsNewsAdapter;
import com.deity.goodluck.entity.LotteryTicketsNewsEntity;
import com.deity.goodluck.widget.WaterView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.deity.goodluck.R.id.content_items;

/**
 *
 */
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.luck_data)public WaterView luckData;
    @BindView(R.id.refresh_layout) public SwipeRefreshLayout mRefreshLayout;
    @BindView(content_items) public RecyclerView mRecycleView;
    private HeaderViewRecyclerAdapter headerViewRecyclerAdapter;
    private LotteryTicketsNewsAdapter mLotteryTicketsNewsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initParams();
    }

    public void initParams(){
        luckData.register();
        mLotteryTicketsNewsAdapter = new LotteryTicketsNewsAdapter(MainActivity.this);
        mLotteryTicketsNewsAdapter.setData(testData());
        mLotteryTicketsNewsAdapter.setRecycleViewOnClickListener(new LotteryTicketsNewsAdapter.RecycleViewOnClickListener() {
            @Override
            public void onItemClick(View view, LotteryTicketsNewsEntity data) {
                Intent intent = new Intent(MainActivity.this, LotteryTicketsNewsActivity.class);
                intent.putExtra("url", data.getNewsImageUrl());
                intent.putExtra("imageUrl", data.getNewsImageUrl());
                intent.putExtra("newsTitle", data.getNewsTitle());
                startActivity(intent);
            }
        });
        headerViewRecyclerAdapter = new HeaderViewRecyclerAdapter(mLotteryTicketsNewsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        mRecycleView.setAdapter(headerViewRecyclerAdapter);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(linearLayoutManager);
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(false);
    }

    public List<LotteryTicketsNewsEntity> testData(){
        List<LotteryTicketsNewsEntity> lotteryTicketsNewsEntities = new ArrayList<>();
        for (int i=0;i<10;i++){
            LotteryTicketsNewsEntity entity = new LotteryTicketsNewsEntity();
            entity.setNewsTitle("时时彩玩法介绍");
            entity.setNewsDate("2016年11月4日23:00:04");
            entity.setNewsDescription("对万位、千位、百位、十位和个位各选1个号码为一注，每位号码最多可0～9全选，投注号码与开奖号码按位一致即中一等奖，单注奖金20000元。投注号码前三位与开奖号码前三位按位一致或投注号码后三位与开奖号码后三位按位一致即中二等奖，单注奖金200元。投注号码前两位与开奖号码前两位按位一致或投注号码后两位与开奖号码后两位按位一致即中三等奖，单注奖金20元。 五星通选一注号码，可三个奖级通吃，共有5次中奖机会，兼中兼得。即中了一等奖，同时也就中了2个二等奖和2个三等奖。同理，中了1注二等奖，同时也中了1注三等奖。");
            entity.setNewsImageUrl("http://p6.qhimg.com/t01c4e11f779f23b734.jpg");
            lotteryTicketsNewsEntities.add(entity);
        }
        return lotteryTicketsNewsEntities;
    }
}
