package com.seveniu.crawler.spider.pipeline;

import com.seveniu.common.json.Json;
import com.seveniu.crawler.spider.Def;
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
@ConditionalOnExpression("${crawler.pipeline.console.enable:false}")
public class ConsolePipeline implements Pipeline {
    @Override
    public void process(ResultItems resultItems, Task task) {
        Object obj = resultItems.get(Def.DATA_CONTENT_RESULT_KEY);
        System.out.println(Json.toJson(obj));
    }
}
