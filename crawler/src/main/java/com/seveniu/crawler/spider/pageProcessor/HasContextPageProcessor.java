package com.seveniu.crawler.spider.pageProcessor;

import com.seveniu.crawler.spider.pageProcessor.parse.PageParseResult;
import com.seveniu.crawler.spider.pageProcessor.parse.TemplatePageParse;
import com.seveniu.entity.data.DataContent;
import com.seveniu.entity.data.PageContent;
import com.seveniu.entity.template.Template;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.util.List;

import static com.seveniu.crawler.spider.Def.DATA_CONTENT_RESULT_KEY;
import static com.seveniu.crawler.spider.Def.REQUEST_CONTEXT_KEY;

/**
 * 层级模板处理，适合有上下级关系的页面爬取
 * Created by seveniu on 7/24/17.
 * *
 */
public class HasContextPageProcessor extends ContextPageProcessor {
    private Template templateStructure;

    public HasContextPageProcessor(Template templateStructure) {
        this.templateStructure = templateStructure;
    }
    // 错误处理
    // 防止循环
    // 层级结构 不用 排重的 downloader


    @Override
    public void process(Page page) {
        // 获取上下文
        RequestContext requestContext = getRequestContext(page);
        if (requestContext.getCurTotalLayer() > Template.PREVENT_CYCLE_LAYER) {
            requestContext.getDataContent().done();
        }

        // 解析页面
        PageParseResult pageParseResult = TemplatePageParse.parse(templateStructure.getPageStructure().get(requestContext.getCurTotalLayer()), page);

        boolean isInContentLayer = templateStructure.getContentStartLayer() <= requestContext.getCurTotalLayer();
        DataContent dataContentContext = requestContext.getDataContent();
        try {
            // 页面内容
            if (isInContentLayer) {
                PageContent pageContent = pageParseResult.getPageContent();
                if (dataContentContext == null) {
                    // root DataContent
                    dataContentContext = new DataContent(page.getUrl().get());
                }
                dataContentContext.addPage(pageContent, requestContext.getCurPageIndex());
            }
            // 下一级 url
            List<String> urls = pageParseResult.getNextLevelUrls();
            for (String url : urls) {
                RequestContext nextRequestContext = RequestContext.createNextLayerRequestContext(requestContext, isInContentLayer, url);
                if (isInContentLayer) {
                    DataContent nextDataContent = new DataContent(url);
                    dataContentContext.addChild(nextDataContent);
                    nextRequestContext.setDataContent(nextDataContent);
                }
                page.addTargetRequest(new Request(url).putExtra(REQUEST_CONTEXT_KEY, nextRequestContext));
            }

            // 多页 url
            processMultiPage(pageParseResult, requestContext, page);
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

}
