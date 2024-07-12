package org.imaginnovate.Controller;

import org.imaginnovate.Dto.UserDto;
import org.imaginnovate.Service.UserService;

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


@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
    @Inject
    UserService usersService;

    @GET
    @RolesAllowed("user")
    public Response getAllUsers() {
        return usersService.getAllUsers();
    }

    @POST
    @Transactional
    public Response createUser(UserDto usersDto) {
        return usersService.createUser(usersDto);
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Integer id) {
        return usersService.getUserById(id);

    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateUserById(@PathParam("id") Integer id, UserDto usersDto) {
        return usersService.updateUserById(id, usersDto);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed("admin")
    public Response deleteUserById(@PathParam("id") Integer id) {
        return usersService.deleteUserById(id);
    }
}
