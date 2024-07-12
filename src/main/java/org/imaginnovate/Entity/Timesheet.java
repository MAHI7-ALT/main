package org.imaginnovate.Entity;

import java.io.Serializable;
import java.time.LocalDateTime;

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

@Entity
@Table(name = "timesheets")
@JsonPropertyOrder({ "id","employeeDivisionId", "employeeProjectId", "projectTaskId", "description", "hoursWorked", "submittedBy",
        "submittedOn", "status", "approvedBy", "createdBy", "createdOn", "modifiedBy", "modifiedOn", "deletedBy",
        "deletedOn" })
public class Timesheet extends BaseAuditFields implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_division_id", nullable = false)
    public EmployeeDivision employeeDivisionId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_project_id", nullable = false)
    public EmployeeProject employeeProjectId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_task_id", nullable = false)
    public ProjectTask projectTaskId;

    @Column(name = "description", length = 100)
    public String description;

    @Column(name = "hours_worked", nullable = false)
    public Short hoursWorked;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "submitted_by", nullable = false)
    public Employee submittedBy;

    @Column(name = "submitted_on", nullable = false)
    public LocalDateTime submittedOn;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "status", nullable = false)
    public TimesheetStatus status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "approved_by", nullable = false)
    public Employee approvedBy;

    public Timesheet() {
    }

    public Timesheet(Integer id,EmployeeDivision employeeDivisionId, EmployeeProject employeeProjectId, ProjectTask projectTaskId, String description,
            Short hoursWorked, Employee submittedBy, LocalDateTime submittedOn, TimesheetStatus status,
            Employee approvedBy) {
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

    public void setApprovedBy(Employee employee) {
        this.approvedBy = employee;
    }

    public boolean isPresent() {
        return approvedBy != null;
    }

}