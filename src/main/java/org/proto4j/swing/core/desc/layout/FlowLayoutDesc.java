package org.proto4j.swing.core.desc.layout; //@date 20.02.2022

import org.proto4j.swing.core.desc.ComponentDesc;
import org.proto4j.swing.core.desc.LayoutDesc;

import java.awt.*;
import java.util.Properties;

/**
 * The basic descriptor for the {@link FlowLayout}.
 *
 * @see LayoutDesc
 */
public class FlowLayoutDesc extends LayoutDesc {

    public FlowLayoutDesc(ComponentDesc parent) {
        super(parent);
    }

    @Override
    public LayoutManager createManager() {
        FlowLayout flowLayout = null;
        Properties options = getDefinedOptions();
        int align = FlowLayout.LEADING;
        String key = options.getProperty("ALIGN");

        if (hasOption(key)) {
            align = getOption(key, Integer.class);
            if (align < 0 || align > 4) {
                align = FlowLayout.LEADING;
            }
        }

        key = options.getProperty("H_GAP");
        int h = -1, v = -1;
        if (hasOption(key)) {
            h = getOption(key, Integer.class);
        }

        key = options.getProperty("V_GAP");
        if (hasOption(key)) {
            v = getOption(key, Integer.class);
        }

        if (h != -1 && v != -1) {
            //noinspection MagicConstant
            flowLayout = new FlowLayout(align, h, v);
        } else {
            //noinspection MagicConstant
            flowLayout = new FlowLayout(align);
        }
        return flowLayout;
    }

    @Override
    public boolean isAbsolute() {
        return false;
    }

    @Override
    public Object createConstraints(ComponentDesc child) {
        return null;
    }
}
