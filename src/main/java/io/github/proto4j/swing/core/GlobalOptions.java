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

package io.github.proto4j.swing.core; //@date 01.10.2022

import javax.swing.*;
import java.util.Properties;

public final class GlobalOptions {

    public static class LayoutOptions extends Properties {

        public LayoutOptions() {
            put("H_GAP", "hgap");
            put("V_GAP", "vgap");
            put("ROWS", "rows");
            put("ALIGN", "align");
            put("COLUMNS", "columns");
            put("BOX_AXIS", "axis");
        }
    }

    public static class MarginOptions extends Properties {
        public MarginOptions() {
            put("BEVEL_TYPE", "bevelType");
            put("SHADOW", "shadow");
            put("HIGHLIGHT", "highlight");
            put("LINE_COLOR ", "lineColor");
            put("LINE_THICKNESS", "lineThickness");
            put("LINE_ROUNDED", "lineRounded");
            put("ETCHED_TYPE", "etchedType");
            put("ALIGN", "align");
            put("LEFT", "emptyLeft");
            put("RIGHT", "emptyRight");
            put("TOP", "emptyTop");
            put("BOTTOM", "emptyBottom");
            put("TITLE", "title");
            put("COMPOUND_INSIDE", "compoundInside");
            put("COMPOUND_OUTSIDE", "compoundInside");
        }
    }

    public static class ModelOptions extends Properties {
        public ModelOptions() {
            put("TYPE", "value");
        }
    }

    public static class SwingOptions extends Properties {
        public SwingOptions() {
            put("CONSTRAINTS", "constraints");
            put("TARGET", "target");
            put("TITLE", "title");
            put("TEXT", "text");
            put("INIT", "init");
            put("BACKGROUND", "background");
            put("FOREGROUND", "foreground");
            put("ENABLED", "enabled");
        }
    }

    public static class PositionOptions extends Properties {
        public PositionOptions() {
            put("X", "x");
            put("Y", "y");
            put("LAYOUT_CONSTRAINTS", "constraints");
            put("GB_GRIDX", "gridx");
            put("GB_GRIDY", "gridy");
            put("GB_GRID_WIDTH", "gridWidth");
            put("GB_GRID_HEIGHT", "gridHeight");
            put("GB_WEIGHTX", "weightx");
            put("GB_WEIGHTY", "weighty");
            put("GB_ANCHOR", "anchor");
            put("GB_FILL", "gridx");
            put("GB_INSETS", "insets");
            put("GB_IPADX", "ipadx");
            put("GB_IPADY", "ipady");
            put("SP_V_CONSTRAINTS", "vConstraints");
            put("SP_C_CONSTRAINTS", "cConstraints");
            put("SP_PAD", "pad");
        }
    }

    public static class SwingWindowOptions extends Properties {
        public SwingWindowOptions() {
            put("CLOSE_OPERATION", "closeOperation");
            put("RESIZABLE", "resizable");
            put("ALWAYS_ON_TOP", "alwaysOnTop");
        }
    }

    public static class BoundsOptions extends Properties {
        public BoundsOptions() {
            put("MIN", "min");
            put("MAX", "max");
            put("BOUNDS", "bounds");
            put("SIZE", "size");
            put("PREFERRED", "preferred");
        }
    }

    public static void setLookAndFeel(String name) {
        try {
            UIManager.setLookAndFeel(getLaFInfo(name));
        } catch (Exception var2) {
            System.err.println(var2.toString());
        }
    }

    private static String getLaFInfo(String name) {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if (info.getName().equals(name) || info.getClassName().equals(name)) {
                return info.getClassName();
            }
        }
        return null;
    }
}
