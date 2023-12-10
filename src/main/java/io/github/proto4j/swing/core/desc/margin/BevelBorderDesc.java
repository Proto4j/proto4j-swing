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

package io.github.proto4j.swing.core.desc.margin; //@date 19.02.2022


import io.github.proto4j.swing.core.GlobalDesc;
import io.github.proto4j.swing.core.desc.MarginDesc;

import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.util.Properties;

/**
 * Basic implementation for the {@link BevelBorder} description.
 *
 * @see MarginDesc
 * @since 1.0
 */
public class BevelBorderDesc extends MarginDesc {

    @Override
    public boolean shouldPlaceBorder() {
        return hasOption(getDefinedOptions().getProperty("BEVEL_TYPE", ""));
    }

    @Override
    public Border create() {
        Border border = null;
        Properties options = getDefinedOptions();

        String key = options.getProperty("BEVEL_TYPE", "undefined");
        int type = BevelBorder.LOWERED;
        if (hasOption(key)) {
            type = getOption(key, Integer.class);

            if (type < 0 || type > 1) {
                type = BevelBorder.LOWERED;
            }
        }

        String h = options.getProperty("HIGHLIGHT", "undefined");
        String s = options.getProperty("SHADOW", "undefined");
        if (!hasOption(s) || !hasOption(h)) {
            //noinspection MagicConstant
            border = new BevelBorder(type);
        } else {
            String shadow = getOption(s, String.class);
            String highlight = getOption(h, String.class);

            border = new BevelBorder(type, GlobalDesc.getColor(highlight), GlobalDesc.getColor(shadow));
        }
        return border;
    }

}
