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

import io.github.proto4j.swing.annotation.Option;
import io.github.proto4j.swing.core.GlobalDesc;
import io.github.proto4j.swing.core.desc.MarginDesc;

import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Properties;

/**
 * Basic implementation for the {@link TitledBorder} description.
 *
 * @see MarginDesc
 * @since 1.0
 */
public class TitledBorderDesc extends MarginDesc {

    @Override
    public boolean shouldPlaceBorder() {
        return true;
    }

    @Override
    public Border create() {
        Properties options = getDefinedOptions();
        if (options == null) {
            return null;
        }

        String key = options.getProperty("TITLE", UNDEFINED);
        if (!hasOption(key)) {
            return null;
        }

        String title = get(key).toString();
        if (Option.Query.isQuery(title)) {
            title = new Option.Query(title, null).get();
        }
        key = options.getProperty("LINE_COLOR", UNDEFINED);
        if (hasOption(key)) {
            Color color = GlobalDesc.getColor(get(key).toString());

            // In future releases all properties that are hardcoded right
            // now will be available in the Margin.class annotation.
            return new TitledBorder(
                    null, title, TitledBorder.LEADING,
                    TitledBorder.DEFAULT_POSITION,
                    Font.getFont(Font.SANS_SERIF), color
            );
        }
        return new TitledBorder(title);
    }
}
