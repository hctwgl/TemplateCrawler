package com.seveniu.crawler.spider.pipeline;

import com.seveniu.crawler.spider.Def;
import com.seveniu.crawler.spider.TemplateSpider;
import com.seveniu.entity.data.Data;
import com.seveniu.entity.data.DataRepository;
import com.seveniu.entity.data.content.DataContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * Created by seveniu on 8/9/17.
 * *
 */
@Service
@ConditionalOnExpression("${crawler.pipeline.mysql.enable:false}")
public class MysqlPipeline implements Pipeline {

    @Autowired
    DataRepository dataRepository;


    @Override
    public void process(ResultItems resultItems, Task task) {
        Object obj = resultItems.get(Def.DATA_CONTENT_RESULT_KEY);
        if (obj == null) {
            return;
        }
        TemplateSpider templateSpider = (TemplateSpider) task;
        DataContent dataContent = (DataContent) obj;
        Data data = new Data();
        data.setUrl(dataContent.getUrl());
        data.setDataContent(dataContent);
        data.setTaskId(templateSpider.getCrawlerTask().getTask().getId());
        data.setStatisticId(templateSpider.getCrawlerTask().getTaskStatistic().getId());
        dataRepository.save(data);
    }

}
