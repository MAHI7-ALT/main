package org.imaginnovate.Dto;

import java.time.LocalDateTime;

import org.imaginnovate.BaseClass.BaseAuditFieldsDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "projectId", "taskId", "createdBy", "createdOn", "modifiedBy", "modifiedOn", "deletedBy",
        "deletedOn" })
public class ProjectTaskDto extends BaseAuditFieldsDTO {

    public Integer id;
    public Integer projectId;
    public Integer taskId;

    public ProjectTaskDto() {
    }

    public ProjectTaskDto(Integer id, Integer projectId, Integer taskId, Integer createdBy, LocalDateTime createdOn,
            Integer modifiedBy, LocalDateTime modifiedOn, Integer deletedBy, LocalDateTime deletedOn) {
        super(createdBy, createdOn, modifiedBy, modifiedOn, deletedBy, deletedOn);
        this.id = id;
        this.projectId = projectId;
        this.taskId = taskId;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return this.projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public ProjectTaskDto id(Integer id) {
        setId(id);
        return this;
    }

    public ProjectTaskDto projectId(Integer projectId) {
        setProjectId(projectId);
        return this;
    }

    public ProjectTaskDto taskId(Integer taskId) {
        setTaskId(taskId);
        return this;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", projectId='" + getProjectId() + "'" +
                ", taskId='" + getTaskId() + "'" +
                "}" + "," + super.toString();
    }
}
