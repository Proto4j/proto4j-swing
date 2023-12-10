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

package io.github.proto4j.swing.annotation;//@date 04.09.2022

import io.github.proto4j.swing.laf.DefaultLAFProvider;
import io.github.proto4j.swing.laf.LAFProvider;

import javax.swing.BoxLayout;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to include components in the GUI generation. There
 * are only two attributes this annotation provides:
 * <ul>
 *     <li>{@code ComponentNumber} and</li>
 *     <li>{@code provider}</li>
 * </ul>
 * The component number is defined as the default annotation attribute and
 * specifies the order this component is added to the parent component.
 * <p>
 * <b>TIP:</b> The component number can be used with the {@link BoxLayout} due
 * to the fact that this layout places their components in the order they were
 * added to it. Another place would be the menu-context.
 * <p>
 * For example:
 * <pre>
 *      &#064;Swing  // can only be used on field declarations
 *      private JSeparator separator;
 * </pre>
 * <p>
 * Changes on version {@code 1.1.0}: This annotation can be used on classes
 * also to provide more generation flexibility.
 *
 * @see LAFProvider
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Swing {
    /**
     * @return The order this component is added to the parent component.
     */
    int value() default 1;

    /**
     * @return the class of the {@code LAFProvider} that should be used.
     */
    Class<? extends LAFProvider> provider() default DefaultLAFProvider.class;
}
