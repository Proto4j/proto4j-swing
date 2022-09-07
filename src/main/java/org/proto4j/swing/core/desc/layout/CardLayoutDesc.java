package org.proto4j.swing.core.desc.layout; //@date 20.02.2022

import org.proto4j.swing.annotation.Option;
import org.proto4j.swing.core.GlobalDesc;
import org.proto4j.swing.core.desc.*;

import java.awt.*;
import java.util.Properties;

/**
 * The basic descriptor for the {@link CardLayout}.
 *
 * @see LayoutDesc
 */
public class CardLayoutDesc extends LayoutDesc {

    private static int placedTabs = 0;

    public CardLayoutDesc(ComponentDesc parent) {
        super(parent);
    }

    @Override
    public LayoutManager createManager() {
        Properties options = getDefinedOptions();
        String key = options.getProperty("H_GAP");
        int    h   = 0, v = 0;
        if (hasOption(key)) {
            h = getOption(key, Integer.class);
        }

        key = options.getProperty("V_GAP");
        if (hasOption(key)) {
            v = getOption(key, Integer.class);
        }

        return new CardLayout(h, v);
    }

    @Override
    public boolean isAbsolute() {
        return false;
    }

    @Override
    public Object createConstraints(ComponentDesc child) {
        Properties options = GlobalDesc.getSharedOption(Option.class);
        if (options == null) {
            throw new DescInitializationException("Options not defined");
        }

        String title = "<? " + placedTabs++ + '>';
        if (placedTabs == Integer.MAX_VALUE) {
            placedTabs = 0;
        }

        String key = options.getProperty("TITLE");
        GenericDesc<Option> desc = child.getDesc(Option.class);
        if (desc != null && desc.hasOption(key)) {
            title = desc.get(key).toString();
        }

        return title;
    }

}
