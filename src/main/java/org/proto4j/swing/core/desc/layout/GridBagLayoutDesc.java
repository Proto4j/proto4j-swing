package org.proto4j.swing.core.desc.layout; //@date 20.02.2022


import org.proto4j.swing.annotation.Position;
import org.proto4j.swing.core.GlobalDesc;
import org.proto4j.swing.core.desc.*;

import java.awt.*;
import java.util.Objects;
import java.util.Properties;


/**
 * The basic descriptor for the {@link GridBagLayout}.
 *
 * @see LayoutDesc
 */
public class GridBagLayoutDesc extends GridLayoutDesc {

    protected GridBagLayoutDesc(ComponentDesc parent) {
        super(parent);
    }

    @Override
    public LayoutManager createManager() {
        return new GridBagLayout();
    }

    @Override
    public Object createConstraints(ComponentDesc child) {
        Objects.requireNonNull(child);

        GenericDesc<Position> desc = child.getDesc(Position.class);
        if (desc == null) {
            throw new NullPointerException("PositionDes has to be present");
        }

        GridBagConstraints gbc = new GridBagConstraints();
        Properties opt = GlobalDesc.getSharedOption(Position.class);
        if (opt == null) {
            throw new DescInitializationException("options not defined");
        }

        setConstraint("GB_ANCHOR", opt, Integer.class, i -> gbc.anchor = i);
        setConstraint("GB_FILL", opt, Integer.class, i -> gbc.fill = i);
        setConstraint("GB_GRID_X", opt, Integer.class, i -> gbc.gridx = i);
        setConstraint("GB_GRID_Y", opt, Integer.class, i -> gbc.gridy = i);
        setConstraint("GB_GRID_WIDTH", opt, Integer.class, i -> gbc.gridwidth = i);
        setConstraint("GB_GRID_HEIGHT", opt, Integer.class, i -> gbc.gridheight = i);
        setConstraint("GB_WEIGHTX", opt, Double.class, i -> gbc.weightx = i);
        setConstraint("GB_WEIGHTY", opt, Double.class, i -> gbc.weighty = i);
        setConstraint("GB_IPADX", opt, Integer.class, i -> gbc.ipadx = i);
        setConstraint("GB_IPADY", opt, Integer.class, i -> gbc.ipady = i);

        setConstraint("GB_INSETS", opt, int[].class, i -> {
            if (i.length == 4) {
                for (int j : i) {
                    if (j < 0) return;
                }
                gbc.insets = new Insets(i[0], i[1], i[2], i[3]);
            }
        });
        return gbc;
    }

}
