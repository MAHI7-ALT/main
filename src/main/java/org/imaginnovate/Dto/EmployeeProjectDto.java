package org.imaginnovate.Dto;

import java.time.LocalDateTime;

import org.imaginnovate.BaseClass.BaseAuditFieldsDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "employeeId", "projectId", "canApproveTimesheets", "createdBy", "createdOn", "modifiedBy",
        "modifiedOn", "deletedBy", "deletedOn" })
public class EmployeeProjectDto extends BaseAuditFieldsDTO {

    public Integer id;
    public Integer employeeId;
    public Integer projectId;
    public Boolean canApproveTimesheets;

    public EmployeeProjectDto() {
    }

    public EmployeeProjectDto(Integer id, Integer employeeId, Integer projectId, Boolean canApproveTimesheets,
            Integer createdBy, LocalDateTime createdOn,
            Integer modifiedBy, LocalDateTime modifiedOn, Integer deletedBy, LocalDateTime deletedOn) {
        super(createdBy, createdOn, modifiedBy, modifiedOn, deletedBy, deletedOn);
        this.id = id;
        this.employeeId = employeeId;
        this.projectId = projectId;
        this.canApproveTimesheets = canApproveTimesheets;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getProjectId() {
        return this.projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Boolean getCanApproveTimesheets() {
        return this.canApproveTimesheets;
    }

    public void setCanApproveTimesheets(Boolean canApproveTimesheets) {
        this.canApproveTimesheets = canApproveTimesheets;
    }

    public EmployeeProjectDto id(Integer id) {
        setId(id);
        return this;
    }

    public EmployeeProjectDto employeeId(Integer employeeId) {
        setEmployeeId(employeeId);
        return this;
    }

    public EmployeeProjectDto projectId(Integer projectId) {
        setProjectId(projectId);
        return this;
    }

    public EmployeeProjectDto canApproveTimesheets(Boolean canApproveTimesheets) {
        setCanApproveTimesheets(canApproveTimesheets);
        return this;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", employeeId='" + getEmployeeId() + "'" +
                ", projectId='" + getProjectId() + "'" +
                ", canApproveTimesheets='" + getCanApproveTimesheets() + "'" +
                "}" + "," + super.toString();
    }
}
