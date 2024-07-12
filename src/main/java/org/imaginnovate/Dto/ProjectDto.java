package org.imaginnovate.Dto;

import java.time.LocalDateTime;

import org.imaginnovate.BaseClass.BaseAuditFieldsDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name", "description", "divisionId", "createdBy", "createdOn", "modifiedBy", "modifiedOn", "deletedBy", "deletedOn" })
public class ProjectDto extends BaseAuditFieldsDTO {

    public Integer id;
    public String name;
    public String description;
    public Integer divisionId;

    public ProjectDto() {
    }

    public ProjectDto(Integer id, String name, String description, Integer divisionId, Integer createdBy,
                LocalDateTime createdOn, Integer modifiedBy, LocalDateTime modifiedOn,Integer deletedBy, LocalDateTime deletedOn) {
    super(createdBy, createdOn, modifiedBy, modifiedOn, deletedBy, deletedOn);
    this.id = id;
    this.name = name;
    this.description = description;
    this.divisionId = divisionId;
}


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDivisionId() {
        return this.divisionId;
    }

    public void setDivisionId(Integer divisionId) {
        this.divisionId = divisionId;
    }

    // Fluent setters

    public ProjectDto id(Integer id) {
        setId(id);
        return this;
    }

    public ProjectDto name(String name) {
        setName(name);
        return this;
    }

    public ProjectDto description(String description) {
        setDescription(description);
        return this;
    }

    public ProjectDto divisionId(Integer divisionId) {
        setDivisionId(divisionId);
        return this;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", name='" + getName() + "'" +
                ", description='" + getDescription() + "'" +
                ", divisionId='" + getDivisionId() + "'" +
                "}" + "," + super.toString();
    }
}
