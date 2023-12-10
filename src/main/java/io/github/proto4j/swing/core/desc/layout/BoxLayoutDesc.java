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

package io.github.proto4j.swing.core.desc.layout; //@date 19.02.2022

import io.github.proto4j.swing.core.desc.ComponentDesc;
import io.github.proto4j.swing.core.desc.LayoutDesc;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

/**
 * The basic descriptor for the {@link BoxLayout}.
 *
 * @see LayoutDesc
 */
public class BoxLayoutDesc extends LayoutDesc {

    public BoxLayoutDesc(ComponentDesc parent) {
        super(parent);
    }

    @Override
    public LayoutManager createManager() {
        Properties options = getDefinedOptions();
        int x = BoxLayout.X_AXIS;
        String key = options.getProperty("BOX_AXIS");

        if (hasOption(key)) {
            x = getOption(key, Integer.class);
            if (x < 0 || x > BoxLayout.PAGE_AXIS) {
                x = BoxLayout.X_AXIS;
            }
        }

        if (getParent() == null) {
            return null;
        }

        Object parent = getParent().getInstance();
        if (!(parent instanceof Container)) {
            return null;
        }
        else if (parent instanceof RootPaneContainer) {
            parent = ((RootPaneContainer) parent).getContentPane();
        }

        //noinspection MagicConstant
        return new BoxLayout((Container) parent, x);
    }

    @Override
    public boolean isAbsolute() {
        return false;
    }

    @Override
    public Object createConstraints(ComponentDesc child) {
        return null;
    }
}
