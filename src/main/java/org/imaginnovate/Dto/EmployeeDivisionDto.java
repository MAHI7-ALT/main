package org.imaginnovate.Dto;

import java.time.LocalDateTime;

import org.imaginnovate.BaseClass.BaseAuditFieldsDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "employeeId", "divisionId","primaryDivision", "canApproveTimesheets", "createdBy", "createdOn", "modifiedBy",
        "modifiedOn", "deletedBy", "deletedOn" })
public class EmployeeDivisionDto extends BaseAuditFieldsDTO {

    public Integer id;
    public Integer employeeId;
    public Integer divisionId;
    public Boolean primaryDivision;
    public Boolean canApproveTimesheets;

    public EmployeeDivisionDto() {
    }

    public EmployeeDivisionDto(Integer id, Integer employeeId, Integer divisionId, Boolean primaryDivision, Boolean canApproveTimesheets,
    Integer createdBy, LocalDateTime createdOn,
    Integer modifiedBy, LocalDateTime modifiedOn, Integer deletedBy, LocalDateTime deletedOn)
{
        super(createdBy, createdOn, modifiedBy, modifiedOn, deletedBy, deletedOn);
        this.id = id;
        this.employeeId = employeeId;
        this.divisionId = divisionId;
        this.primaryDivision=primaryDivision;
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

    public Integer getDivisionId() {
        return this.divisionId;
    }

    public void setDivisionId(Integer divisionId) {
        this.divisionId = divisionId;
    }

    public Boolean getPrimaryDivision() {
        return primaryDivision;
    }

    public void setPrimaryDivision(Boolean primaryDivision) {
        this.primaryDivision = primaryDivision;
    }
    
    public Boolean getCanApproveTimesheets() {
        return this.canApproveTimesheets;
    }

    public void setCanApproveTimesheets(Boolean canApproveTimesheets) {
        this.canApproveTimesheets = canApproveTimesheets;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", employeeId='" + getEmployeeId()+
                ", divisionId='" + getDivisionId() +
                ",primaryDivision="+getPrimaryDivision()+
                ", canApproveTimesheets='" + getCanApproveTimesheets() + "'" +
                "}" + "," + super.toString();
    }


}
