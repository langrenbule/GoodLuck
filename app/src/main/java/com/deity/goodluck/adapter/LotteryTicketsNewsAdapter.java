package com.deity.goodluck.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deity.goodluck.R;
import com.deity.goodluck.entity.LotteryTicketsNewsEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Deity on 2016/11/4.
 */

public class LotteryTicketsNewsAdapter extends RecyclerView.Adapter<LotteryTicketsNewsAdapter.ViewHolder> implements  View.OnClickListener{

    private LayoutInflater inflater;
    private Context context;
    private List<LotteryTicketsNewsEntity> LotteryTicketsNewsList = new ArrayList<>();
    private RecycleViewOnClickListener recycleViewOnClickListener;

    public LotteryTicketsNewsAdapter(Context context){
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<LotteryTicketsNewsEntity> LotteryTicketsNewsList){
        this.LotteryTicketsNewsList = LotteryTicketsNewsList;
    }

    public interface RecycleViewOnClickListener{
        void onItemClick(View view,LotteryTicketsNewsEntity data);
    }

    public void setRecycleViewOnClickListener(RecycleViewOnClickListener recycleViewOnClickListener) {
        this.recycleViewOnClickListener = recycleViewOnClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_lottery_ticket, parent, false);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return new LotteryTicketsNewsAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LotteryTicketsNewsEntity entity = LotteryTicketsNewsList.get(position);
        holder.newsTitle.setText(entity.getNewsTitle());
        holder.newsDescription.setText(entity.getNewsDescription());
//        Glide.with(context).load(entity.getNewsImageUrl()).into(holder.newsImage);
        holder.newsData.setText(entity.getNewsDate());
        holder.itemView.setTag(entity);
    }

    @Override
    public int getItemCount() {
        if (null==LotteryTicketsNewsList) return 0;
        return LotteryTicketsNewsList.size();
    }

    @Override
    public void onClick(View view) {
        if (null!=recycleViewOnClickListener){
            recycleViewOnClickListener.onItemClick(view, (LotteryTicketsNewsEntity) view.getTag());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.news_title) public TextView newsTitle;
        @BindView(R.id.news_content) public TextView newsDescription;
        @BindView(R.id.news_image) public ImageView newsImage;
        @BindView(R.id.news_date) public TextView  newsData;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
