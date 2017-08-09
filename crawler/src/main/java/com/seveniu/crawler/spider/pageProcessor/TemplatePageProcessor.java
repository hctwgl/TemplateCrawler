package com.seveniu.crawler.spider.pageProcessor;

import com.seveniu.crawler.spider.pageProcessor.parse.PageParseResult;
import com.seveniu.crawler.spider.pageProcessor.parse.TemplatePageParse;
import com.seveniu.entity.data.DataContent;
import com.seveniu.entity.data.PageContent;
import com.seveniu.entity.template.Template;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

import static com.seveniu.crawler.spider.Def.DATA_CONTENT_RESULT_KEY;
import static com.seveniu.crawler.spider.Def.REQUEST_CONTEXT_KEY;

/**
 * Created by seveniu on 7/24/17.
 * *
 */
public class TemplatePageProcessor implements PageProcessor {
    private Template templateStructure;

    public TemplatePageProcessor(Template templateStructure) {
        this.templateStructure = templateStructure;
    }


    @Override
    public void process(Page page) {
        // 获取上下文
        RequestContext requestContext = getRequestContext(page);
        // 解析页面
        PageParseResult pageParseResult = TemplatePageParse.parse(templateStructure.getPageStructure().get(requestContext.getCurTotalLayer()), page);

        boolean isInContentLayer = templateStructure.getContentLayer() >= requestContext.getCurTotalLayer();
        DataContent dataContentContext = requestContext.getDataContent();
        try {
            // 页面内容
            if (isInContentLayer) {
                PageContent pageContent = pageParseResult.getPageContent();
                if (dataContentContext == null) {
                    // root DataContent
                    dataContentContext = new DataContent(page.getUrl().get());
                }
                dataContentContext.getPageContents().set(requestContext.getCurPageNum(), pageContent);
            }
            // 下一级 url
            List<String> urls = pageParseResult.getNextUrls();
            for (String url : urls) {
                RequestContext nextRequestContext = RequestContext.createNextLayerRequestContext(requestContext, isInContentLayer, url);
                if (isInContentLayer) {
                    DataContent nextDataContent = new DataContent(url);
                    dataContentContext.addChild(nextDataContent);
                    nextRequestContext.setDataContent(nextDataContent);
                }
                page.addTargetRequest(new Request(url).putExtra(REQUEST_CONTEXT_KEY, nextRequestContext));
            }

            // 同一级 url
            List<String> pageUrls = pageParseResult.getPageUrls();
            for (String url : pageUrls) {
                page.addTargetRequest(new Request(url).putExtra(REQUEST_CONTEXT_KEY, RequestContext.createNextPageRequestContext(requestContext)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dataContentContext != null) {
                DataContent doneDataContent = dataContentContext.done();
                if (doneDataContent != null) {
                    page.putField(DATA_CONTENT_RESULT_KEY, doneDataContent);
                }
            }
        }
    }

    private static RequestContext getRequestContext(Page page) {
        Object obj = page.getRequest().getExtra(REQUEST_CONTEXT_KEY);
        if (obj == null) {
            return new RequestContext();
        }
        return (RequestContext) obj;
    }


    @Override
    public Site getSite() {
        return null;
    }
}
