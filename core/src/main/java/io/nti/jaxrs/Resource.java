package io.nti.jaxrs;

import io.nti.jaxrs.processors.RequestContext;

import javax.ws.rs.core.Response;
import java.util.regex.Pattern;

/**
 * @author Jeff Hutchins
 */
public abstract class Resource {

    public abstract Response process(RequestContext context);

    public abstract Pattern getBaseMatch();

//    public abstract String accepts();
}
