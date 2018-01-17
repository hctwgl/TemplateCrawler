package com.seveniu.dataprocess.processrecord;

import com.fasterxml.jackson.core.type.TypeReference;
import com.seveniu.common.json.Json;
import com.seveniu.entity.base.BaseAuditableEntity;
import com.seveniu.entity.data.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by seveniu on 1/15/18.
 * *
 */
@Entity
public class ProcessRecord extends BaseAuditableEntity {
    @Column(unique = true, nullable = false)
    private Long dataId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProcessStatus processStatus = ProcessStatus.UNDO;
    @Lob
    @Column(unique = true, nullable = false)
    private String dataStr;
    @Lob
    private String processResultsStr;
    @Transient
    private Map<String, Result> processResults;
    @Transient
    private Data data;

    public ProcessRecord() {
    }

    public ProcessRecord(Data data) {
        this.dataStr = Json.toJson(data);
        this.dataId = data.getId();
        this.timeCreated = new Date();
        this.data = data;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public ProcessStatus getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
    }

    public String getDataStr() {
        return dataStr;
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }

    public Data getData() {
        if (data == null) {
            data = Json.toObject(this.dataStr, Data.class);
        }
        return data;
    }

    public void setData(Data data) {
        this.data = data;
        this.dataStr = Json.toJson(data);
    }

    public String getProcessResultsStr() {
        return processResultsStr;
    }

    public void setProcessResultsStr(String processResultsStr) {
        this.processResultsStr = processResultsStr;
    }

    public Map<String, Result> getProcessResults() {
        if (processResults == null) {
            this.processResults = Json.toObject(this.processResultsStr, new TypeReference<Map<String, Result>>() {
            });
        }
        return processResults;
    }

    public void setProcessResults(Map<String, Result> processResults) {
        this.processResults = processResults;
    }

    public void addProcessResults(String key, Result result) {
        if (processResults == null) {
            processResults = new HashMap<>();
        }
        this.processResults.put(key, result);
        this.processResultsStr = Json.toJson(this.processResults);
    }

    public static class Result {
        private boolean success;
        private String info;

        public Result() {
        }

        public Result(boolean success, String info) {
            this.success = success;
            this.info = info;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

}
