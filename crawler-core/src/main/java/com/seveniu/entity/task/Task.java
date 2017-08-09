package com.seveniu.entity.task;


import com.seveniu.common.json.Json;
import com.seveniu.entity.base.BaseAuditableEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dhlz on 12/31/16.
 * *
 */
@Entity
public class Task extends BaseAuditableEntity {

    private String name;
    private Long templateId; // required
    private int cycle = 0; // unit second, 0 run only once
    private int priority = 1; // required


    @Lob
    private String startUrls;
    @Transient
    private String[] startUrlList;
    private short threadNum = 1;
    private boolean proxy = false;
    private boolean javascript = false;


    private Long createUserId;
    @Enumerated(value = EnumType.STRING)
    private TaskRunStatus runStatus;
    private Date nextRunTime;
    private Date lastStartTime;
    private Date lastDoneTime;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public TaskRunStatus getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(TaskRunStatus runStatus) {
        this.runStatus = runStatus;
    }

    public Date getNextRunTime() {
        return nextRunTime;
    }

    public void setNextRunTime(Date nextRunTime) {
        this.nextRunTime = nextRunTime;
    }

    public Date getLastStartTime() {
        return lastStartTime;
    }

    public void setLastStartTime(Date lastStartTime) {
        this.lastStartTime = lastStartTime;
    }

    public Date getLastDoneTime() {
        return lastDoneTime;
    }

    public void setLastDoneTime(Date lastDoneTime) {
        this.lastDoneTime = lastDoneTime;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    /**
     * 禁止主动调用
     */
    public String getStartUrls() {
        return this.startUrls;
    }

    /**
     * 禁止主动调用
     */
    public void setStartUrls(String startUrls) {
        this.startUrls = startUrls;
    }

    public String[] getStartUrlList() {
        if (startUrlList == null && this.startUrls != null) {
            return Json.toObject(this.startUrls, String[].class);
        }
        return this.startUrlList;
    }

    public void setStartUrlList(String[] startUrlList) {
        this.startUrls = Json.toJson(this.startUrlList);
        this.startUrlList = startUrlList;
    }

    public short getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(short threadNum) {
        this.threadNum = threadNum;
    }

    public boolean isProxy() {
        return proxy;
    }

    public void setProxy(boolean proxy) {
        this.proxy = proxy;
    }

    public boolean isJavascript() {
        return javascript;
    }

    public void setJavascript(boolean javascript) {
        this.javascript = javascript;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", name=" + name + '\'' +
                ", templateId=" + templateId +
                '}';
    }
}
