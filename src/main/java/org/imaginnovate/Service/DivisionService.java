package org.imaginnovate.Service;

import java.util.List;

import org.imaginnovate.Dto.DivisionDto;
import org.imaginnovate.Entity.Division;
import org.imaginnovate.Entity.Employee;
import org.imaginnovate.Repository.DivisionRepo;
import org.imaginnovate.Repository.EmployeeRepo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class DivisionService {

    @Inject
    DivisionRepo divisionsRepo;

    @Inject
    EmployeeRepo employeeRepo;

    public Response getAllDivisions() {
        try {
            List<DivisionDto> divisions = divisionsRepo.findAllDivisions();
            if (divisions.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("There are no divisions")
                        .build();
            }
            return Response.ok(divisions).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Internal server error while fetching divisions")
                    .build();
        }
    }

    @Transactional
    public Response createDivision(DivisionDto divisionsDto) {
        if (divisionsDto.getId() != null) {
            Division existingDivision = divisionsRepo.findById(divisionsDto.getId());
            if (existingDivision != null) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("The division already exists")
                        .build();
            }
        }
    
        if (divisionsDto.getCreatedBy() != null) {
            Employee createdBy = employeeRepo.findByIdOptional(divisionsDto.getCreatedBy()).orElse(null);
            if (createdBy == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("CreatedBy employee with ID " + divisionsDto.getCreatedBy() + " not found")
                        .build();
            }
    
            boolean canApprove = employeeRepo.canApproveTimesheets(divisionsDto.getCreatedBy());
            if (!canApprove) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Employee with ID " + divisionsDto.getCreatedBy() + " does not have createdBy rights")
                        .build();
            }
        } else {
            Employee reportsToEmployee = employeeRepo.findById(divisionsDto.getCreatedBy());
            if (reportsToEmployee != null) {
                divisionsDto.setCreatedBy(reportsToEmployee.id);
            }
        }
    
        Division division = new Division();
        division.name = divisionsDto.name;
    
        if (divisionsDto.getParentId() != null && divisionsDto.getParentId() != 0) {
            Division parentDivision = divisionsRepo.findById(divisionsDto.getParentId());
            if (parentDivision != null) {
                division.parentId = parentDivision;
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Parent division with ID " + divisionsDto.getParentId() + " not found")
                        .build();
            }
        }
    
        Employee createdBy = employeeRepo.findById(divisionsDto.getCreatedBy());
        division.createdBy = createdBy;
        division.createdOn = divisionsDto.getCreatedOn();
    
        divisionsRepo.persist(division);
        divisionsDto.setId(division.id);
        divisionsDto.setParentId(division.parentId != null ? division.parentId.id : null);
        divisionsDto.setCreatedBy(division.createdBy != null ? division.createdBy.id : null);
        divisionsDto.setCreatedOn(division.createdOn);
        divisionsDto.setModifiedBy(division.modifiedBy != null ? division.modifiedBy.id : null);
        divisionsDto.setModifiedOn(division.modifiedOn);
        divisionsDto.setDeletedBy(division.deletedBy != null ? division.deletedBy.id : null);
        divisionsDto.setDeletedOn(division.deletedOn);
    
        return Response.status(Response.Status.CREATED)
                .entity(divisionsDto)
                .build();
    }
    
    public Response getDivisionById(int id) {
        try {
            Division division = divisionsRepo.findById(id);
            if (division == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Division not found")
                        .build();
            }
            return Response.ok(convertToDto(division)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Internal server error while fetching division by ID")
                    .build();
        }
    }
    @Transactional
    public Response updateDivisionById(int id, DivisionDto divisionsDto) {
        try {
            Division division = divisionsRepo.findById(id);
            if (division == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Division not found")
                        .build();
            }
    
            if (divisionsDto.name != null) {
                division.name = divisionsDto.name;
            }
    
            if (divisionsDto.getParentId() != null && divisionsDto.getParentId() != 0) {
                Division parentDivision = divisionsRepo.findById(divisionsDto.getParentId());
                if (parentDivision != null) {
                    division.parentId = parentDivision;
                } else {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Parent division with ID " + divisionsDto.getParentId() + " not found")
                            .build();
                }
            }
    
            if (divisionsDto.getCreatedBy() != null) {
                Employee createdBy = employeeRepo.findById(divisionsDto.getCreatedBy());
                if (createdBy == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Employee with ID " + divisionsDto.getCreatedBy() + " not found")
                            .build();
                }
    
                boolean canApprove = employeeRepo.canApproveTimesheets(divisionsDto.getCreatedBy());
                if (!canApprove) {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity("Employee with ID " + divisionsDto.getCreatedBy() + " does not have createdBy rights")
                            .build();
                }
    
                division.createdBy = createdBy;
            }
    
            if (divisionsDto.getCreatedOn() != null) {
                division.createdOn = divisionsDto.getCreatedOn();
            }
    
            if (divisionsDto.getModifiedBy() != null) {
                Employee modifiedBy = employeeRepo.findById(divisionsDto.getModifiedBy());
                if (modifiedBy == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Employee with ID " + divisionsDto.getModifiedBy() + " not found")
                            .build();
                }
    
                boolean canApprove = employeeRepo.canApproveTimesheets(divisionsDto.getModifiedBy());
                if (!canApprove) {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity("Employee with ID " + divisionsDto.getModifiedBy() + " does not have modifiedBy rights")
                            .build();
                }
    
                division.modifiedBy = modifiedBy;
            }
    
            if (divisionsDto.getModifiedOn() != null) {
                division.modifiedOn = divisionsDto.getModifiedOn();
            }
    
            if (divisionsDto.getDeletedBy() != null) {
                Employee deletedBy = employeeRepo.findById(divisionsDto.getDeletedBy());
                if (deletedBy == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Employee with ID " + divisionsDto.getDeletedBy() + " not found")
                            .build();
                }
    
                boolean canApprove = employeeRepo.canApproveTimesheets(divisionsDto.getDeletedBy());
                if (!canApprove) {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity("Employee with ID " + divisionsDto.getDeletedBy() + " does not have deletedBy rights")
                            .build();
                }
    
                division.deletedBy = deletedBy;
            }
    
            if (divisionsDto.getDeletedOn() != null) {
                division.deletedOn = divisionsDto.getDeletedOn();
            }
    
            divisionsRepo.persist(division);
            DivisionDto updatedDto = convertToDto(division);
            return Response.ok(updatedDto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Internal server error while updating division")
                    .build();
        }
    }
    

@Transactional
public Response deleteDivisionById(int id) {
    try {
        Division division = divisionsRepo.findById(id);
        if (division == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Division not found")
                    .build();
        }

        divisionsRepo.delete(division);
        
        return Response.status(Response.Status.OK)
                .entity("The division with id " + id + " deleted successfully")
                .build();
    } catch (Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Internal server error while deleting division: " + e.getMessage())
                .build();
    }
}


    private DivisionDto convertToDto(Division division) {
        DivisionDto dto = new DivisionDto();
        dto.id = division.id;
        dto.name = division.name;
        dto.parentId = division.parentId != null ? division.parentId.id : null;
        dto.createdBy = division.createdBy != null ? division.createdBy.id : null;
        dto.createdOn = division.createdOn;
        dto.modifiedBy = division.modifiedBy != null ? division.modifiedBy.id : null;
        dto.modifiedOn = division.modifiedOn;
        dto.deletedBy = division.deletedBy != null ? division.deletedBy.id : null;
        dto.deletedOn = division.deletedOn;
        return dto;
    }
}
