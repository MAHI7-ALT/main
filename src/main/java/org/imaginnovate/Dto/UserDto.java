package org.imaginnovate.Dto;

import java.time.LocalDateTime;

import org.imaginnovate.BaseClass.BaseAuditFieldsDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "userName", "employeeId", "password", "resetToken", "resetTokenExpiresAt", "createdBy",
        "createdOn", "modifiedBy", "modifiedOn", "deletedBy", "deletedOn" })
public class UserDto extends BaseAuditFieldsDTO {

    public Integer id;
    public String userName;
    public Integer employeeId;
    public String password;
    public String resetToken;
    public LocalDateTime resetTokenExpiresAt;

    public UserDto() {
    }

    public UserDto(Integer id, String userName, Integer employeeId, String password, String resetToken,
            LocalDateTime resetTokenExpiresAt, Integer createdBy, LocalDateTime createdOn,
            Integer modifiedBy, LocalDateTime modifiedOn, Integer deletedBy, LocalDateTime deletedOn) {
        super(createdBy, createdOn, modifiedBy, modifiedOn, deletedBy, deletedOn);
        this.id = id;
        this.userName = userName;
        this.employeeId = employeeId;
        this.password = password;
        this.resetToken = resetToken;
        this.resetTokenExpiresAt = resetTokenExpiresAt;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getResetToken() {
        return this.resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public LocalDateTime getResetTokenExpiresAt() {
        return this.resetTokenExpiresAt;
    }

    public void setResetTokenExpiresAt(LocalDateTime resetTokenExpiresAt) {
        this.resetTokenExpiresAt = resetTokenExpiresAt;
    }

    public UserDto id(Integer id) {
        setId(id);
        return this;
    }

    public UserDto userName(String userName) {
        setUserName(userName);
        return this;
    }

    public UserDto employeeId(Integer employeeId) {
        setEmployeeId(employeeId);
        return this;
    }

    public UserDto password(String password) {
        setPassword(password);
        return this;
    }

    public UserDto resetToken(String resetToken) {
        setResetToken(resetToken);
        return this;
    }

    public UserDto resetTokenExpiresAt(LocalDateTime resetTokenExpiresAt) {
        setResetTokenExpiresAt(resetTokenExpiresAt);
        return this;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", userName='" + getUserName() + "'" +
                ", employeeId='" + getEmployeeId() + "'" +
                ", password='" + getPassword() + "'" +
                ", resetToken='" + getResetToken() + "'" +
                ", resetTokenExpiresAt='" + getResetTokenExpiresAt() + "'" +
                "}" + "," + super.toString();
    }

    public void setUserName(Object username2) {
        this.userName = (String) username2;
    }
}
