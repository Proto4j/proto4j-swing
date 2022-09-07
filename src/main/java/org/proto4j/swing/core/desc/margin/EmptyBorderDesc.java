package org.proto4j.swing.core.desc.margin; //@date 20.02.2022

import org.proto4j.swing.core.desc.MarginDesc;

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
