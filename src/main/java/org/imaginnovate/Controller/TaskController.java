package org.imaginnovate.Controller;

import org.imaginnovate.Dto.TaskDto;
import org.imaginnovate.Service.TaskService;

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


@Path("/tasks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TaskController {
    @Inject
    TaskService taskService;

    @GET
    public Response getAllTasks() {
        return taskService.getAllTasks();
    }

    @POST
    @Transactional
    public Response createTask(TaskDto taskDto) {
        return taskService.createTask(taskDto);

    }

    @GET
    @Path("/{id}")
    public Response getTaskById(@PathParam("id") Integer id) {
        return taskService.getTaskById(id);

    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteTaskById(@PathParam("id") Integer id) {
        return taskService.deleteTaskById(id);
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateTaskById(@PathParam("id") Integer id, TaskDto taskDto) {
        return taskService.updateTaskById(id, taskDto);
    }

}