package org.imaginnovate.Service;

import java.util.List;

import org.imaginnovate.Dto.TimesheetStatusDto;
import org.imaginnovate.Entity.Employee;
import org.imaginnovate.Entity.TimesheetStatus;
import org.imaginnovate.Repository.EmployeeRepo;
import org.imaginnovate.Repository.TimesheetStatusRepo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class TimesheetStatusService {

    @Inject
    private TimesheetStatusRepo timesheetStatusRepo;

    @Inject
    private EmployeeRepo employeeRepo;

    public Response getAllTimesheetStatus() {
        try {
            List<TimesheetStatus> timesheetStatuses = timesheetStatusRepo.listAll();
            if (timesheetStatuses.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("There is no timesheetStatuses ").build();
            }
            return Response.status(Response.Status.OK).entity(timesheetStatuses).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }

    @Transactional
    public Response createTimesheetStatus(TimesheetStatusDto timesheetStatusDto) {
        try {
            TimesheetStatus existingTimesheetStatus = timesheetStatusRepo.findById(timesheetStatusDto.id);
            if (existingTimesheetStatus != null) {
                return Response.status(Response.Status.CONFLICT).entity("The timesheetStatus already existed").build();
            }
            TimesheetStatus timesheetStatus = new TimesheetStatus();
            timesheetStatus.id = (timesheetStatusDto.id);
            timesheetStatus.name = timesheetStatusDto.name;
            if (timesheetStatusDto.getCreatedBy() != null) {
                Employee createdBy = employeeRepo.findByIdOptional(timesheetStatusDto.getCreatedBy()).orElse(null);
                if (createdBy == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("CreatedBy employee with ID " + timesheetStatusDto.getCreatedBy() + " not found")
                            .build();
                }
        
                boolean canApprove = employeeRepo.canApproveTimesheets(timesheetStatusDto.getCreatedBy());
                if (!canApprove) {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity("Employee with ID " + timesheetStatusDto.getCreatedBy() + " does not have createdBy rights")
                            .build();
                }
            }

            timesheetStatus.createdOn = timesheetStatusDto.createdOn;
            timesheetStatusRepo.persist(timesheetStatus);
            timesheetStatusDto.setId(timesheetStatus.id);
            return Response.status(Response.Status.OK).entity(timesheetStatusDto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }

    public Response getTimesheetStatusById(Byte id) {
        try {
            TimesheetStatus timesheetStatus = timesheetStatusRepo.findById(id);
            if (timesheetStatus == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("There is no timesheetStatus found with Id " + "getId()").build();
            }
            return Response.status(Response.Status.OK).entity(timesheetStatus).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }
    @Transactional
    public Response updateTimesheetStatusById(Byte id, TimesheetStatusDto timesheetStatusDto) {
        try {
            TimesheetStatus existingTimesheetStatus = timesheetStatusRepo.findById(id);
            if (existingTimesheetStatus == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Timesheet status with ID " + id + " not found")
                        .build();
            }
    
            if (timesheetStatusDto.getName() != null) {
                existingTimesheetStatus.name = timesheetStatusDto.getName();
            }
            if (timesheetStatusDto.getCreatedOn() != null) {
                existingTimesheetStatus.createdOn = timesheetStatusDto.getCreatedOn();
            }
            if (timesheetStatusDto.getModifiedOn() != null) {
                existingTimesheetStatus.modifiedOn = timesheetStatusDto.getModifiedOn();
            }
    
            if (timesheetStatusDto.getModifiedBy() != null) {
                Employee modifiedBy = employeeRepo.findById(timesheetStatusDto.getModifiedBy());
                if (modifiedBy != null) {
                    boolean canApprove = employeeRepo.canApproveTimesheets(timesheetStatusDto.getModifiedBy());
                    if (!canApprove) {
                        return Response.status(Response.Status.FORBIDDEN)
                                .entity("Employee with ID " + timesheetStatusDto.getModifiedBy() + " does not have approval rights")
                                .build();
                    }
                    existingTimesheetStatus.modifiedBy = modifiedBy;
                } else {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Employee with ID " + timesheetStatusDto.getModifiedBy() + " not found")
                            .build();
                }
            }
    
            if (timesheetStatusDto.getDeletedBy() != null) {
                Employee deletedBy = employeeRepo.findById(timesheetStatusDto.getDeletedBy());
                if (deletedBy != null) {
                    boolean canApprove = employeeRepo.canApproveTimesheets(timesheetStatusDto.getDeletedBy());
                    if (!canApprove) {
                        return Response.status(Response.Status.FORBIDDEN)
                                .entity("Employee with ID " + timesheetStatusDto.getDeletedBy() + " does not have approval rights")
                                .build();
                    }
                    existingTimesheetStatus.deletedBy = deletedBy;
                } else {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Employee with ID " + timesheetStatusDto.getDeletedBy() + " not found")
                            .build();
                }
            }
    
            if (timesheetStatusDto.getCreatedBy() != null) {
                Employee createdBy = employeeRepo.findById(timesheetStatusDto.getCreatedBy());
                if (createdBy != null) {
                    boolean canApprove = employeeRepo.canApproveTimesheets(timesheetStatusDto.getCreatedBy());
                    if (!canApprove) {
                        return Response.status(Response.Status.FORBIDDEN)
                                .entity("Employee with ID " + timesheetStatusDto.getCreatedBy() + " does not have approval rights")
                                .build();
                    }
                    existingTimesheetStatus.createdBy = createdBy;
                } else {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Employee with ID " + timesheetStatusDto.getCreatedBy() + " not found")
                            .build();
                }
            }
    
            if (timesheetStatusDto.getDeletedOn() != null) {
                existingTimesheetStatus.deletedOn = timesheetStatusDto.getDeletedOn();
            }
    
            timesheetStatusRepo.persist(existingTimesheetStatus);
    
            TimesheetStatusDto timesheetStatusDto1 = new TimesheetStatusDto();
            timesheetStatusDto1.setId(existingTimesheetStatus.id);
            timesheetStatusDto1.setName(existingTimesheetStatus.name);
            timesheetStatusDto1.setCreatedOn(existingTimesheetStatus.createdOn);
            timesheetStatusDto1.setModifiedOn(existingTimesheetStatus.modifiedOn);
            timesheetStatusDto1.setModifiedBy(existingTimesheetStatus.modifiedBy != null ? existingTimesheetStatus.modifiedBy.id : null);
            timesheetStatusDto1.setDeletedBy(existingTimesheetStatus.deletedBy != null ? existingTimesheetStatus.deletedBy.id : null);
            timesheetStatusDto1.setDeletedOn(existingTimesheetStatus.deletedOn);
            timesheetStatusDto1.setCreatedBy(existingTimesheetStatus.createdBy != null ? existingTimesheetStatus.createdBy.id : null);
    
            return Response.status(Response.Status.OK)
                    .entity(timesheetStatusDto1)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Internal Server error while updating timesheet status")
                    .build();
        }
    }
    

    @Transactional
    public Response deleteTimesheetStatusById(Byte id) {
        try {
            TimesheetStatus timesheetStatus = timesheetStatusRepo.findById(id);
            if (timesheetStatus == null) {
                throw new RuntimeException("Timesheet status not found");
            } else {
                timesheetStatusRepo.delete(timesheetStatus);
            }
            return Response.status(Response.Status.OK).entity(timesheetStatus).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }
}