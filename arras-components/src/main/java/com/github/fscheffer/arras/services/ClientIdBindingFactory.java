package com.github.fscheffer.arras.services;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.services.BindingFactory;

public class ClientIdBindingFactory implements BindingFactory {

    @Override
    public Binding newBinding(String description, ComponentResources container, ComponentResources component,
                              String expression, Location location) {

        return new ClientIdBinding(location, container, expression);
    }
}