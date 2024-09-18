package org.imaginnovate.Service;

import java.util.List;

import org.imaginnovate.Dto.UserDto;
import org.imaginnovate.Entity.Employee;
import org.imaginnovate.Entity.User;
import org.imaginnovate.Repository.EmployeeRepo;
import org.imaginnovate.Repository.UserRepo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepo usersRepo;

    @Inject
    EmployeeRepo employeesRepo;

    public Response getAllUsers() {
        try {
            List<UserDto> users = usersRepo.findAllUsers();
            if (users.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("There are no existing users").build();
            }
            return Response.status(Response.Status.OK).entity(users).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }

    @Transactional
    public Response createUser(UserDto usersDto) {
        if (usersDto.getId() != null && usersDto.getId() > 0) {
            if (usersRepo.findById(usersDto.getId()) != null) {
                return Response.status(Response.Status.CONFLICT).entity("User already exists").build();
            }
        }

        if (usersDto.getUserName() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Username cannot be null").build();
        }

        User user = new User();
        user.userName = usersDto.getUserName();
        user.password = usersDto.getPassword();
        user.resetToken = usersDto.getResetToken();
        user.resetTokenExpiresAt = usersDto.getResetTokenExpiresAt();

        if (usersDto.getEmployeeId() != 0) {
            Employee employee = employeesRepo.findById(usersDto.getEmployeeId());
            if (employee != null) {
                user.employeeId = employee;
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Employee with ID " + usersDto.getEmployeeId() + " not found").build();
            }
        }

        if (usersDto.getCreatedBy() != null) {
            Employee createdBy = employeesRepo.findByIdOptional(usersDto.getCreatedBy()).orElse(null);
            if (createdBy == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("CreatedBy employee with ID " + usersDto.getCreatedBy() + " not found")
                        .build();
            }
    
            boolean canApprove = employeesRepo.canApproveTimesheets(usersDto.getCreatedBy());
            if (!canApprove) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Employee with ID " + usersDto.getCreatedBy() + " does not have createdBy rights")
                        .build();
            }
        }else {
            Employee reportsToEmployee = employeesRepo.findById(usersDto.getCreatedBy());
            if (reportsToEmployee != null) {
                usersDto.setCreatedBy(reportsToEmployee.id);
            }
        }
        user.createdOn = usersDto.getCreatedOn();
        user.modifiedOn = usersDto.getModifiedOn();
        user.deletedOn = usersDto.getDeletedOn();

        usersRepo.persist(user);
        usersDto.setId(user.id);
        return Response.status(Response.Status.OK).entity(usersDto).build();
    }

    public Response getUserById(Integer id) {
        try {
            User users = usersRepo.findById(id);
            if (users == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("No users found").build();
            }
            UserDto dto = new UserDto();
            dto.setId(users.id);
            dto.setUserName(users.userName);
            dto.setPassword(users.password);
            dto.setEmployeeId(users.employeeId.id);
            dto.setResetToken(users.resetToken);
            dto.setResetTokenExpiresAt(users.resetTokenExpiresAt);
            dto.setCreatedBy(users.createdBy.id);
            dto.setCreatedOn(users.createdOn);
            if (users.deletedBy != null) {
                dto.setDeletedBy(users.deletedBy.id);
            }
            dto.setDeletedOn(users.deletedOn);
            if (users.modifiedBy != null) {
                dto.setModifiedBy(users.modifiedBy.id);
            }
            dto.setModifiedOn(users.modifiedOn);
            return Response.status(Response.Status.OK).entity(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(" Internal Server Error").build();
        }
    }

    @Transactional
    public Response updateUserById(Integer id, UserDto userDto) {
        try {
            User user = usersRepo.findById(id);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
            }
            
            if (userDto.getUserName() != null) {
                user.userName = userDto.getUserName();
            }
            if (userDto.getPassword() != null) {
                user.password = userDto.getPassword();
            }
            if (userDto.getResetToken() != null) {
                user.resetToken = userDto.getResetToken();
            }
            if (userDto.getResetTokenExpiresAt() != null) {
                user.resetTokenExpiresAt = userDto.getResetTokenExpiresAt();
            }
    
            if (userDto.getEmployeeId() != null) {
                Employee employee = employeesRepo.findById(userDto.getEmployeeId());
                if (employee != null) {
                    user.employeeId = employee;
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Employee ID").build();
                }
            }
    
            if (userDto.getCreatedBy() != null) {
                Employee createdBy = employeesRepo.findById(userDto.getCreatedBy());
                if (createdBy != null) {
                    boolean canApprove = employeesRepo.canApproveTimesheets(userDto.getCreatedBy());
                    if (!canApprove) {
                        return Response.status(Response.Status.FORBIDDEN)
                                .entity("Employee with ID " + userDto.getCreatedBy() + " does not have approval rights")
                                .build();
                    }
                    user.createdBy = createdBy;
                } else {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Employee with ID " + userDto.getCreatedBy() + " (CreatedBy) not found")
                            .build();
                }
            }
    
            if (userDto.getModifiedBy() != null) {
                Employee modifiedBy = employeesRepo.findById(userDto.getModifiedBy());
                if (modifiedBy != null) {
                    boolean canApprove = employeesRepo.canApproveTimesheets(userDto.getModifiedBy());
                    if (!canApprove) {
                        return Response.status(Response.Status.FORBIDDEN)
                                .entity("Employee with ID " + userDto.getModifiedBy() + " does not have approval rights")
                                .build();
                    }
                    user.modifiedBy = modifiedBy;
                } else {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Employee with ID " + userDto.getModifiedBy() + " (ModifiedBy) not found")
                            .build();
                }
            }
    
            if (userDto.getDeletedBy() != null) {
                Employee deletedBy = employeesRepo.findById(userDto.getDeletedBy());
                if (deletedBy != null) {
                    boolean canApprove = employeesRepo.canApproveTimesheets(userDto.getDeletedBy());
                    if (!canApprove) {
                        return Response.status(Response.Status.FORBIDDEN)
                                .entity("Employee with ID " + userDto.getDeletedBy() + " does not have approval rights")
                                .build();
                    }
                    user.deletedBy = deletedBy;
                } else {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Employee with ID " + userDto.getDeletedBy() + " (DeletedBy) not found")
                            .build();
                }
            }
    
            if (userDto.getCreatedOn() != null) {
                user.createdOn = userDto.getCreatedOn();
            }
    
            if (userDto.getModifiedOn() != null) {
                user.modifiedOn = userDto.getModifiedOn();
            }
    
            if (userDto.getDeletedOn() != null) {
                user.deletedOn = userDto.getDeletedOn();
            }
    
            usersRepo.persist(user);
    
            UserDto dto = new UserDto();
            dto.setId(user.id);
            dto.setUserName(user.userName);
            dto.setPassword(user.password);
            dto.setEmployeeId(user.employeeId.id);
            dto.setResetToken(user.resetToken);
            dto.setResetTokenExpiresAt(user.resetTokenExpiresAt);
            dto.setCreatedBy(user.createdBy != null ? user.createdBy.id : null);
            dto.setCreatedOn(user.createdOn);
            dto.setDeletedBy(user.deletedBy != null ? user.deletedBy.id : null);
            dto.setDeletedOn(user.deletedOn);
            dto.setModifiedBy(user.modifiedBy != null ? user.modifiedBy.id : null);
            dto.setModifiedOn(user.modifiedOn);
            return Response.status(Response.Status.OK).entity(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }
    
    @Transactional
    public Response deleteUserById(Integer id) {
        try {
            User users = usersRepo.findById(id);
            if (users == null) {
                throw new IllegalArgumentException("User with ID " + id + " not found");
            } else {
                usersRepo.deleteById(id);
            }
            return Response.status(Response.Status.OK).entity("User with ID " + id + " deleted successfully").build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }
}
