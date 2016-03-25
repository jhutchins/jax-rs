package io.nti.jaxrs.processors;

import com.google.testing.compile.JavaFileObjects;
import org.junit.Test;

import javax.tools.JavaFileObject;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.truth.Truth.*;
import static com.google.testing.compile.JavaSourcesSubjectFactory.*;

/**
 * @author Jeff Hutchins
 */
public class ProcessorTest {

    private JavaFileObject getObject(String name) {
        return JavaFileObjects.forResource(name.replace(".", "/") + ".java");
    }

    @Test
    public void testPathProcessing() throws Exception {
        final List<JavaFileObject> inputs = new LinkedList<>();
        inputs.add(getObject("ResourceInput"));
        inputs.add(getObject("Book"));

        assert_().about(javaSources())
                .that(inputs)
                .processedWith(new ResourceProcessor())
                .compilesWithoutError();
    }

    @Test
    public void testResourceProcessing() throws Exception {
        final List<JavaFileObject> inputs = new LinkedList<>();
        inputs.add(getObject("ResourceInputProxy"));
        inputs.add(getObject("ResourceInput"));
        inputs.add(getObject("Book"));

        assert_().about(javaSources())
                .that(inputs)
                .processedWith(new ServletProcessor())
                .compilesWithoutError();
    }
}
