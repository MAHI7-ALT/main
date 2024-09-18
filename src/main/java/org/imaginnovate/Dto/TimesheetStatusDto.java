package org.imaginnovate.Dto;

import java.time.LocalDateTime;

import org.imaginnovate.BaseClass.BaseAuditFieldsDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name", "createdBy", "createdOn", "modifiedBy", "modifiedOn", "deletedBy", "deletedOn" })
public class TimesheetStatusDto extends BaseAuditFieldsDTO {

    public Byte id;
    public String name;

    public TimesheetStatusDto() {
    }

    public TimesheetStatusDto(Byte id, String name, Integer createdBy, LocalDateTime createdOn,
            Integer modifiedBy, LocalDateTime modifiedOn, Integer deletedBy, LocalDateTime deletedOn) {
        super(createdBy, createdOn, modifiedBy, modifiedOn, deletedBy, deletedOn);
        this.id = id;
        this.name = name;
    }

    public Byte getId() {
        return this.id;
    }

    public void setId(Byte id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TimesheetStatusDto id(Byte id) {
        setId(id);
        return this;
    }

    public TimesheetStatusDto name(String name) {
        setName(name);
        return this;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", name='" + getName() + "'" +
                "}" + "," + super.toString();
    }

   

}
