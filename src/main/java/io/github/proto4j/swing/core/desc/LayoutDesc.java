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

package io.github.proto4j.swing.core.desc; //@date 05.09.2022

import io.github.proto4j.swing.annotation.Layout;
import io.github.proto4j.swing.core.desc.layout.SpringLayoutDesc;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * The base class for all {@code LayoutDesc} inheritors. This abstract
 * layer is used to provide the general requirements on this class.
 * <p>
 * There will be only one {@code LayoutDesc} at a definition on a
 * {@link ComponentDesc} instance.
 *
 * @see GenericDesc
 * @since 1.0
 */
public abstract class LayoutDesc extends GenericDesc<Layout> {

    /**
     * the component parent with additional configuration variables.
     */
    private final ComponentDesc parent;

    /**
     * Creates a new {@code LayoutDesc}. This constructor is protected so only
     * inheritors can call it.
     */
    protected LayoutDesc(ComponentDesc parent) {
        super(Layout.class);
        this.parent = parent;
    }

    /**
     * Based on the configuration given by the annotations on each field, this
     * descriptor tries to create the corresponding {@link LayoutManager}.
     * <p>
     * Implementations of this method should return {@code null} of failure
     * instead of throwing an exception.
     *
     * @return the qualified LayoutManager
     */
    public abstract LayoutManager createManager();

    /**
     * Indicates that the implemented {@link LayoutDesc} will represent the
     * absolute layout.
     *
     * @return {@code true} if the absolute layout should be used; false
     *         otherwise
     */
    public abstract boolean isAbsolute();

    /**
     * Creates layout constraints if possible by using extra configuration
     * variables by the parent {@link ComponentDesc}.
     * <p>
     * This method may return {@code null} if no constraints are  defined or
     * can be used. Another way is to throw an exception in order to show the
     * user a misconfiguration.
     *
     * @return the layout constraints for the given child component
     * @throws DescInitializationException if the options are not
     *                                     defined
     * @throws NullPointerException        if the given child component
     *                                     is {@code null}
     */
    public abstract Object createConstraints(ComponentDesc child);

    /**
     * Tries to add the given {@link Component} to the parent {@link Container}
     * object.
     *
     * @param component the component that will be added to the container
     * @param target the {@link Container} where the {@link Component} should
     *         be added to
     */
    public void bind(ComponentDesc component, Container target) {
        if (target == null || getParent() == null) {
            return;
        }

        if (isAbsolute()) {
            target.add((Component) component.getInstance());
        } else {
            Object constrains = createConstraints(component);
            if (constrains == null) {
                target.add((Component) component.getInstance());
                return;
            }

            if (!(constrains instanceof SpringLayoutDesc.SpringLayoutConstraints)) {
                target.add((Component) component.getInstance(), constrains);
            }
            else {
                if (this instanceof SpringLayoutDesc) {
                    SpringLayout lm = (SpringLayout) createManager();
                    SpringLayoutDesc.SpringLayoutConstraints ct =
                            (SpringLayoutDesc.SpringLayoutConstraints) constrains;

                    lm.putConstraint(ct.vConstraints, (Component) component.getInstance(),
                                     ct.pad, ct.cConstraints, target);
                }
            }
        }
    }

    /**
     * Applies the {@link LayoutManager} to the given component.
     *
     * @param component the {@link Component} reference
     */
    @Override
    public void applyTo(Component component) {
        if (!(component instanceof Container)) {
            return;
        }

        Container c = (Container) component;
        if (isAbsolute()) {
            c.setLayout(null);
        } else {
            if (getParent() == null) {
                return;
            }

            LayoutManager manager = createManager();
            if (manager != null) {
                c.setLayout(manager);
            }
        }
    }

    protected ComponentDesc getParent() {
        return parent;
    }

    protected <T> void setConstraint(String name, Properties options, Class<T> cls, Consumer<? super T> consumer) {
        String key = options.getProperty(name);
        if (hasOption(key)) {
            T value = getOption(key, cls);
            consumer.accept(value);
        }
    }

}
