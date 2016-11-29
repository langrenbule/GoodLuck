package com.deity.goodluck;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.deity.goodluck.adapter.NewTicketAdapter;
import com.deity.goodluck.data.Params;
import com.deity.goodluck.entity.LotteryTicketsNewsEntity;
import com.deity.goodluck.network.NetWorkData;
import com.deity.goodluck.widget.WaterView;
import com.othershe.baseadapter.ViewHolder;
import com.othershe.baseadapter.interfaces.OnItemClickListeners;
import com.othershe.baseadapter.interfaces.OnLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.InstrumentedActivity;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.deity.goodluck.R.id.content_items;

/**
 *
 */
public class MainActivity extends InstrumentedActivity implements SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.luck_data)public WaterView luckData;
    @BindView(R.id.luck_code)public TextView luck_code;
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

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static boolean isForeground = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initParams();
        registerMessageReceiver();
    }

    public void initParams(){
        luckData.register();
        mRefreshLayout.setOnRefreshListener(this);
        mAdapter = new NewTicketAdapter(this,null,false);
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
        initLuckCode();
    }

    public void initLuckCode(){
        String luckCode = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("luckCode","暂无消息");
        luck_code.setText(luckCode);
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
            mAdapter.setNewData(mDatas);
        }else {
            //加载失败，更新footer view提示
            LotteryTicketsNewsEntity entity = new LotteryTicketsNewsEntity();
            entity.setNewsTitle("加载失败,请下拉刷新重新加载!");
            mDatas = new ArrayList<>();
            mDatas.add(entity);
            mAdapter.setNewData(mDatas);
            mAdapter.setLoadFailedView(R.layout.load_failed_layout);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground=true;
        initLuckCode();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(mMessageReceiver);
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!TextUtils.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                System.out.println("hello world>>>>"+showMsg);
            }
        }
    }

    public enum  UIEvent{
        UPDATE_CODE;
        private String description;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUIEvent(UIEvent event){
        switch (event){
            case UPDATE_CODE:
                if (!TextUtils.isEmpty(event.getDescription())) {
                    luck_code.setText(event.getDescription());
                    getDefaultSharedPreferences(MainActivity.this).edit().putString("luckCode", event.getDescription()).apply();
                }
                break;
        }
    }
}
