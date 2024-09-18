package org.imaginnovate.Service;

import java.util.List;

import org.imaginnovate.Dto.TaskDto;
import org.imaginnovate.Entity.Employee;
import org.imaginnovate.Entity.Task;
import org.imaginnovate.Repository.EmployeeRepo;
import org.imaginnovate.Repository.TaskRepo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class TaskService {

    @Inject
    TaskRepo tasksRepo;

    @Inject
    EmployeeRepo employeesRepo;

    public Response getAllTasks() {
        try {
            List<TaskDto> tasks = tasksRepo.findAllTasks();
            if (tasks.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("There are no tasks").build();
            }
            return Response.status(Response.Status.OK).entity(tasks).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }

    @Transactional
    public Response createTask(TaskDto taskDto) {
        try {
            if (taskDto.getId() != null) {
                Task existingTask = tasksRepo.findById(taskDto.getId());
                if (existingTask != null) {
                    return Response.status(Response.Status.CONFLICT)
                            .entity("Task with name '" + taskDto.getName() + "' already exists").build();
                }
            }

            Task task = new Task();
            task.name = taskDto.getName();
            task.description = taskDto.getDescription();

            if (taskDto.getCreatedBy() != null) {
                Employee createdBy = employeesRepo.findByIdOptional(taskDto.getCreatedBy()).orElse(null);
                if (createdBy == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("CreatedBy employee with ID " + taskDto.getCreatedBy() + " not found")
                            .build();
                }
        
                boolean canApprove = employeesRepo.canApproveTimesheets(taskDto.getCreatedBy());
                if (!canApprove) {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity("Employee with ID " + taskDto.getCreatedBy() + " does not have createdBy rights")
                            .build();
                }
            }else {
                Employee reportsToEmployee = employeesRepo.findById(taskDto.getCreatedBy());
                if (reportsToEmployee != null) {
                    taskDto.setCreatedBy(reportsToEmployee.id);
                }
            }

            task.createdOn = taskDto.getCreatedOn();
            task.modifiedOn = taskDto.getModifiedOn();
            task.deletedOn = taskDto.getDeletedOn();
            tasksRepo.persist(task);
            TaskDto dto = new TaskDto();
            dto.id = task.id;
            dto.name = task.name;
            dto.description = task.description;
            dto.createdBy = task.createdBy != null ? task.id : null;
            dto.createdOn = task.createdOn;
            dto.deletedBy = task.deletedBy != null ? task.id : null;
            dto.deletedOn = task.deletedOn;
            dto.modifiedBy = task.modifiedBy != null ? task.id : null;
            dto.modifiedOn = task.modifiedOn;

            return Response.status(Response.Status.OK).entity(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Internal Server error: " + e.getMessage()).build();
        }
    }
    @Transactional
    public Response updateTaskById(Integer id, TaskDto taskDto) {
        try {
            Task task = tasksRepo.findById(id);
            if (task == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Task with ID " + id + " not found")
                        .build();
            }
    
            if (taskDto.getDescription() != null) {
                task.description = taskDto.getDescription();
            }
    
            if (taskDto.getModifiedBy() != null) {
                Employee modifiedBy = employeesRepo.findById(taskDto.getModifiedBy());
                if (modifiedBy == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Employee with ID " + taskDto.getModifiedBy() + " (ModifiedBy) not found")
                            .build();
                }
                
                boolean canApprove = employeesRepo.canApproveTimesheets(taskDto.getModifiedBy());
                if (!canApprove) {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity("Employee with ID " + taskDto.getModifiedBy() + " does not have approval rights")
                            .build();
                }
    
                task.modifiedBy = modifiedBy;
            }
    
            if (taskDto.getModifiedOn() != null) {
                task.modifiedOn = taskDto.getModifiedOn();
            }
    
            if (taskDto.getDeletedBy() != null) {
                Employee deletedBy = employeesRepo.findById(taskDto.getDeletedBy());
                if (deletedBy == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Employee with ID " + taskDto.getDeletedBy() + " (DeletedBy) not found")
                            .build();
                }
    
                boolean canApprove = employeesRepo.canApproveTimesheets(taskDto.getDeletedBy());
                if (!canApprove) {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity("Employee with ID " + taskDto.getDeletedBy() + " does not have approval rights")
                            .build();
                }
    
                task.deletedBy = deletedBy;
            }
    
            if (taskDto.getDeletedOn() != null) {
                task.deletedOn = taskDto.getDeletedOn();
            }
    
            if (taskDto.getCreatedBy() != null) {
                Employee createdBy = employeesRepo.findById(taskDto.getCreatedBy());
                if (createdBy == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Employee with ID " + taskDto.getCreatedBy() + " (CreatedBy) not found")
                            .build();
                }
    
                boolean canApprove = employeesRepo.canApproveTimesheets(taskDto.getCreatedBy());
                if (!canApprove) {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity("Employee with ID " + taskDto.getCreatedBy() + " does not have approval rights")
                            .build();
                }
    
                task.createdBy = createdBy;
            }
    
            if (taskDto.getCreatedOn() != null) {
                task.createdOn = taskDto.getCreatedOn();
            }
    
            tasksRepo.persist(task);
    
            TaskDto dto = convertToDto(task);
    
            return Response.status(Response.Status.OK).entity(dto).build();
    
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Internal Server error while updating task")
                    .build();
        }
    }
    
    private TaskDto convertToDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.id = task.id;
        dto.name = task.name;
        dto.description = task.description;
        dto.createdBy = task.createdBy != null ? task.createdBy.id : null;
        dto.createdOn = task.createdOn;
        dto.deletedBy = task.deletedBy != null ? task.deletedBy.id : null;
        dto.deletedOn = task.deletedOn;
        dto.modifiedBy = task.modifiedBy != null ? task.modifiedBy.id : null;
        dto.modifiedOn = task.modifiedOn;
        return dto;
    }
    

    public Response getTaskById(Integer id) {
        try {
            Task task = tasksRepo.findById(id);
            if (task == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Task with ID " + id + " not found").build();
            }

            TaskDto dto = new TaskDto();
            dto.id = task.id;
            dto.name = task.name;
            dto.description = task.description;
            dto.createdBy = task.createdBy != null ? task.id : null;
            dto.createdOn = task.createdOn;
            dto.deletedBy = task.deletedBy != null ? task.id : null;
            dto.deletedOn = task.deletedOn;
            dto.modifiedBy = task.modifiedBy != null ? task.id : null;
            dto.modifiedOn = task.modifiedOn;

            return Response.status(Response.Status.OK).entity(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }

    @Transactional
    public Response deleteTaskById(Integer id) {
        try {
            Task task = tasksRepo.findById(id);
            if (task == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Task with ID " + id + " not found")
                        .build();
            } else {
                tasksRepo.delete(task);
            }
            return Response.status(Response.Status.OK)
                    .entity("Task with ID " + id + " deleted successfully")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }
}