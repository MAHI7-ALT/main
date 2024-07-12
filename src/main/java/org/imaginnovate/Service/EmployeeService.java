package org.imaginnovate.Service;

import java.util.List;

import org.imaginnovate.Dto.EmployeeDto;
import org.imaginnovate.Entity.Employee;
import org.imaginnovate.Repository.DivisionRepo;
import org.imaginnovate.Repository.EmployeeRepo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class EmployeeService {
    @Inject
    EmployeeRepo employeeRepo;

    @Inject
    DivisionRepo divisionRepo;

    public Response getAllEmployees() {
        try {
            List<EmployeeDto> employees = employeeRepo.findAllEmployees();
            if (employees == null || employees.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("There are no employees")
                        .build();
            } else {
                return Response.ok(employees).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Internal server error while fetching employees: " + e.getMessage())
                    .build();
        }
    }

    @Transactional
    public Response createEmployee(EmployeeDto employeeDto) {
        try {
            if (employeeDto.getId() != null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("ID should not be provided, it will be generated automatically")
                        .build();
            }
    
            if (employeeDto.getReportsToId() != null) {
                Employee reportsTo = findEmployeeByIdOptional(employeeDto.getReportsToId());
                if (reportsTo == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("ReportsTo employee with ID " + employeeDto.getReportsToId() + " not found")
                            .build();
                }
            }
    
            if (employeeDto.getCreatedBy() != null) {
                Employee createdBy = findEmployeeByIdOptional(employeeDto.getCreatedBy());
                if (createdBy == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("CreatedBy employee with ID " + employeeDto.getCreatedBy() + " not found")
                            .build();
                }
    
                boolean canApprove = employeeRepo.canApproveTimesheets(employeeDto.getCreatedBy());
                if (!canApprove) {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity("Employee with ID " + employeeDto.getCreatedBy() + " does not have createdBy rights")
                            .build();
                }
            }
    
            Employee employee = convertToEntity(employeeDto);
            employeeRepo.persist(employee);
            return Response.status(Response.Status.CREATED)
                    .entity(convertToDto(employee))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Internal server error while adding employee")
                    .build();
        }
    }
    

    @Transactional
    public Response updateEmployeeById(int id, EmployeeDto employeeDto) {
        Employee existingEmployee = employeeRepo.findById(id);
        if (existingEmployee == null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Employee not exists")
                    .build();
        }
    
        if (employeeDto.firstName != null) {
            existingEmployee.firstName = employeeDto.firstName;
        }
        if (employeeDto.lastName != null) {
            existingEmployee.lastName = employeeDto.lastName;
        }
        if (employeeDto.email != null) {
            existingEmployee.email = employeeDto.email;
        }
        if (employeeDto.designation != null) {
            existingEmployee.designation = employeeDto.designation;
        }
        if (employeeDto.startDate != null) {
            existingEmployee.startDate = employeeDto.startDate;
        }
        existingEmployee.endDate = employeeDto.endDate;
    
        if (employeeDto.getReportsToId() != null && employeeDto.getReportsToId() != 0) {
            Employee reportsTo = employeeRepo.findById(employeeDto.getReportsToId());
            if (reportsTo != null) {
                existingEmployee.reportsToId = reportsTo;
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Employee with ID " + employeeDto.getReportsToId() + " not found")
                        .build();
            }
        }
    
        if (employeeDto.getCreatedBy() != null) {
            Employee createdBy = employeeRepo.findById(employeeDto.getCreatedBy());
            if (createdBy == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("CreatedBy employee with ID " + employeeDto.getCreatedBy() + " not found")
                        .build();
            }
    
            boolean canApprove = employeeRepo.canApproveTimesheets(employeeDto.getCreatedBy());
            if (!canApprove) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Employee with ID " + employeeDto.getCreatedBy() + " does not have createdBy rights")
                        .build();
            }
    
            existingEmployee.createdBy = createdBy;
        }
    
        if (employeeDto.getCreatedOn() != null) {
            existingEmployee.createdOn = employeeDto.getCreatedOn();
        }
    
        if (employeeDto.getModifiedBy() != null) {
            Employee modifiedBy = employeeRepo.findById(employeeDto.getModifiedBy());
            if (modifiedBy == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("ModifiedBy employee with ID " + employeeDto.getModifiedBy() + " not found")
                        .build();
            }
    
            boolean canApprove = employeeRepo.canApproveTimesheets(employeeDto.getModifiedBy());
            if (!canApprove) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Employee with ID " + employeeDto.getModifiedBy() + " does not have modifiedBy rights")
                        .build();
            }
    
            existingEmployee.modifiedBy = modifiedBy;
        }
    
        if (employeeDto.getModifiedOn() != null) {
            existingEmployee.modifiedOn = employeeDto.getModifiedOn();
        }
    
        if (employeeDto.getDeletedBy() != null) {
            Employee deletedBy = employeeRepo.findById(employeeDto.getDeletedBy());
            if (deletedBy == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("DeletedBy employee with ID " + employeeDto.getDeletedBy() + " not found")
                        .build();
            }
    
            boolean canApprove = employeeRepo.canApproveTimesheets(employeeDto.getDeletedBy());
            if (!canApprove) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Employee with ID " + employeeDto.getDeletedBy() + " does not have deletedBy rights")
                        .build();
            }
    
            existingEmployee.deletedBy = deletedBy;
        }
    
        if (employeeDto.getDeletedOn() != null) {
            existingEmployee.deletedOn = employeeDto.getDeletedOn();
        }
    
        employeeRepo.persist(existingEmployee);
        EmployeeDto updatedEmployeeDto = convertToDto(existingEmployee);
        return Response.ok(updatedEmployeeDto).build();
    }
    
    @Transactional
    public Response deleteEmployeeById(int id) {
        try {
            Employee employee = findEmployeeByIdOptional(id);
            if (employee == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Employee with ID " + id + " not found")
                        .build();
            } else {
                employeeRepo.delete(employee);
            }
            return Response.status(Response.Status.OK).entity("Employee id with Id" + id + "successfully deleted")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal server error").build();
        }
    }

    public Response getEmployeeById(int id) {
        try {
            Employee employee = employeeRepo.findById(id);
            if (employee == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Employee with the id is not found")
                        .build();
            }
            return Response.ok(convertToDto(employee)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Internal server error while fetching employee by ID")
                    .build();
        }
    }

    private Employee convertToEntity(EmployeeDto dto) {
        Employee employee = new Employee();
        employee.firstName = dto.getFirstName();
        employee.lastName = dto.getLastName();
        employee.email = dto.getEmail();
        employee.gender = dto.getGender();
        employee.designation = dto.getDesignation();
        employee.startDate = dto.getStartDate();
        employee.endDate = dto.getEndDate();

        if (dto.getReportsToId() != null) {
            Employee reportsTo = findEmployeeByIdOptional(dto.getReportsToId());
            if (reportsTo != null) {
                employee.reportsToId = reportsTo;
            } else {
                throw new IllegalArgumentException("Invalid reportsToId: " + dto.getReportsToId());
            }
        } else {
            employee.reportsToId = null;
        }

        if (dto.getCreatedBy() != null) {
            Employee createdBy = findEmployeeByIdOptional(dto.getCreatedBy());
            if (createdBy != null) {
                employee.createdBy = createdBy;
            } else {
                throw new IllegalArgumentException("Invalid createdBy ID: " + dto.getCreatedBy());
            }
        } else {
            employee.createdBy = null;
        }

      
        employee.modifiedBy = dto.getModifiedBy() != null ? findEmployeeByIdOptional(dto.getModifiedBy()) : null;
        employee.deletedBy = dto.getDeletedBy() != null ? findEmployeeByIdOptional(dto.getDeletedBy()) : null;
        employee.createdOn = dto.getCreatedOn();
        employee.modifiedOn = dto.getModifiedOn();
        employee.deletedOn = dto.getDeletedOn();
        return employee;
    }

    private Employee findEmployeeByIdOptional(Integer deletedBy) {
        if (deletedBy == null)
            return null;
        return employeeRepo.findById(deletedBy);
    }

    private EmployeeDto convertToDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();
        dto.id = employee.id;
        dto.firstName = employee.firstName;
        dto.lastName = employee.lastName;
        dto.gender = employee.gender;
        dto.email = employee.email;
        dto.designation = employee.designation;
        dto.startDate = employee.startDate;
        dto.endDate = employee.endDate;
        dto.reportsToId = employee.reportsToId != null ? employee.reportsToId.id : null;
        dto.createdBy = employee.createdBy != null ? employee.createdBy.id : null;
        dto.createdOn = employee.createdOn;
        dto.modifiedBy = employee.modifiedBy != null ? employee.modifiedBy.id : null;
        dto.modifiedOn = employee.modifiedOn;
        dto.deletedBy = employee.deletedBy != null ? employee.deletedBy.id : null;
        dto.deletedOn = employee.deletedOn;
        return dto;
    }

    // public Employee findEmployeeByIdOptional(Integer integer) {
    //     if (integer == null)
    //         return null;
    //     return employeeRepo.findByIdOptional(integer)
    //             .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + integer + " not found"));
    // }
}
