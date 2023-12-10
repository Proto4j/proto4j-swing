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

package io.github.proto4j.swing.core; //@date 06.09.2022

import io.github.proto4j.swing.Entry;
import io.github.proto4j.swing.FieldReference;
import io.github.proto4j.swing.annotation.Layout;
import io.github.proto4j.swing.annotation.Option;
import io.github.proto4j.swing.core.desc.ComponentDesc;
import io.github.proto4j.swing.core.desc.DescInitializationException;
import io.github.proto4j.swing.core.desc.GenericDesc;
import io.github.proto4j.swing.core.desc.LayoutDesc;
import io.github.proto4j.swing.core.desc.layout.BorderLayoutDesc;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Properties;

/**
 * This class is the next level handler in the generation process of every
 * gui. It is called after the {@link SwingReader} and should add the given
 * components to their parent component.
 *
 * @see SwingReader
 * @since 1.0
 */
public class SwingLinker {

    /**
     * Executes all 'init' methods defined in the {@link Option} annotations
     * and tries to add the fields to their target components.
     *
     * @param entry the GUI entry
     * @throws NullPointerException if the given entry is {@code null}
     * @see Option#target()
     * @see Option#init()
     */
    public static void readEntry(Entry<?> entry) throws Exception {
        Objects.requireNonNull(entry);

        Properties properties = GlobalDesc.getSharedOption(Option.class);
        SwingLinker linker = new SwingLinker();

        String init = properties.getProperty("INIT");
        String target = properties.getProperty("TARGET");
        FieldReference<?>[] fields = entry.getDeclaredFields();

        Arrays.sort(fields, Comparator.comparingInt(FieldReference::getId));
        for (FieldReference<?> ref : fields) {
            GenericDesc<Option> desc = ref.getDescription().getDesc(Option.class);
            if (desc.hasOption(init)) {
                Object gui = entry.getGUI();
                Method method =
                        gui.getClass().getDeclaredMethod(desc.get(init).toString());

                method.setAccessible(true);
                method.invoke(gui);
            }

            if (desc.hasOption(target)) {
                FieldReference<?> refTarget =
                        entry.getDeclaredField(desc.get(target).toString());
                if (refTarget != null) {
                    linker.bind(ref, refTarget);
                }
            }
        }
    }

    /**
     * Tries to add the given component stored in a {@link FieldReference}
     * object to the target component.
     *
     * @param reference the source reference
     * @param target the target reference
     * @throws NullPointerException if any of the provided arguments is
     *                              {@code null}
     */
    public void bind(FieldReference<?> reference, FieldReference<?> target) throws NullPointerException {
        Objects.requireNonNull(reference);
        Objects.requireNonNull(target);

        Object src = reference.get();
        // REVISIT: Check the component type
        Container     dest    = (Container) target.get();
        ComponentDesc srcDesc = reference.getDescription();

        if (src instanceof JMenuBar) {
            if (dest instanceof JFrame) {
                ((JFrame) dest).setJMenuBar((JMenuBar) src);
            }
            else {
                if (!(dest instanceof JRootPane)) {
                    throw new UnsupportedOperationException("dest is not a JRootPane");
                }
                ((JRootPane) dest).setJMenuBar((JMenuBar) src);
            }
            return;
        }

        if (dest instanceof JMenuBar || dest instanceof JMenu) {
            dest.add((Component) src);
            return;
        }

        if (dest instanceof JTabbedPane) {
            Properties options = GlobalDesc.getSharedOption(Option.class);
            if (options == null) {
                throw new DescInitializationException("options not defined");
            }

            String title = options.getProperty("TITLE");
            String value =
                    srcDesc.getDesc(Option.class).get(title).toString();
            ((JTabbedPane) dest).addTab(value, (Component) src);
            return;
        }

        if (dest instanceof JScrollPane) {
            ((JScrollPane) dest).setViewportView((Component) src);
            return;
        }

        if (dest instanceof JSplitPane) {
            Object constraints =
                    BorderLayoutDesc.createLayoutConstraints(srcDesc);

            if (constraints != null) {
                if (constraints.equals(BorderLayout.NORTH)
                        || constraints.equals(BorderLayout.EAST)) {
                    ((JSplitPane) dest).setLeftComponent((Component) src);
                    return;
                }
                else if (constraints.equals(BorderLayout.SOUTH)
                        || constraints.equals(BorderLayout.WEST)) {
                    ((JSplitPane) dest).setRightComponent((Component) src);
                    return;
                }
            }
        }

        // We assume that all GenericDesc<Layout> must be a member of the
        // LayoutDesc class.
        LayoutDesc desc = (LayoutDesc)
                target.getDescription().getDesc(Layout.class);

        desc.bind(srcDesc, dest);
    }
}
