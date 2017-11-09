package com.seveniu.crawler.spider.pageProcessor;

import com.seveniu.crawler.spider.pageProcessor.parse.PageParseResult;
import com.seveniu.crawler.spider.pageProcessor.parse.TemplatePageParse;
import com.seveniu.entity.data.DataContent;
import com.seveniu.entity.data.PageContent;
import com.seveniu.entity.template.Template;
import com.seveniu.entity.template.structure.PageTemplate;
import us.codecraft.webmagic.Page;

import java.util.List;
import java.util.stream.Collectors;

import static com.seveniu.crawler.spider.Def.DATA_CONTENT_RESULT_KEY;

/**
 * Created by seveniu on 11/9/17.
 * *
 */
public class NoContextPageProcessor extends ContextPageProcessor {
    private Template templateStructure;

    public NoContextPageProcessor(Template templateStructure) {
        this.templateStructure = templateStructure;
    }
    // 错误处理
    // 防止循环
    // 层级结构 不用 排重的 downloader


    @Override
    public void process(Page page) {
        // 获取上下文
        RequestContext requestContext = getRequestContext(page);
        String pageUrl = page.getUrl().get();
        List<PageTemplate> collect = templateStructure.getPageStructure().stream().filter(pageS -> pageS.urlMatch(pageUrl)).collect(Collectors.toList());

        DataContent dataContentContext = requestContext.getDataContent();
        try {
            for (PageTemplate pageTemplate : collect) {
                // 解析页面
                PageParseResult pageParseResult = TemplatePageParse.parse(pageTemplate, page);
                PageContent pageContent = pageParseResult.getPageContent();
                if (dataContentContext == null) {
                    // root DataContent
                    dataContentContext = new DataContent(page.getUrl().get());
                }
                dataContentContext.addPage(pageContent, requestContext.getCurPageIndex());


                // 多页 url
                processMultiPage(pageParseResult, requestContext, page);

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

}
