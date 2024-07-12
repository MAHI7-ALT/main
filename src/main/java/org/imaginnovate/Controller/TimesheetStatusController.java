package org.imaginnovate.Controller;

import org.imaginnovate.Dto.TimesheetStatusDto;
import org.imaginnovate.Service.TimesheetStatusService;

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

@Path("/timesheet-statuses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TimesheetStatusController {

    @Inject
    TimesheetStatusService timesheetStatusService;

    @GET
    @Transactional
    public Response getAllTimesheetStatuses() {
        return timesheetStatusService.getAllTimesheetStatus();
    }

    @POST
    @Transactional
    //@RolesAllowed("admin")
    public Response createTimesheetStatus(TimesheetStatusDto timesheetStatusDto) {
        return timesheetStatusService.createTimesheetStatus(timesheetStatusDto);
    }

    @GET
    @Path("/{id}")
    public Response getTimesheetStatusById(@PathParam("id") Byte id) {
        return timesheetStatusService.getTimesheetStatusById(id);
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateTimesheetStatusById(@PathParam("id") Byte id, TimesheetStatusDto timesheetStatusDto) {
        return timesheetStatusService.updateTimesheetStatusById(id, timesheetStatusDto);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteTimesheetStatusById(@PathParam("id") Byte id) {
        return timesheetStatusService.deleteTimesheetStatusById(id);
    }
}
