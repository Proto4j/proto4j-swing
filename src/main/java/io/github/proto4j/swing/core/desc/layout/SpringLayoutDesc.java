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

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

/**
 * The basic descriptor for the {@link SpringLayout}.
 *
 * @see LayoutDesc
 */
public class SpringLayoutDesc extends LayoutDesc {

    private final SpringLayout springLayout = new SpringLayout();

    /**
     * Creates a new {@code LayoutDesc}. This constructor is protected so only
     * inheritors can call it.
     *
     * @param parent the parent component
     */
    public SpringLayoutDesc(ComponentDesc parent) {
        super(parent);
    }

    @Override
    public LayoutManager createManager() {
        return springLayout;
    }

    @Override
    public boolean isAbsolute() {
        return false;
    }

    @Override
    public Object createConstraints(ComponentDesc child) {
        Properties opt = GlobalDesc.getSharedOption(Position.class);
        if (opt == null) {
            throw new DescInitializationException("options not defined");
        }

        GenericDesc<Position> desc        = child.getDesc(Position.class);
        SpringLayoutConstraints constraints = new SpringLayoutConstraints();

        setConstraint("SP_C_CONSTRAINTS", opt, String.class, s -> constraints.cConstraints = s);
        setConstraint("SP_V_CONSTRAINTS", opt, String.class, s -> constraints.vConstraints = s);
        setConstraint("SP_PAD", opt, Integer.class, i -> constraints.pad = i);
        return constraints;
    }

    public static class SpringLayoutConstraints {
        public int    pad;
        public String vConstraints;
        public String cConstraints;

        public SpringLayoutConstraints() {
            pad          = 5;
            vConstraints = SpringLayout.BASELINE;
            cConstraints = SpringLayout.BASELINE;
        }
    }
}
