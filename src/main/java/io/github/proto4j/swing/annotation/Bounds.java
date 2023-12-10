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

package io.github.proto4j.swing.annotation;//@date 02.11.2022

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface Bounds {

    /**
     * Sets the preferred size of this annotated component. The returned array
     * should have a size of at least {@code 2} and should contain the
     * following values:
     * <pre>
     *     preferred := { &lt;width&gt;, &lt;height&gt; }
     * </pre>
     *
     * @return the preferred size for the annotated component.
     */
    int[] preferred() default {-1, -1};

    /**
     * Resizes this component so that it has the given width and height. The
     * structure of this array is the following:
     * <pre>
     *     size := { &lt;width&gt;, &lt;height&gt; }
     * </pre>
     *
     * @return the new size for the annotated component
     */
    int[] size() default {-1, -1};

    /**
     * Specifies the bounds for the annotated component. This attribute is
     * structured as follows:
     * <pre>
     *     bounds := { &lt;x&gt;, &lt;y&gt;, &lt;width&gt;, &lt;height&gt; }
     * </pre>
     *
     * @return the new bounds' values for the annotated component
     */
    int[] bounds() default {-1, -1, -1, -1};

    /**
     * Sets the minimum size for the annotated component. The structure of
     * the returned array should be the same as defined in {@link #preferred()}.
     *
     * @return the new minimum size
     * @see #preferred()
     */
    int[] min() default {-1, -1};

    /**
     * Sets the maximum size for the annotated component. The structure of
     * the returned array should be the same as defined in {@link #preferred()}.
     *
     * @return the new maximum siz
     * @see #preferred()
     */
    int[] max() default {-1, -1};

}
