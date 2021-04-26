package vip.creatio.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation, meaning that the instance of this class is immutable
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Immutable { }
