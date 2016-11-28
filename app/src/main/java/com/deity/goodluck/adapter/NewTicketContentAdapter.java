package com.deity.goodluck.adapter;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import com.deity.goodluck.R;
import com.deity.goodluck.entity.LotteryTicketsNewsContentEntity;
import com.othershe.baseadapter.ViewHolder;
import com.othershe.baseadapter.base.CommonBaseAdapter;

import java.util.List;

/**
 * Created by Deity on 2016/11/28.
 */

public class NewTicketContentAdapter extends CommonBaseAdapter<LotteryTicketsNewsContentEntity> {

    private Context context;

    public NewTicketContentAdapter(Context context, List<LotteryTicketsNewsContentEntity> datas, boolean isLoadMore) {
        super(context, datas, isLoadMore);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, LotteryTicketsNewsContentEntity data) {
//        holder.setText(R.id.text, Html.fromHtml(data.getTicketContent()));
        ((TextView) holder.getView(R.id.text)).setText(Html.fromHtml(data.getTicketContent()));
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.news_content_item;
    }
}
