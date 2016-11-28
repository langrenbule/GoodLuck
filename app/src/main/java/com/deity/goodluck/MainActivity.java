package com.deity.goodluck;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.deity.goodluck.adapter.NewTicketAdapter;
import com.deity.goodluck.data.Params;
import com.deity.goodluck.entity.LotteryTicketsNewsEntity;
import com.deity.goodluck.network.NetWorkData;
import com.deity.goodluck.widget.WaterView;
import com.othershe.baseadapter.ViewHolder;
import com.othershe.baseadapter.interfaces.OnItemClickListeners;
import com.othershe.baseadapter.interfaces.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.deity.goodluck.R.id.content_items;

/**
 *
 */
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.luck_data)public WaterView luckData;
    @BindView(R.id.refresh_layout) public SwipeRefreshLayout mRefreshLayout;
    @BindView(content_items) public RecyclerView mRecycleView;
//    private HeaderViewRecyclerAdapter headerViewRecyclerAdapter;
    private NewTicketAdapter mAdapter;
//    private LotteryTicketsNewsAdapter mLotteryTicketsNewsAdapter;
    private Subscription subscription;
    @BindView(R.id.reLoadImage) public ImageView reloadImag;
    /**
     * 当前页面
     */
    public int currentNewsPage = 1;
    /**
     * 数据
     */
    private List<LotteryTicketsNewsEntity> mDatas;
    private List<LotteryTicketsNewsEntity> mTotalDatas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initParams();
    }

    public void initParams(){
        luckData.register();
        mRefreshLayout.setOnRefreshListener(this);
        mAdapter = new NewTicketAdapter(this,null,true);
        //初始化EmptyView
        View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_layout, (ViewGroup) mRecycleView.getParent(), false);
        mAdapter.setEmptyView(emptyView);
        //初始化 开始加载更多的loading View
        mAdapter.setLoadingView(R.layout.load_loading_layout);

        //设置加载更多触发的事件监听
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
//                currentNewsPage++;
                getNewItems(currentNewsPage);
            }
        });
        //设置item点击事件监听
        mAdapter.setOnItemClickListener(new OnItemClickListeners<LotteryTicketsNewsEntity>() {
            @Override
            public void onItemClick(ViewHolder viewHolder, LotteryTicketsNewsEntity data, int position) {
                Intent intent = new Intent(MainActivity.this, LotteryTicketsNewsActivity.class);
                intent.putExtra("url", data.getArticleUrl());
                intent.putExtra("imageUrl", data.getNewsImageUrl());
                intent.putExtra("newsTitle", data.getNewsTitle());
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);

        mRecycleView.setAdapter(mAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getNewItems(currentNewsPage);
            }
        }, 1000);
    }

    public interface OnItemClickListener<T> {
        void onItemClick(ViewHolder viewHolder, T data, int position);
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        getNewItems(currentNewsPage);
    }


    /**
     * 请求网络数据
     */
    public List<LotteryTicketsNewsEntity> requestNetWorkData(String targetUrl, int currentPage) {
        List<LotteryTicketsNewsEntity> newsItems = null;
        try {
            newsItems = NetWorkData.getInstance().getArticleItems(targetUrl, currentPage);
        } catch (Exception e) {
            System.out.println("request Exception>>>" + e.getMessage());
        }
        return newsItems;
    }

    public void getNewItems(final int currentPage){
        subscription = Observable.create(new Observable.OnSubscribe<List<LotteryTicketsNewsEntity>>() {
            @Override
            public void call(Subscriber<? super List<LotteryTicketsNewsEntity>> subscriber) {
                List<LotteryTicketsNewsEntity> newItems = requestNetWorkData(Params.targetUrl, currentPage);
                subscriber.onNext(newItems);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    Subscriber<List<LotteryTicketsNewsEntity>> subscriber = new Subscriber<List<LotteryTicketsNewsEntity>>() {
        @Override
        public void onCompleted() {
//            Log.i(TAG,"OK");
//            mDatas = NewItemDaoImpl.instance.queryNewItemEntities(newsType,(currentNewsPage-1));
//            updateUI();
        }

        @Override
        public void onError(Throwable e) {
//            Log.i(TAG,">>>>ERROR");
            updateUI(null);
            Toast.makeText(MainActivity.this,"好像到底了!",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<LotteryTicketsNewsEntity> newItems) {
//            NewItemDaoImpl.instance.addNewItemEntities(newItems);
//            mDatas = NewItemDaoImpl.instance.queryNewItemEntities(newsType,(currentNewsPage-1));
            updateUI(newItems);
            //如果成功获取到，那么就直接显示呗
//            Log.i(TAG,"onNext");
        }
    };

    public void updateUI(List<LotteryTicketsNewsEntity> mDatas){
        mRefreshLayout.setRefreshing(false);
        if (null!=mDatas) {
            //刷新数据
            mAdapter.setLoadMoreData(mDatas);
        }else {
            //加载失败，更新footer view提示
            mAdapter.setLoadFailedView(R.layout.load_failed_layout);
        }

    }
}
