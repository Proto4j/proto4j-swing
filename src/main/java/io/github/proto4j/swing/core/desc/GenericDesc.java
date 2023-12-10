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

import io.github.proto4j.swing.core.GlobalDesc;

import java.awt.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

/**
 * The base class for all option related descriptions.
 * <p>
 * This class is used to be the base for all attribute descriptions and their
 * implementations. It is bound to an annotation type in order to make it
 * easier to filter them in a list (concurrency).
 *
 * @param <A> the annotation type
 */
public abstract class GenericDesc<A extends Annotation> {

    /**
     * A simple property descriptor indicating that a property wasn't found.
     *
     * @since 1.1.0
     */
    public static final String UNDEFINED = "<>";

    /**
     * A simple constant specifying whether the option is valid.
     *
     * @since 1.1.0
     */
    public static final int INVALID_INT = -1;

    /**
     * The annotation type this description is bound to.
     */
    private final Class<A> annotationType;

    /**
     * A thread-safe implementation of the {@link java.util.Hashtable} used
     * for storing attribute related data.
     */
    private final Properties properties = new Properties();

    /**
     * Creates a new {@code GenericDesc} from the given annotation class.
     *
     * @param cls the annotation class
     * @throws NullPointerException if the given annotation class is {@code null}
     */
    protected GenericDesc(Class<A> cls) {
        Objects.requireNonNull(cls);
        annotationType = cls;
    }

    /**
     * Applies the stored options on the given component.
     *
     * @param component the {@link Component} reference
     */
    public abstract void applyTo(Component component);

    /**
     * @return The annotation type this description is bound to.
     */
    public Class<A> annotationType() {
        return annotationType;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * description contains no mapping for the key.
     *
     * @param key the option's name
     * @return the value to which the specified key is mapped, or null if this
     *         description contain no mapping for the key
     */
    public Object get(String key) {
        Objects.requireNonNull(key);
        if (key.equals(UNDEFINED)) {
            return null;
        }
        return properties.get(key);
    }

    /**
     * If the specified key is not already associated with a value (or is
     * mapped to null) associates it with the given value.
     *
     * @param key the option's name
     * @param value the option's value
     */
    public void put(String key, Object value) {
        Objects.requireNonNull(key);
        properties.putIfAbsent(key, value);
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * description contains no mapping for the key and casts it to the given
     * format.
     *
     * @param key the option's name
     * @param cls the type's class
     * @param <R> the return type
     * @return the value to which the specified key is mapped, or null if this
     *         description contain no mapping for the key
     * @throws NullPointerException if any of the provided arguments is
     *                              {@code null}
     * @throws ClassCastException   if an error is thrown during the object
     *                              casting
     */
    public <R> R getOption(String key, Class<R> cls)
            throws NullPointerException, ClassCastException {
        Objects.requireNonNull(key);
        Objects.requireNonNull(cls);

        Object value = get(key);
        if (value == null) {
            // At this point we don't want to throw any kind of exception,
            // because to would interrupt the whole generation process.
            return null;
        }

        if (!cls.isAssignableFrom(value.getClass())) {
            // NOTE: If the value is stored in the wrong format, there will
            // be no exception either. see #hasOption()
            return null;
        }

        return cls.cast(value);
    }

    /**
     * Tests if the specified object is a key in this description.
     *
     * @param key the option's name
     * @return true if and only if the specified object is a key in this
     *         description object; false otherwise.
     */
    public boolean hasOption(String key) {
        if (key == null) return false;

        Object value = get(key);
        if (value == null) {
            return false;
        }

        if (value instanceof String) {
            // Default string options that are not used are marked with an
            // empty string of length 0.
            return ((String) value).length() > 0;
        } else if (value instanceof Number) {
            // Default number options that are not used are marked with a
            // negative number.
            return ((Number) value).intValue() >= 0;
        }
        return true;
    }

    /**
     * Tries to insert all attributes provided by the given {@link Annotation}
     * object.
     *
     * @param value the annotation instance
     */
    public void read(A value) {
        Objects.requireNonNull(value);

        Properties options = getDefinedOptions();
        Class<?>   cls     = value.getClass();
        for (Object key : options.keySet()) {
            String name = options.get(key).toString();

            try {
                Method m = cls.getDeclaredMethod(name);
                this.put(name, m.invoke(value));
            } catch (ReflectiveOperationException refl) {
                // ignore that and continue
            }
        }
    }

    /**
     * Tries to insert all attributes provided by the given {@link GenericDesc}
     * object.
     *
     * @param desc the parent description
     * @implSpec mainly used by {@code CompoundBorderDesc}
     */
    public void read(GenericDesc<A> desc) throws NullPointerException {
        Objects.requireNonNull(desc);

        for (Object key : desc.keys()) {
            Object value = desc.get(key.toString());
            this.put(key.toString(), value);
        }
    }

    /**
     * Returns a Set view of the keys contained in this {@code GenericDesc}.
     *
     * @return a Set view of the keys contained in this {@code GenericDesc}.
     */
    public Set<Object> keys() {
        return properties.keySet();
    }

    /**
     * @return the defined option namespace for this description
     */
    public Properties getDefinedOptions() {
        Properties properties = GlobalDesc.getSharedOption(annotationType());
        if (properties == null) {
            throw new DescInitializationException("options not defined");
        }
        return properties;
    }

}
