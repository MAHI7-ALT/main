package org.imaginnovate.Dto;

import java.time.LocalDateTime;

import org.imaginnovate.BaseClass.BaseAuditFieldsDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name", "description", "createdBy", "createdOn", "modifiedBy", "modifiedOn", "deletedBy",
        "deletedOn" })
public class TaskDto extends BaseAuditFieldsDTO {

    public Integer id;
    public String name;
    public String description;

    public TaskDto() {
    }

    public TaskDto(Integer id, String name, String description, Integer createdBy, LocalDateTime createdOn,
            Integer modifiedBy, LocalDateTime modifiedOn, Integer deletedBy, LocalDateTime deletedOn) {
        super(createdBy, createdOn, modifiedBy, modifiedOn, deletedBy, deletedOn);
        this.id = id;
        this.name = name;
        this.description = description;
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

    public TaskDto id(Integer id) {
        setId(id);
        return this;
    }

    public TaskDto name(String name) {
        setName(name);
        return this;
    }

    public TaskDto description(String description) {
        setDescription(description);
        return this;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", name='" + getName() + "'" +
                ", description='" + getDescription() + "'" +
                "}" + "," + super.toString();
    }
}
