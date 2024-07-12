package org.imaginnovate.BaseClass;

import java.time.LocalDateTime;

import org.imaginnovate.Entity.Employee;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseAuditFields extends PanacheEntityBase {

    @ManyToOne
    @JoinColumn(name = "created_by")
    public Employee createdBy;

    @Column(name = "created_on", nullable = false)
    public LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name = "modified_by")
    public Employee modifiedBy;

    @Column(name = "modified_on")
    public LocalDateTime modifiedOn;

    @ManyToOne
    @JoinColumn(name = "deleted_by")
    public Employee deletedBy;

    @Column(name = "deleted_on")
    public LocalDateTime deletedOn;

    public BaseAuditFields() {
    }


    public BaseAuditFields(Employee createdBy, LocalDateTime createdOn, Employee modifiedBy, LocalDateTime modifiedOn, Employee deletedBy, LocalDateTime deletedOn) {
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.modifiedBy = modifiedBy;
        this.modifiedOn = modifiedOn;
        this.deletedBy = deletedBy;
        this.deletedOn = deletedOn;
    }

}
