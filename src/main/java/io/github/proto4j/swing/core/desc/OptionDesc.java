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

import io.github.proto4j.swing.annotation.Option;
import io.github.proto4j.swing.core.GlobalDesc;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Properties;

/**
 * The wrapper class for the {@link Option} annotation.
 *
 * @see Option
 * @since 1.0
 */
public class OptionDesc extends GenericDesc<Option> {

    /**
     * Creates a new {@code OptionDesc}.
     */
    public OptionDesc() {
        super(Option.class);
    }

    /**
     * {@inheritDoc}
     *
     * @param component the {@link Component} reference
     */
    @Override
    public void applyTo(Component component) {
        Properties options = getDefinedOptions();

        // Apply the component's background color
        String bg = options.getProperty("BACKGROUND", UNDEFINED);
        if (hasOption(bg)) {
            Color color = GlobalDesc.getColor(getOption(bg, String.class));

            // currently the returned color will be null if the format
            // is not recognized.
            if (color != null) {
                component.setBackground(color);
            }
        }

        // Apply the component's foreground color
        String fg = options.getProperty("FOREGROUND", UNDEFINED);
        if (hasOption(fg)) {
            Color color = GlobalDesc.getColor(getOption(fg, String.class));
            // Currently the returned color will be null if the format
            // is not recognized.
            if (color != null) {
                component.setForeground(color);
            }
        }

        // This option set the text of text-based containers such as JLabel or
        // AbstractButton.class
        String txt = options.getProperty("TEXT", UNDEFINED);
        if (hasOption(txt)) {
            String textData = getOption(txt, String.class);
            if (Option.Query.isQuery(textData)) {
                Option.Query query = new Option.Query(textData, component);
                textData = query.get();
            }

            if (component instanceof JLabel) {
                ((JLabel) component).setText(textData);
            } else if (component instanceof JTextComponent) {
                ((JTextComponent) component).setText(textData);
            } else if (component instanceof AbstractButton) {
                ((AbstractButton) component).setText(textData);
            } else if (component instanceof JToolTip) {
                ((JToolTip) component).setTipText(textData);
            }
        }

        String title = options.getProperty("TITLE", UNDEFINED);
        if (hasOption(title)) {
            String titleData = getOption(title, String.class);
            if (Option.Query.isQuery(titleData)) {
                Option.Query query = new Option.Query(titleData, component);
                titleData = query.get();
            }

            if (component instanceof Frame) {
                ((Frame) component).setTitle(titleData);
            }
        }

        String pEnabled = options.getProperty("ENABLED", UNDEFINED);
        if (hasOption(pEnabled)) {
            boolean enabled = getOption(pEnabled, Boolean.class);
            component.setEnabled(enabled);
        }

    }


}
