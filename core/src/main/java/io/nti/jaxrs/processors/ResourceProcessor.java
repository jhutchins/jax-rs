package io.nti.jaxrs.processors;

import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

/**
 * @author Jeff Hutchins
 */
@SupportedAnnotationTypes("javax.ws.rs.Path")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ResourceProcessor extends AbstractProcessor {

    private final PebbleTemplate resource;
    private Messager messager;

    public ResourceProcessor() throws PebbleException {
        super();
        final PebbleEngine engine = new PebbleEngine.Builder().autoEscaping(false).build();
        resource = engine.getTemplate("templates/Resource.tpl");
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
    }


    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element e : roundEnv.getElementsAnnotatedWith(Path.class)) {
            if (e.getKind() != ElementKind.CLASS) {
                continue;
            }
            try {
                createClass((TypeElement) e);
            } catch (Exception ex) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Failed ");
            }
        }
        return true;
    }

    private void createClass(TypeElement element) throws Exception {
        final Path path = element.getAnnotation(Path.class);
        final ResourceDefinition.Builder resourceBuilder = ResourceDefinition.builder()
                .delegate(element.getQualifiedName())
                .generator(getClass().getCanonicalName())
                .path(path);

        for (Element e : element.getEnclosedElements()) {
            final ElementKind kind = e.getKind();
            messager.printMessage(Diagnostic.Kind.NOTE, "Got a " + kind);
            switch (e.getKind()) {
                case CONSTRUCTOR:
                    resourceBuilder.constructor((ExecutableElement) e);
                    break;
                case METHOD:
                    ExecutableElement method = (ExecutableElement) e;
                    Path methodPath = method.getAnnotation(Path.class);
                    HttpMethod httpMethod = null;
                    for (AnnotationMirror annotation : method.getAnnotationMirrors()) {
                        final Element annotationElement = annotation.getAnnotationType().asElement();
                        final HttpMethod httpAnnotation = annotationElement.getAnnotation(HttpMethod.class);
                        if (httpAnnotation != null) {
                            httpMethod = httpAnnotation;
                            break;
                        }
                    }
                    // If not annotated with path or an http method than we need not be concerned.
                    if (methodPath == null && httpMethod == null) {
                        continue;
                    }
                    resourceBuilder.methodDefinition(MethodDefinition.build(method, httpMethod, path, methodPath));
                    break;
                default:
                    break;
            }
        }

        final ResourceDefinition resourceDefinition = resourceBuilder.build();
        final JavaFileObject out = processingEnv.getFiler().createSourceFile(resourceDefinition.getLocation());
        try (final Writer writer = out.openWriter()) {
            resource.evaluate(writer, resourceDefinition.getData());
        }
    }
}
