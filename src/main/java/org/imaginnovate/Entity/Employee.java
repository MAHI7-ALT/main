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
@Table(name = "employees")
@JsonPropertyOrder({ "id", "firstName", "lastName", "gender", "email", "designation", "startDate", "endDate",
        "reportsToId", "createdBy", "createdOn", "modifiedBy", "modifiedOn", "deletedBy", "deletedOn" })
public class Employee extends BaseAuditFields implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "first_name", nullable = false, length = 60)
    public String firstName;

    @Column(name = "last_name", nullable = false, length = 40)
    public String lastName;

    @Column(name = "gender", nullable = false, length = 1)
    public Character gender;

    @Column(name = "email", unique = true, nullable = false, length = 80)
    public String email;

    @Column(name = "designation", nullable = false, length = 30)
    public String designation;

    @Column(name = "start_date", nullable = false)
    public LocalDateTime startDate;

    @Column(name = "end_date")
    public LocalDateTime endDate;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "reports_to_id", nullable = true)
    public Employee reportsToId;

    public Employee() {
    }

    public Employee(Integer id, String firstName, String lastName, Character gender, String email, String designation,
            LocalDateTime startDate, LocalDateTime endDate, Employee reportsToId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.designation = designation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reportsToId = reportsToId;
    }

    public Employee getReportsToId() {
        return this.reportsToId;
    }

  

}