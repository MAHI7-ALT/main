package org.imaginnovate.Service;

import java.util.List;

import org.imaginnovate.Dto.ProjectTaskDto;
import org.imaginnovate.Entity.Employee;
import org.imaginnovate.Entity.Project;
import org.imaginnovate.Entity.ProjectTask;
import org.imaginnovate.Entity.Task;
import org.imaginnovate.Repository.EmployeeRepo;
import org.imaginnovate.Repository.ProjectRepo;
import org.imaginnovate.Repository.ProjectTaskRepo;
import org.imaginnovate.Repository.TaskRepo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class ProjectTaskService {

    @Inject
    ProjectTaskRepo projectTasksRepo;

    @Inject
    ProjectRepo projectsRepo;

    @Inject
    EmployeeRepo employeesRepo;

    @Inject
    TaskRepo tasksRepo;

    public Response getAllProjectTasks() {
        try {
            List<ProjectTaskDto> projectTasks = projectTasksRepo.findAllProjectTasks();
            if (projectTasks.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("There are no project tasks").build();
            }
            return Response.status(Response.Status.OK).entity(projectTasks).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }

    @Transactional
    public Response createProjectTask(ProjectTaskDto projectTasksDto) {
        ProjectTask projectTask = projectTasksRepo.findByProjectAndTask(projectTasksDto.projectId,
                projectTasksDto.taskId);
        if (projectTask != null) {
            return Response.status(Response.Status.CONFLICT).entity("Project Task already exists").build();
        }

        Project project = projectsRepo.findById(projectTasksDto.projectId);
        if (project == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Project with ID " + projectTasksDto.projectId + " not found").build();
        }

        Task task = tasksRepo.findById(projectTasksDto.taskId);
        if (task == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Task with ID " + projectTasksDto.taskId + " not found").build();
        }
        if (projectTasksDto.getCreatedBy() != null) {
            Employee createdBy = employeesRepo.findByIdOptional(projectTasksDto.getCreatedBy()).orElse(null);
            if (createdBy == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("CreatedBy employee with ID " + projectTasksDto.getCreatedBy() + " not found")
                        .build();
            }
    
            boolean canApprove = employeesRepo.canApproveTimesheets(projectTasksDto.getCreatedBy());
            if (!canApprove) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Employee with ID " + projectTasksDto.getCreatedBy() + " does not have createdBy rights")
                        .build();
            }
        }else {
            Employee reportsToEmployee = employeesRepo.findById(projectTasksDto.getCreatedBy());
            if (reportsToEmployee != null) {
                projectTasksDto.setCreatedBy(reportsToEmployee.id);
            }
        }

        ProjectTask newProjectTask = new ProjectTask();
        newProjectTask.id = projectTasksDto.id;
        newProjectTask.projectId = project;
        newProjectTask.taskId = task;
        newProjectTask.createdOn = projectTasksDto.createdOn;

        projectTasksRepo.persist(newProjectTask);
        projectTasksDto.setId(newProjectTask.id);

        return Response.status(Response.Status.CREATED).entity(projectTasksDto).build();
    }

    @Transactional
    public Response updateProjectTaskById(Integer id, ProjectTaskDto projectTasksDto) {
        try {
            ProjectTask projectTasks = projectTasksRepo.findById(id);
            if (projectTasks == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Project Task with ID " + id + " not found")
                        .build();
            }
    
            if (projectTasksDto.getProjectId() != null) {
                Project projects = projectsRepo.findById(projectTasksDto.getProjectId());
                if (projects != null) {
                    projectTasks.projectId = projects;
                } else {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Project with ID " + projectTasksDto.getProjectId() + " not found")
                            .build();
                }
            }
    
            if (projectTasksDto.getTaskId() != null) {
                Task tasks = tasksRepo.findById(projectTasksDto.getTaskId());
                if (tasks != null) {
                    projectTasks.taskId = tasks;
                } else {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Task with ID " + projectTasksDto.getTaskId() + " not found")
                            .build();
                }
            }
    
            if (projectTasksDto.getCreatedOn() != null) {
                projectTasks.createdOn = projectTasksDto.getCreatedOn();
            }
    
            if (projectTasksDto.getModifiedOn() != null) {
                projectTasks.modifiedOn = projectTasksDto.getModifiedOn();
            }
    
            if (projectTasksDto.getModifiedBy() != null) {
                Employee modifiedBy = employeesRepo.findById(projectTasksDto.getModifiedBy());
                if (modifiedBy == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Employee with ID " + projectTasksDto.getModifiedBy() + " not found")
                            .build();
                }
    
                boolean canApprove = employeesRepo.canApproveTimesheets(projectTasksDto.getModifiedBy());
                if (!canApprove) {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity("Employee with ID " + projectTasksDto.getModifiedBy() + " does not have modifiedBy rights")
                            .build();
                }
    
                projectTasks.modifiedBy = modifiedBy;
            }
    
            if (projectTasksDto.getDeletedBy() != null) {
                Employee deletedBy = employeesRepo.findById(projectTasksDto.getDeletedBy());
                if (deletedBy == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Employee with ID " + projectTasksDto.getDeletedBy() + " not found")
                            .build();
                }
    
                boolean canApprove = employeesRepo.canApproveTimesheets(projectTasksDto.getDeletedBy());
                if (!canApprove) {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity("Employee with ID " + projectTasksDto.getDeletedBy() + " does not have deletedBy rights")
                            .build();
                }
    
                projectTasks.deletedBy = deletedBy;
            }
    
            if (projectTasksDto.getDeletedOn() != null) {
                projectTasks.deletedOn = projectTasksDto.getDeletedOn();
            }
    
            if (projectTasksDto.getCreatedBy() != null) {
                Employee createdBy = employeesRepo.findById(projectTasksDto.getCreatedBy());
                if (createdBy == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Employee with ID " + projectTasksDto.getCreatedBy() + " not found")
                            .build();
                }
    
                boolean canApprove = employeesRepo.canApproveTimesheets(projectTasksDto.getCreatedBy());
                if (!canApprove) {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity("Employee with ID " + projectTasksDto.getCreatedBy() + " does not have createdBy rights")
                            .build();
                }
    
                projectTasks.createdBy = createdBy;
            }
    
            projectTasksRepo.persist(projectTasks);
            ProjectTaskDto updatedDto = convertToDto(projectTasks);
            return Response.ok(updatedDto).build();
    
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Internal Server error")
                    .build();
        }
    }
    
    private ProjectTaskDto convertToDto(ProjectTask projectTask) {
        ProjectTaskDto dto = new ProjectTaskDto();
        dto.setId(projectTask.id);
        dto.setProjectId(projectTask.projectId != null ? projectTask.projectId.id : null);
        dto.setTaskId(projectTask.taskId != null ? projectTask.taskId.id : null);
        dto.setCreatedBy(projectTask.createdBy != null ? projectTask.createdBy.id : null);
        dto.setCreatedOn(projectTask.createdOn);
        dto.setModifiedBy(projectTask.modifiedBy != null ? projectTask.modifiedBy.id : null);
        dto.setModifiedOn(projectTask.modifiedOn);
        dto.setDeletedBy(projectTask.deletedBy != null ? projectTask.deletedBy.id : null);
        dto.setDeletedOn(projectTask.deletedOn);
        return dto;
    }
    
    public Response getProjectTaskById(Integer id) {
        try {
            ProjectTask projectTasks = projectTasksRepo.findById(id);
            if (projectTasks == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Project Task with ID " + id + " not found")
                        .build();
            }
            ProjectTaskDto dto1 = new ProjectTaskDto();
            dto1.id = projectTasks.id;
            dto1.projectId = projectTasks.projectId.id;
            dto1.taskId = projectTasks.taskId.id;
            dto1.createdBy = projectTasks.createdBy != null ? projectTasks.id : null;
            dto1.createdOn = projectTasks.createdOn;
            dto1.deletedBy = projectTasks.deletedBy != null ? projectTasks.id : null;
            dto1.deletedOn = projectTasks.deletedOn;
            dto1.modifiedBy = projectTasks.modifiedBy != null ? projectTasks.id : null;
            dto1.modifiedOn = projectTasks.modifiedOn;

            return Response.status(Response.Status.OK).entity(dto1).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }

    @Transactional
    public Response deleteProjectTaskById(Integer id) {
        try {
            ProjectTask projectTasks = projectTasksRepo.findById(id);
            if (projectTasks == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Project Task with ID " + id + " not found")
                        .build();
            } else {
                projectTasksRepo.delete(projectTasks);
            }
            return Response.status(Response.Status.OK)
                    .entity("Project Task with ID " + id + " deleted successfully")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }
}
