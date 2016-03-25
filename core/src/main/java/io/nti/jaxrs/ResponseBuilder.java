package io.nti.jaxrs;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author Jeff Hutchins
 */
public class ResponseBuilder extends Response.ResponseBuilder {

    private int status;
    private Object entity;

    @Override
    public Response build() {
        return new io.nti.jaxrs.Response(status, entity);
    }

    @Override
    public Response.ResponseBuilder clone() {
        return null;
    }

    @Override
    public Response.ResponseBuilder status(int status) {
        this.status = status;
        return this;
    }

    @Override
    public Response.ResponseBuilder entity(Object entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public Response.ResponseBuilder entity(Object entity, Annotation[] annotations) {
        return null;
    }

    @Override
    public Response.ResponseBuilder allow(String... methods) {
        return null;
    }

    @Override
    public Response.ResponseBuilder allow(Set<String> methods) {
        return null;
    }

    @Override
    public Response.ResponseBuilder cacheControl(CacheControl cacheControl) {
        return null;
    }

    @Override
    public Response.ResponseBuilder encoding(String encoding) {
        return null;
    }

    @Override
    public Response.ResponseBuilder header(String name, Object value) {
        return null;
    }

    @Override
    public Response.ResponseBuilder replaceAll(MultivaluedMap<String, Object> headers) {
        return null;
    }

    @Override
    public Response.ResponseBuilder language(String language) {
        return null;
    }

    @Override
    public Response.ResponseBuilder language(Locale language) {
        return null;
    }

    @Override
    public Response.ResponseBuilder type(MediaType type) {
        return null;
    }

    @Override
    public Response.ResponseBuilder type(String type) {
        return null;
    }

    @Override
    public Response.ResponseBuilder variant(Variant variant) {
        return null;
    }

    @Override
    public Response.ResponseBuilder contentLocation(URI location) {
        return null;
    }

    @Override
    public Response.ResponseBuilder cookie(NewCookie... cookies) {
        return null;
    }

    @Override
    public Response.ResponseBuilder expires(Date expires) {
        return null;
    }

    @Override
    public Response.ResponseBuilder lastModified(Date lastModified) {
        return null;
    }

    @Override
    public Response.ResponseBuilder location(URI location) {
        return null;
    }

    @Override
    public Response.ResponseBuilder tag(EntityTag tag) {
        return null;
    }

    @Override
    public Response.ResponseBuilder tag(String tag) {
        return null;
    }

    @Override
    public Response.ResponseBuilder variants(Variant... variants) {
        return null;
    }

    @Override
    public Response.ResponseBuilder variants(List<Variant> variants) {
        return null;
    }

    @Override
    public Response.ResponseBuilder links(Link... links) {
        return null;
    }

    @Override
    public Response.ResponseBuilder link(URI uri, String rel) {
        return null;
    }

    @Override
    public Response.ResponseBuilder link(String uri, String rel) {
        return null;
    }
}
