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

package io.github.proto4j.swing.annotation;//@date 14.02.2022

import javax.swing.*;
import java.awt.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to place the components anywhere you like. The
 * annotation class defines a lot of different attributes where each has
 * a specific use-case. Usually, only a few of these attributes are used
 * within the generation process.
 * <p>
 * The defined attributes may be used within the declared {@code Layout} to
 * use.
 *
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Position {

    /**
     * Returns specific constraints used by the {@link BorderLayout}.
     *
     * @return specific constraints used by the {@link BorderLayout}.
     * @implNote Used on {@link BorderLayout}
     */
    String constraints() default "";

    /**
     * Returns the absolute X coordinate.
     *
     * @return The absolute X coordinate
     */
    int x() default -1;

    /**
     * Returns the absolute Y coordinate.
     *
     * @return The absolute Y coordinate
     */
    int y() default -1;

    /**
     * Returns the height of the annotated component.
     *
     * @return The height of the annotated component
     * @see Bounds#size()
     * @deprecated since 1.1.0
     */
    int height() default -1;

    /**
     * Returns the width of the annotated component.
     *
     * @return The width of the annotated component
     * @see Bounds#size()
     * @deprecated since 1.1.0
     */
    int width() default -1;

    //GridBagLayout

    /**
     * Specifies the cell containing the leading edge of the component's
     * display area, where the first cell in a row has {@code gridx=0}.
     *
     * @return the leading edge of the component's display area
     * @implNote Used on {@link GridBagConstraints} by the {@link GridBagLayout}
     * @see GridBagConstraints#gridx
     */
    int gridx() default -1;

    /**
     * Specifies the cell at the top of the component's display area,
     * where the topmost cell has {@code gridy=0}.
     *
     * @return the cell at the top of the component's display area
     * @implNote Used on {@link GridBagConstraints} by the {@link GridBagLayout}
     * @see GridBagConstraints#gridy
     */
    int gridy() default -1;

    /**
     * Specifies the number of cells in a row for the component's
     * display area.
     *
     * @return the number of cells in a row for the component's
     *         display area.
     * @implNote Used on {@link GridBagConstraints} by the {@link GridBagLayout}
     * @see GridBagConstraints#gridwidth
     */
    int gridWidth() default -1;

    /**
     * Specifies the number of cells in a column for the component's
     * display area.
     *
     * @return the number of cells in a column for the component's
     *         display area.
     * @implNote Used on {@link GridBagConstraints} by the {@link GridBagLayout}
     * @see GridBagConstraints#gridheight
     */
    int gridHeight() default -1;

    /**
     * Specifies how to distribute extra horizontal space.
     * <p>
     * If all the weights are zero, all the extra space appears between the
     * grids of the cell and the left and right edges.
     *
     * @return the value on how to distribute extra horizontal space
     * @implNote Used on {@link GridBagConstraints} by the {@link GridBagLayout}
     * @see GridBagConstraints#weightx
     */
    double weightx() default -1.;

    /**
     * Specifies how to distribute extra vertical space.
     * <p>
     * If all the weights are zero, all the extra space appears between the
     * grids of the cell and the top and bottom edges.
     *
     * @return the value on how to distribute extra vertical space
     * @implNote Used on {@link GridBagConstraints} by the {@link GridBagLayout}
     * @see GridBagConstraints#weighty
     */
    double weighty() default -1.;

    /**
     * This attribute is used when the component is smaller than its display
     * area. It determines where, within the display area, to place the
     * component.
     *
     * @return The point where the component should be placed within the
     *         display area.
     * @implNote Used on {@link GridBagConstraints} by the {@link GridBagLayout}
     * @see GridBagConstraints#anchor
     */
    int anchor() default -1;

    /**
     * This field is used when the component's display area is larger
     * than the component's requested size. It determines whether to
     * resize the component, and if so, how.
     *
     * @return whether the component should be resized
     * @implNote Used on {@link GridBagConstraints} by the {@link GridBagLayout}
     * @see GridBagConstraints#fill
     */
    int fill() default -1;

    /**
     * This attribute specifies the external padding of the component, the
     * minimum amount of space between the component and the edges of its
     * display area.
     * <p>
     * The values provided here must be an array of four {@link Integer}
     * numbers. They are wrapped in the following form:
     * <pre>
     *     int[] padding = {top, left, bottom, right};
     *     Insets insets = new Insets(padding[0], ...);
     * </pre>
     *
     * @return The external padding for the component
     * @implNote Used on {@link GridBagConstraints} by the {@link GridBagLayout}
     * @see Insets
     * @see GridBagConstraints#insets
     */
    int[] insets() default {0, 0, 0, 0};

    /**
     * This attribute specifies the internal padding of the component, how much
     * space to add to the minimum width of the component. The width of the
     * component is at least its minimum width plus {@code ipadx} pixels.
     *
     * @return The internal padding of the component
     * @implNote Used on {@link GridBagConstraints} by the {@link GridBagLayout}
     * @see GridBagConstraints#ipadx
     */
    int ipadx() default -1;

    /**
     * This attribute specifies the internal padding, how much space to add
     * to the minimum height of the component. The height of the component is
     * at least its minimum height plus {@code ipady} pixels.
     *
     * @return The internal padding
     * @implNote Used on {@link GridBagConstraints} by the {@link GridBagLayout}
     * @see GridBagConstraints#ipady
     */
    int ipady() default -1;

    //SpringLayout

    /**
     * Returns specific constraints used by the {@link SpringLayout}.
     *
     * @return specific constraints used by the {@link SpringLayout}.
     * @implNote Used on {@link SpringLayout}
     */
    String vConstraints() default "";

    /**
     * Returns specific constraints used by the {@link SpringLayout}.
     *
     * @return specific constraints used by the {@link SpringLayout}.
     * @implNote Used on {@link SpringLayout}
     */
    String cConstraints() default "";

    /**
     * Returns the used padding when adding components to the
     * {@link SpringLayout}.
     *
     * @return the used padding when adding components to the
     *         {@link SpringLayout}
     * @implNote Used on {@link SpringLayout}
     */
    int pad() default -1;
}
