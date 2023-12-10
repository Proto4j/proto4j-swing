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
import io.github.proto4j.swing.annotation.SwingWindow;
import io.github.proto4j.swing.core.desc.GenericDesc;
import io.github.proto4j.swing.core.desc.WindowDesc;

import java.lang.annotation.Annotation;

/**
 * The basic implementation for the {@link SwingWindow} annotation.
 *
 * @since 1.0
 */
public class WindowHandler extends AbstractSwingHandler<SwingWindow> {

    static {
        CACHE.register(new WindowHandler());
    }

    /**
     * @return the annotation type that can be handled
     */
    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
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
    @Override
    protected GenericDesc<SwingWindow> getDesc(FieldReference<?> reference, SwingWindow value) {
        return new WindowDesc();
    }
}
