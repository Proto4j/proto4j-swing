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

package io.github.proto4j.swing.annotation;//@date 04.09.2022

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * The base class for the internal {@link Annotation} handling. Two implementations
 * are provided by default: {@code SingleAnnotationContext} and
 * {@code MultipleAnnotationContext}.
 * <p>
 * This interface provides some utility methods when working with annotated
 * elements and their annotations.
 *
 * @param <A> the annotation type
 * @since 1.0
 */
public abstract class AnnotationContext<A extends Annotation> {

    /**
     * The annotation type
     */
    private final Class<A> type;

    /**
     * Retrieves an {@link AnnotationContext} from the given annotation type
     * and the dedicated {@link AnnotatedElement}.
     *
     * @param cls the annotation type
     * @param ae the annotated element
     * @return a new Single- or MultipleAnnotationContext
     * @param <R> the type of the annotation
     * @throws ReflectiveOperationException if an error occurs
     */
    public static <R extends Annotation> AnnotationContext<R> exchange(Class<R> cls, AnnotatedElement ae)
        throws ReflectiveOperationException {
        if (cls == null || ae == null) {
            return new SingleAnnotationContext<>(cls, null);
        }

        // automatically detect whether multiple annotation declarations
        // are present by retrieving the Repeatable annotation value
        if (cls.isAnnotationPresent(Repeatable.class)) {
            Class<? extends Annotation> rt = cls.getDeclaredAnnotation(Repeatable.class).value();

            // create a MultipleAnnotationContext instance if 'rt' is present
            if (ae.isAnnotationPresent(rt)) {
                Annotation rta = ae.getDeclaredAnnotation(rt);

                for (Method method : rta.annotationType().getDeclaredMethods()) {
                    if (method.getName().equals("value")) {
                        Object values = method.invoke(rta);
                        //noinspection unchecked
                        return new MultipleAnnotationContext<>(cls, (R[]) values);
                    }
                }
                throw new IllegalArgumentException("Could not read repeatable annotation");
            }
        }
        return new SingleAnnotationContext<>(cls, ae.getDeclaredAnnotation(cls));
    }

    /**
     * Collects all {@link AnnotationContext} instances from the given annotated
     * element nad stores them into an array.
     *
     * @param ae the annotated element
     * @return a new array storing all {@link AnnotationContext} instances.
     */
    public static AnnotationContext<?>[] collect(AnnotatedElement ae) {
        if (ae == null) return new AnnotationContext[0];

        Annotation[] values = ae.getDeclaredAnnotations();
        AnnotationContext<?>[] contexts = new AnnotationContext[values.length];
        for (int i = 0; i < contexts.length; i++) {
            Annotation a0 = values[i];

            //noinspection unchecked
            contexts[i] = new SingleAnnotationContext<>((Class<Annotation>) a0.annotationType(), a0);
        }
        return contexts;
    }

    public AnnotationContext(Class<A> type) {
        this.type = type;
    }

    /**
     * @return The annotation type of this context
     */
    public Class<A> annotationType() {
        return type;
    }

    /**
     * @return the stored annotation instance
     */
    public abstract A annotationValue();

    /**
     * @return the stored annotations as an array
     */
    public abstract A[] toArray();

    /**
     * @return true when the annotation value is not null or the array length
     *         is greater that {@code 0}; false otherwise
     */
    public abstract boolean isPresent();

    /**
     * Checks that the stored annotation value or array is not null.
     *
     * @throws NullPointerException if no value is present
     */
    public abstract void requireNonNull() throws NullPointerException;

    /**
     * If a value is present, performs the given action with the value,
     * otherwise does nothing.
     *
     * @param consumer the action to be performed, if a value is present
     * @throws NullPointerException if value is present and the given action is
     *         {@code null}
     */
    public void ifPresent(Consumer<? super A> consumer) {
        Objects.requireNonNull(consumer);

        if (isPresent()) {
            consumer.accept(annotationValue());
        }
    }

    /**
     * If a value array is present, performs the given action with every value
     * in that array, otherwise does nothing.
     *
     * @param consumer the action to be performed, if a value is present
     * @throws NullPointerException if value is present and the given action is
     *         {@code null}
     */
    public void ifPresentProcess(Consumer<? super A> consumer) {
        Objects.requireNonNull(consumer);

        if (isPresent()) {
            for (A a : toArray()) {
                consumer.accept(a);
            }
        }
    }

}
