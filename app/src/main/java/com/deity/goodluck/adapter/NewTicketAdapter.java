package com.deity.goodluck.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.deity.goodluck.R;
import com.deity.goodluck.entity.LotteryTicketsNewsEntity;
import com.othershe.baseadapter.ViewHolder;
import com.othershe.baseadapter.base.CommonBaseAdapter;

import java.util.List;

/**
 * Created by Deity on 2016/11/28.
 */

public class NewTicketAdapter extends CommonBaseAdapter<LotteryTicketsNewsEntity> {
    private Context context;

    public NewTicketAdapter(Context context, List<LotteryTicketsNewsEntity> datas, boolean isLoadMore) {
        super(context, datas, isLoadMore);
        this.context = context;
    }


    @Override
    protected void convert(ViewHolder holder, LotteryTicketsNewsEntity data) {
        holder.setText(R.id.news_title,data.getNewsTitle());
        holder.setText(R.id.news_content,data.getNewsTitle());
        holder.setText(R.id.news_date,data.getNewsDate());
        Glide.with(context).load(data.getNewsImageUrl()).thumbnail(1.0f).into((ImageView) holder.getView(R.id.news_image));
        holder.setIsRecyclable(true);;
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_lottery_ticket;
    }
}
