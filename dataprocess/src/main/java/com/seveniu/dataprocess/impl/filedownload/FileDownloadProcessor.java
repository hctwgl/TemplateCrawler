package com.seveniu.dataprocess.impl.filedownload;

import com.seveniu.common.exception.ExceputionUtil;
import com.seveniu.common.json.Json;
import com.seveniu.dataprocess.DataProcessor;
import com.seveniu.dataprocess.processrecord.ProcessRecord;
import com.seveniu.entity.data.content.FieldContent;
import com.seveniu.entity.data.content.Link;
import com.seveniu.entity.template.structure.field.FieldType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by seveniu on 1/15/18.
 * *
 */
@Service
public class FileDownloadProcessor implements DataProcessor {
    @Autowired
    FileDownloader fileDownloader;

    @Override
    public ProcessRecord.Result process(ProcessRecord processRecord) {
        try {
            Set<String> urls = processRecord.getData().getDataContent().getPageContents().stream()
                    .filter(Objects::nonNull)
                    .flatMap(pageContent -> pageContent.getFieldContents().stream())
                    .filter(fieldContent -> fieldContent != null && fieldContent.getFieldType().equals(FieldType.DOWNLOAD_LINK))
                    .map(FieldContent::getLinks)
                    .flatMap(Collection::stream)
                    .map(Link::getUrl)
                    .collect(Collectors.toSet());
            Map<String, Result> resultMap = fileDownloader.download(urls);
            return new ProcessRecord.Result(true, Json.toJson(resultMap));
//            TimeUnit.SECONDS.sleep(7);
//            return new ProcessRecord.Result(true, "");

        } catch (InterruptedException e) {
            e.printStackTrace();
            return new ProcessRecord.Result(false, ExceputionUtil.stackTraceToString(e));
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
