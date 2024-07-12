package org.imaginnovate.Service;

import java.util.List;

import org.imaginnovate.Dto.TimesheetDto;
import org.imaginnovate.Entity.Division;
import org.imaginnovate.Entity.Employee;
import org.imaginnovate.Entity.EmployeeDivision;
import org.imaginnovate.Entity.EmployeeProject;
import org.imaginnovate.Entity.Project;
import org.imaginnovate.Entity.ProjectTask;
import org.imaginnovate.Entity.Timesheet;
import org.imaginnovate.Entity.TimesheetStatus;
import org.imaginnovate.Repository.DivisionRepo;
import org.imaginnovate.Repository.EmployeeDivisionRepo;
import org.imaginnovate.Repository.EmployeeProjectRepo;
import org.imaginnovate.Repository.EmployeeRepo;
import org.imaginnovate.Repository.ProjectRepo;
import org.imaginnovate.Repository.ProjectTaskRepo;
import org.imaginnovate.Repository.TimesheetRepo;
import org.imaginnovate.Repository.TimesheetStatusRepo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class TimesheetService {
    @Inject
    TimesheetRepo timesheetRepo;

    @Inject
    EmployeeRepo employeesRepo;

    @Inject
    ProjectRepo projectsRepo;

    @Inject
    DivisionRepo divisionRepo;

    @Inject
    TimesheetStatusRepo timesheetStatusRepo;

    @Inject
    EmployeeProjectRepo employeeProjectRepo;

    @Inject
    EmployeeDivisionRepo employeeDivisionRepo;

    @Inject
    ProjectTaskRepo projectTaskRepo;

    @Inject
    EmployeeService employeeService;

    public Response getAllTimesheets() {
        try {
            List<TimesheetDto> timesheets = timesheetRepo.findAllTimesheets();
            if (timesheets == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("There are no timesheets").build();
            }
            return Response.status(Response.Status.OK).entity(timesheets).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }

    @Transactional
    public Response updateTimesheetById(Integer id, TimesheetDto timesheetDto) {
        try {
            Timesheet existingTimesheet = timesheetRepo.findById(id);
            if (existingTimesheet == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Timesheet not found").build();
            }
    
            if (timesheetDto.getEmployeeDivisionId() != null) {
                EmployeeDivision employeeDivision = employeeDivisionRepo.findById(timesheetDto.getEmployeeDivisionId());
                if (employeeDivision != null) {
                    existingTimesheet.employeeDivisionId = employeeDivision;
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Employee Division ID").build();
                }
            }
    
            if (timesheetDto.getEmployeeProjectId() != null) {
                EmployeeProject employeeProject = employeeProjectRepo.findById(timesheetDto.getEmployeeProjectId());
                if (employeeProject != null) {
                    existingTimesheet.employeeProjectId = employeeProject;
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Employee Project ID").build();
                }
            }
    
            if (timesheetDto.getProjectTaskId() != null) {
                ProjectTask projectTask = projectTaskRepo.findById(timesheetDto.getProjectTaskId());
                if (projectTask != null) {
                    existingTimesheet.projectTaskId = projectTask;
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Project Task ID").build();
                }
            }
    
            if (timesheetDto.getStatus() != null) {
                TimesheetStatus timesheetStatus = timesheetStatusRepo.findById(timesheetDto.getStatus());
                if (timesheetStatus != null) {
                    existingTimesheet.status = timesheetStatus;
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Timesheet Status ID").build();
                }
            }
    
            if (timesheetDto.getApprovedBy() != null) {
                Employee employee = employeesRepo.findById(timesheetDto.getApprovedBy());
                if (employee != null) {
                    boolean canApprove = employeesRepo.canApproveTimesheets(timesheetDto.getApprovedBy());
                    if (!canApprove) {
                        return Response.status(Response.Status.FORBIDDEN)
                                .entity("Employee with ID " + timesheetDto.getApprovedBy() + " does not have approval rights")
                                .build();
                    }
                    existingTimesheet.approvedBy = employee;
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Employee ID").build();
                }
            }
    
            if (timesheetDto.getSubmittedBy() != null) {
                Employee employee = employeesRepo.findById(timesheetDto.getSubmittedBy());
                if (employee != null) {
                    existingTimesheet.submittedBy = employee;
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Employee ID").build();
                }
            }
    
            if (timesheetDto.getCreatedBy() != null) {
                Employee employee = employeesRepo.findById(timesheetDto.getCreatedBy());
                if (employee != null) {
                    boolean canApprove = employeesRepo.canApproveTimesheets(timesheetDto.getCreatedBy());
                    if (!canApprove) {
                        return Response.status(Response.Status.FORBIDDEN)
                                .entity("Employee with ID " + timesheetDto.getCreatedBy() + " does not have approval rights")
                                .build();
                    }
                    existingTimesheet.createdBy = employee;
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Employee ID").build();
                }
            }
    
            if (timesheetDto.getModifiedBy() != null) {
                Employee employee = employeesRepo.findById(timesheetDto.getModifiedBy());
                if (employee != null) {
                    boolean canApprove = employeesRepo.canApproveTimesheets(timesheetDto.getModifiedBy());
                    if (!canApprove) {
                        return Response.status(Response.Status.FORBIDDEN)
                                .entity("Employee with ID " + timesheetDto.getModifiedBy() + " does not have approval rights")
                                .build();
                    }
                    existingTimesheet.modifiedBy = employee;
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Employee ID").build();
                }
            }
    
            if (timesheetDto.getDeletedBy() != null) {
                Employee employee = employeesRepo.findById(timesheetDto.getDeletedBy());
                if (employee != null) {
                    boolean canApprove = employeesRepo.canApproveTimesheets(timesheetDto.getDeletedBy());
                    if (!canApprove) {
                        return Response.status(Response.Status.FORBIDDEN)
                                .entity("Employee with ID " + timesheetDto.getDeletedBy() + " does not have approval rights")
                                .build();
                    }
                    existingTimesheet.deletedBy = employee;
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Employee ID").build();
                }
            }
    
            existingTimesheet.description = timesheetDto.getDescription();
            existingTimesheet.createdOn = timesheetDto.getCreatedOn();
            existingTimesheet.modifiedOn = timesheetDto.getModifiedOn();
            existingTimesheet.deletedOn = timesheetDto.getDeletedOn();
            existingTimesheet.submittedOn = timesheetDto.getSubmittedOn();
            existingTimesheet.hoursWorked = timesheetDto.getHoursWorked();
    
            timesheetRepo.persist(existingTimesheet);
    
            TimesheetDto updatedTimesheetDto = convertToDto1(existingTimesheet);
    
            return Response.status(Response.Status.OK).entity(updatedTimesheetDto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }
    
    private TimesheetDto convertToDto1(Timesheet timesheet) {
        TimesheetDto dto = new TimesheetDto();
        dto.setId(timesheet.id);
        dto.setEmployeeDivisionId(timesheet.employeeDivisionId != null ? timesheet.employeeDivisionId.id : null);
        dto.setEmployeeProjectId(timesheet.employeeProjectId != null ? timesheet.employeeProjectId.id : null);
        dto.setProjectTaskId(timesheet.projectTaskId != null ? timesheet.projectTaskId.id : null);
        dto.setStatus(timesheet.status != null ? timesheet.status.id : null);
        dto.setApprovedBy(timesheet.approvedBy != null ? timesheet.approvedBy.id : null);
        dto.setSubmittedBy(timesheet.submittedBy != null ? timesheet.submittedBy.id : null);
        dto.setCreatedBy(timesheet.createdBy != null ? timesheet.createdBy.id : null);
        dto.setModifiedBy(timesheet.modifiedBy != null ? timesheet.modifiedBy.id : null);
        dto.setDeletedBy(timesheet.deletedBy != null ? timesheet.deletedBy.id : null);
        dto.setDescription(timesheet.description);
        dto.setCreatedOn(timesheet.createdOn);
        dto.setModifiedOn(timesheet.modifiedOn);
        dto.setDeletedOn(timesheet.deletedOn);
        dto.setSubmittedOn(timesheet.submittedOn);
        dto.setHoursWorked(timesheet.hoursWorked);
        return dto;
    }
    
    
    @Transactional
    public Response deleteTimesheetById(Integer id) {
        try {
            Timesheet timesheet = timesheetRepo.findById(id);
            if (timesheet == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Timesheet with Id " + id + " not found")
                        .build();
            } else {
                timesheetRepo.delete(timesheet);
            }

            return Response.status(Response.Status.OK)
                    .entity("Timesheet with Id " + id + " is  successfully deleted")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }

    public Response getTimesheetById(Integer id) {
        try {
            Timesheet timesheet = timesheetRepo.findById(id);
            if (timesheet == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Timesheet not found").build();
            }

            TimesheetDto timesheetDto = new TimesheetDto();

            timesheetDto.setId(timesheet.id);
            timesheetDto.setEmployeeDivisionId(
                    timesheet.employeeDivisionId != null ? timesheet.employeeDivisionId.id : null);
            timesheetDto
                    .setEmployeeProjectId(timesheet.employeeProjectId != null ? timesheet.employeeProjectId.id : null);
            timesheetDto.setProjectTaskId(timesheet.projectTaskId != null ? timesheet.projectTaskId.id : null);
            timesheetDto.setDescription(timesheet.description);
            timesheetDto.setHoursWorked(timesheet.hoursWorked);
            timesheetDto.setSubmittedBy(timesheet.submittedBy != null ? timesheet.submittedBy.id : null);
            timesheetDto.setSubmittedOn(timesheet.submittedOn);
            timesheetDto.setStatus(timesheet.status != null ? timesheet.status.id : null);
            timesheetDto.setApprovedBy(timesheet.approvedBy != null ? timesheet.approvedBy.id : null);
            timesheetDto.setCreatedBy(timesheet.createdBy != null ? timesheet.createdBy.id : null);
            timesheetDto.setCreatedOn(timesheet.createdOn);
            timesheetDto.setDeletedBy(timesheet.deletedBy != null ? timesheet.deletedBy.id : null);
            timesheetDto.setDeletedOn(timesheet.deletedOn);
            timesheetDto.setModifiedBy(timesheet.modifiedBy != null ? timesheet.modifiedBy.id : null);
            timesheetDto.setModifiedOn(timesheet.modifiedOn);

            return Response.ok(timesheetDto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal server error found").build();
        }
    }
    @Transactional
    public Response approveTimesheetInDivisionByHeirarchy(TimesheetDto timesheetDto) {
        Employee approvingEmployee = employeesRepo.findById(timesheetDto.getApprovedBy());
        if (approvingEmployee == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Approving employee not found").build();
        }
    
        Employee submittedByEmployee = employeesRepo.findById(timesheetDto.getSubmittedBy());
        if (submittedByEmployee == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Employee who submitted timesheet not found").build();
        }
    
        if (!canApproveTimesheet(approvingEmployee, submittedByEmployee)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Employee cannot approve timesheets for the submittedBy employee").build();
        }
    
        Timesheet timesheet = timesheetRepo.findById(timesheetDto.getId());
        if (timesheet == null) {
            timesheet = new Timesheet();
        }

        if (timesheetDto.getCreatedBy() != null) {
            Employee createdBy = employeesRepo.findByIdOptional(timesheetDto.getCreatedBy()).orElse(null);
            if (createdBy == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("CreatedBy employee with ID " + timesheetDto.getCreatedBy() + " not found")
                        .build();
            }
    
            boolean canApprove = employeesRepo.canApproveTimesheets(timesheetDto.getCreatedBy());
            if (!canApprove) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Employee with ID " + timesheetDto.getCreatedBy() + " does not have createdBy rights")
                        .build();
            }
        }
    
        updateFromDto(timesheet, timesheetDto, approvingEmployee);
        timesheetRepo.persist(timesheet);
        timesheetDto.setId(timesheet.id);
    
        return Response.status(Response.Status.OK).entity(timesheetDto).build();
    }
    
    private boolean canApproveTimesheet(Employee approvingEmployee, Employee submittedByEmployee) {
        return hasApprovalPermission(approvingEmployee) &&
                (approvingEmployee.id.equals(submittedByEmployee.id) ||
                isDirectSupervisorWithPermission(approvingEmployee, submittedByEmployee));
    }
    
    private boolean hasApprovalPermission(Employee employee) {
        List<EmployeeDivision> divisions = employeeDivisionRepo.findByEmployeeId(employee.id);
        for (EmployeeDivision division : divisions) {
            if (Boolean.TRUE.equals(division.canApproveTimesheets)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isDirectSupervisorWithPermission(Employee approvingEmployee, Employee submittedByEmployee) {
        Employee supervisor = employeesRepo.findById(submittedByEmployee.getReportsToId().id);
        return supervisor != null && supervisor.equals(approvingEmployee);
    }
    
    
    private void updateFromDto(Timesheet timesheet, TimesheetDto timesheetDto, Employee approvingEmployee) {
        Employee submittedByEmployee = employeesRepo.findById(timesheetDto.getSubmittedBy());
        if (submittedByEmployee == null) {
            throw new RuntimeException("Submitted by employee not found");
        }
        if (canApproveTimesheet(approvingEmployee, submittedByEmployee)) {
            timesheet.setApprovedBy(approvingEmployee);
        } else {
            throw new RuntimeException("Approving employee cannot approve timesheets for the submittedBy employee");
        }
    
        timesheet.approvedBy = approvingEmployee;
        timesheet.employeeProjectId = timesheetDto.getEmployeeProjectId() != null
                ? employeeProjectRepo.findById(timesheetDto.getEmployeeProjectId())
                : null;
        timesheet.projectTaskId = timesheetDto.getProjectTaskId() != null
                ? projectTaskRepo.findById(timesheetDto.getProjectTaskId())
                : null;
        timesheet.description = timesheetDto.getDescription();
        timesheet.hoursWorked = timesheetDto.getHoursWorked();
        timesheet.submittedBy = timesheetDto.getSubmittedBy() != null
                ? employeesRepo.findById(timesheetDto.getSubmittedBy())
                : null;
        timesheet.submittedOn = timesheetDto.getSubmittedOn();
        timesheet.status = timesheetDto.getStatus() != null ? timesheetStatusRepo.findById(timesheetDto.getStatus())
                : null;
        timesheet.createdBy = timesheetDto.getCreatedBy() != null ? employeesRepo.findById(timesheetDto.getCreatedBy())
                : null;
        timesheet.createdOn = timesheetDto.getCreatedOn();
        timesheet.deletedBy = timesheetDto.getDeletedBy() != null ? employeesRepo.findById(timesheetDto.getDeletedBy())
                : null;
        timesheet.deletedOn = timesheetDto.getDeletedOn();
        timesheet.modifiedBy = timesheetDto.getModifiedBy() != null
                ? employeesRepo.findById(timesheetDto.getModifiedBy())
                : null;
        timesheet.modifiedOn = timesheetDto.getModifiedOn();
        timesheet.employeeDivisionId = timesheetDto.getEmployeeDivisionId() != null
                ? employeeDivisionRepo.findById(timesheetDto.getEmployeeDivisionId())
                : null;
    }
    

    @Transactional
    public Response approveTimesheetInProject(TimesheetDto timesheetDto) {

        Employee employee = employeesRepo.findById(timesheetDto.getApprovedBy());
        if (employee == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Employee not found").build();
        }

        if (!employeeProjectRepo.canApproveInProject(employee)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Employee does not have project approval permissions").build();
        }

        Timesheet timesheet = timesheetRepo.findById(timesheetDto.getId());
        if (timesheet == null) {
            timesheet = new Timesheet();
        }

        if (timesheetDto.getCreatedBy() != null) {
            Employee createdBy = employeesRepo.findByIdOptional(timesheetDto.getCreatedBy()).orElse(null);
            if (createdBy == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("CreatedBy employee with ID " + timesheetDto.getCreatedBy() + " not found")
                        .build();
            }
    
            boolean canApprove = employeesRepo.canApproveTimesheets(timesheetDto.getCreatedBy());
            if (!canApprove) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Employee with ID " + timesheetDto.getCreatedBy() + " does not have createdBy rights")
                        .build();
            }
        }

        updateTimesheetDto(timesheet, timesheetDto, employee);
        timesheetRepo.persist(timesheet);
        timesheetDto.setId(timesheet.id);

        return Response.status(Response.Status.OK).entity(timesheetDto).build();
    }

    @Transactional
    public Response approveTimesheetInSomeDivisions(TimesheetDto timesheetDto) {
        Employee employee = employeesRepo.findById(timesheetDto.getApprovedBy());
        if (employee == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Employee not found").build();
        }
    
        EmployeeDivision employeeDivision = employeeDivisionRepo.findById(timesheetDto.getEmployeeDivisionId());
        if (employeeDivision == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Employee Division not found").build();
        }
    
        Division division = divisionRepo.findById(employeeDivision.divisionId);
        if (division == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Division not found").build();
        }
    
        if (!employeeDivisionRepo.canApproveSomeDivisions(employee, division)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Employee does not have division approval permissions").build();
        }
    
        Timesheet timesheet = timesheetRepo.findById(timesheetDto.getId());
        if (timesheet == null) {
            timesheet = new Timesheet();
        }

        if (timesheetDto.getCreatedBy() != null) {
            Employee createdBy = employeesRepo.findByIdOptional(timesheetDto.getCreatedBy()).orElse(null);
            if (createdBy == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("CreatedBy employee with ID " + timesheetDto.getCreatedBy() + " not found")
                        .build();
            }
    
            boolean canApprove = employeesRepo.canApproveTimesheets(timesheetDto.getCreatedBy());
            if (!canApprove) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Employee with ID " + timesheetDto.getCreatedBy() + " does not have createdBy rights")
                        .build();
            }
        }
        updateTimesheetDto(timesheet, timesheetDto, employee);
        timesheetRepo.persist(timesheet);
        timesheetDto.setId(timesheet.id);
        return Response.status(Response.Status.OK).entity(timesheetDto).build();
    }
    

    @Transactional
    public Response approveTimesheetInSomeProjects(TimesheetDto timesheetDto) {
        Employee employee = employeesRepo.findById(timesheetDto.getApprovedBy());
        if (employee == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Employee not found").build();
        }
    
        EmployeeProject employeeProject = employeeProjectRepo.findById(timesheetDto.getEmployeeProjectId());
        if (employeeProject == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Employee Project not found").build();
        }
    
        Project project = projectsRepo.findById(employeeProject.projectId.id);
        if (project == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }
    
        if (!employeeProjectRepo.canApproveSomeProjects(employee.id, project.id)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Employee does not have project approval permissions").build();
        }
    
        Timesheet timesheet = timesheetRepo.findById(timesheetDto.getId());
        if (timesheet == null) {
            timesheet = new Timesheet();
        }

        if (timesheetDto.getCreatedBy() != null) {
            Employee createdBy = employeesRepo.findByIdOptional(timesheetDto.getCreatedBy()).orElse(null);
            if (createdBy == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("CreatedBy employee with ID " + timesheetDto.getCreatedBy() + " not found")
                        .build();
            }
    
            boolean canApprove = employeesRepo.canApproveTimesheets(timesheetDto.getCreatedBy());
            if (!canApprove) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Employee with ID " + timesheetDto.getCreatedBy() + " does not have createdBy rights")
                        .build();
            }
        }
        updateTimesheetDto(timesheet, timesheetDto, employee);
        timesheetRepo.persist(timesheet);
        timesheetDto.setId(timesheet.id);
        return Response.status(Response.Status.OK).entity(timesheetDto).build();
    }


    @Transactional
    public Response approveTimesheetInDivision(TimesheetDto timesheetDto) {
        Employee employee = employeesRepo.findById(timesheetDto.getApprovedBy());
        if (employee == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Employee not found").build();
        }

        if (!employeeDivisionRepo.canApproveInDivision(employee)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Employee does not have division approval permissions").build();
        }

        Timesheet timesheet = timesheetRepo.findById(timesheetDto.getId());
        if (timesheet == null) {
            timesheet = new Timesheet();
        }

        if (timesheetDto.getCreatedBy() != null) {
            Employee createdBy = employeesRepo.findByIdOptional(timesheetDto.getCreatedBy()).orElse(null);
            if (createdBy == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("CreatedBy employee with ID " + timesheetDto.getCreatedBy() + " not found")
                        .build();
            }
    
            boolean canApprove = employeesRepo.canApproveTimesheets(timesheetDto.getCreatedBy());
            if (!canApprove) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Employee with ID " + timesheetDto.getCreatedBy() + " does not have createdBy rights")
                        .build();
            }
        }

        updateTimesheetDto(timesheet, timesheetDto, employee);
        timesheetRepo.persist(timesheet);
        timesheetDto.setId(timesheet.id);

        return Response.status(Response.Status.OK).entity(timesheetDto).build();
    }

    private void updateTimesheetDto(Timesheet timesheet, TimesheetDto timesheetDto, Employee approvingEmployee) {
        timesheet.approvedBy = approvingEmployee;
        timesheet.employeeProjectId = timesheetDto.getEmployeeProjectId() != null
                ? employeeProjectRepo.findById(timesheetDto.getEmployeeProjectId())
                : null;
        timesheet.projectTaskId = timesheetDto.getProjectTaskId() != null
                ? projectTaskRepo.findById(timesheetDto.getProjectTaskId())
                : null;
        timesheet.description = timesheetDto.getDescription();
        timesheet.hoursWorked = timesheetDto.getHoursWorked();
        timesheet.submittedBy = timesheetDto.getSubmittedBy() != null
                ? employeesRepo.findById(timesheetDto.getSubmittedBy())
                : null;
        timesheet.submittedOn = timesheetDto.getSubmittedOn();
        timesheet.status = timesheetDto.getStatus() != null ? timesheetStatusRepo.findById(timesheetDto.getStatus())
                : null;
        timesheet.createdBy = timesheetDto.getCreatedBy() != null ? employeesRepo.findById(timesheetDto.getCreatedBy())
                : null;
        timesheet.createdOn = timesheetDto.getCreatedOn();
        timesheet.deletedBy = timesheetDto.getDeletedBy() != null ? employeesRepo.findById(timesheetDto.getDeletedBy())
                : null;
        timesheet.deletedOn = timesheetDto.getDeletedOn();
        timesheet.modifiedBy = timesheetDto.getModifiedBy() != null
                ? employeesRepo.findById(timesheetDto.getModifiedBy())
                : null;
        timesheet.modifiedOn = timesheetDto.getModifiedOn();
        timesheet.employeeDivisionId = timesheetDto.getEmployeeDivisionId() != null
                ? employeeDivisionRepo.findById(timesheetDto.getEmployeeDivisionId())
                : null;
    }

}