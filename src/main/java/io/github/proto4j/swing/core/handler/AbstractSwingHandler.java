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

package io.github.proto4j.swing.core.handler; //@date 06.09.2022

import io.github.proto4j.swing.FieldReference;
import io.github.proto4j.swing.annotation.AnnotationContext;
import io.github.proto4j.swing.core.SwingHandler;
import io.github.proto4j.swing.core.desc.ComponentDesc;
import io.github.proto4j.swing.core.desc.GenericDesc;

import java.awt.*;
import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * This class provides a generic implementation of a {@link SwingHandler}
 * class. The basic execution flow is the following:
 * <ol>
 *     <li>Check if the annotation is present (Exception on failure)</li>
 *     <li>Retrieve the {@link GenericDesc} through
 *     {@link #getDesc(FieldReference, Annotation)}</li>
 *     <li>Add the description to the {@link ComponentDesc} stored in the
 *     {@code FieldReference}</li>
 *     <li>Read the contents of the given annotation with
 *     {@link GenericDesc#read(Annotation)}</li>
 *     <li>Call {@link GenericDesc#applyTo(Component)} with the linked component
 *     from the {@code FieldReference}</li>
 * </ol>
 * The last three points should always be executed to prevent any unknown
 * exceptions would be thrown.
 *
 * @param <A> the annotation type
 * @see GenericDesc
 * @since 1.0
 */
public abstract class AbstractSwingHandler<A extends Annotation> implements SwingHandler {

    /**
     * Handles the incoming {@link FieldReference} with its {@code ComponentDesc}
     * and the references field.
     * <p>
     * <b>INFO:</b> This method makes use of the thread-safe class
     * {@link FieldReference} so that no external synchronization has to be
     * done.
     *
     * @param reference the field's reference
     * @param context the loaded annotation wrapped into an {@link AnnotationContext}
     */
    @Override
    public void onElement(FieldReference<?> reference, AnnotationContext<?> context) {
        Objects.requireNonNull(reference);
        Objects.requireNonNull(context);

        if (!context.isPresent()) {
            // This branch is unlikely to be executed, but it is more safely
            // to check if the annotation is present.
            return;
        }

        //noinspection unchecked
        A value = (A) context.annotationValue();
        // retrieve the description
        GenericDesc<A> desc = getDesc(reference, value);
        if (desc == null) {
            String msg = getClass().getSimpleName() + ": no implementation found";
            throw new UnsupportedOperationException(msg);
        }

        // Add the description to the ComponentDesc object
        reference.getDescription().addIfAbsent(desc);

        // At first, the data provided by the annotation has to be
        // loaded. Next, the values are applied to the given component.
        desc.read(value);
        desc.applyTo((Component) reference.get());
    }

    /**
     * Tries to create the {@link GenericDesc} object from the given reference
     * and annotation value.
     *
     * @param reference the {@link FieldReference} object
     * @param value the annotation value
     * @return a qualified {@link GenericDesc} that can read an annotation of
     *         type {@code <A>}
     */
    protected abstract GenericDesc<A> getDesc(FieldReference<?> reference, A value);
}
