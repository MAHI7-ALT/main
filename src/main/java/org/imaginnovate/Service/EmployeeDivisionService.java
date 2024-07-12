package org.imaginnovate.Service;

import java.util.List;

import org.imaginnovate.Dto.EmployeeDivisionDto;
import org.imaginnovate.Entity.Division;
import org.imaginnovate.Entity.Employee;
import org.imaginnovate.Entity.EmployeeDivision;
import org.imaginnovate.Repository.DivisionRepo;
import org.imaginnovate.Repository.EmployeeDivisionRepo;
import org.imaginnovate.Repository.EmployeeRepo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class EmployeeDivisionService {

    @Inject
    EmployeeDivisionRepo employeeDivisionsRepo;

    @Inject
    DivisionRepo divisionsRepo;

    @Inject
    EmployeeRepo employeesRepo;

    @Transactional
    public Response getAllEmployeeDivisions() {
        try {
            List<EmployeeDivisionDto> employeeDivisionsDto = employeeDivisionsRepo.findAllEmployeeDivisions();
            if (employeeDivisionsDto.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("There are no employee divisions").build();
            }
            return Response.status(Response.Status.OK).entity(employeeDivisionsDto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
        }
    }

    @Transactional
    public Response createEmployeeDivision(EmployeeDivisionDto employeeDivisionsDto) {
        try {
        if (employeeDivisionsDto.getEmployeeId() == 0 || employeeDivisionsDto.getDivisionId() == 0) {
            Division division = divisionsRepo.findById(employeeDivisionsDto.getDivisionId());
            if (division == null) {
                return Response.status(Response.Status.CONFLICT).entity("Division not found").build();
            }
        }
        Employee employee = employeesRepo.findById(employeeDivisionsDto.getEmployeeId());
        if (employee == null) {
            return Response.status(Response.Status.CONFLICT).entity("Employee not found").build();
        }

        boolean exists = employeeDivisionsRepo.existsByDivisionIdAndEmployeeId(employeeDivisionsDto.getDivisionId(),
                employeeDivisionsDto.getEmployeeId());
        if (exists) {
            return Response.status(Response.Status.CONFLICT).entity("Employee Division already exists").build();
        }

        if (employeeDivisionsDto.getCreatedBy() != null) {
            Employee createdBy = employeesRepo.findByIdOptional(employeeDivisionsDto.getCreatedBy()).orElse(null);
            if (createdBy == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("CreatedBy employee with ID " + employeeDivisionsDto.getCreatedBy() + " not found")
                        .build();
            }
    
            boolean canApprove = employeesRepo.canApproveTimesheets(employeeDivisionsDto.getCreatedBy());
            if (!canApprove) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Employee with ID " + employeeDivisionsDto.getCreatedBy() + " does not have createdBy rights")
                        .build();
            }
        }

        EmployeeDivision employeeDivision = new EmployeeDivision();
        employeeDivision.employeeId = employee;
        employeeDivision.divisionId = divisionsRepo.findById(employeeDivisionsDto.getDivisionId());
        employeeDivision.primaryDivision = employeeDivisionsDto.getPrimaryDivision();
        employeeDivision.canApproveTimesheets = employeeDivisionsDto.getCanApproveTimesheets();
        employeeDivision.createdOn = employeeDivisionsDto.getCreatedOn();
        employeeDivisionsRepo.persist(employeeDivision);
        employeeDivisionsDto.setId(employeeDivision.id);

        return Response.status(Response.Status.CREATED).entity(employeeDivisionsDto).build();
        } catch (Exception e) {
        return
        Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }

    public Response getEmployeeDivisionById(int id) {
        try {
            EmployeeDivision ed = employeeDivisionsRepo.findById(id);
            if (ed == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Employee Division with ID " + id + " not found")
                        .build();
            }

            EmployeeDivisionDto employeeDivisionDto = convertToDto(ed);
            return Response.status(Response.Status.OK)
                    .entity(employeeDivisionDto)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Internal Server error")
                    .build();
        }
    }

    private EmployeeDivisionDto convertToDto(EmployeeDivision ed) {
        EmployeeDivisionDto dto = new EmployeeDivisionDto();
        dto.setId(ed.id);
        dto.setEmployeeId(ed.employeeId != null ? ed.employeeId.id : null);
        dto.setDivisionId(ed.divisionId != null ? ed.divisionId.id : null);
        dto.setPrimaryDivision(ed.primaryDivision);
        dto.setCanApproveTimesheets(ed.canApproveTimesheets);
        dto.setCreatedBy(ed.createdBy != null ? ed.createdBy.id : null);
        dto.setCreatedOn(ed.createdOn);
        dto.setModifiedBy(ed.modifiedBy != null ? ed.modifiedBy.id : null);
        dto.setModifiedOn(ed.modifiedOn);
        dto.setDeletedBy(ed.deletedBy != null ? ed.deletedBy.id : null);
        dto.setDeletedOn(ed.deletedOn);
        return dto;
    }

    @Transactional
public Response updateEmployeeDivisionById(Integer id, EmployeeDivisionDto employeeDivisionsDto) {
    try {
        EmployeeDivision employeeDivision = employeeDivisionsRepo.findById(id);
        if (employeeDivision == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("No employee division found").build();
        }

        if (employeeDivisionsDto.getPrimaryDivision() != null) {
            employeeDivision.primaryDivision = employeeDivisionsDto.getPrimaryDivision();
        }

        if (employeeDivisionsDto.getCanApproveTimesheets() != null) {
            employeeDivision.canApproveTimesheets = employeeDivisionsDto.getCanApproveTimesheets();
        }

        if (employeeDivisionsDto.getEmployeeId() != null) {
            if (employeeDivisionsDto.getEmployeeId() != 0) {
                Employee employee = employeesRepo.findById(employeeDivisionsDto.getEmployeeId());
                if (employee != null) {
                    employeeDivision.employeeId = employee;
                } else {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Employee with ID " + employeeDivisionsDto.getEmployeeId() + " not found")
                            .build();
                }
            } else {
                employeeDivision.employeeId = null;
            }
        }

        if (employeeDivisionsDto.getDivisionId() != null) {
            if (employeeDivisionsDto.getDivisionId() != 0) {
                Division division = divisionsRepo.findById(employeeDivisionsDto.getDivisionId());
                if (division != null) {
                    employeeDivision.divisionId = division;
                } else {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Division with ID " + employeeDivisionsDto.getDivisionId() + " not found")
                            .build();
                }
            } else {
                employeeDivision.divisionId = null;
            }
        }

        if (employeeDivisionsDto.getCreatedBy() != null) {
            Employee createdBy = employeesRepo.findById(employeeDivisionsDto.getCreatedBy());
            if (createdBy == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Employee with ID " + employeeDivisionsDto.getCreatedBy() + " not found")
                        .build();
            }

            boolean canApprove = employeesRepo.canApproveTimesheets(employeeDivisionsDto.getCreatedBy());
            if (!canApprove) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Employee with ID " + employeeDivisionsDto.getCreatedBy() + " does not have createdBy rights")
                        .build();
            }

            employeeDivision.createdBy = createdBy;
        }

        if (employeeDivisionsDto.getCreatedOn() != null) {
            employeeDivision.createdOn = employeeDivisionsDto.getCreatedOn();
        }

        if (employeeDivisionsDto.getModifiedBy() != null) {
            Employee modifiedBy = employeesRepo.findById(employeeDivisionsDto.getModifiedBy());
            if (modifiedBy == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Employee with ID " + employeeDivisionsDto.getModifiedBy() + " not found")
                        .build();
            }

            boolean canApprove = employeesRepo.canApproveTimesheets(employeeDivisionsDto.getModifiedBy());
            if (!canApprove) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Employee with ID " + employeeDivisionsDto.getModifiedBy() + " does not have modifiedBy rights")
                        .build();
            }

            employeeDivision.modifiedBy = modifiedBy;
        }

        if (employeeDivisionsDto.getModifiedOn() != null) {
            employeeDivision.modifiedOn = employeeDivisionsDto.getModifiedOn();
        }

        if (employeeDivisionsDto.getDeletedBy() != null) {
            Employee deletedBy = employeesRepo.findById(employeeDivisionsDto.getDeletedBy());
            if (deletedBy == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Employee with ID " + employeeDivisionsDto.getDeletedBy() + " not found")
                        .build();
            }

            boolean canApprove = employeesRepo.canApproveTimesheets(employeeDivisionsDto.getDeletedBy());
            if (!canApprove) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Employee with ID " + employeeDivisionsDto.getDeletedBy() + " does not have deletedBy rights")
                        .build();
            }

            employeeDivision.deletedBy = deletedBy;
        }

        if (employeeDivisionsDto.getDeletedOn() != null) {
            employeeDivision.deletedOn = employeeDivisionsDto.getDeletedOn();
        }

        employeeDivisionsRepo.persist(employeeDivision);
        EmployeeDivisionDto updatedDto = convertToDto(employeeDivision);
        return Response.status(Response.Status.OK).entity(updatedDto).build();

    } catch (Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
    }
}

    @Transactional
    public Response deleteEmployeeDivisionById(Integer id) {
        try {
            EmployeeDivision ed = employeeDivisionsRepo.findByEmployeeDivisionId(id);
            if (ed == null) {
                throw new IllegalArgumentException("EmployeeDivision with ID " + id + " not found");
            } else {
                employeeDivisionsRepo.delete(ed);
            }
            return Response.status(Response.Status.OK)
                    .entity("EmployeeDivision with ID " + id + " deleted successfully")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }
}
