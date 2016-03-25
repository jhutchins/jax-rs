package io.nti.jaxrs.processors;

import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Generated;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

/**
 * @author Jeff Hutchins
 */
@SupportedAnnotationTypes("javax.annotation.Generated")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ServletProcessor extends AbstractProcessor{

    public static final String PACKAGE = "io.nti.jaxrs.impl.generated";
    public static final String CLASS = "GeneratedServlet";

    private static final String RESOURCE_PROCESSOR = ResourceProcessor.class.getCanonicalName();

    private final PebbleTemplate servlet;
    private Messager messager;
    private boolean processed = false;

    public ServletProcessor() throws Exception {
        super();
        final PebbleEngine engine = new PebbleEngine.Builder().autoEscaping(false).build();
        servlet = engine.getTemplate("templates/Servlet.tpl");
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (processed) {
            return true;
        }

        final Map<String, Object> data = new HashMap<>();
        final List<String> resources = new LinkedList<>();

        data.put("package", PACKAGE);
        data.put("class", CLASS);
        data.put("generator", getClass().getCanonicalName());

        for (Element e : roundEnv.getElementsAnnotatedWith(Generated.class)) {
            final String[] generator = e.getAnnotation(Generated.class).value();

            if (e.getKind() != ElementKind.CLASS || generator.length == 0 || !generator[0].equals(RESOURCE_PROCESSOR)) {
                continue;
            }
            resources.add(e.asType().toString());
        }

        data.put("resources", resources);

        final JavaFileObject out;
        try {
            out = processingEnv.getFiler().createSourceFile(PACKAGE + "." + CLASS);
            try (final Writer writer = out.openWriter()) {
                servlet.evaluate(writer, data);
            }
        } catch (Exception e) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Failed to generate servlet");
        }
        processed = true;
        return true;
    }
}
