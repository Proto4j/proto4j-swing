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

package io.github.proto4j.swing.laf;//@date 05.09.2022

import java.awt.*;

/**
 * The base class for all component oriented listeners. There are three kinds
 * if states that will be provided within the {@code state} parameter:
 * <ul>
 *     <li>{@code SETUP}: setup call for the underlying Look And Feel </li>
 *     <li>{@code INSTALL}: apply the underlying LAF to the given
 *  *     {@link Component}</li>
 *     <li>{@code UNINSTALL}: revert all changes made to the given
 *     {@link Component}</li>
 * </ul>
 *
 * @since 1.0
 */
public interface LAFChangeListener {

    /**
     * Indicates the underlying {@code  LookAndFeel} should be initialized.
     */
    public static final String SETUP = "SETUP";

    /**
     * Indicates that the underlying LookAndFeel should be applied to the
     * given {@link Component}.
     */
    public static final String INSTALL = "INSTALL";

    /**
     * Indicates that changes to the given component should be reverted.
     */
    public static final String UNINSTALL = "UNINSTALL";

    /**
     * This method gets called when a bound property of the current look and
     * feel is changed.
     *
     * @param state the current state; one of: {@code SETUP}, {@code INSTALL}
     *         or {@code UNINSTALL}
     * @param component the component for installing and uninstalling and
     *         {@code null} if setup is called
     */
    public void update(String state, Component component);
}
