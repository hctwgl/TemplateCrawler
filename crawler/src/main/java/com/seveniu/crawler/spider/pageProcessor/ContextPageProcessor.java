package com.seveniu.crawler.spider.pageProcessor;

import com.seveniu.crawler.spider.pageProcessor.parse.PageParseResult;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

import static com.seveniu.crawler.spider.Def.REQUEST_CONTEXT_KEY;

/**
 * Created by seveniu on 11/9/17.
 * *
 */
public abstract class ContextPageProcessor implements PageProcessor {
    RequestContext getRequestContext(Page page) {
        Object obj = page.getRequest().getExtra(REQUEST_CONTEXT_KEY);
        if (obj == null) {
            return new RequestContext();
        }
        return (RequestContext) obj;
    }

    void processMultiPage(PageParseResult pageParseResult, RequestContext requestContext, Page page) {
        List<String> pageUrls = pageParseResult.getPageUrls();
        for (String url : pageUrls) {
            // 克隆一个新的实例，并且内置的 page index 加一
            RequestContext nextPageRequestContext = RequestContext.createNextPageRequestContext(requestContext);
            page.addTargetRequest(new Request(url).putExtra(REQUEST_CONTEXT_KEY, nextPageRequestContext));
        }
    }

    @Override
    public Site getSite() {
        return Site.me();
    }
}
