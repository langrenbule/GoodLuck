package com.deity.goodluck.network;

import com.deity.goodluck.data.Params;
import com.deity.goodluck.entity.LotteryTicketContents;
import com.deity.goodluck.entity.LotteryTicketsNewsContentEntity;
import com.deity.goodluck.entity.LotteryTicketsNewsEntity;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


/**
 * 数据获取
 * Created by Deity on 2016/11/28.
 */

public class NetWorkData {

    private static NetWorkData instance = new NetWorkData();

    private NetWorkData(){}

    public static NetWorkData getInstance(){
        return instance;
    }

    public List<LotteryTicketsNewsEntity> getArticleItems(String baseUrl, int currentPage){
        String currentUrl = baseUrl+currentPage;
        Document document = getUrlDoc(currentUrl);
        List<LotteryTicketsNewsEntity> newItemList = new ArrayList<>();
        LotteryTicketsNewsEntity entity = null;
        if (null==document) return null;

        Element elementParent = document.select(".article-list").select(".latest-notes").first();
        if (null==elementParent) return null;
        Elements elements = elementParent.children();
        for (Element element:elements){
            entity = new LotteryTicketsNewsEntity();
            //创建时间
            String createTime = element.select("p.list-top").select("span").attr("data-shared-at");
            entity.setNewsDate(createTime);

            //标题
            String title = element.select("h4.title").text();
            entity.setNewsTitle(title);
            //文章地址
            String articleUrl =  element.select("h4.title").select("a").attr("href");
//            if (!TextUtils.isEmpty(articleUrl)){
                entity.setArticleUrl(Params.baseUrl+articleUrl);
//            }
            //图片
            Element imageUrl = element.select("a.wrap-img").first();
            if (null!=imageUrl){
                entity.setNewsImageUrl(imageUrl.select("img").attr("src"));
            }
            newItemList.add(entity);
        }
        return newItemList;
    }

    public LotteryTicketContents getArticleContents(String baseUrl){
        Document document = getUrlDoc(baseUrl);
//        List<NewBornContentEntity> newBornContentEntityList = new ArrayList<>();
        LotteryTicketContents contents = new LotteryTicketContents();
        if (null==document) return null;
        Elements elements = document.select("div.preview").first().children();
        if (null==elements) return null;
        for (Element element:elements){
//            LotteryTicketsNewsContentEntity entity = new LotteryTicketsNewsContentEntity();
//            String title = element.select("h1.title").text();
//            if (null!=title&&!title.equals("")){
//                entity.setTicketContent(title);
//                entity.setTicketType(LotteryTicketsNewsContentEntity.ContentType.CONTENT_TYPE_TITLE.getCode());
//                contents.getLotteryTicketsNewsContentList().add(entity);
//            }
            LotteryTicketsNewsContentEntity mainContent = new LotteryTicketsNewsContentEntity();
            String showContent = element.select("div.show-content").html();
            if (null!=showContent&&!showContent.equals("")){
                mainContent.setTicketType(LotteryTicketsNewsContentEntity.ContentType.CONTENT_TYPE_CONTENT.getCode());
                mainContent.setTicketContent(showContent);
                contents.getLotteryTicketsNewsContentList().add(mainContent);
            }
        }
//        System.out.println(contents.getLotteryTicketsNewsContentList().get(1).getTicketContent());
        return contents;
    }

    public Document getUrlDoc(String url) {
        Document doc = null;
        try {
            Connection conneciton = Jsoup.connect(url);
            conneciton.timeout(10*1000);//10秒超时
            conneciton.userAgent(Params.AGENT);
            doc = conneciton.get();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("链接目标地址失败:"+e.getMessage());
            return null;
        }
        return doc;
    }
}
