package org.imaginnovate.Entity;

import java.io.Serializable;

import org.imaginnovate.BaseClass.BaseAuditFields;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.enterprise.context.Dependent;
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
@Dependent
@Table(name = "employee_divisions", uniqueConstraints = @UniqueConstraint(columnNames = { "employee_id",
        "division_id" }))
@JsonPropertyOrder({ "id", "employeeId", "divisionId", "canApproveTimesheets", "createdBy", "createdOn", "modifiedBy",
        "modifiedOn", "deletedBy", "deletedOn" })
public class EmployeeDivision extends BaseAuditFields implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "employee_id", nullable = false)
    public Employee employeeId;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "division_id", nullable = false)
    public Division divisionId;

    @Column(name = "primary_division", nullable = false)
    public Boolean primaryDivision;

    @Column(name = "can_approve_timesheets", nullable = false)
    public Boolean canApproveTimesheets;

    public EmployeeDivision() {
    }

    public EmployeeDivision(Integer id, Employee employeeId, Division divisionId,Boolean primaryDivision, Boolean canApproveTimesheets) {
        this.id = id;
        this.employeeId = employeeId;
        this.divisionId = divisionId;
        this.primaryDivision=primaryDivision;
        this.canApproveTimesheets = canApproveTimesheets;
    }

}