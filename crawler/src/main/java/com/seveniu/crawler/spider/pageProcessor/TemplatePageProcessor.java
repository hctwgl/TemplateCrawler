package com.seveniu.crawler.spider.pageProcessor;

import com.seveniu.crawler.spider.pageProcessor.parse.PageParseResult;
import com.seveniu.crawler.spider.pageProcessor.parse.TemplatePageParse;
import com.seveniu.entity.data.DataContent;
import com.seveniu.entity.data.PageContent;
import com.seveniu.entity.template.structure.TemplateStructure;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * Created by seveniu on 7/24/17.
 * *
 */
public class TemplatePageProcessor implements PageProcessor {
    public static final String REQUEST_CONTEXT_KEY = "REQUEST_CONTEXT_KEY";
    private TemplateStructure templateStructure;

    public TemplatePageProcessor(TemplateStructure templateStructure) {
        this.templateStructure = templateStructure;
    }


    @Override
    public void process(Page page) {

        PageParseResult pageParseResult = TemplatePageParse.parse(templateStructure, page);

        RequestContext requestContext = getRequestContext(page);
        boolean isInContentLayer = templateStructure.getContentLayer() >= requestContext.getCurTotalLayer();
        DataContent dataContentContext = requestContext.getDataContent();
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
            RequestContext nextRequestContext = RequestContext.createNextLayerRequestContext(requestContext, url);
            if (isInContentLayer) {
                DataContent nextDataContent = new DataContent(url);
                dataContentContext.getChildren().add(nextDataContent);
                nextRequestContext.setDataContent(nextDataContent);
            }
            page.addTargetRequest(new Request(url).putExtra(REQUEST_CONTEXT_KEY, nextRequestContext));
        }

        // 同一级 url
        List<String> pageUrls = pageParseResult.getPageUrls();
        for (String url : pageUrls) {
            page.addTargetRequest(new Request(url).putExtra(REQUEST_CONTEXT_KEY, RequestContext.createNextPageRequestContext(requestContext)));
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
