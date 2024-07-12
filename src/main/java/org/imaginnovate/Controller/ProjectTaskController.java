package org.imaginnovate.Controller;

import org.imaginnovate.Dto.ProjectTaskDto;
import org.imaginnovate.Service.ProjectTaskService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/project-tasks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProjectTaskController {

    @Inject
    ProjectTaskService projectTasksService;

    @GET
    public Response getAllProjectsTasks() {
        return projectTasksService.getAllProjectTasks();
    }

    @POST
    @Transactional
    public Response createProjectTask(ProjectTaskDto projectTasksDto) {
        return projectTasksService.createProjectTask(projectTasksDto);

    }

    @GET
    @Path("/{id}")
    public Response getProjectTaskById(@PathParam("id") Integer id) {
        return projectTasksService.getProjectTaskById(id);

    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteProjectTaskById(@PathParam("id") int id) {
        return projectTasksService.deleteProjectTaskById(id);
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateProjectTaskById(@PathParam("id") int id, ProjectTaskDto projectTasksDto) {
        return projectTasksService.updateProjectTaskById(id, projectTasksDto);
    }

} 