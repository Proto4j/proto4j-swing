package org.proto4j.swing.core.desc.margin; //@date 19.02.2022

import org.proto4j.swing.annotation.Option;
import org.proto4j.swing.core.GlobalDesc;
import org.proto4j.swing.core.desc.MarginDesc;

import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Properties;

/**
 * Basic implementation for the {@link TitledBorder} description.
 *
 * @see MarginDesc
 * @since 1.0
 */
public class TitledBorderDesc extends MarginDesc {

    @Override
    public boolean shouldPlaceBorder() {
        return true;
    }

    @Override
    public Border create() {
        Properties options = getDefinedOptions();
        if (options == null) {
            return null;
        }

        String key = options.getProperty("TITLE", UNDEFINED);
        if (!hasOption(key)) {
            return null;
        }

        String title = get(key).toString();
        if (Option.Query.isQuery(title)) {
            title = new Option.Query(title, null).get();
        }
        key = options.getProperty("LINE_COLOR", UNDEFINED);
        if (hasOption(key)) {
            Color color = GlobalDesc.getColor(get(key).toString());

            // In future releases all properties that are hardcoded right
            // now will be available in the Margin.class annotation.
            return new TitledBorder(
                    null, title, TitledBorder.LEADING,
                    TitledBorder.DEFAULT_POSITION,
                    Font.getFont(Font.SANS_SERIF), color
            );
        }
        return new TitledBorder(title);
    }
}
