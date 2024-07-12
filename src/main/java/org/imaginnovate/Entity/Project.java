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

@Entity
@Table(name = "projects")
@JsonPropertyOrder({ "id", "name", "description", "divisionId", "createdBy", "createdOn", "modifiedBy", "modifiedOn",
        "deletedBy", "deletedOn" })
public class Project extends BaseAuditFields implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "name", unique = true, nullable = false, length = 50)
    public String name;

    @Column(name = "description", length = 200)
    public String description;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "division_id", nullable = false)
    public Division divisionId;

    public Project() {
    }

    public Project(Integer id, String name, String description, Division divisionId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.divisionId = divisionId;
    }

}
