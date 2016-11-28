package com.deity.goodluck;

import com.deity.goodluck.network.NetWorkData;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    /**
     * http://www.jianshu.com/users/d9edcb44e2f2/latest_articles?page=4
     */
    @Test
    public void testGetArticle(){
        String baseUrl = "http://www.jianshu.com/users/c9d40feb9981/latest_articles?page=";
        NetWorkData.getInstance().getArticleItems(baseUrl,1);
        String contentUrl = "http://www.jianshu.com/p/148b54cd633b";
        NetWorkData.getInstance().getArticleContents(contentUrl);
    }
}