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

package io.github.proto4j.swing.core.desc; //@date 05.09.2022

import io.github.proto4j.swing.annotation.Margin;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * The base class for all {@code MarginDesc} inheritors. This abstract
 * layer is used to provide the general requirements on this class.
 * <p>
 * There will be only one {@code MarginDesc} at a time on a
 * {@link ComponentDesc} instance.
 *
 * @see ComponentDesc
 * @since 1.0
 */
public abstract class MarginDesc extends GenericDesc<Margin> {

    /**
     * Creates a new {@code MarginDesc}
     */
    protected MarginDesc() {
        super(Margin.class);
    }

    /**
     * @return {@code true} if a {@link Border} should be created, {@code false}
     *         otherwise
     */
    public abstract boolean shouldPlaceBorder();

    /**
     * Tries to create the {@link Border}. This method should return {@code null}
     * on failure.
     *
     * @return the new {@link Border} instance
     */
    public abstract Border create();

    /**
     * {@inheritDoc}
     *
     * @param component the {@link Component} reference
     */
    @Override
    public void applyTo(Component component) {
        if (component instanceof JComponent) {
            Border border = create();
            if (border != null) {
                ((JComponent) component).setBorder(border);
            }
        }
    }

}
