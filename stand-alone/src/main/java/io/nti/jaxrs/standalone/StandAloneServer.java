package io.nti.jaxrs.standalone;

import io.nti.jaxrs.processors.ServletProcessor;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.Servlet;

/**
 * @author Jeff Hutchins
 */
public class StandAloneServer {

    private final Server server = new Server(8080);

    @SuppressWarnings("unchecked")
    public void start() throws Exception {
        final ServletHandler handler = new ServletHandler();
        final String className = ServletProcessor.PACKAGE + "." + ServletProcessor.CLASS;

        server.setHandler(handler);
        handler.addServletWithMapping((Class<? extends Servlet>) Class.forName(className), "/*");
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public void run() throws Exception {
        this.start();
        server.join();
    }

    public static void main(String[] args) throws Exception {
        new StandAloneServer().run();
    }
}
