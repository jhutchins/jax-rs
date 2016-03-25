package io.nti.jaxrs.processors;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jeff Hutchins
 */
public class ResourceDefinitionBuilder {

    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    private List<String> constructorParams = new LinkedList<>();
    private Set<Method> methods;
    private String accepts;
    private String delegateClass;

    public ResourceDefinitionBuilder constructor(ExecutableElement constructor) {
        Map<String, Object> parameters = new HashMap<>();
        for (VariableElement var : constructor.getParameters()) {
            // TODO: constructor stuff
        }
        return this;
    }

    public ResourceDefinitionBuilder methods(Set<Method> methods) {
        this.methods = methods;
        return this;
    }

    public ResourceDefinitionBuilder accepts(String accepts) {
        this.accepts = accepts;
        return this;
    }

    public ResourceDefinitionBuilder delegateClass(String delegateClass) {
        this.delegateClass = delegateClass;
        return this;
    }

    public Map<String, Object> build() {
        final Map<String, Object> result = new HashMap<>();
        result.put("name", delegateClass);
        result.put("proxyName", "Proxy" + COUNTER.getAndIncrement());
        result.put("constructor", constructorParams);
        return result;
    }
}
