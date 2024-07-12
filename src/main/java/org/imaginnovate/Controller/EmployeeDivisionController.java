package org.imaginnovate.Controller;

import org.imaginnovate.Dto.EmployeeDivisionDto;
import org.imaginnovate.Service.EmployeeDivisionService;

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


@Path("/employee-divisions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeDivisionController {

    @Inject
    EmployeeDivisionService employeeDivisionService;

    @GET
    public Response getAllEmployeeDivisions() {
        return employeeDivisionService.getAllEmployeeDivisions();
    }

    @POST
    @Transactional
    public Response createEmployeeDivision(EmployeeDivisionDto employeeDivisionsDto) {
        return employeeDivisionService.createEmployeeDivision(employeeDivisionsDto);
    }
    

    @GET
    @Path("/{id}")
    public Response getEmployeeDivisionById(@PathParam("id") int id) {
        return employeeDivisionService.getEmployeeDivisionById(id);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteEmployeeDivisionById(@PathParam("id") int id) {
        return employeeDivisionService.deleteEmployeeDivisionById(id);
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateEmployeeDivisionById(@PathParam("id") int id, EmployeeDivisionDto employeeDivisionsDto) {
        return employeeDivisionService.updateEmployeeDivisionById(id, employeeDivisionsDto);
    }

}
