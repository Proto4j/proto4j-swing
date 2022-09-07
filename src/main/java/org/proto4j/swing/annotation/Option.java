package org.proto4j.swing.annotation;//@date 12.02.2022

import java.awt.*;
import java.lang.annotation.*;

/**
 * The {@code Option} annotation is used to set general information to the annotated
 * component. There are only a few attributes this annotation provides.
 * <p>
 * <b>TIP:</b> This annotation is {@link Repeatable}, so there can be multiple
 * definition on each component. Duplicate attributes will be overridden.
 *
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Repeatable(Options.class)
public @interface Option {

    /**
     * This attribute specifies the parent component the annotated component
     * should be added to. The keyword {@code this} refers to the class object
     * which is annotated with {@code Swing}.
     *
     * @return The parent component's field name
     */
    String target() default "";

    /**
     * This is the title of the annotated {@link Frame} component.
     *
     * @return the title of the frame
     * @apiNote Used on classes that inherit {@link Frame} attributes
     */
    String title() default "";

    /**
     * The single line of text this component will display.
     *
     * @return The text this component will display
     * @apiNote Used on components that supports the {@code setText} method
     */
    String text() default "";

    /**
     * If the component's attributes should be changed locally in a defined
     * method.
     * <p>
     * It specifies a method in the same class which is called after the
     * component has been initialized. This method takes no arguments.
     *
     * @return the target method name
     */
    String init() default "";

    /**
     * Specifies the background color of the annotated component.
     *
     * @return The background color name
     */
    String background() default "";

    /**
     * Specifies the foreground color of the annotated component.
     *
     * @return The foreground color name
     */
    String foreground() default "";

}
