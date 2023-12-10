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

import io.github.proto4j.swing.annotation.SwingWindow;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

/**
 * The wrapper class for the {@link SwingWindow} annotation.
 *
 * @see SwingWindow
 * @since 1.0
 */
public class WindowDesc extends GenericDesc<SwingWindow> {

    /**
     * Creates a new {@code WindowDesc}.
     */
    public WindowDesc() {
        super(SwingWindow.class);
    }

    /**
     * {@inheritDoc}
     *
     * @param component the {@link Component} reference
     */
    @Override
    public void applyTo(Component component) {
        Properties options = getDefinedOptions();

        if (component instanceof Frame) {
            Frame frame = (Frame) component;

            String key = options.getProperty("RESIZABLE", UNDEFINED);
            if (hasOption(key)) {
                frame.setResizable(getOption(key, Boolean.class));
            }

            key = options.getProperty("ALWAYS_ON_TOP", UNDEFINED);
            if (hasOption(key)) {
                frame.setAlwaysOnTop(getOption(key, Boolean.class));
            }

            if (frame instanceof JFrame) {
                key = options.getProperty("CLOSE_OPERATION", UNDEFINED);
                if (hasOption(key)) {
                    //noinspection MagicConstant
                    ((JFrame) frame).setDefaultCloseOperation(getOption(key, Integer.class));
                }
            }
        }
    }

}
