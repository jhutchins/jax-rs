package io.nti.jaxrs.processors;

import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.PathParam;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jeff Hutchins
 */
public class Method {

    private final String httpVerb;
    private final String name;
    private final String returnType;
    private final List<String> parameters;

    Method(String httpVerb, String name, String returnType, List<String> parameters) {
        this.httpVerb = httpVerb;
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getHttpVerb() {
        return this.httpVerb;
    }

    public String getName() {
        return this.name;
    }

    public String getReturn() {
        return this.returnType;
    }

    public List<String> getParameters() {
        return this.parameters;
    }

    public static class Builder {
        private String type;
        private String name;
        private String returnType;
        private List<String> parameters = new LinkedList<>();


        public Builder type(HttpMethod type) {
            if (type != null) {
                this.type = type.value();
            }
            return this;
        }

        public Builder name(Name name) {
            return name(name.toString());
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder returnType(TypeMirror returnType) {
            if (returnType.getKind() == TypeKind.VOID) {
                this.returnType = "void";
            } else {
                this.returnType = ((DeclaredType) returnType).asElement().toString();
            }
            return this;
        }

        public Builder parameters(List<? extends VariableElement> elements) {
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

        public Method build() {
            return new Method(type, name, returnType, parameters);
        }
    }
}
