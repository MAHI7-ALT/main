package org.imaginnovate.Entity;

import java.io.Serializable;

import org.imaginnovate.BaseClass.BaseAuditFields;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "timesheet_status")
@JsonPropertyOrder({ "id", "name", "createdBy", "createdOn", "modifiedBy", "modifiedOn", "deletedBy", "deletedOn" })
public class TimesheetStatus extends BaseAuditFields implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Byte id;

    @Column(name = "name", nullable = false, length = 10)
    public String name;

    public TimesheetStatus() {
    }

    public TimesheetStatus(Byte id, String name) {
        this.id = id;
        this.name = name;
    }

}
