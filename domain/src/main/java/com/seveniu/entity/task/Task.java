package com.seveniu.entity.task;


import com.seveniu.entity.BaseAuditableEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by dhlz on 12/31/16.
 * *
 */
@Entity
public class Task extends BaseAuditableEntity {

    private String name;
    private Long createUserId;
    @Enumerated(value = EnumType.STRING)
    private TaskRunStatus runStatus;
    private int cycle;
    private int cycleUnit;

    @Transient
    private TaskCondition condition;
    private String conditionStr;


    private Date createTime;
    private Date updateTime;
    private Date nextRunTime;
    private Date lastStartTime;
    private Date lastDoneTime;

    @ElementCollection
    private List<Long> assignUserIds;

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

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public int getCycleUnit() {
        return cycleUnit;
    }

    public void setCycleUnit(int cycleUnit) {
        this.cycleUnit = cycleUnit;
    }

    public TaskCondition getCondition() {
        return condition;
    }

    public void setCondition(TaskCondition condition) {
        this.condition = condition;
    }

    public String getConditionStr() {
        return conditionStr;
    }

    public void setConditionStr(String conditionStr) {
        this.conditionStr = conditionStr;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

//    public List<User> getAssignUsers() {
//        return assignUsers;
//    }
//
//    public void setAssignUsers(List<User> assignUsers) {
//        this.assignUsers = assignUsers;
//    }


    public List<Long> getAssignUserIds() {
        return assignUserIds;
    }

    public void setAssignUserIds(List<Long> assignUserIds) {
        this.assignUserIds = assignUserIds;
    }
}
