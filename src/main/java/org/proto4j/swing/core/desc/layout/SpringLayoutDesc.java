package org.proto4j.swing.core.desc.layout; //@date 20.02.2022

import org.proto4j.swing.annotation.Position;
import org.proto4j.swing.core.GlobalDesc;
import org.proto4j.swing.core.desc.*;

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

        GenericDesc<Position>   desc        = child.getDesc(Position.class);
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
