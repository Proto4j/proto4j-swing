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

package io.github.proto4j.swing; //@date 07.09.2022

import io.github.proto4j.swing.annotation.ActionHandler;
import io.github.proto4j.swing.annotation.AnnotationContext;
import io.github.proto4j.swing.annotation.EntryPoint;
import io.github.proto4j.swing.annotation.GUI;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.Objects;

/**
 * This class provides basic operations for the {@link Entry}.
 * <p>
 * It comes with two important methods: {@link #linkAction(Object)} and
 * {@link #start(Object...)}. Their implementation is standardized, so there
 * is no need for external implementation.
 *
 * @param <G> the GUI's type
 */
public abstract class AbstractEntry<G> implements Entry<G> {

    // class object used to create the GUI's instance
    private final Class<G> type;

    /**
     * Create a new {@link AbstractEntry} object.
     *
     * @param type the GUI's type
     */
    protected AbstractEntry(Class<G> type) {
        Objects.requireNonNull(type);
        if (!type.isAnnotationPresent(GUI.class)) {
            throw new UnsupportedOperationException("Class is not annotated with @GUI");
        }
        this.type = type;
    }

    /**
     * A simple delegation method to provide the {@link EventListener} object.
     *
     * @param cls the {@link EventListener} class
     * @param ls the listener object - currently either directly the
     *         {@code EventListener} or a {@link Method} that should be casted to the
     *         {@code EventListener}.
     * @param parent the declaring object
     * @param <E> the event listener type
     * @return the {@link EventListener} instance
     */
    public abstract <E extends EventListener> E createListener(Class<E> cls, Object ls, Object parent);

    /**
     * @return The GUI's type
     */
    @Override
    public Class<G> getType() {
        return type;
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
        G instance = getGUI();
        Objects.requireNonNull(instance);

        for (Method m : instance.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(EntryPoint.class)) {
                try {
                    m.setAccessible(true);
                    m.invoke(instance, args);
                } catch (ReflectiveOperationException e) {
                    // ignore that
                }
                return;
            }
        }
        throw new NullPointerException("entry point not found");
    }

    /**
     * Searches for the {@link ActionHandler} annotation and tries to link
     * the given EventListener to the specified target.
     *
     * @param src the event listener object
     */
    @Override
    public void linkAction(Object src) {
        Objects.requireNonNull(src);

        try {
            AnnotationContext<ActionHandler> base =
                    AnnotationContext.exchange(ActionHandler.class, src.getClass());


            if (!(src instanceof Method)) {
                if (base.isPresent()) {
                    Object ls = createListener(base.annotationValue().type(), src, null);
                    if (ls != null) {
                        addListener(ls, base);
                    }
                }
            }

            for (Method m : src.getClass().getDeclaredMethods()) {
                m.setAccessible(true);
                base = AnnotationContext.exchange(ActionHandler.class, m);
                if (base.isPresent()) {
                    Class<? extends EventListener> type =
                            base.annotationValue().type();

                    Object ls = createListener(type, m, src);
                    if (ls != null) {
                        addListener(ls, base);
                    }
                }
            }
        } catch (ReflectiveOperationException e) {
            // ignore that
        }
    }

    @Override
    public <T extends EventListener> boolean linkAction(String fieldName, Class<T> cls, T listener) {
        FieldReference<?> ref = getDeclaredField(fieldName);
        if (ref == null) {
            return false;
        }
        try {
            Object component = ref.get();
            String name = "add" + cls.getSimpleName();

            Method targetMethod =
                    component.getClass().getMethod(name, cls);

            targetMethod.setAccessible(true);
            targetMethod.invoke(component, listener);
            ref.addEventListener(listener);
            return true;
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }

    protected void addListener(Object listener, AnnotationContext<ActionHandler> ctx) {
        String[] targets = ctx.annotationValue().value();

        if (targets.length != 0) {
            for (String target : targets) {
                FieldReference<?> ref = getDeclaredField(target);
                if (ref == null) {
                    continue;
                }

                Object component = ref.get();
                try {
                    String name = "add"
                            + ctx.annotationValue().type().getSimpleName();
                    Class<? extends EventListener> type =
                            ctx.annotationValue().type();

                    Method targetMethod =
                            component.getClass().getMethod(name, type);

                    targetMethod.setAccessible(true);

                    EventListener el = type.cast(listener);
                    targetMethod.invoke(component, el);
                    ref.addEventListener(el);
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                    // ignore errors
                }
            }
        }
    }

}
