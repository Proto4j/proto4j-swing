package org.proto4j.swing.annotation;//@date 19.02.2022

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to apply various kinds of models on the annotated
 * components.
 *
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Model {

    /**
     * @return the model class
     */
    Class<?> value();
}
