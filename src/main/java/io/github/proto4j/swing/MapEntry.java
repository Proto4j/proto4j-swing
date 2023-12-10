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

import io.github.proto4j.swing.annotation.EntryPoint;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.nio.channels.AlreadyBoundException;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class MapEntry<G> extends AbstractEntry<G> {

    private final Map<String, FieldReference<?>> fields = new HashMap<>();
    private final Map<String, Entry<?>>          nested = new HashMap<>();

    private volatile G instance;

    MapEntry(G gui) {
        //noinspection unchecked
        this((Class<G>) gui.getClass());
        instance = gui;
    }

    MapEntry(Class<G> type) {
        super(type);
    }

    /**
     * Returns the GUI-object stored in this {@code Entry}. This method
     * will throw a {@link NullPointerException} if the given instance
     * is {@code null}.
     *
     * @return the stored GUI instance
     * @throws NullPointerException if the stored GUI is {@code null}
     */
    @Override
    public G getGUI() {
        return instance;
    }

    /**
     * Applies a new gui value.
     *
     * @param gui the new GUI instance
     */
    @Override
    public void setGUI(G gui) {
        if (instance == null) {
            this.instance = gui;
        }
    }

    /**
     * Returns a field reference that is mapped to the given name. The name will
     * be the same as the one at the field's declaration.
     *
     * @param name the field's name
     * @return the corresponding {@link FieldReference}; {@code null} if no
     *         mapping exists for the given name.
     */
    @Override
    public FieldReference<?> getDeclaredField(String name) {
        return fields.get(name);
    }

    /**
     * Returns a field reference that is mapped to the given name. The name will
     * be the same as the one at the field's declaration.
     * <p>
     * This method will fail if the provided class type is not an assignable
     * form of the stored one.
     *
     * @param name the field's name
     * @param cls the component class type
     * @return the corresponding {@link FieldReference}; {@code null} if no
     *         mapping exists for the given name.
     * @throws ClassCastException if there is an error while casting
     */
    @Override
    public <V> FieldReference<V> getDeclaredField(String name, Class<V> cls) throws ClassCastException {
        //noinspection unchecked
        return (FieldReference<V>) getDeclaredField(name);
    }

    /**
     * Tries to insert the given field and throws an exception on error.
     *
     * @param reference the field's reference
     * @throws NullPointerException  if the given reference is {@code null}
     * @throws AlreadyBoundException if there is already a mapping with the
     *                               field's name
     */
    @Override
    public <V> void putField(FieldReference<V> reference) throws NullPointerException, AlreadyBoundException {
        Objects.requireNonNull(reference);
        String name = reference.getDescription().getFieldName();

        if (fields.containsKey(name)) {
            throw new AlreadyBoundException();
        }
        fields.put(name, reference);
    }

    /**
     * Returns an array of {@link FieldReference} objects reflecting all the fields declared
     * by the GUI class.
     *
     * @return an array of {@link FieldReference} objects
     */
    @Override
    public FieldReference<?>[] getDeclaredFields() {
        return fields.values().toArray(FieldReference[]::new);
    }

    /**
     * Searches for the {@link EntryPoint} annotation and executes the method
     * with it.
     *
     * @param args the method's arguments
     * @throws NullPointerException if no method was found
     */
    @Override
    public void start(Object... args) throws NullPointerException {
        Objects.requireNonNull(instance);

        for (Method m : getGUI().getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(EntryPoint.class)) {
                try {
                    m.setAccessible(true);
                    m.invoke(getGUI(), args);
                } catch (ReflectiveOperationException e) {
                    // ignore that
                }
                return;
            }
        }
        throw new NullPointerException("entry point not found");
    }

    /**
     * If nested GUIs are provided they can be fetched from here.
     *
     * @param name the field's name
     * @param type gui's class type
     * @return the {@link Entry} storing all information about the GUI
     */
    @Override
    public <T_G> Entry<T_G> getNestedGUI(String name, Class<T_G> type) {
        Objects.requireNonNull(type);
        //noinspection unchecked
        return (Entry<T_G>) getNestedGUI(name);
    }

    /**
     * If nested GUIs are provided they can be fetched from here.
     *
     * @param name the field's name
     * @return the {@link Entry} storing all information about the GUI
     */
    @Override
    public Entry<?> getNestedGUI(String name) {
        Objects.requireNonNull(name);

        return nested.get(name);
    }

    /**
     * Returns all nested GUI entries.
     *
     * @return all nested GUI entries
     */
    @Override
    public Entry<?>[] getNestedGUIs() {
        if (nested.isEmpty()) {
            return new Entry[0];
        }
        return nested.values().toArray(Entry[]::new);
    }

    /**
     * Tries to insert the given nested GUI entry and throws an exception on
     * error.
     *
     * @param name the field's name
     * @param entry the {@link Entry} object
     * @throws NullPointerException  if the given reference is {@code null}
     * @throws AlreadyBoundException if there is already a mapping with the
     *                               field's name
     */
    @Override
    public <T_G> void putNestedGUI(String name, Entry<T_G> entry) throws NullPointerException, AlreadyBoundException {
        Objects.requireNonNull(name);
        Objects.requireNonNull(entry);

        if (nested.containsKey(name)) {
            throw new AlreadyBoundException();
        }
        nested.put(name, entry);
    }

    // Still under development
    @Override
    public <E extends EventListener> E createListener(Class<E> cls, Object ls, Object parent) {
        if (cls.isAssignableFrom(ls.getClass())) {
            return cls.cast(ls);
        }

        if (ls instanceof Method) {
            Object proxy = Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new InvocationHandler() {
                final Method m = (Method) ls;

                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (method.getDeclaringClass() == Object.class) {
                        return method.invoke(this, args);
                    }

                    // Name check does not work, because methods could get a different
                    // name compared to their related base method name
                    if (Modifier.isAbstract(method.getModifiers())) {
                        return m.invoke(parent, args);
                    }
                    return null;
                }
            });
            return cls.cast(proxy);
        }
        return null;
    }


}
