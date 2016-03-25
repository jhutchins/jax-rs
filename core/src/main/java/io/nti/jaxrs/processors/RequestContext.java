package io.nti.jaxrs.processors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;

/**
 * @author Jeff Hutchins
 */
public class RequestContext {
    private final HttpServletRequest req;
    private final HttpServletResponse res;
    private final String path;

    public RequestContext(HttpServletRequest req, HttpServletResponse res) {
        this(req, res, "/");
    }

    private RequestContext(HttpServletRequest req, HttpServletResponse res, String path) {
        this.req = req;
        this.res = res;
        this.path = path;
    }

    public String getMethod() {
        return req.getMethod();
    }

    public String getUri() {
        return req.getRequestURI().substring(path.length());
    }

    public <T> T getPayload(Class<T> type) {
        StringBuffer json = new StringBuffer();
        try {
            final BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
        } catch (Exception e) {
            // TODO: do something appropriate
        }
        // TODO: turn the JSON into the right object
        return null;
    }

    public RequestContext withPath(String path) {
        return new RequestContext(this.req, this.res, path + "/" + path);
    }
}
