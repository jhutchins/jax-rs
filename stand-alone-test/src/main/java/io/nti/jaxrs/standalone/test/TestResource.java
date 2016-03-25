package io.nti.jaxrs.standalone.test;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

/**
 * @author Jeff Hutchins
 */
@Path("testing")
public class TestResource {

    @GET
    public String get() {
        return "This was a get";
    }

    @POST
    public String post() {
        return "This was a post";
    }

    @PUT
    public String put() {
        return "This was a put";
    }

    @HEAD
    public String head() {
        return "This was a head";
    }

    @DELETE
    public void delete() {
    }
}
