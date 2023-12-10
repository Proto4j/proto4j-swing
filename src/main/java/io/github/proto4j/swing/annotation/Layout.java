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

import javax.swing.BoxLayout;
import java.awt.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The main case of this annotation is the definition of how to lay out
 * Containers. There are only a few attributes to have in mind while using
 * this annotation.
 * <p>
 * <b>CAUTION:</b> If this annotation is used on a {@code RootPaneContainer}
 * the layout of the content pane is changed.
 *
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface Layout {

    /**
     * @return the used layout manager type
     */
    Class<? extends LayoutManager> value() default LayoutManager.class;

    /**
     * @return whether the absolute layout should be used
     */
    boolean absoluteLayout() default false;

    /**
     * Constructs a border layout with the horizontal gaps between components.
     *
     * @return The horizontal gaps between components
     * @apiNote Used on {@link BorderLayout} and {@link FlowLayout}
     */
    int hgap() default -1;

    /**
     * Constructs a border layout with the vertical gaps between components.
     *
     * @return The vertical gaps between components
     * @apiNote Used on {@link BorderLayout} and {@link FlowLayout}
     */
    int vgap() default -1;

    /**
     * @return The axis to lay out components along
     * @apiNote Used on {@link BoxLayout}
     */
    int axis() default -1;

    /**
     * This attribute determines how each row distributes empty space.
     *
     * @return how each row distributes empty space
     * @apiNote Used on {@link FlowLayout}
     */
    int align() default -1;

    //GridLayout

    /**
     * This is the number of rows specified for the grid.
     *
     * @return the number of rows for the grid
     * @apiNote Used on {@link GridLayout}
     */
    int rows() default -1;

    /**
     * This is the number of columns specified for the grid.
     *
     * @return the number of columns for the grid
     * @apiNote Used on {@link GridLayout}
     */
    int columns() default -1;
}
