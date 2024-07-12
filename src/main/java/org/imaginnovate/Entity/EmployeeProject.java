package org.imaginnovate.Entity;

import java.io.Serializable;

import org.imaginnovate.BaseClass.BaseAuditFields;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "employee_projects", uniqueConstraints = @UniqueConstraint(columnNames = { "employee_id", "project_id" }))
@JsonPropertyOrder({ "id", "employeeId", "projectId", "canApproveTimesheets", "createdBy", "createdOn", "modifiedBy",
        "modifiedOn", "deletedBy", "deletedOn" })
public class EmployeeProject extends BaseAuditFields implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "employee_id", nullable = false)
    public Employee employeeId;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "project_id", nullable = false)
    public Project projectId;

    @Column(name = "can_approve_timesheets", nullable = false)
    public Boolean canApproveTimesheets;

    public EmployeeProject() {
    }

    public EmployeeProject(Integer id, Employee employeeId, Project projectId, Boolean canApproveTimesheets) {
        this.id = id;
        this.employeeId = employeeId;
        this.projectId = projectId;
        this.canApproveTimesheets = canApproveTimesheets;
    }

}
