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

import io.github.proto4j.swing.ServiceManager;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * The base class for all LookAndFeel providers that implement own versions of
 * the components listed below. This provider will function as a wrapper that
 * is used within the generation process in order to create the needed component
 * instances.
 * <p>
 * If the component type declared in the GUI class is not found within this
 * provider, the method {@link #getComponent(Class)} is called to retrieve
 * an instance for the defined type. This method returns null on failure and
 * should not throw any errors.
 * <p>
 * This provider acts as a service which will be loaded through the LAFManager
 * class. Therefore, it is possible to implement and use different {@link LAFProvider}s
 * at runtime.
 *
 * @since 1.0
 */
public interface LAFProvider {

    /**
     * In order to make it possible to have multiple {@link LAFProvider}
     * instances registered to the system, this manager is used.
     * <p>
     * To register new {@link LAFProvider}s, they should call the
     * {@link ServiceManager#register(Object)} function in a static context.
     *
     * @see ServiceManager
     */
    public static final ServiceManager<LAFProvider> MANAGER = ServiceManager.from(LAFProvider.class);

    /**
     * @return the {@link ServiceManager} instance for the {@link LAFProvider}
     */
    public static ServiceManager<LAFProvider> getManager() {
        return MANAGER;
    }

    /**
     * Tries to create a new {@link Component} object based on the given component
     * type. This method should return {@code null} if no implementation for
     * the given type was found or the type is not specified (e.g. abstract
     * class).
     *
     * @param cls the component type
     * @return {@code null} on failure, otherwise a {@link Component} object
     */
    public default Component getComponent(Class<?> cls) {
        Objects.requireNonNull(cls);

        String name = "get" + cls.getSimpleName();
        try {
            Method base = getClass().getMethod(name);
            base.setAccessible(true);

            Object value = base.invoke(this);
            if (!Component.class.isAssignableFrom(value.getClass())) {
                // This check can be replaced with an instanceof-check
                return null;
            }
            return (Component) value;
        } catch (ReflectiveOperationException e) {
            try {
                // Try to create a new custom class
                return (Component) cls.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e2) {
                // ignore that and pass null
            }
        }
        return null;
    }

    /**
     * @return a new {@link JTextField} object
     */
    public default JTextField getJTextField() {
        return new JTextField();
    }

    /**
     * @return a new {@link JSpinner} object
     */
    public default JSpinner getJSpinner() {
        return new JSpinner();
    }

    /**
     * @return a new {@link JProgressBar} object
     */
    public default JProgressBar getJProgressBar() {
        return new JProgressBar();
    }

    /**
     * @return a new {@link JTextArea} object
     */
    public default JTextArea getJTextArea() {
        return new JTextArea();
    }

    /**
     * @return a new {@link JLabel} object
     */
    public default JLabel getJLabel() {
        return new JLabel();
    }

    /**
     * @return a new {@link JSeparator} object
     */
    public default JSeparator getJSeparator() {
        return new JSeparator();
    }

    /**
     * @return a new {@link JPasswordField} object
     */
    public default JPasswordField getJPasswordField() {
        return new JPasswordField();
    }

    /**
     * @return a new {@link JFormattedTextField} object
     */
    public default JFormattedTextField getJFormattedTextField() {
        return new JFormattedTextField();
    }

    /**
     * @param <E> the type of the elements of this list
     * @return a new {@link JList} object
     */
    public default <E> JList<E> getJList() {
        return new JList<>();
    }

    /**
     * @return a new {@link JButton} object
     */
    public default JButton getJButton() {
        return new JButton();
    }

    /**
     * @return a new {@link JMenu} object
     */
    public default JMenu getJMenu() {
        return new JMenu();
    }

    /**
     * @return a new {@link JMenuItem} object
     */
    public default JMenuItem getJMenuItem() {
        return new JMenuItem();
    }

    /**
     * @return a new {@link JRadioButtonMenuItem} object
     */
    public default JRadioButtonMenuItem getJRadioButtonMenuItem() {
        return new JRadioButtonMenuItem();
    }

    /**
     * @return a new {@link JCheckBoxMenuItem} object
     */
    public default JCheckBoxMenuItem getJCheckBoxMenuItem() {
        return new JCheckBoxMenuItem();
    }

    /**
     * @return a new {@link JMenuBar} object
     */
    public default JMenuBar getJMenuBar() {
        return new JMenuBar();
    }

    /**
     * @return a new {@link JTextPane} object
     */
    public default JTextPane getJTextPane() {
        return new JTextPane();
    }

    /**
     * @return a new {@link JRadioButton} object
     */
    public default JRadioButton getJRadioButton() {
        return new JRadioButton();
    }

    /**
     * @param <E> the type of the elements of this combo box
     * @return a new {@link JComboBox} object
     */
    public default <E> JComboBox<E> getJComboBox(){
        return new JComboBox<>();
    }

    /**
     * @return a new {@link JTable} object
     */
    public default JTable getJTable() {
        return new JTable();
    }

    /**
     * @return a new {@link JToolBar} object
     */
    public default JToolBar getJToolBar() {
        return new JToolBar();
    }

    /**
     * @return a new {@link JInternalFrame} object
     */
    public default JInternalFrame getJInternalFrame() {
        return new JInternalFrame();
    }

    /**
     * @return a new {@link JFrame} object
     */
    public default JFrame getJFrame() {
        return new JFrame();
    }

    /**
     * @return a new {@link JCheckBox} object
     */
    public default JCheckBox getJCheckBox() {
        return new JCheckBox();
    }

    /**
     * @return a new {@link JPanel} object
     */
    public default JPanel getJPanel() {
        return new JPanel();
    }

    /**
     * @return a new {@link JSplitPane} object
     */
    public default JSplitPane getJSplitPane() {
        return new JSplitPane();
    }

    /**
     * @return a new {@link JTabbedPane} object
     */
    public default JTabbedPane getJTabbedPane() {
        return new JTabbedPane();
    }

    /**
     * @return a new {@link JToggleButton} object
     */
    public default JToggleButton getJToggleButton() {
        return new JToggleButton();
    }

    /**
     * @return a new {@link JScrollPane} object
     */
    public default JScrollPane getJScrollPane() {
        return new JScrollPane();
    }

    /**
     * @return a new {@link JScrollBar} object
     */
    public default JScrollBar getJScrollBar() {
        return new JScrollBar();
    }

    /**
     * @return a new {@link JPopupMenu} object
     */
    public default JPopupMenu getJPopupMenu() {
        return new JPopupMenu();
    }

    /**
     * @return a new {@link JDesktopPane} object
     */
    public default JDesktopPane getJDesktopPane() {
        return new JDesktopPane();
    }

    /**
     * @return a new {@link JTree} object
     */
    public default JTree getJTree() {
        return new JTree();
    }

    /**
     * @return a new {@link Frame} object
     */
    public default Frame getFrame() {
        return new Frame();
    }

    /**
     * @return a new {@link JDialog} object
     */
    public default JDialog getJDialog() { return new JDialog(); }

    /**
     * @return a new {@link JFileChooser} object
     */
    public default JFileChooser getFileChooser() {
        return new JFileChooser();
    }

    /**
     * @return a new {@link JColorChooser} object
     */
    public default JColorChooser getColorChooser() {
        return new JColorChooser();
    }
}
