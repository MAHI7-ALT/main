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
@Table(name = "users")
@JsonPropertyOrder({ "id", "userName", "employeeId", "password", "resetToken", "resetTokenExpiresAt", "createdBy",
        "createdOn", "modifiedBy", "modifiedOn", "deletedBy", "deletedOn" })
public class User extends BaseAuditFields implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "user_name", nullable = false, length = 60, unique = true)
    public String userName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id", nullable = false)
    public Employee employeeId;

    @Column(name = "password", nullable = false, length = 12)
    public String password;

    @Column(name = "reset_token", nullable = false, length = 10)
    public String resetToken;

    @Column(name = "reset_token_expires_at", nullable = false)
    public LocalDateTime resetTokenExpiresAt;

    public User() {
    }

    public User(Integer id, String userName, Employee employeeId, String password, String resetToken,
            LocalDateTime resetTokenExpiresAt) {
        this.id = id;
        this.userName = userName;
        this.employeeId = employeeId;
        this.password = password;
        this.resetToken = resetToken;
        this.resetTokenExpiresAt = resetTokenExpiresAt;
    }

}