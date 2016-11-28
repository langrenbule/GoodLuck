package com.deity.goodluck;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.deity.goodluck.adapter.NewTicketContentAdapter;
import com.deity.goodluck.entity.LotteryTicketsNewsContentEntity;
import com.deity.goodluck.network.NetWorkData;
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
 * Created by Deity on 2016/11/4.
 */

public class LotteryTicketsNewsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mListView;
    private List<LotteryTicketsNewsContentEntity> mDatas;
    @BindView(R.id.backdrop) public ImageView backdrop;
    private NewTicketContentAdapter mAdapter;
    private static final String TAG = LotteryTicketsNewsActivity.class.getSimpleName();
    private Subscription subscription;

    /**
     * 该页面的url
     */
    private String articleUrl;
    private String articleTitle;
    private SwipeRefreshLayout refresh_layout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LotteryTicketsNewsActivity.this.finish();
            }
        });
        refresh_layout = (SwipeRefreshLayout) this.findViewById(R.id.refresh_layout);
        refresh_layout.setOnRefreshListener(this);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        Bundle extras = getIntent().getExtras();
        articleTitle = extras.getString("newsTitle");
        collapsingToolbar.setTitle(articleTitle);
        articleUrl = extras.getString("url");
        String imageUrl = extras.getString("imageUrl");
        if(!TextUtils.isEmpty(imageUrl)){
            Glide.with(this).load(imageUrl).placeholder(R.mipmap.icon_laucher).into(backdrop);
        }
        mListView = (RecyclerView) findViewById(content_items);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LotteryTicketsNewsActivity.this);
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(linearLayoutManager);
        mAdapter = new NewTicketContentAdapter(this,null,false);
        //初始化EmptyView
        View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_layout, (ViewGroup) mListView.getParent(), false);
        mAdapter.setEmptyView(emptyView);
        //初始化 开始加载更多的loading View
        mAdapter.setLoadingView(R.layout.load_loading_layout);

        //设置加载更多触发的事件监听
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                getNewItems(articleUrl);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(layoutManager);

        mListView.setAdapter(mAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh_layout.setRefreshing(true);
                getNewItems(articleUrl);
            }
        }, 1000);
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        refresh_layout.setRefreshing(true);
        getNewItems(articleUrl);
    }

    public void getNewItems(final String destUrl){
        subscription = Observable.create(new Observable.OnSubscribe<List<LotteryTicketsNewsContentEntity>>() {
            @Override
            public void call(Subscriber<? super List<LotteryTicketsNewsContentEntity>> subscriber) {
                try {
                    mDatas = NetWorkData.getInstance().getArticleContents(articleUrl).getLotteryTicketsNewsContentList();
                }catch (Exception e){
                    Log.i(TAG,"ERROR"+e.getMessage());
                }
                subscriber.onNext(mDatas);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    Subscriber<List<LotteryTicketsNewsContentEntity>> subscriber = new Subscriber<List<LotteryTicketsNewsContentEntity>>() {
        @Override
        public void onCompleted() {
            Log.i(TAG,"OK");
//            mAdapter.notifyDataSetChanged();
            mAdapter.setLoadEndView(R.layout.load_end_layout);
            refresh_layout.setRefreshing(false);
        }

        @Override
        public void onError(Throwable e) {
            Log.i(TAG,"ERROR");
            mAdapter.setLoadFailedView(R.layout.load_failed_layout);
            refresh_layout.setRefreshing(false);
        }

        @Override
        public void onNext(List<LotteryTicketsNewsContentEntity> newItems) {
            Log.i(TAG,"onNext");
            if (null==newItems||newItems.isEmpty()){
                newItems = new ArrayList<>();
                LotteryTicketsNewsContentEntity news = new LotteryTicketsNewsContentEntity();
                news.setTicketType(LotteryTicketsNewsContentEntity.ContentType.CONTENT_TYPE_CONTENT.getCode());
                news.setTicketContent("加载失败，请下拉刷新重新加载！");
                newItems.add(news);
            }
            mDatas = newItems;
            mAdapter.setNewData(mDatas);
            refresh_layout.setRefreshing(false);
        }
    };
}
