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

import io.github.proto4j.swing.annotation.Position;

import java.awt.*;
import java.util.Properties;

/**
 * The wrapper class for the {@link Position} annotation.
 *
 * @see Position
 * @since 1.0
 */
public class PositionDesc extends GenericDesc<Position> {

    /**
     * Creates a new {@code PositionDesc}.
     */
    public PositionDesc() {
        super(Position.class);
    }

    /**
     * {@inheritDoc}
     *
     * @param component the {@link Component} reference
     */
    @Override
    public void applyTo(Component component) {
        // This annotation is mainly used to store layout information for
        // other SwingHandler objects.
        Properties options = getDefinedOptions();

        Point loc = getLocation(component.getLocation(), options);
        component.setBounds(loc.x, loc.y, component.getWidth(), component.getHeight());
    }


    private Point getLocation(Point base, Properties options) {
        String xPos = options.getProperty("X", UNDEFINED);
        int    temp;
        if (hasOption(xPos)
                && (temp = getOption(xPos, Integer.class)) != INVALID_INT) {
            base.x = temp;
        }

        String yPos = options.getProperty("Y", UNDEFINED);
        if (hasOption(yPos)
                && (temp = getOption(yPos, Integer.class)) != INVALID_INT) {
            base.y = temp;
        }
        return base;
    }

}

