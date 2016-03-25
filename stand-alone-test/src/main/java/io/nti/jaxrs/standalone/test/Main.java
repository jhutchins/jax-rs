package io.nti.jaxrs.standalone.test;

import io.nti.jaxrs.standalone.StandAloneServer;

/**
 * @author Jeff Hutchins
 */
public class Main {
    public static void main(String[] args) throws Exception {
        new StandAloneServer().run();
    }
}
