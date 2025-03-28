package com.example.rest;

import com.example.db.entity.UserEntity;
import com.example.db.dao.UserDao;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javassist.NotFoundException;

import java.util.List;

@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserRestService {

    @Inject
    UserDao userDao;

    @Path("/create")
    @POST
    public Response createUser(UserEntity userEntity) {
       UserEntity createdUser = userDao.createUser(userEntity);
        return Response.ok(createdUser).build();
    }

    @Path("/update/{id}")
    @PUT
    public Response updateUser(@PathParam("id") Long id, UserEntity userEntity) throws NotFoundException {
        if (userEntity == null || !id.equals(userEntity.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID in path does not match ID in body").build();
        }
        UserEntity updatedUser = userDao.updateUser(userEntity);

        if (updatedUser != null) {
            return Response.ok(updatedUser).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User with ID " + id + " not found").build();
        }
    }

    @Path("/{id}")
    @GET
    public UserEntity getUser(@PathParam("id") Long id) {
        return userDao.getUserById(id);
    }

    @Path("/list")
    @GET
    public List<UserEntity> getListOfUsers () {
         return userDao.getUsers();
    }

    @DELETE
    @Path("/delete/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        UserEntity deletedUser =  userDao.deleteUserById(id);
        if (deletedUser != null) {
            return Response.ok(deletedUser).entity("User with id = "+id + " successful deleted").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("User not found").build(); // 204 No Content
    }

}
