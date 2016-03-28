package io.nti.jaxrs.standalone.test;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

/**
 * @author Jeff Hutchins
 */
@Path("methods")
public class MethodTestResource {

    @GET
    public String get() {
        return "This was a get request";
    }

    @POST
    public String post() {
        return "This was a post request";
    }

    @PUT
    public String put() {
        return "This was a put request";
    }

    @HEAD
    public String head() {
        return "This was a head request";
    }

    @DELETE
    public String delete() {
        return "This was a delete request";
    }

    @OPTIONS
    public String options() {
        return "This was an options request";
    }
}