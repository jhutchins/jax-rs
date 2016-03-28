package io.nti.jaxrs.standalone.test;

import io.nti.jaxrs.standalone.test.annotations.OTHER;
import io.nti.jaxrs.standalone.test.annotations.SPECIAL_GET;

import javax.ws.rs.Path;

/**
 * @author Jeff Hutchins
 */
@Path("annotations")
public class AnnotationTestResource {

    @OTHER
    public String other() {
        return "This was an other request";
    }

    @SPECIAL_GET
    public String get() {
        return "This was a get request";
    }
}
