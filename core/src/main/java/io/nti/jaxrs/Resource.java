package io.nti.jaxrs;

import io.nti.jaxrs.processors.RequestContext;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.regex.Pattern;

/**
 * @author Jeff Hutchins
 */
public abstract class Resource {

    public abstract Response process(RequestContext context);

    public abstract Pattern getBaseMatch();

//    public abstract String accepts();

    protected Response haveHead() {
        return Response.ok().build();
    }

    protected Response voidMethod() {
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    protected Response responseMethod(Response response) {
        if (response == null) {
            return voidMethod();
        }
        return response;
    }

    protected <T> Response genericMethod(GenericEntity<T> genericEntity) {
        if (genericEntity == null) {
            return voidMethod();
        }
        return Response.ok(genericEntity).build();
    }

    protected <T> Response other(T other) {
        if (other == null) {
            return voidMethod();
        }
        return Response.ok(other).build();
    }
}
