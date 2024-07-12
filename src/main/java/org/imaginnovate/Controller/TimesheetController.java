package org.imaginnovate.Controller;

import org.imaginnovate.Dto.TimesheetDto;
import org.imaginnovate.Service.TimesheetService;

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


@Path("/timesheets")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TimesheetController {

    @Inject
    TimesheetService timesheetService;

    @GET
    public Response getAllTimesheets() {
        return timesheetService.getAllTimesheets();
    }

    @GET
    @Path("/{id}")
    public Response getTimesheetById(@PathParam("id") Integer id) {
        return timesheetService.getTimesheetById(id);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteTimesheetById(@PathParam("id") int id) {
        return timesheetService.deleteTimesheetById(id);
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateTimesheetById(@PathParam("id") int id, TimesheetDto timesheetDto) {
        return timesheetService.updateTimesheetById(id, timesheetDto);
    }

    @POST
    @Path("/approve-divisions-by-heirarchy")
    @Transactional
    public Response approveTimesheetInDivisionByHeirarchy(TimesheetDto timesheetDto) {
        return timesheetService.approveTimesheetInDivisionByHeirarchy(timesheetDto);
    }

    @POST
    @Path("/approve-all-projects")
    @Transactional
    public Response approveTimesheetInProject(TimesheetDto timesheetDto) {
        return timesheetService.approveTimesheetInProject(timesheetDto);
    }

    @POST
    @Path("/approve-some-divisions")
    @Transactional
    public Response approveTimesheetInSomeDivisions(TimesheetDto timesheetDto) {
        return timesheetService.approveTimesheetInSomeDivisions(timesheetDto);
    }

    @POST
    @Path("/approve-some-projects")
    @Transactional
    public Response approveTimesheetInSomeProjects(TimesheetDto timesheetDto) {
        return timesheetService.approveTimesheetInSomeProjects(timesheetDto);
    }

    @POST
    @Path("/approve-all-divisions")
    @Transactional
    public Response approveTimesheetInAllDivisisons(TimesheetDto timesheetDto) {
        return timesheetService.approveTimesheetInDivision(timesheetDto);
    }

}