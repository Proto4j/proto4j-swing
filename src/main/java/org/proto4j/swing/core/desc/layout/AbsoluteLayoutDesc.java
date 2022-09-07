package org.proto4j.swing.core.desc.layout; //@date 14.02.2022


import org.proto4j.swing.core.desc.ComponentDesc;
import org.proto4j.swing.core.desc.LayoutDesc;

import java.awt.*;

/**
 * The basic descriptor for the absolute layout.
 *
 * @see LayoutDesc
 */
public class AbsoluteLayoutDesc extends LayoutDesc {


    public AbsoluteLayoutDesc(ComponentDesc parent) {
        super(parent);
    }

    @Override
    public LayoutManager createManager() {
        return null;
    }

    @Override
    public boolean isAbsolute() {
        return true;
    }

    @Override
    public Object createConstraints(ComponentDesc child) {
        return null;
    }
}
