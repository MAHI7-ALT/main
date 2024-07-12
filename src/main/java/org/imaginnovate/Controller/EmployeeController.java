package org.imaginnovate.Controller;

import org.imaginnovate.Dto.EmployeeDto;
import org.imaginnovate.Service.EmployeeService;

import jakarta.annotation.security.RolesAllowed;
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


@Path("/employees")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeController {

    @Inject
    EmployeeService employeeService;

    @GET
    @RolesAllowed("admin")
    public Response getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @POST
    @Transactional
    public Response createEmployee(EmployeeDto employeeDto) {
        return employeeService.createEmployee(employeeDto);
    }

    @GET
    @Path("/{id}")
    public Response getEmployeeById(@PathParam("id") int id) {
        return employeeService.getEmployeeById(id);
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateEmployeeById(@PathParam("id") int id, EmployeeDto employeeDto) {
        return employeeService.updateEmployeeById(id, employeeDto);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteEmployeeById(@PathParam("id") int id) {
        return employeeService.deleteEmployeeById(id);

    }
}
