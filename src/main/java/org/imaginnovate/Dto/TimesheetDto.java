package org.imaginnovate.Dto;

import java.time.LocalDateTime;

import org.imaginnovate.BaseClass.BaseAuditFieldsDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id","employeeDivisionId", "employeeProjectId", "projectTaskId", "description", "hoursWorked", "submittedBy",
        "submittedOn", "status", "approvedBy", "createdBy", "createdOn", "modifiedBy", "modifiedOn", "deletedBy",
        "deletedOn" })
public class TimesheetDto extends BaseAuditFieldsDTO {
    public Integer id;
    public Integer employeeDivisionId;
    public Integer employeeProjectId;
    public Integer projectTaskId;
    public String description;
    public Short hoursWorked;
    public Integer submittedBy;
    public LocalDateTime submittedOn;
    public Byte status;
    public Integer approvedBy;

    public TimesheetDto() {
    }

    public TimesheetDto(Integer id, Integer employeeDivisionId, Integer employeeProjectId, Integer projectTaskId, String description,
            Short hoursWorked, Integer submittedBy, LocalDateTime submittedOn, Byte status, Integer approvedBy,
            Integer createdBy, LocalDateTime createdOn, Integer modifiedBy, LocalDateTime modifiedOn, Integer deletedBy,
            LocalDateTime deletedOn) {
        super(createdBy, createdOn, modifiedBy, modifiedOn, deletedBy, deletedOn);
        this.id = id;
        this.employeeDivisionId = employeeDivisionId;
        this.employeeProjectId = employeeProjectId;
        this.projectTaskId = projectTaskId;
        this.description = description;
        this.hoursWorked = hoursWorked;
        this.submittedBy = submittedBy;
        this.submittedOn = submittedOn;
        this.status = status;
        this.approvedBy = approvedBy;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeDivisionId() {
        return this.employeeDivisionId;
    }   

    public void setEmployeeDivisionId(Integer employeeDivisionId) {
        this.employeeDivisionId = employeeDivisionId;
    }

    public Integer getEmployeeProjectId() {
        return this.employeeProjectId;
    }

    public void setEmployeeProjectId(Integer employeeProjectId) {
        this.employeeProjectId = employeeProjectId;
    }

    public Integer getProjectTaskId() {
        return this.projectTaskId;
    }

    public void setProjectTaskId(Integer projectTaskId) {
        this.projectTaskId = projectTaskId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Short getHoursWorked() {
        return this.hoursWorked;
    }

    public void setHoursWorked(Short hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public Integer getSubmittedBy() {
        return this.submittedBy;
    }

    public void setSubmittedBy(Integer submittedBy) {
        this.submittedBy = submittedBy;
    }

    public LocalDateTime getSubmittedOn() {
        return this.submittedOn;
    }

    public void setSubmittedOn(LocalDateTime submittedOn) {
        this.submittedOn = submittedOn;
    }

    public Byte getStatus() {
        return this.status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getApprovedBy() {
        return this.approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", employeeDivisionId='" + getEmployeeDivisionId() + "'" +
                ", employeeProjectId='" + getEmployeeProjectId() + "'" +
                ", projectTaskId='" + getProjectTaskId() + "'" +
                ", description='" + getDescription() + "'" +
                ", hoursWorked='" + getHoursWorked() + "'" +
                ", submittedBy='" + getSubmittedBy() + "'" +
                ", submittedOn='" + getSubmittedOn() + "'" +
                ", status='" + getStatus() + "'" +
                ", approvedBy='" + getApprovedBy() + "'" +
                "}" + "," + super.toString();
    }

}
