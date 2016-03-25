package io.nti.jaxrs.processors;

import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;

import com.google.common.collect.Maps;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

/**
 * @author Jeff Hutchins
 */
@SupportedAnnotationTypes("javax.ws.rs.Path")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ResourceProcessor extends AbstractProcessor {

    private final Pattern pathParams = Pattern.compile("(\\{.+?})");
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
        final Map<String, Object> data = Maps.newHashMap();
        final List<String> regexs = new LinkedList<>();
        final List<Method> methods = new LinkedList<>();

        final String path = element.getAnnotation(Path.class).value();

        data.put("name", element.getQualifiedName());
        data.put("proxyName", element.getSimpleName().toString() + "Proxy");

        for (Element e : element.getEnclosedElements()) {
            final ElementKind kind = e.getKind();
            messager.printMessage(Diagnostic.Kind.NOTE, "Got a " + kind);
            if (kind.equals(ElementKind.CONSTRUCTOR)) {
                ExecutableElement constructor = (ExecutableElement) e;
                Map<String, Object> parameters = new HashMap<>();
                for (VariableElement var : constructor.getParameters()) {
                    // TODO: constructor stuff
                }
                data.put("constructor", parameters);
            }
            else {
                if (kind.equals(ElementKind.METHOD)) {
                    ExecutableElement method = (ExecutableElement) e;
                    Path methodPath = method.getAnnotation(Path.class);
                    HttpMethod httpMethod = null;
                    for (AnnotationMirror annotation : method.getAnnotationMirrors()) {
                        final Element annotationElement = annotation.getAnnotationType().asElement();
                        final HttpMethod httpAnnotation = annotationElement.getAnnotation(HttpMethod.class);
                        if (httpAnnotation != null) {
                            if (httpMethod == null) {
                                httpMethod = httpAnnotation;
                            } else {
                                messager.printMessage(Diagnostic.Kind.ERROR, "Duplicate HTTP verb annotations");
                            }
                        }
                    }
                    if (methodPath == null && httpMethod == null) {
                        continue;
                    }
                    regexs.add(new StringBuilder("^")
                            .append(regexify(path))
                            .append(methodPath == null ? "" : regexify(methodPath.value()))
                            .append("$")
                            .toString());
                    methods.add(Method.builder()
                            .type(httpMethod)
                            .name(method.getSimpleName())
                            .returnType(method.getReturnType())
                            .parameters(method.getParameters())
                            .build());
                }
            }
        }

        data.put("regexs", regexs);
        data.put("methods", methods);
        data.put("generator", getClass().getCanonicalName());
        data.put("base", "^" + regexify(path));

        final String location = "io.nti.jaxrs.impl.generated." + element.getSimpleName() + "Proxy";
        final JavaFileObject out = processingEnv.getFiler().createSourceFile(location);
        try (final Writer writer = out.openWriter()) {
            resource.evaluate(writer, data);
        }
    }

    private String regexify(String value) {
        Matcher matcher = pathParams.matcher(value);
        while (matcher.find()) {
            final String match = matcher.group(1);
            StringBuilder rule = new StringBuilder("(?<");
            final int index = match.indexOf(":");
            if (index > -1) {
                rule.append(match.substring(1, index));
                rule.append(">");
                rule.append(match.substring(index + 1, match.length() - 1).replace("\\", "\\\\"));
                rule.append(")");
            } else {
                rule.append(match.substring(1, match.length() - 1));
                rule.append(">[^/]+)");
            }
            value = value.replace(match, rule.toString());
        }
        return value;
    }
}
