package org.proto4j.swing.core.desc.margin; //@date 19.02.2022


import org.proto4j.swing.core.GlobalDesc;
import org.proto4j.swing.core.desc.MarginDesc;

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
