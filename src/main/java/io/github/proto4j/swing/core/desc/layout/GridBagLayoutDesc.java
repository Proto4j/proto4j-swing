/*
 * MIT License
 *
 * Copyright (c) 2022 Proto4j
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.proto4j.swing.core.desc.layout; //@date 20.02.2022


import io.github.proto4j.swing.annotation.Position;
import io.github.proto4j.swing.core.GlobalDesc;
import io.github.proto4j.swing.core.desc.ComponentDesc;
import io.github.proto4j.swing.core.desc.DescInitializationException;
import io.github.proto4j.swing.core.desc.GenericDesc;
import io.github.proto4j.swing.core.desc.LayoutDesc;

import java.awt.*;
import java.util.Objects;
import java.util.Properties;


/**
 * The basic descriptor for the {@link GridBagLayout}.
 *
 * @see LayoutDesc
 */
public class GridBagLayoutDesc extends GridLayoutDesc {

    public GridBagLayoutDesc(ComponentDesc parent) {
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
