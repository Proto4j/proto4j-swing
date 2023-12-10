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

package io.github.proto4j.swing.core.desc.layout; //@date 14.02.2022

import io.github.proto4j.swing.annotation.Position;
import io.github.proto4j.swing.core.GlobalDesc;
import io.github.proto4j.swing.core.desc.ComponentDesc;
import io.github.proto4j.swing.core.desc.GenericDesc;
import io.github.proto4j.swing.core.desc.LayoutDesc;

import java.awt.*;
import java.util.Objects;
import java.util.Properties;

/**
 * The basic descriptor for the {@link BorderLayout}.
 *
 * @see LayoutDesc
 */
public class BorderLayoutDesc extends LayoutDesc {

    public BorderLayoutDesc(ComponentDesc parent) {
        super(parent);
    }

    public static Object createLayoutConstraints(ComponentDesc child) {
        Objects.requireNonNull(child);

        GenericDesc<Position> desc = child.getDesc(Position.class);
        String constraints = BorderLayout.CENTER;

        if (desc != null) {
            Properties options = GlobalDesc.getSharedOption(Position.class);

            if (options != null) {
                String key = options.getProperty("LAYOUT_CONSTRAINTS");

                if (desc.hasOption(key)) {
                    constraints = desc.get(key).toString();
                }
            }
        }
        return constraints;
    }

    @Override
    public LayoutManager createManager() {
        Properties options = getDefinedOptions();
        String key = options.getProperty("H_GAP");
        int h = 0, v = 0;

        if (hasOption(key)) {
            h = getOption(key, Integer.class);
        }

        key = options.getProperty("V_GAP");
        if (hasOption(key)) {
            v = getOption(key, Integer.class);
        }

        return new BorderLayout(h, v);
    }

    @Override
    public boolean isAbsolute() {
        return false;
    }

    @Override
    public Object createConstraints(ComponentDesc child) {
        return createLayoutConstraints(child);
    }

}
