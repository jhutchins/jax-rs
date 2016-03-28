package io.nti.jaxrs.processors;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import static io.nti.jaxrs.processors.Utils.*;

/**
 * @author Jeff Hutchins
 */
public class MethodDefinition {

    private final String httpVerb;
    private final String execution;
    private final String regex;

    MethodDefinition(String httpVerb, String execution, String regex) {
        this.httpVerb = httpVerb;
        this.execution = execution;
        this.regex = regex;
    }

    public static MethodDefinition build(ExecutableElement method, HttpMethod http, Path parent, Path self) {
        return new Builder()
                .type(http)
                .name(method.getSimpleName())
                .returnType(method.getReturnType())
                .parameters(method.getParameters())
                .path(parent, self)
                .build();
    }

    public String getHttpVerb() {
        return this.httpVerb;
    }

    public String getExecution() {
        return this.execution;
    }

    public String getRegex() {
        return this.regex;
    }

    private  static class Builder {
        private String type;
        private String name;
        private String returnType;
        private List<String> parameters = new LinkedList<>();
        private String regex;


        Builder type(HttpMethod type) {
            if (type != null) {
                this.type = type.value();
            }
            return this;
        }

        Builder name(Name name) {
            return name(name.toString());
        }

        Builder name(String name) {
            this.name = name;
            return this;
        }

        Builder returnType(TypeMirror returnType) {
            if (returnType.getKind() != TypeKind.VOID) {
                this.returnType = ((DeclaredType) returnType).asElement().toString();
            }
            return this;
        }

        Builder parameters(List<? extends VariableElement> elements) {
            for (VariableElement element : elements) {
                PathParam pathParam;
                if ((pathParam = element.getAnnotation(PathParam.class)) != null) {
                    String item = "matcher.group(\"" + pathParam.value() + "\")";
                    switch (element.asType().toString()) {
                        case "int":
                            item = "Integer.valueOf(" + item + ")";
                            break;
                        case "boolean":
                            item = "Boolean.valueOf(" + item + ")";
                            break;
                        case "double":
                            item = "Double.valueOf(" + item + ")";
                            break;
                        case "long":
                            item = "Long.valueOf(" + item + ")";
                            break;
                    }
                    parameters.add(item);
                } else {
                    parameters.add("context.getPayload(" + element.asType().toString() + ".class)");
                }
            }
            return this;
        }

        Builder path(Path parent, Path path) {
            regex = "^" + regexify(parent.value()) + (path == null ? "" : "/" + regexify(path.value())) + "$";
            return this;
        }

        private String invoke() {
            return "resource." + name + "(" + parameters.stream().collect(Collectors.joining(", ")) + ")";
        }

        public MethodDefinition build() {
            final String execution;
            if (returnType != null && !type.equals("HEAD")) {
                switch (returnType) {
                    case "javax.ws.rs.core.Response":
                        execution = "return responseMethod(" + invoke() + ");";
                        break;
                    case "javax.ws.rs.core.GenericEntity":
                        execution = "return genericMethod(" + invoke() + ");";
                        break;
                    default:
                        execution = "return other(" + invoke() + ");";
                        break;
                }
            } else if (returnType != null) {
                execution = invoke() + ";\n            return haveHead();";
            } else {
                execution = invoke() + ";\n            return voidMethod();";
            }
            return new MethodDefinition(type, execution, regex);
        }
    }
}
