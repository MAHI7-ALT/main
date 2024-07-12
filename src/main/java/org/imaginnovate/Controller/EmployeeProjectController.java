package org.imaginnovate.Controller;

import org.imaginnovate.Dto.EmployeeProjectDto;
import org.imaginnovate.Service.EmployeeProjectService;

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


@Path("/employee-projects")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeProjectController {
    @Inject
    EmployeeProjectService employeeProjectsService;

    @GET
    public Response getAllEmployeeProjects() {
        return employeeProjectsService.getAllEmployeeProjects();
    }

    @POST
    @Transactional
    public Response createEmployeeProject(EmployeeProjectDto employeeProjectsDto) {
        return employeeProjectsService.createEmployeeProject(employeeProjectsDto);
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateEmployeeProjectById(@PathParam("id") int id, EmployeeProjectDto employeeProjectsDto) {
        return employeeProjectsService.updateEmployeeProjectById(id, employeeProjectsDto);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteEmployeeProjectById(@PathParam("id") int id) {
        return employeeProjectsService.deleteEmployeeProjectById(id);
    }

    @GET
    @Path("/{id}")
    public Response getEmployeeProjectById(@PathParam("id") int id) {
        return employeeProjectsService.getEmployeeProjectById(id);
    }
}
