package org.imaginnovate.BaseClass;

import java.time.LocalDateTime;

public class BaseAuditFieldsDTO {

    public Integer createdBy;
    public LocalDateTime createdOn;
    public Integer modifiedBy;
    public LocalDateTime modifiedOn;
    public Integer deletedBy;
    public LocalDateTime deletedOn;

    public BaseAuditFieldsDTO() {
    }

    public BaseAuditFieldsDTO(Integer createdBy, LocalDateTime createdOn, Integer modifiedBy, LocalDateTime modifiedOn,
            Integer deletedBy, LocalDateTime deletedOn) {
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.modifiedBy = modifiedBy;
        this.modifiedOn = modifiedOn;
        this.deletedBy = deletedBy;
        this.deletedOn = deletedOn;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getModifiedBy() {
        return this.modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedOn() {
        return this.modifiedOn;
    }

    public void setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Integer getDeletedBy() {
        return this.deletedBy;
    }

    public void setDeletedBy(Integer deletedBy) {
        this.deletedBy = deletedBy;
    }

    public LocalDateTime getDeletedOn() {
        return this.deletedOn;
    }

    public void setDeletedOn(LocalDateTime deletedOn) {
        this.deletedOn = deletedOn;
    }

    public BaseAuditFieldsDTO createdBy(Integer createdBy) {
        setCreatedBy(createdBy);
        return this;
    }

    public BaseAuditFieldsDTO createdOn(LocalDateTime createdOn) {
        setCreatedOn(createdOn);
        return this;
    }

    public BaseAuditFieldsDTO modifiedBy(Integer modifiedBy) {
        setModifiedBy(modifiedBy);
        return this;
    }

    public BaseAuditFieldsDTO modifiedOn(LocalDateTime modifiedOn) {
        setModifiedOn(modifiedOn);
        return this;
    }

    public BaseAuditFieldsDTO deletedBy(Integer deletedBy) {
        setDeletedBy(deletedBy);
        return this;
    }

    public BaseAuditFieldsDTO deletedOn(LocalDateTime deletedOn) {
        setDeletedOn(deletedOn);
        return this;
    }

    @Override
    public String toString() {
        return "{" +
                " createdBy='" + getCreatedBy() + "'" +
                ", createdOn='" + getCreatedOn() + "'" +
                ", modifiedBy='" + getModifiedBy() + "'" +
                ", modifiedOn='" + getModifiedOn() + "'" +
                ", deletedBy='" + getDeletedBy() + "'" +
                ", deletedOn='" + getDeletedOn() + "'" +
                "}";
    }

}
