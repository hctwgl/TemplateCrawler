package com.seveniu.crawler.spider.pageProcessor;

import com.seveniu.entity.data.DataContent;

import javax.persistence.Transient;

/**
 * 每一个请求都会有一个实例，并且都是不同的实例，保证线程安全
 * Created by seveniu on 7/24/17.
 * *
 */
public class RequestContext {
    private int curTotalLayer = 0; //记录总层数 初始化
    private int curContentLayer = 0; // 当前层数，从0开始
    private int curPageIndex = 0; // 同一层 页数， 从 0 开始，这个数字可能是不准确的，只是用来排序

    @Transient
    private DataContent dataContent; // 有线程安全的问题

    public int getCurTotalLayer() {
        return curTotalLayer;
    }

    public int getCurContentLayer() {
        return curContentLayer;
    }

    public int getCurPageIndex() {
        return curPageIndex;
    }

    public DataContent getDataContent() {
        return dataContent;
    }

    public RequestContext setDataContent(DataContent dataContent) {
        this.dataContent = dataContent;
        return this;
    }

    /**
     * 创建一个新的用于下一层级的 RequestContext 实例
     *
     * @param upLayerContext
     * @param isInContentLayer
     * @param url
     * @return
     */
    public static RequestContext createNextLayerRequestContext(RequestContext upLayerContext, boolean isInContentLayer, String url) {
        RequestContext requestContext = new RequestContext();
        requestContext.curTotalLayer = upLayerContext.curTotalLayer + 1;
        if (isInContentLayer) {
            requestContext.curContentLayer = upLayerContext.curContentLayer + 1;
            DataContent nextDataContent = new DataContent(url);
            upLayerContext.getDataContent().addChild(nextDataContent);
            requestContext.setDataContent(nextDataContent);
        } else {
            requestContext.curContentLayer = upLayerContext.curContentLayer;
        }
        requestContext.curPageIndex = upLayerContext.curPageIndex;
        return requestContext;
    }

    /**
     * 创建一个新的用于下一页的 RequestContext 实例
     * @param upLayerContext
     * @return
     */
    public static RequestContext createNextPageRequestContext(RequestContext upLayerContext) {
        RequestContext requestContext = new RequestContext();
        requestContext.curTotalLayer = upLayerContext.curTotalLayer;
        requestContext.curContentLayer = upLayerContext.curContentLayer;
        requestContext.curPageIndex = upLayerContext.curPageIndex + 1;
        requestContext.dataContent = upLayerContext.dataContent;
        return requestContext;
    }
}
