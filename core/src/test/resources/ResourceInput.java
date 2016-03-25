package com.example;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author Jeff Hutchins
 */
@Path("testing")
public class ResourceInput {

    @POST
    public Response create(Book book) {
        return null;
    }

    @GET
    @Path("{id}")
    public Book get(@PathParam("id") String id) {
        return null;
    }

    @GET
    @Path("{id:\\d+}/testing/{other}")
    public Book put(@PathParam("id") String id) {
        return null;
    }

    @DELETE
    @Path("{id:\\d+}")
    public void delete(@PathParam("id") int id) {
        
    }

    private void blank() {

    }
}
