package com.seveniu.crawler.spider.pipeline;

import com.seveniu.crawler.spider.Def;
import com.seveniu.crawler.spider.TemplateSpider;
import com.seveniu.entity.data.DataContent;
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
        Data data = new Data();
        data.setDataContent((DataContent) obj);
        data.setTaskId(templateSpider.getCrawlerTask().getTask().getId());
        dataRepository.save(data);
    }

}
