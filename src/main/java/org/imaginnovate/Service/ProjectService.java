package org.imaginnovate.Service;

import java.util.List;

import org.imaginnovate.Dto.ProjectDto;
import org.imaginnovate.Entity.Division;
import org.imaginnovate.Entity.Employee;
import org.imaginnovate.Entity.Project;
import org.imaginnovate.Repository.DivisionRepo;
import org.imaginnovate.Repository.EmployeeRepo;
import org.imaginnovate.Repository.ProjectRepo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@ApplicationScoped
public class ProjectService {

    @Inject
    ProjectRepo projectsRepo;

    @Inject
    DivisionRepo divisionsRepo;

    @Inject
    EmployeeRepo employeesRepo;

    public Response getAllProjects() {
        try {
            List<ProjectDto> projectsDto = projectsRepo.findAllProjects();
            if (projectsDto.isEmpty()) {
                return Response.status(Status.NOT_FOUND).entity("There is no existing projects").build();
            }
            return Response.status(Response.Status.OK).entity(projectsDto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }

    @Transactional
    public Response createProject(ProjectDto projectDto) {
        try {
            if (projectDto.getId() != null) {
                Project existingProject = projectsRepo.findById(projectDto.getId());
                if (existingProject != null) {
                    return Response.status(Response.Status.CONFLICT)
                            .entity("Project with ID " + projectDto.getId() + " already exists").build();
                }
            }

            Project project = new Project();
            project.name = projectDto.getName();
            project.description = projectDto.getDescription();

            if (projectDto.getDivisionId() != null && projectDto.getDivisionId() != 0) {
                Division division = divisionsRepo.findById(projectDto.getDivisionId());
                if (division != null) {
                    project.divisionId = division;
                } else {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Division with ID " + projectDto.getDivisionId() + " not found").build();
                }
            }

            if (projectDto.getCreatedBy() != null) {
                Employee createdBy = employeesRepo.findByIdOptional(projectDto.getCreatedBy()).orElse(null);
                if (createdBy == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("CreatedBy employee with ID " + projectDto.getCreatedBy() + " not found")
                            .build();
                }
        
                boolean canApprove = employeesRepo.canApproveTimesheets(projectDto.getCreatedBy());
                if (!canApprove) {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity("Employee with ID " + projectDto.getCreatedBy() + " does not have createdBy rights")
                            .build();
                }
            }else {
                Employee reportsToEmployee = employeesRepo.findById(projectDto.getCreatedBy());
                if (reportsToEmployee != null) {
                    projectDto.setCreatedBy(reportsToEmployee.id);
                }
            }


            project.createdOn = projectDto.getCreatedOn();
            project.modifiedOn = projectDto.getModifiedOn();
            project.deletedOn = projectDto.getDeletedOn();
            projectsRepo.persist(project);
            projectDto.setId(project.id);
            projectDto.setCreatedBy(project.createdBy.id);

            return Response.status(Response.Status.CREATED).entity(projectDto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Internal Server error while creating project: " + e.getMessage()).build();
        }
    }

    @Transactional
public Response updateProjectById(int id, ProjectDto projectDto) {
    try {
        Project project = projectsRepo.findById(id);
        if (project == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Project with ID " + id + " not found")
                    .build();
        }

        if (projectDto.getName() != null) {
            project.name = projectDto.getName();
        }

        if (projectDto.getDescription() != null) {
            project.description = projectDto.getDescription();
        }

        if (projectDto.getDivisionId() != null && projectDto.getDivisionId() != 0) {
            Division division = divisionsRepo.findById(projectDto.getDivisionId());
            if (division != null) {
                project.divisionId = division;
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Division with ID " + projectDto.getDivisionId() + " not found")
                        .build();
            }
        }

        if (projectDto.getCreatedBy() != null) {
            Employee createdBy = employeesRepo.findById(projectDto.getCreatedBy());
            if (createdBy == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Employee with ID " + projectDto.getCreatedBy() + " not found")
                        .build();
            }

            boolean canApprove = employeesRepo.canApproveTimesheets(projectDto.getCreatedBy());
            if (!canApprove) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Employee with ID " + projectDto.getCreatedBy() + " does not have createdBy rights")
                        .build();
            }

            project.createdBy = createdBy;
        }

        if (projectDto.getModifiedBy() != null) {
            Employee modifiedBy = employeesRepo.findById(projectDto.getModifiedBy());
            if (modifiedBy == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Employee with ID " + projectDto.getModifiedBy() + " not found")
                        .build();
            }

            boolean canApprove = employeesRepo.canApproveTimesheets(projectDto.getModifiedBy());
            if (!canApprove) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Employee with ID " + projectDto.getModifiedBy() + " does not have modifiedBy rights")
                        .build();
            }

            project.modifiedBy = modifiedBy;
        }

        if (projectDto.getDeletedBy() != null) {
            Employee deletedBy = employeesRepo.findById(projectDto.getDeletedBy());
            if (deletedBy == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Employee with ID " + projectDto.getDeletedBy() + " not found")
                        .build();
            }

            boolean canApprove = employeesRepo.canApproveTimesheets(projectDto.getDeletedBy());
            if (!canApprove) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Employee with ID " + projectDto.getDeletedBy() + " does not have deletedBy rights")
                        .build();
            }

            project.deletedBy = deletedBy;
        }

        project.createdOn = projectDto.getCreatedOn();
        project.modifiedOn = projectDto.getModifiedOn();
        project.deletedOn = projectDto.getDeletedOn();

        projectsRepo.persist(project);

        // Convert the updated Project entity to a ProjectDto
        ProjectDto updatedProjectDto = convertToDto(project);

        return Response.status(Response.Status.OK).entity(updatedProjectDto).build();
    } catch (Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Internal Server error")
                .build();
    }
}

private ProjectDto convertToDto(Project project) {
    ProjectDto projectDto = new ProjectDto();
    projectDto.setId(project.id);
    projectDto.setName(project.name);
    projectDto.setDescription(project.description);
    projectDto.setDivisionId(project.divisionId != null ? project.divisionId.id : null);
    projectDto.setCreatedBy(project.createdBy != null ? project.createdBy.id : null);
    projectDto.setCreatedOn(project.createdOn);
    projectDto.setModifiedBy(project.modifiedBy != null ? project.modifiedBy.id : null);
    projectDto.setModifiedOn(project.modifiedOn);
    projectDto.setDeletedBy(project.deletedBy != null ? project.deletedBy.id : null);
    projectDto.setDeletedOn(project.deletedOn);
    return projectDto;
}


    public Response getProjectById(int id) {
        try {
            Project projects = projectsRepo.findById(id);
            if (projects == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Project with ID " + id + " not found")
                        .build();
            }
            ProjectDto projectDto = new ProjectDto();
            projectDto.id = (projects.id);
            projectDto.name(projects.name);
            projectDto.setDescription(projects.description);
            if (projects.divisionId != null) {
                projectDto.divisionId = projects.divisionId.id;
            }
            projectDto.createdBy = projects.createdBy != null ? projects.id : null;
            projectDto.createdOn = projects.createdOn;
            projectDto.deletedBy = projects.deletedBy != null ? projects.id : null;
            projectDto.deletedOn = projects.deletedOn;
            projectDto.modifiedBy = projects.modifiedBy != null ? projects.id : null;
            projectDto.modifiedOn = projects.modifiedOn;
            return Response.status(Response.Status.OK).entity(projectDto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }

    @Transactional
    public Response deleteProjectById(int id) {
        try {
            Project projects = projectsRepo.findById(id);
            if (projects == null) {
                return Response.status(Status.NOT_FOUND).entity("Project with ID " + id + " not found").build();
            } else {
                projectsRepo.delete(projects);
            }
            return Response.status(Response.Status.OK)
                    .entity("ProjectService with ID " + id + " deleted successfully")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server error").build();
        }
    }
}