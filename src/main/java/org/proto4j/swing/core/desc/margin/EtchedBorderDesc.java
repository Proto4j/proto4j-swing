package org.proto4j.swing.core.desc.margin; //@date 20.02.2022


import org.proto4j.swing.core.GlobalDesc;
import org.proto4j.swing.core.desc.MarginDesc;

import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.Properties;

/**
 * Basic implementation for the {@link EtchedBorder} description.
 *
 * @see MarginDesc
 * @since 1.0
 */
public class EtchedBorderDesc extends MarginDesc {

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

        String tpKey = options.getProperty("ETCHED_TYPE");
        int    type  = -1;
        if (hasOption(tpKey)) {
            type = getOption(tpKey, Integer.class);
        }

        String opSH   = options.getProperty("SHADOW");
        String opHl   = options.getProperty("HIGHLIGHT");
        Color  shadow = null, highlight = null;

        if (hasOption(opSH) && hasOption(opHl)) {
            shadow    = GlobalDesc.getColor(get(opSH).toString());
            highlight = GlobalDesc.getColor(get(opHl).toString());
        }

        if (type < 0) {
            if (shadow != null || highlight != null) {
                return new EtchedBorder(shadow, highlight);
            }
        }

        if (shadow != null || highlight != null) {
            return new EtchedBorder(type, shadow, highlight);
        }
        return new EtchedBorder();
    }
}
