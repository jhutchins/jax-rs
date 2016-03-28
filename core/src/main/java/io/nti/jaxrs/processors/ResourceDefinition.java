package io.nti.jaxrs.processors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.ws.rs.Path;

import static io.nti.jaxrs.processors.Utils.*;

/**
 * @author Jeff Hutchins
 */
public class ResourceDefinition {

    private static final AtomicInteger COUNTER = new AtomicInteger(0);
    private static final String PACKAGE = "io.nti.jaxrs.impl.generated";

    private final List<String> constructorParams;
    private final Set<MethodDefinition> methodDefinitions;
    private final String accepts;
    private final String delegate;
    private final String regex;
    private final String generator;
    private final String name;
    private Map<String, Object> data;

    ResourceDefinition(
            List<String> constructorParams,
            Set<MethodDefinition> methodDefinitions,
            String accepts,
            String delegate,
            String regex,
            String generator) {
        this.constructorParams = constructorParams;
        this.methodDefinitions = methodDefinitions;
        this.accepts = accepts;
        this.delegate = delegate;
        this.regex = regex;
        this.generator = generator;
        this.name = "Proxy" + COUNTER.getAndIncrement();
    }

    public String getLocation() {
        return PACKAGE + "." + this.name;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Map<String, Object> getData() {
        if (data == null) {
            data = new HashMap<>();
            data.put("name", delegate);
            data.put("proxyName", name);
            data.put("constructor", constructorParams);
            data.put("generator", generator);
            data.put("regex", regex);
            data.put("methodDefinitions", methodDefinitions);
            data.put("base", regex);
            data.put("package", PACKAGE);
        }
        return data;
    }

    static class Builder {
        private final List<String> constructorParams = new LinkedList<>();
        private Set<MethodDefinition> methodDefinitions = new HashSet<>();
        private String accepts;
        private String delegate;
        private String regex;
        private String generator;

        Builder constructor(ExecutableElement constructor) {
            for (VariableElement var : constructor.getParameters()) {
                // TODO: constructor stuff
            }
            return this;
        }

        Builder methodDefinition(MethodDefinition methodDefinition) {
            this.methodDefinitions.add(methodDefinition);
            return this;
        }

        Builder accepts(String accepts) {
            this.accepts = accepts;
            return this;
        }

        Builder delegate(Name delegate) {
            this.delegate = delegate.toString();
            return this;
        }

        Builder path(Path path) {
            this.regex = "^" + regexify(path.value());
            return this;
        }

        Builder generator(String generator) {
            this.generator = generator;
            return this;
        }

        public ResourceDefinition build() {
            return new ResourceDefinition(constructorParams, methodDefinitions, accepts, delegate, regex, generator);
        }
    }
}
