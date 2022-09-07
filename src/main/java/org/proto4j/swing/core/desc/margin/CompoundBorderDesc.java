package org.proto4j.swing.core.desc.margin; //@date 19.02.2022

import org.proto4j.swing.core.GlobalDesc;
import org.proto4j.swing.core.desc.MarginDesc;

import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.util.Properties;

/**
 * Basic implementation for the {@link CompoundBorder} description.
 *
 * @see MarginDesc
 * @since 1.0
 */
public class CompoundBorderDesc extends MarginDesc {

    @Override
    public Border create() {
        Properties options = getDefinedOptions();
        if (options == null) {
            return null;
        }

        MarginDesc inside = null, outside = null;
        String i = options.getProperty("COMPOUND_INSIDE");
        if (hasOption(i)) {
            inside = GlobalDesc.getMarginDesc(getOption(i, Class.class));
        }

        String o = options.getProperty("COMPOUND_OUTSIDE");
        if (hasOption(o)) {
            outside = GlobalDesc.getMarginDesc(getOption(o, Class.class));
        }

        if (inside == null || outside == null) {
            return null;
        }

        inside.read(this);
        outside.read(this);
        return new CompoundBorder(inside.create(), outside.create());
    }

    @Override
    public boolean shouldPlaceBorder() {
        // Here, we are returning true because the create() method will return
        // null on failure. Then, no border is placed and the creation process
        // will be stopped.
        return true;
    }

}
