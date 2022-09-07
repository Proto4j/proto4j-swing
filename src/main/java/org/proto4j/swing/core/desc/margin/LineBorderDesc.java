package org.proto4j.swing.core.desc.margin; //@date 19.02.2022


import org.proto4j.swing.core.GlobalDesc;
import org.proto4j.swing.core.desc.MarginDesc;

import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Properties;

/**
 * Basic implementation for the {@link LineBorder} description.
 *
 * @see MarginDesc
 * @since 1.0
 */
public class LineBorderDesc extends MarginDesc {

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

        String key = options.getProperty("LINE_COLOR");
        if (!hasOption(key)) {
            return null;
        }

        Color color = GlobalDesc.getColor(get(key).toString());
        if (color == null) {
            return null;
        }

        key = options.getProperty("LINE_THICKNESS");
        int thickness = 1;
        if (hasOption(key)) {
            thickness = getOption(key, Integer.class);
        }

        key = options.getProperty("LINE_ROUNDED");
        boolean rounded = false;
        if (hasOption(key)) {
            rounded = getOption(key, Boolean.class);
        }

        return new LineBorder(color, thickness, rounded);
    }
}
