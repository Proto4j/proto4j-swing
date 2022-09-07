package org.proto4j.swing.core.desc.layout; //@date 20.02.2022


import org.proto4j.swing.core.desc.ComponentDesc;
import org.proto4j.swing.core.desc.LayoutDesc;

import java.awt.*;
import java.util.Properties;

/**
 * The basic descriptor for the {@link GridLayout}.
 *
 * @see LayoutDesc
 */
public class GridLayoutDesc extends LayoutDesc {

    public GridLayoutDesc(ComponentDesc parent) {
        super(parent);
    }

    @Override
    public LayoutManager createManager() {
        Properties opt        = getDefinedOptions();
        GridLayout gridLayout = new GridLayout();

        setConstraint("COLUMNS", opt, Integer.class, gridLayout::setColumns);
        setConstraint("ROWS", opt, Integer.class, gridLayout::setRows);
        setConstraint("H_GAP", opt, Integer.class, gridLayout::setHgap);
        setConstraint("V_GAP", opt, Integer.class, gridLayout::setVgap);
        return gridLayout;
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
