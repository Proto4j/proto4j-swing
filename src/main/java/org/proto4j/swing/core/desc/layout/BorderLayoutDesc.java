package org.proto4j.swing.core.desc.layout; //@date 14.02.2022

import org.proto4j.swing.annotation.Position;
import org.proto4j.swing.core.GlobalDesc;
import org.proto4j.swing.core.desc.ComponentDesc;
import org.proto4j.swing.core.desc.GenericDesc;
import org.proto4j.swing.core.desc.LayoutDesc;
import org.proto4j.swing.core.desc.PositionDesc;

import java.awt.*;
import java.util.Objects;
import java.util.Properties;

/**
 * The basic descriptor for the {@link BorderLayout}.
 *
 * @see LayoutDesc
 */
public class BorderLayoutDesc extends LayoutDesc {

    public BorderLayoutDesc(ComponentDesc parent) {
        super(parent);
    }

    @Override
    public LayoutManager createManager() {
        Properties options = getDefinedOptions();
        String key = options.getProperty("H_GAP");
        int h = 0, v = 0;

        if (hasOption(key)) {
            h = getOption(key, Integer.class);
        }

        key = options.getProperty("V_GAP");
        if (hasOption(key)) {
            v = getOption(key, Integer.class);
        }

        return new BorderLayout(h, v);
    }

    @Override
    public boolean isAbsolute() {
        return false;
    }

    @Override
    public Object createConstraints(ComponentDesc child) {
        Objects.requireNonNull(child);

        GenericDesc<Position> desc = child.getDesc(Position.class);
        String constraints = BorderLayout.CENTER;

        if (desc != null) {
            Properties options = GlobalDesc.getSharedOption(Position.class);

            if (options != null) {
                String key = options.getProperty("LAYOUT_CONSTRAINTS");

                if (desc.hasOption(key)) {
                    constraints = desc.get(key).toString();
                }
            }
        }
        return constraints;
    }

}
