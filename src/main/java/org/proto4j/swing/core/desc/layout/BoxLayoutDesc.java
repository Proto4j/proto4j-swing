package org.proto4j.swing.core.desc.layout; //@date 19.02.2022

import org.proto4j.swing.core.desc.ComponentDesc;
import org.proto4j.swing.core.desc.LayoutDesc;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

/**
 * The basic descriptor for the {@link BoxLayout}.
 *
 * @see LayoutDesc
 */
public class BoxLayoutDesc extends LayoutDesc {

    public BoxLayoutDesc(ComponentDesc parent) {
        super(parent);
    }

    @Override
    public LayoutManager createManager() {
        Properties options = getDefinedOptions();
        int x = BoxLayout.X_AXIS;
        String key = options.getProperty("BOX_AXIS");

        if (hasOption(key)) {
            x = getOption(key, Integer.class);
            if (x < 0 || x > BoxLayout.PAGE_AXIS) {
                x = BoxLayout.X_AXIS;
            }
        }

        if (getParent() == null) {
            return null;
        }

        Object parent = getParent().getInstance();
        if (!(parent instanceof Container)) {
            return null;
        }
        else if (parent instanceof RootPaneContainer) {
            parent = ((RootPaneContainer) parent).getContentPane();
        }

        //noinspection MagicConstant
        return new BoxLayout((Container) parent, x);
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
