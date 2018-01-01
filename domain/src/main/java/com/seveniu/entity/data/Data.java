package com.seveniu.entity.data;


import com.seveniu.common.json.Json;
import com.seveniu.entity.base.BaseAuditableEntity;
import com.seveniu.entity.data.content.DataContent;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;

/**
 * Created by dhlz on 12/31/16.
 * *
 */
@Entity
public class Data extends BaseAuditableEntity {
    private String url;
    private Long taskId;
    private Long statisticId;
    @Lob
    private String content;
    @Transient
    private DataContent dataContent;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    /**
     * 不允许主动调用
     */
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DataContent getDataContent() {
        if (dataContent == null) {
            this.dataContent = Json.toObject(this.content, DataContent.class);
        }
        return dataContent;
    }

    public void setDataContent(DataContent dataContent) {
        this.dataContent = dataContent;
        this.content = Json.toJson(this.dataContent);
    }

    public Long getStatisticId() {
        return statisticId;
    }

    public void setStatisticId(Long statisticId) {
        this.statisticId = statisticId;
    }
}
