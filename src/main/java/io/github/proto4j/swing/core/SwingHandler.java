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

package io.github.proto4j.swing.core;//@date 06.09.2022

import io.github.proto4j.swing.annotation.AnnotationContext;
import io.github.proto4j.swing.FieldReference;
import io.github.proto4j.swing.ServiceManager;
import io.github.proto4j.swing.core.desc.ComponentDesc;

import java.lang.annotation.Annotation;

/**
 * SwingHandlers are used to implement the way each option annotation is
 * handled. Because this class uses the {@link ServiceManager} as a general
 * cache it is possible to add own annotations with own handlers.
 * <p>
 * This class is the top level handler in the generation process of every
 * gui. It is called after the {@link java.awt.Component} was initialized
 * and all annotations were read.
 *
 * @see ServiceManager
 * @since 1.0
 */
public interface SwingHandler {

    /**
     * The ServiceManager object storing all loaded {@link SwingHandler}s. They
     * should call the {@link ServiceManager#register(Object)} in a static
     * context to ensure the service wil be added.
     *
     * @since 1.0
     */
    public static final ServiceManager<SwingHandler> CACHE =
            ServiceManager.from(SwingHandler.class);

    /**
     * Handles the incoming {@link FieldReference} with its {@link ComponentDesc}
     * and the references field.
     * <p>
     * <b>INFO:</b> This method makes use of the thread-safe class
     * {@link FieldReference} so that no external synchronization has to be
     * done.
     *
     * @param reference the field's reference
     * @param context the loaded annotation wrapped into an {@link AnnotationContext}
     */
    public void onElement(FieldReference<?> reference, AnnotationContext<?> context);

    /**
     * @return the annotation type that can be handled
     */
    public Class<? extends Annotation> annotationType();

}
