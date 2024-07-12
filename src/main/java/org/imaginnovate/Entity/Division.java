package org.imaginnovate.Entity;

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

@Entity
@Dependent
@Table(name = "divisions")
@JsonPropertyOrder({ "id", "name", "parentId", "createdBy", "createdOn", "modifiedBy", "modifiedOn", "deletedBy",
        "deletedOn" })

public class Division extends BaseAuditFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "name", unique = true, nullable = false, length = 40)
    public String name;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "parent_id")
    public Division parentId;

    public Division() {
    }

    public Division(Integer id, String name, Division parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

}