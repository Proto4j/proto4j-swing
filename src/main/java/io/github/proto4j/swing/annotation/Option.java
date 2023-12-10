/*
 * MIT License
 *
 * Copyright (c) 2022 Proto4j
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.proto4j.swing.annotation;//@date 12.02.2022

import javax.swing.*;
import java.awt.*;
import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.IllegalFormatCodePointException;
import java.util.function.Supplier;

/**
 * The {@code Option} annotation is used to set general information to the annotated
 * component. There are only a few attributes this annotation provides.
 * <p>
 * <b>TIP:</b> This annotation is {@link Repeatable}, so there can be multiple
 * definition on each component. Duplicate attributes will be overridden.
 * <p>
 * Changes since {@code 1.1.0}: Dynamic method invocation to retrieve values
 * for each attribute can now be used. You can simply query the {@link UIManager}
 * for a property, e.g.:
 * <pre>
 *      &#064;Option(text  = "@UIManager#some.property.text")
 *      private JLabel label;
 * </pre>
 * te example above would query the given property string from the
 * {@link UIManager}. Use {@code @} as the first character to indicate that
 * a query should be executed. Accepted values for naw are the following:
 * <ul>
 *      <li>{@code UIManager#key} will be translated into {@link UIManager#getString(Object)}
 *      and the result will be converted into a {@link String}. </li>
 *
 *      <li> {@code class#method} will call the <b>static</b> method of the class,
 *      for example {@code text = "@FooClass#getTextFor"}, if the referenced method
 *      got the following signature: {@code getTextFor(Component)}.
 *      </li>
 * </ul>
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

    /**
     * Specifies whether the annotated component should be enabled or
     * disabled after initialization.
     *
     * @return {@code true} if the annotated component should be enabled;
     *         {@code false} otherwise
     */
    boolean enabled() default true;

    /**
     * A class interpreting an option string and returning a qualified
     * {@link String} as a result.
     *
     * @see Option
     * @since 1.1.0
     */
    // For future releases: maybe add getter
    public static final class Query implements Supplier<String> {

        /**
         * The query string indicator, which is currently defined to be
         * {@code @}.
         *
         * @since 1.1.0
         */
        public static final char indicator = '@';

        /**
         * The currently defined query separator that marks the start for the
         * query argument.
         *
         * @since 1.1.0
         */
        public static final char separator = '#';

        /**
         * The query {@link String}.
         */
        private final String data;

        /**
         * The referenced component.
         */
        private final Object component;

        /**
         * A reference to the class that will be used to get the method
         * that will be invoked.
         */
        private Class<?> cls;

        /**
         * Tha target method, which will be invoked.
         */
        private Method target;

        /**
         * The argument that will be used to invoke the target method.
         */
        private Object argument;

        public static boolean isQuery(String text) {
            return text != null && !text.isEmpty() && text.charAt(0) == indicator;
        }

        /**
         * Creates a new {@link Query} object and tries to parse the given
         * query string.
         *
         * @param data the statement to parse
         * @param component the referenced component
         */
        public Query(String data, Object component) {
            this.data = data;
            this.component = component;
            try {
                parseStatement();
            } catch (ClassNotFoundException e) {
                System.err.println(getClass().getSimpleName() + ':' + e.toString());
            }
        }

        /**
         * Parses the given query statement if possible and throws an
         * exception on invalid input.
         *
         * @throws IllegalFormatCodePointException if no separator was found
         * @throws ClassNotFoundException          if the provided class was not found
         */
        private void parseStatement() throws ClassNotFoundException {
            if (data == null || data.charAt(0) != indicator) {
                return;
            }

            int pos = data.indexOf(separator);
            if (pos == -1) {
                throw new IllegalFormatCodePointException(separator);
            }

            String className = data.substring(1, pos);
            argument = data.substring(pos + 1);

            if (className.equals(UIManager.class.getSimpleName())) {
                cls = UIManager.class;
            } else {
                cls = Class.forName((String) argument);
                for (Method method : cls.getMethods()) {
                    if (method.getName().equals(argument)) {
                        target = method;
                        break;
                    }
                }
                argument = target.getParameterCount() == 0 ? null : component;
            }
        }

        /**
         * Fetches a result.
         *
         * @return a result
         */
        @Override
        public synchronized String get() {
            String result = null;
            try {
                if (cls == UIManager.class) {
                    result = UIManager.getString(argument);
                } else {
                    if (target != null) {
                        // Extra temp argument is necessary, because we don't
                        // want a NPE to be thrown.
                        Object temp = null;
                        if (argument == null) {
                            temp = target.invoke(null);
                        } else {
                            temp = target.invoke(null, argument);
                        }
                        if (temp != null) {
                            result = temp.toString();
                        }
                    }
                }
            } catch (ReflectiveOperationException e) {
                System.err.println(getClass().getSimpleName() + ": " + e.toString());
            }
            return result;
        }
    }

}
