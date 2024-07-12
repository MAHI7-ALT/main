package org.imaginnovate.Service;

import java.util.List;

import org.imaginnovate.Dto.EmployeeProjectDto;
import org.imaginnovate.Entity.Employee;
import org.imaginnovate.Entity.EmployeeProject;
import org.imaginnovate.Entity.Project;
import org.imaginnovate.Repository.EmployeeProjectRepo;
import org.imaginnovate.Repository.EmployeeRepo;
import org.imaginnovate.Repository.ProjectRepo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class EmployeeProjectService {

    @Inject
    EmployeeProjectRepo employeeProjectsRepo;

    @Inject
    EmployeeRepo employeesRepo;

    @Inject
    ProjectRepo projectRepo;

    public Response getAllEmployeeProjects() {
        try {
            List<EmployeeProjectDto> employeeProjectsDto = employeeProjectsRepo.findAllEmployeeProjects();
            if (employeeProjectsDto.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("There are no employee projects").build();
            }
            return Response.status(Response.Status.OK).entity(employeeProjectsDto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }

    @Transactional
    public Response createEmployeeProject(EmployeeProjectDto employeeProjectsDto) {
        try {
            List<EmployeeProject> existingEmployeeProjects = employeeProjectsRepo.findByEmployeeAndProject(
                    employeeProjectsDto.getEmployeeId(), employeeProjectsDto.getProjectId());

            if (!existingEmployeeProjects.isEmpty()) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("Employee-Project pair already exists")
                        .build();
            }

            Employee employees = employeesRepo.findById(employeeProjectsDto.getEmployeeId());
            if (employees == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Employee with ID " + employeeProjectsDto.getEmployeeId() + " not found")
                        .build();
            }

            Project projects = projectRepo.findById(employeeProjectsDto.getProjectId());
            if (projects == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Project with ID " + employeeProjectsDto.getProjectId() + " not found")
                        .build();
            }
            if (employeeProjectsDto.getCreatedBy() != null) {
                Employee createdBy = employeesRepo.findByIdOptional(employeeProjectsDto.getCreatedBy()).orElse(null);
                if (createdBy == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("CreatedBy employee with ID " + employeeProjectsDto.getCreatedBy() + " not found")
                            .build();
                }
        
                boolean canApprove = employeesRepo.canApproveTimesheets(employeeProjectsDto.getCreatedBy());
                if (!canApprove) {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity("Employee with ID " + employeeProjectsDto.getCreatedBy() + " does not have createdBy rights")
                            .build();
                }
            }

            EmployeeProject employeeProjects = new EmployeeProject();
            employeeProjects.id = employeeProjectsDto.id;
            employeeProjects.employeeId = employees;
            employeeProjects.projectId = projects;
            employeeProjects.canApproveTimesheets = employeeProjectsDto.canApproveTimesheets;
            employeeProjects.createdOn = employeeProjectsDto.createdOn;

            employeeProjectsRepo.persist(employeeProjects);
            employeeProjectsDto.setId(employeeProjects.id);

            return Response.status(Response.Status.CREATED).entity(employeeProjectsDto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }
    @Transactional
public Response updateEmployeeProjectById(int id, EmployeeProjectDto employeeProjectsDto) {
    try {
        EmployeeProject employeeProject = employeeProjectsRepo.findById(id);
        if (employeeProject == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("There is no employee project with ID " + id)
                    .build();
        }

        if (employeeProjectsDto.getEmployeeId() != null) {
            Employee employee = employeesRepo.findById(employeeProjectsDto.getEmployeeId());
            if (employee != null) {
                employeeProject.employeeId = employee;
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Employee with ID " + employeeProjectsDto.getEmployeeId() + " not found")
                        .build();
            }
        }

        if (employeeProjectsDto.getProjectId() != null) {
            Project project = projectRepo.findById(employeeProjectsDto.getProjectId());
            if (project != null) {
                employeeProject.projectId = project;
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Project with ID " + employeeProjectsDto.getProjectId() + " not found")
                        .build();
            }
        }

        if (employeeProjectsDto.getCanApproveTimesheets() != null) {
            employeeProject.canApproveTimesheets = employeeProjectsDto.getCanApproveTimesheets();
        }

        if (employeeProjectsDto.getCreatedOn() != null) {
            employeeProject.createdOn = employeeProjectsDto.getCreatedOn();
        }

        if (employeeProjectsDto.getModifiedOn() != null) {
            employeeProject.modifiedOn = employeeProjectsDto.getModifiedOn();
        }

        if (employeeProjectsDto.getCreatedBy() != null) {
            Employee createdBy = employeesRepo.findById(employeeProjectsDto.getCreatedBy());
            if (createdBy == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Employee with ID " + employeeProjectsDto.getCreatedBy() + " not found")
                        .build();
            }

            boolean canApprove = employeesRepo.canApproveTimesheets(employeeProjectsDto.getCreatedBy());
            if (!canApprove) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Employee with ID " + employeeProjectsDto.getCreatedBy() + " does not have createdBy rights")
                        .build();
            }

            employeeProject.createdBy = createdBy;
        }

        if (employeeProjectsDto.getModifiedBy() != null) {
            Employee modifiedBy = employeesRepo.findById(employeeProjectsDto.getModifiedBy());
            if (modifiedBy == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Employee with ID " + employeeProjectsDto.getModifiedBy() + " not found")
                        .build();
            }

            boolean canApprove = employeesRepo.canApproveTimesheets(employeeProjectsDto.getModifiedBy());
            if (!canApprove) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Employee with ID " + employeeProjectsDto.getModifiedBy() + " does not have modifiedBy rights")
                        .build();
            }

            employeeProject.modifiedBy = modifiedBy;
        }

        if (employeeProjectsDto.getDeletedBy() != null) {
            Employee deletedBy = employeesRepo.findById(employeeProjectsDto.getDeletedBy());
            if (deletedBy == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Employee with ID " + employeeProjectsDto.getDeletedBy() + " not found")
                        .build();
            }

            boolean canApprove = employeesRepo.canApproveTimesheets(employeeProjectsDto.getDeletedBy());
            if (!canApprove) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Employee with ID " + employeeProjectsDto.getDeletedBy() + " does not have deletedBy rights")
                        .build();
            }

            employeeProject.deletedBy = deletedBy;
        }

        if (employeeProjectsDto.getDeletedOn() != null) {
            employeeProject.deletedOn = employeeProjectsDto.getDeletedOn();
        }

        employeeProjectsRepo.persist(employeeProject);

        EmployeeProjectDto dto = new EmployeeProjectDto();
        dto.setId(employeeProject.id);
        dto.setEmployeeId(employeeProject.employeeId.id);
        dto.setProjectId(employeeProject.projectId.id);
        dto.setCanApproveTimesheets(employeeProject.canApproveTimesheets);
        dto.setCreatedBy(employeeProject.createdBy != null ? employeeProject.createdBy.id : null);
        dto.setCreatedOn(employeeProject.createdOn);
        dto.setModifiedBy(employeeProject.modifiedBy != null ? employeeProject.modifiedBy.id : null);
        dto.setModifiedOn(employeeProject.modifiedOn);
        dto.setDeletedBy(employeeProject.deletedBy != null ? employeeProject.deletedBy.id : null);
        dto.setDeletedOn(employeeProject.deletedOn);

        return Response.status(Response.Status.OK).entity(dto).build();
    } catch (Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Internal Server error while updating employee project")
                .build();
    }
}


    public Response getEmployeeProjectById(int id) {
        try {
            EmployeeProject ep = employeeProjectsRepo.findById(id);
            if (ep == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("There is no employee project with ID " + id + "")
                        .build();
            }
            EmployeeProjectDto dto = new EmployeeProjectDto();
            dto.id(ep.id);
            dto.employeeId(ep.employeeId.id);
            dto.projectId(ep.projectId.id);
            dto.canApproveTimesheets(ep.canApproveTimesheets);
            dto.createdBy(ep.createdBy != null ? ep.id : null);
            dto.createdOn(ep.createdOn);
            dto.deletedBy(ep.deletedBy != null ? ep.id : null);
            dto.deletedOn(ep.deletedOn);
            dto.modifiedBy(ep.modifiedBy != null ? ep.id : null);
            dto.modifiedOn = ep.modifiedOn;

            return Response.status(Response.Status.OK).entity(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }

    @Transactional
    public Response deleteEmployeeProjectById(int id) {
        try {
            EmployeeProject employeeProjects = employeeProjectsRepo.findById(id);
            if (employeeProjects == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Employee Project with ID " + id + " not found")
                        .build();
            } else {
                employeeProjectsRepo.delete(employeeProjects);
            }
            return Response.status(Response.Status.OK)
                    .entity("Employee Project with ID " + id + " deleted successfully")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }
}
