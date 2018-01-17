package com.seveniu.dataprocess.processrecord;

import com.seveniu.entity.data.Data;
import com.seveniu.entity.data.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by seveniu on 1/15/18.
 * *
 */
@Service
public class ProcessRecordService {
    @Autowired
    ProcessRecordRepository processRecordRepository;
    @Autowired
    DataService dataService;

    public ProcessRecord insert(Data data) {
        ProcessRecord processRecord = new ProcessRecord(data);
        return processRecordRepository.save(processRecord);
    }

    @Transactional
    public ProcessRecord getOne() {
        Data first = dataService.getFirst();
        if (first == null) {
            return null;
        }
        ProcessRecord processRecord = new ProcessRecord(first);
        processRecord = processRecordRepository.save(processRecord);
        dataService.delete(first.getId());
        return processRecord;
    }

    public void delete(Long dataId) {
        processRecordRepository.deleteByDataId(dataId);
    }

    public ProcessRecord save(ProcessRecord processRecord) {
        return processRecordRepository.save(processRecord);
    }

    @Transactional
    public void updateStatus(Long id, ProcessStatus status) {
        processRecordRepository.updateProcessStatus(status, id);
    }
}
