package com.seveniu.dataprocess;

import com.seveniu.common.concurrent.BoundedExecutor;
import com.seveniu.dataprocess.processrecord.ProcessRecord;
import com.seveniu.dataprocess.processrecord.ProcessRecordService;
import com.seveniu.dataprocess.processrecord.ProcessStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by seveniu on 1/15/18.
 * *
 */
@Service
public class DataProcessManager {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ProcessRecordService processRecordService;
    @Autowired
    List<DataProcessor> dataProcessorList;
    private ExecutorService execute;

    @PostConstruct
    public void start() {

        dataProcessorList.sort(Comparator.comparingInt(DataProcessor::getOrder));

        new Thread(() -> {

            execute = BoundedExecutor.getBoundedExecutor(0, 5, 20, 10 * 60 * 1000);
            while (true) {
                ProcessRecord one = processRecordService.getOne();
                if (one == null) {
                    try {
                        TimeUnit.SECONDS.sleep(10);
                        continue;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                execute.execute(() -> {
                    long start = System.currentTimeMillis();
                    one.setProcessStatus(ProcessStatus.DOING);
                    processRecordService.updateStatus(one.getId(), ProcessStatus.DOING);
                    for (DataProcessor dataProcessor : dataProcessorList) {
                        if (dataProcessor.enable()) {
                            ProcessRecord.Result result = dataProcessor.process(one);
                            if (result != null) {

                                one.addProcessResults(dataProcessor.getName(), result);
                                if (!result.isSuccess()) {
                                    one.setProcessStatus(ProcessStatus.ERROR);
                                    logger.warn("process data : {} error ", one.getDataId());
                                    processRecordService.save(one);
                                    return;
                                }
                            }
                        }
                    }
                    one.setProcessStatus(ProcessStatus.SUCCESS);
                    processRecordService.save(one);
                    logger.info("process data : {} success, cost {}s", one.getDataId(), (System.currentTimeMillis() - start) / 1000);
                });
            }
        }).start();
    }


}
