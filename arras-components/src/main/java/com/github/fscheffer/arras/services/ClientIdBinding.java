package com.github.fscheffer.arras.services;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.internal.bindings.AbstractBinding;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.runtime.Component;

public class ClientIdBinding extends AbstractBinding {

    private ComponentResources container;

    private String             expression;

    public ClientIdBinding(Location location, ComponentResources container, String expression) {
        super(location);
        this.container = container;
        this.expression = expression;
    }

    @Override
    public Object get() {

        Component component = this.container.getEmbeddedComponent(this.expression);

        ClientElement clientElement = ClientElement.class.cast(component);

        return clientElement.getClientId();
    }
}