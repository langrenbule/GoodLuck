package com.deity.goodluck.entity;

/**
 * 文章内容
 * Created by Deity on 2016/11/28.
 */

public class LotteryTicketsNewsContentEntity {

    public enum ContentType{
        CONTENT_TYPE_TITLE(0),
        CONTENT_TYPE_CONTENT(1);

        private int code;
        ContentType(int code){
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
    /**正文内容*/
    private String ticketContent;
    /**类型*/
    private int ticketType;

    public String getTicketContent() {
        return ticketContent;
    }

    public void setTicketContent(String ticketContent) {
        this.ticketContent = ticketContent;
    }

    public int getTicketType() {
        return ticketType;
    }

    public void setTicketType(int ticketType) {
        this.ticketType = ticketType;
    }
}
