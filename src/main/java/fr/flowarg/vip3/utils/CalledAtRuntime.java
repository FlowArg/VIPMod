package fr.flowarg.vip3.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated element will be called at runtime by a core mod. This avoids useless warnings in the IDE.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE_USE, ElementType.METHOD})
public @interface CalledAtRuntime {}
