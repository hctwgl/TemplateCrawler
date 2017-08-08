package com.seveniu.entity.base;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * Created by seveniu on 2/21/17.
 * *
 */
@MappedSuperclass
public class BaseAuditableEntity extends BaseEntity {
    @Enumerated(EnumType.STRING)
    protected EntityStatus status;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    protected Date timeCreated;
    protected Date timeUpdated;
    protected Date timeDeleted;
    protected Long createdBy;
    protected Long updatedBy;

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Date getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(Date timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    public Date getTimeDeleted() {
        return timeDeleted;
    }

    public void setTimeDeleted(Date timeDeleted) {
        this.timeDeleted = timeDeleted;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
}
