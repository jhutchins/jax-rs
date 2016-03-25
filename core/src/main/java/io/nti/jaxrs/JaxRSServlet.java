package io.nti.jaxrs;

import io.nti.jaxrs.processors.RequestContext;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jeff Hutchins
 */
public abstract class JaxRSServlet extends GenericServlet {

    private final Set<Resource> resources = new HashSet<>();

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpServletRequest request;
        HttpServletResponse response;

        if (!(req instanceof HttpServletRequest && res instanceof HttpServletResponse)) {
            throw new ServletException("non-HTTP request or response");
        }

        request = (HttpServletRequest) req;
        response = (HttpServletResponse) res;

        final RequestContext context = new RequestContext(request, response);

        // This will be the default response
        response.setStatus(404);

        for (Resource resource : resources) {
            if (resource.getBaseMatch().matcher(context.getUri()).matches()) {
                try {
                    final Response result = resource.process(context);
                    response.setStatus(result.getStatus());
                    if (result.hasEntity()) {
                        final ServletOutputStream outputStream = response.getOutputStream();
                        try {
                            final String s = result.getEntity().toString();
                            outputStream.print(s);
                        } finally {
                            outputStream.close();
                        }
                    }
                } catch (Exception e) {
                    response.setStatus(500);
                }
            }
        }
    }

    protected void addResource(Resource resource) {
        resources.add(resource);
    }
}
