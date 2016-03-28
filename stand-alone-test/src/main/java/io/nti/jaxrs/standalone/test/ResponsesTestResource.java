package io.nti.jaxrs.standalone.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

/**
 * @author Jeff Hutchins
 */
@Path("response")
public class ResponsesTestResource {

    public static final String RESPONSE = "result";

    @GET
    @Path("string")
    public String stringMethod() {
        return RESPONSE;
    }

    @GET
    @Path("string/null")
    public String stringMethodNull() {
        return null;
    }

    @GET
    @Path("void")
    public void voidMethod() {

    }

    @GET
    @Path("response")
    public Response responseMethod() {
        return Response.ok(RESPONSE).build();
    }

    @GET
    @Path("response/null")
    public Response responseMethodNull() {
        return null;
    }

    @GET
    @Path("response/none")
    public Response responseMethodNone() {
        final Response ok = Response.ok().build();
        ok.getStatus();
        return Response.ok(null).build();
    }

    @GET
    @Path("generic")
    public GenericEntity<String> genericMethod() {
        return new GenericEntity<String>(RESPONSE) {};
    }

    @GET
    @Path("generic/null")
    public GenericEntity<String> genericMethodNull() {
        return null;
    }
}
