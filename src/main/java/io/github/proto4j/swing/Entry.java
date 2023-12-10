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

package io.github.proto4j.swing;//@date 06.09.2022

import io.github.proto4j.swing.annotation.ActionHandler;
import io.github.proto4j.swing.annotation.EntryPoint;
import io.github.proto4j.swing.core.SwingReader;
import io.github.proto4j.swing.core.SwingLinker;

import java.nio.channels.AlreadyBoundException;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Objects;

/**
 * The base class for objects that store generated information about a GUI.
 * <p>
 * This class model is not thread-safe in its internal implementation but
 * could be so, if en own implementation would be provided. The reading/
 * generation process is as easy as it could be. Just follow the execution
 * workflow presented at the {@link SwingReader} declaration.
 * <p>
 * The standard way how to create a simple GUI is the following:
 * <pre>
 *     Entry&lt;MyGUI&gt; entry = Entry.of(MyGUI.class);
 * </pre>
 *
 * @param <G> the GUI's type
 * @see SwingReader
 * @see SwingLinker
 * @see FieldReference
 * @since 1.0
 */
public interface Entry<G> {

    /**
     * Generates a new GUI from the given class. This method provides the
     * internal implementation of the {@link Entry} interface. It can be
     * used by default, but should be replaced in future releases.
     * <p>
     * For the basic execution workflow see {@link SwingReader}. The used
     * {@link Entry} implementation makes use of the {@link HashMap} to store
     * field reference objects.
     * <p>
     * It is also possible to provide constructor arguments for the given GUI
     * class. They will be used to create an object of type {@code <R>}.
     *
     * @param cls the GUI's class
     * @param args the constructor arguments
     * @param <R> the GUI type
     * @return a new GUI instance
     * @throws Exception if an error occurs
     */
    public static <R> Entry<R> of(Class<R> cls, Object... args) throws Exception {
        Objects.requireNonNull(cls);

        Entry<R> entry = new MapEntry<>(cls);
        try (SwingReader reader = new SwingReader()) {
            reader.read(entry, args);
            SwingLinker.readEntry(entry);
        }
        return entry;
    }

    /**
     * Generates a new GUI from the given object. This method provides the
     * internal implementation of the {@link Entry} interface. It can be
     * used by default.
     * <p>
     * For the basic execution workflow see {@link SwingReader}. The used
     * {@link Entry} implementation makes use of the {@link HashMap} to store
     * field reference objects.
     * <p>
     * Use this method to generate GUI components at runtime while the actual
     * GUI component was created before calling {@code wrap}.
     *
     * @param obj the GUI instance
     * @param <R> the GUI type
     * @return a new GUI instance
     * @throws Exception if an error occurs
     */
    public static <R> Entry<R> wrap(R obj) throws Exception {
        Objects.requireNonNull(obj);

        Entry<R> entry = new MapEntry<>(obj);
        try (SwingReader reader = new SwingReader()) {
            reader.read(entry, new Object[0]);
            SwingLinker.readEntry(entry);
        }
        return entry;
    }

    /**
     * Returns the GUI-object stored in this {@code Entry}. This method
     * will throw a {@link NullPointerException} if the given instance
     * is {@code null}.
     *
     * @return the stored GUI instance
     * @throws NullPointerException if the stored GUI is {@code null}
     */
    public G getGUI();

    /**
     * Applies a new gui value.
     *
     * @param gui the new GUI instance
     */
    public void setGUI(G gui);

    /**
     * @return The GUI's type
     */
    public Class<G> getType();

    /**
     * If nested GUIs are provided they can be fetched from here.
     *
     * @param name the field's name
     * @param type gui's class type
     * @param <T_G> the GUI type
     * @return the {@link Entry} storing all information about the GUI
     */
    public <T_G> Entry<T_G> getNestedGUI(String name, Class<T_G> type);

    /**
     * If nested GUIs are provided they can be fetched from here.
     *
     * @param name the field's name
     * @return the {@link Entry} storing all information about the GUI
     */
    public Entry<?> getNestedGUI(String name);

    /**
     * Returns all nested GUI entries.
     *
     * @return all nested GUI entries
     */
    public Entry<?>[] getNestedGUIs();

    /**
     * Tries to insert the given nested GUI entry and throws an exception on
     * error.
     *
     * @param name the field's name
     * @param entry the {@link Entry} object
     * @param <T_G> the GUI type
     * @throws NullPointerException  if the given reference is {@code null}
     * @throws AlreadyBoundException if there is already a mapping with the
     *                               field's name
     */
    public <T_G> void putNestedGUI(String name, Entry<T_G> entry)
            throws NullPointerException, AlreadyBoundException;

    /**
     * Returns a field reference that is mapped to the given name. The name will
     * be the same as the one at the field's declaration.
     *
     * @param name the field's name
     * @return the corresponding {@link FieldReference}; {@code null} if no
     *         mapping exists for the given name.
     */
    public FieldReference<?> getDeclaredField(String name);

    /**
     * Returns a field reference that is mapped to the given name. The name will
     * be the same as the one at the field's declaration.
     * <p>
     * This method will fail if the provided class type is not an assignable
     * form of the stored one.
     *
     * @param name the field's name
     * @param cls the component class type
     * @param <V> the component's type
     * @return the corresponding {@link FieldReference}; {@code null} if no
     *         mapping exists for the given name.
     * @throws ClassCastException if there is an error while casting
     */
    public <V> FieldReference<V> getDeclaredField(String name, Class<V> cls)
            throws ClassCastException;

    /**
     * Tries to insert the given field and throws an exception on error.
     *
     * @param reference the field's reference
     * @param <V> the component type
     * @throws NullPointerException  if the given reference is {@code null}
     * @throws AlreadyBoundException if there is already a mapping with the
     *                               field's name
     */
    public <V> void putField(FieldReference<V> reference)
            throws NullPointerException, AlreadyBoundException;

    /**
     * Returns an array of {@link FieldReference} objects reflecting all the fields declared
     * by the GUI class.
     *
     * @return an array of {@link FieldReference} objects
     */
    public FieldReference<?>[] getDeclaredFields();

    /**
     * Searches for the {@link EntryPoint} annotation and executes the method
     * with it.
     *
     * @param args the method's arguments
     * @throws NullPointerException if no method was found
     */
    public void start(Object... args) throws NullPointerException;

    /**
     * Searches for the {@link ActionHandler} annotation and tries to link
     * the given EventListener to the specified target.
     *
     * @param src the event listener object
     */
    public void linkAction(Object src);

    /**
     * Tries to link the given EventListener to the specified target.
     *
     * @param <T> the listener type
     * @param fieldName the target field name
     * @param cls the listener base class
     * @param listener the event listener object
     * @return {@code true} if the listener has been added successfully
     */
    public <T extends EventListener> boolean linkAction(String fieldName, Class<T> cls, T listener);
}
