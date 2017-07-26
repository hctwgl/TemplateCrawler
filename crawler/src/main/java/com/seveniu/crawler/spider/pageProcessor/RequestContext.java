package com.seveniu.crawler.spider.pageProcessor;

import com.seveniu.entity.data.DataContent;

/**
 * Created by seveniu on 7/24/17.
 * *
 */
public class RequestContext {
    private int curTotalLayer = 0; // 初始化
    private int curContentLayer = 0;
    private int curPageNum = 0;
    private DataContent dataContent;

    public int getCurTotalLayer() {
        return curTotalLayer;
    }

    public int getCurContentLayer() {
        return curContentLayer;
    }

    public int getCurPageNum() {
        return curPageNum;
    }

    public DataContent getDataContent() {
        return dataContent;
    }

    public RequestContext setDataContent(DataContent dataContent) {
        this.dataContent = dataContent;
        return this;
    }

    public static RequestContext createNextLayerRequestContext(RequestContext upLayerContext, boolean isInContentLayer, String url) {
        RequestContext requestContext = new RequestContext();
        requestContext.curTotalLayer = upLayerContext.curTotalLayer + 1;
        if (isInContentLayer) {
            requestContext.curContentLayer = upLayerContext.curContentLayer + 1;
            DataContent nextDataContent = new DataContent(url);
            upLayerContext.getDataContent().getChildren().add(nextDataContent);
            requestContext.setDataContent(nextDataContent);
        } else {
            requestContext.curContentLayer = upLayerContext.curContentLayer;
        }
        requestContext.curPageNum = upLayerContext.curPageNum;
        return requestContext;
    }

    public static RequestContext createNextPageRequestContext(RequestContext upLayerContext) {
        RequestContext requestContext = new RequestContext();
        requestContext.curTotalLayer = upLayerContext.curTotalLayer;
        requestContext.curContentLayer = upLayerContext.curContentLayer;
        requestContext.curPageNum = upLayerContext.curPageNum + 1;
        requestContext.dataContent = upLayerContext.dataContent;
        return requestContext;
    }
}
