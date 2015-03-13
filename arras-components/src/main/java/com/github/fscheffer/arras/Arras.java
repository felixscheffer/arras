package com.github.fscheffer.arras;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marker annotation for the the {@link org.apache.tapestry5.services.javascript.JavaScriptStack} that are provided by the Arras.
 */
@Target({ ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD })
@Retention(RUNTIME)
public @interface Arras {

}
