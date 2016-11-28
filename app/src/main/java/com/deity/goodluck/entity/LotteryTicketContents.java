package com.deity.goodluck.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章内容
 * Created by Deity on 2016/11/28.
 */

public class LotteryTicketContents {
    private List<LotteryTicketsNewsContentEntity> lotteryTicketsNewsContentList=new ArrayList<>();
    private String nextPageUrl;

    public List<LotteryTicketsNewsContentEntity> getLotteryTicketsNewsContentList() {
        return lotteryTicketsNewsContentList;
    }

    public void setLotteryTicketsNewsContentList(List<LotteryTicketsNewsContentEntity> lotteryTicketsNewsContentList) {
        this.lotteryTicketsNewsContentList = lotteryTicketsNewsContentList;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }
}
