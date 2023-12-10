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

package io.github.proto4j.swing.annotation;//@date 19.02.2022

import javax.swing.border.*;
import java.lang.annotation.*;

/**
 * In order to add custom borders to a component or to change the border of
 * a specific component this annotation is used.
 * <p>
 * Each attribute is used for a different border type. The different attributes
 * and its context of use are described below. The implemented BorderTypes are
 * the following:
 * <ul>
 *     <li>{@link EtchedBorder}</li>
 *     <li>{@link BevelBorder}</li>
 *     <li>{@link TitledBorder}</li>
 *     <li>{@link LineBorder}</li>
 *     <li>{@link CompoundBorder}</li>
 *     <li>{@link EmptyBorder}</li>
 * </ul>
 * Some attributes related to the borders are used by two or more types. For
 * example, add a border to a simple {@link javax.swing.JLabel}:
 * <pre>
 *  &#064;Margin(value = TitledBorder.class, title = "some text")
 *  private JLabel label;
 * </pre>
 *
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Margin {

    /**
     * @return the actual border type
     */
    Class<? extends Border> value();

    // EmptyBorder attributes
    /**
     * @return The top inset of the border.
     * @apiNote Used on {@link EmptyBorder}
     */
    int emptyTop() default -1;

    /**
     * @return The bottom inset of the border.
     * @apiNote Used on {@link EmptyBorder}
     */
    int emptyBottom() default -1;

    /**
     * @return The left inset of the border.
     * @apiNote Used on {@link EmptyBorder}
     */
    int emptyLeft() default -1;

    /**
     * @return The right inset of the border.
     * @apiNote Used on {@link EmptyBorder}
     */
    int emptyRight() default -1;

    // TitledBorder attributes
    /**
     * @return The title the border should display.
     * @apiNote Used on {@link TitledBorder}
     */
    String title() default "";

    // BevelBorder attributes
    /**
     * @return The bevel type.
     * @apiNote Used on {@link BevelBorder}
     */
    int bevelType() default -1;

    /**
     * @return The color to use for the highlight.
     * @apiNote Used on {@link EtchedBorder} and {@link BevelBorder}
     */
    String highlight() default ""; //also used for etchedBorder

    /**
     * @return The color to use for the shadow.
     * @apiNote Used on {@link EtchedBorder} and {@link BevelBorder}
     */
    String shadow() default "";

    // LineBorder attributes
    /**
     * Retrieves the color of the border to add. Yet, only color names are
     * accepted. If no color is found to that name, no action will be taken.
     *
     * @return Color of the border.
     * @apiNote Used on {@link LineBorder}
     */
    String lineColor() default "";

    /**
     * @return Thickness of the border.
     * @apiNote Used on {@link LineBorder}
     */
    int lineThickness() default -1;

    /**
     * @return Whether the border has rounded corners.
     * @apiNote Used on {@link LineBorder}
     */
    boolean lineRounded() default false;

    // CompoundBorder attributes
    /**
     * This value specifies the border type which will be added to the
     * component.
     *
     * @return the inside compound border
     * @apiNote Used on {@link CompoundBorder}
     */
    Class<? extends Border> compoundInside() default Border.class;

    /**
     * This value specifies the border type which will be added to the
     * component.
     *
     * @return the outside compound border
     * @apiNote Used on {@link CompoundBorder}
     */
    Class<? extends Border> compoundOutside() default Border.class;

    // EtchedBorder attributes

    /**
     * Declared the used etched border type.
     *
     * @return the {@link EtchedBorder#getEtchType()} value
     * @apiNote Used on {@link EtchedBorder}
     */
    int etchedType() default -1;
}
