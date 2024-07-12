package org.imaginnovate.Controller;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.imaginnovate.Dto.DivisionDto;
import org.imaginnovate.Service.DivisionService;

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


@Path("/divisions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DivisionController {

    @Inject
    DivisionService divisionService;

    @GET
    public Response getAllDivisions() {
        return divisionService.getAllDivisions();
    }

    @POST
    @Transactional
    public Response createDivision(@RequestBody DivisionDto divisionDto) {
        return divisionService.createDivision(divisionDto);
    }

    @GET
    @Path("/{id}")
    public Response getDivisionById(@PathParam("id") int id) {
        return divisionService.getDivisionById(id);
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateDivisionById(@PathParam("id") int id, DivisionDto divisionsDto) {
        return divisionService.updateDivisionById(id, divisionsDto);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteDivisionById(@PathParam("id") int id) {
        return divisionService.deleteDivisionById(id);

    }
}
