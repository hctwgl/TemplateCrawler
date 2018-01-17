package com.seveniu.dataprocess.impl.custom;

import com.fasterxml.jackson.core.type.TypeReference;
import com.seveniu.common.json.Json;
import com.seveniu.dataprocess.DataProcessor;
import com.seveniu.dataprocess.impl.filedownload.FileDownloadProcessor;
import com.seveniu.dataprocess.impl.filedownload.Result;
import com.seveniu.dataprocess.processrecord.ProcessRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by seveniu on 1/17/18.
 * *
 */
@Service
public class Custom implements DataProcessor {
    @Autowired
    FileDownloadProcessor fileDownloadProcessor;

    @Override
    public ProcessRecord.Result process(ProcessRecord processRecord) {
        Map<String, ProcessRecord.Result> processResults = processRecord.getProcessResults();
        ProcessRecord.Result result = processResults.get(fileDownloadProcessor.getName());
        if (result.isSuccess()) {
            Map<String, Result> resultMap = Json.toObject(result.getInfo(), new TypeReference<Map<String, Result>>() {
            });
        }
        return new ProcessRecord.Result(true, null);
    }

    @Override
    public String getName() {
        return "custom";
    }

    @Override
    public int getOrder() {
        return 1;
    }


    @Override
    public boolean enable() {
        return false;
    }
}
