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

import io.github.proto4j.swing.annotation.*;
import io.github.proto4j.swing.Entry;
import io.github.proto4j.swing.FieldReference;
import io.github.proto4j.swing.ServiceManager;
import io.github.proto4j.swing.core.desc.ComponentDesc;
import io.github.proto4j.swing.laf.LAFProvider;

import java.awt.*;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * A basic class which is used by parent classes to delegate the creation
 * process of the GUI object.
 * <p>
 * This class is designed to be used in a try-catch resource statement. It
 * will close automatically after the statement is finished. For example,
 * <pre>
 *     Entry&lt;MyGUI&gt; entry = ...
 *     try (SwingReader reader = new SwingReader()) {
 *         reader.read(entry, arguments);
 *         SwingLinker.readEntry(entry);
 *     }
 *     //...
 * </pre>
 * Note the {@link SwingLinker} statement above - it is used to execute all
 * defined 'init' methods from the {@link Option} annotation and tries to
 * bind each component to their specified parent component
 *
 * @see java.lang.AutoCloseable
 * @since 1.0
 */
public class SwingReader implements AutoCloseable {

    // Indicates whether this reader is already closed
    private volatile boolean closed = false;

    /**
     * Tries to fill the given {@link Entry} with a GUI instance and all
     * possible {@link FieldReference} objects.
     *
     * @param entry the entry to be filled
     * @param args constructor arguments for the GUI-class
     * @param <T_GUI> the GUI type
     * @throws Exception if an error occurs
     */
    public synchronized <T_GUI> void read(Entry<T_GUI> entry, Object[] args) throws Exception {
        Objects.requireNonNull(entry);
        Objects.requireNonNull(args);

        Class<T_GUI> type = entry.getType();
        if (type == null) {
            throw new NullPointerException("GUI type must not be null");
        }

        if (isClosed()) {
            throw new IllegalStateException("Reader is closed");
        }

        T_GUI root = entry.getGUI();
        if (root == null) {
            root = setGUIInstance(entry, args, type);
        }

        ServiceManager<LAFProvider> manager = LAFProvider.getManager();
        handleRootComponent(entry, root, manager);
        for (Field field : type.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                // Static fields are ignored by default - there could be an
                // implementation for handling static fields in the future.
                continue;
            }
            handleComponent(entry, root, manager, field);
        }
    }

    private <T_GUI> T_GUI setGUIInstance(Entry<T_GUI> entry, Object[] args,
                                         Class<T_GUI> type) {
        Class<?>[] argTypes = wrapArgs(args);
        T_GUI      root     = null;
        try {
            if (argTypes.length == 0) {
                root = type.getDeclaredConstructor().newInstance();
            } else {
                Constructor<T_GUI> c = type.getConstructor(argTypes);
                root = c.newInstance(args);
            }
            Objects.requireNonNull(root, "GUI instance not initialized");
            entry.setGUI(root);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
        return root;
    }

    private <T_GUI> void handleRootComponent
            (Entry<T_GUI> entry, T_GUI root, ServiceManager<LAFProvider> manager)
            throws Exception {
        int id = 0;
        AnnotationContext<Swing> ctx =
                AnnotationContext.exchange(Swing.class, root.getClass());
        LAFProvider provider = null;

        if (!ctx.isPresent()) {
            AnnotationContext<SwingWindow> wctx =
                    AnnotationContext.exchange(SwingWindow.class, root.getClass());
            if (!wctx.isPresent()) {
                return;
            }
            provider = manager.get(p -> p.getClass() == wctx.annotationValue().provider());
        } else {
            provider = manager.get(p -> p.getClass() == ctx.annotationValue().provider());
        }

        Objects.requireNonNull(provider, "Undefined LAFProvider");
        if (!(root instanceof Component)) {
            throw new IllegalArgumentException("Annotated component not an instance of Component.class");
        }
        Component component = (Component) root;
        createFieldReference(entry, root, root.getClass(), id, component);
    }

    private <T_GUI> void handleComponent
            (Entry<T_GUI> entry, T_GUI root, ServiceManager<LAFProvider> manager, Field field)
            throws Exception {
        int id = 0;

        AnnotationContext<Swing> ctx =
                AnnotationContext.exchange(Swing.class, field);
        LAFProvider provider = null;

        if (!ctx.isPresent()) {
            AnnotationContext<SwingWindow> wctx =
                    AnnotationContext.exchange(SwingWindow.class, field);
            if (!wctx.isPresent()) {
                // This definition error will be ignored by default
                return;
            }
            id       = wctx.annotationValue().value();
            provider = manager.get(p -> p.getClass() == wctx.annotationValue().provider());
        } else {
            id       = ctx.annotationValue().value();
            provider = manager.get(p -> p.getClass() == ctx.annotationValue().provider());
        }

        Objects.requireNonNull(provider, "Undefined LAFProvider");
        Component component = null;
        AnnotationContext<Nested> nctx =
                AnnotationContext.exchange(Nested.class, field);

        if (nctx.isPresent()) {
            Entry<?> nested = Entry.of(field.getType());
            entry.putNestedGUI(field.getName(), nested);
            component = (Component) nested.getGUI();
        } else {
            if (!Modifier.isFinal(field.getModifiers())) {
                component = provider.getComponent(field.getType());
            } else {
                enterCriticalSection(field);
                component = (Component) field.get(root);
                leaveCriticalSection(field);
            }
        }

        createFieldReference(entry, root, field, id, component);
    }

    private <T_GUI> void createFieldReference
            (Entry<T_GUI> entry, T_GUI root, AnnotatedElement field, int id, Component component) {
        Objects.requireNonNull(component, "Could not create Component");
        AnnotationContext<?>[]    all  = AnnotationContext.collect(field);
        ComponentDesc             desc = ComponentDesc.createDesc(field, root);
        FieldReference<Component> ref  = new FieldReference<>(id);

        ref.setComponentDesc(desc);
        ref.setParent(root);
        ref.set(component);
        for (AnnotationContext<?> ac : all) {
            // SwingHandlers are loaded dynamically at runtime with an
            // object of the ServiceManager class.
            SwingHandler handler = SwingHandler.CACHE
                    .getOrDefault(s -> s.annotationType() == ac.annotationType(), null);

            if (handler == null) {
                // This error should be ignored, because there can be a lot
                // more on the target field that should be ignored.
                continue;
            }
            handler.onElement(ref, ac);
        }
        // Finally add the loaded entry and continue with the next field.
        entry.putField(ref);
    }

    /**
     * @return Whether this reader is already closed or still can be used
     */
    public synchronized boolean isClosed() {
        return closed;
    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * {@code try}-with-resources statement.
     *
     * @throws Exception if this resource cannot be closed
     */
    @Override
    public synchronized void close() throws Exception {
        closed = true;
    }

    private Class<?>[] wrapArgs(Object[] args) {
        Class<?>[] types = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            types[i] = args[i].getClass();
        }
        return types;
    }

    private synchronized void enterCriticalSection(Field field) {
        field.setAccessible(true);
    }

    private synchronized void leaveCriticalSection(Field field) {
        field.setAccessible(false);
    }
}
