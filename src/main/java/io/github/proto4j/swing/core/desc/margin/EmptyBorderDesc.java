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

package io.github.proto4j.swing.core.desc.margin; //@date 20.02.2022

import io.github.proto4j.swing.core.desc.MarginDesc;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Properties;

/**
 * Basic implementation for the {@link EmptyBorder} description.
 *
 * @see MarginDesc
 * @since 1.0
 */
public class EmptyBorderDesc extends MarginDesc {

    @Override
    public boolean shouldPlaceBorder() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Border create() {
        Properties options = super.getDefinedOptions();
        if (options == null) {
            return null;
        }

        Insets insets = new Insets(
                getIntOption("TOP", options),
                getIntOption("LEFT", options),
                getIntOption("RIGHT", options),
                getIntOption("BOTTOM", options)
        );

        return new EmptyBorder(insets);
    }

    private int getIntOption(String key, Properties properties) {
        String kV = properties.getProperty(key);
        if (kV == null || !hasOption(kV)) {
            return 1;
        }
        return getOption(kV, Integer.class);
    }

}
