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

package io.github.proto4j.swing; //@date 06.09.2022

import io.github.proto4j.swing.core.desc.ComponentDesc;

import java.lang.reflect.Field;
import java.util.EventListener;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * When a GUI is generated, {@link FieldReference}s are created automatically.
 * They are storing different informational data about the linked field.
 * <p>
 * This class is thread-safe: multiple threads can share a single FieldReference
 * object without the need for external synchronization.
 *
 * @param <V> the component's type (field's value type)
 */
public final class FieldReference<V> {

    /**
     * When fields want to reference the root component, they should use this
     * variable to point to it.
     * <p>
     * <b>Note: </b>The {@link #get()} method will return the parent/ root
     * component on field references with {@code $this} as its name.
     *
     * @since 1.1.0
     */
    public static final String THIS = "$this";

    /**
     * The component number.
     */
    private final int id;

    /**
     * A small collection of added event listeners.
     */
    private transient final List<EventListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * Access hook to prevent multiple callers on one method.
     */
    private final Object hook = new Object();

    /**
     * The description of the referenced field.
     */
    private volatile ComponentDesc desc;

    /**
     * The GUI instance object.
     */
    private volatile Object parent;

    /**
     * The field's value type.
     */
    private volatile Class<?> type;

    /**
     * Creates a new {@link FieldReference} with the given id;
     *
     * @param id the field's component number
     */
    public FieldReference(int id) {
        this.id = id;
    }

    /**
     * Tries to return the stored field value and cast it to the referenced
     * type {@code <V>}.
     *
     * @return the field's value
     * @throws NullPointerException if the stored instance is {@code null}
     * @throws ClassCastException   if the stored value can not be casted to
     *                              {@code <V>}
     */
    public V get() throws NullPointerException, ClassCastException {
        synchronized (getHook()) {
            Object value = desc.getInstance();
            if (desc.getFieldName().equals(THIS)) {
                // See THIS documentation for more information
                //noinspection unchecked
                return (V) parent;
            }
            Objects.requireNonNull(value, "Linked instance is null");
            //noinspection unchecked
            return (V) value;
        }
    }

    /**
     * Applies a new value to the linked field.
     *
     * @param value the new value
     * @throws NullPointerException if the value is {@code null}
     */
    public void set(V value) throws NullPointerException {
        // this method should not take any action if we want
        // to change the root element.
        if (desc.getFieldName().equals(THIS)) {
            return;
        }

        Field field = (Field) desc.getField();
        field.setAccessible(true);
        try {
            synchronized (getHook()) {
                field.set(parent, value);
                desc.setInstance(value);
                type = value.getClass();
            }
        } catch (IllegalAccessException e) {
            // pass that
        }
    }

    /**
     * @return the field's component number
     */
    public int getId() {
        // Although, this method can be synchronized, this implementation
        // leaves the synchronization out.
        return id;
    }

    /**
     * @return the description for the linked component
     */
    public ComponentDesc getDescription() {
        synchronized (getHook()) {
            return desc;
        }
    }

    /**
     * Returns the stored component type. This value may be null if the
     * linked field has no value applied to it yet.
     *
     * @return the stored component type
     */
    public Class<?> getType() {
        synchronized (getHook()) {
            return type;
        }
    }

    /**
     * Appends the given {@link EventListener} at the end of the stored
     * listeners.
     *
     * @param listener the {@link EventListener} to add
     */
    public void addEventListener(EventListener listener) {
        Objects.requireNonNull(listener);
        synchronized (getHook()) {
            listeners.add(listener);
        }
    }

    /**
     * @return all stored {@link EventListener} objects
     */
    public EventListener[] getEventListeners() {
        synchronized (getHook()) {
            if (listeners.size() == 0) {
                return new EventListener[0];
            }
            return listeners.toArray(EventListener[]::new);
        }
    }

    /**
     * Applies a new {@link ComponentDesc} to this reference if there is
     * none defined.
     *
     * @param desc the new {@link ComponentDesc} object
     */
    public void setComponentDesc(ComponentDesc desc) {
        synchronized (getHook()) {
            if (this.desc != null) {
                return;
            }
            this.desc = desc;
        }
    }

    /**
     * Sets the referenced GUI instance.
     *
     * @param parent the GUI's instance
     */
    public void setParent(Object parent) {
        synchronized (getHook()) {
            this.parent = parent;
        }
    }

    private Object getHook() {
        return hook;
    }
}
