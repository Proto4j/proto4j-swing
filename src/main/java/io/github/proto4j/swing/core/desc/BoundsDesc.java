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

package io.github.proto4j.swing.core.desc; //@date 02.11.2022

import io.github.proto4j.swing.annotation.Bounds;

import java.awt.*;
import java.util.Properties;
import java.util.function.Consumer;

public class BoundsDesc extends GenericDesc<Bounds> {

    /**
     * Creates a new {@code GenericDesc} from the given annotation class.
     *
     * @throws NullPointerException if the given annotation class is {@code null}
     */
    public BoundsDesc() {
        super(Bounds.class);
    }

    /**
     * Applies the stored options on the given component.
     *
     * @param component the {@link Component} reference
     */
    @Override
    public void applyTo(Component component) {
        Properties options = getDefinedOptions();

        String pBounds = options.getProperty("BOUNDS", UNDEFINED);
        if (hasOption(pBounds)) {
            int[] bounds = getOption(pBounds, int[].class);
            if (bounds != null && bounds.length >= 4 && isValid(bounds)) {
                component.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
            }
        }

        setSize("SIZE", options, component::setSize);
        setSize("MIN", options, component::setMinimumSize);
        setSize("MAX", options, component::setMaximumSize);
        setSize("PREFERRED", options, component::setPreferredSize);
    }

    private void setSize(String key, Properties options, Consumer<Dimension> consumer) {
        String name = options.getProperty(key, UNDEFINED);
        if (hasOption(name)) {
            int[] values = getOption(name, int[].class);
            if (isValid(values) && values.length >= 2) {
                consumer.accept(new Dimension(values[0], values[1]));
            }
        }
    }

    private boolean isValid(int[] values) {
        for (int value : values) {
            if (value == INVALID_INT) {
                return false;
            }
        }
        return true;
    }
}
