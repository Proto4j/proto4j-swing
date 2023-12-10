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

package io.github.proto4j.swing.core.desc; //@date 06.09.2022

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class ComponentDescImpl extends ComponentDesc {

    private final List<GenericDesc<?>> list = new ArrayList<>();

    /**
     * Creates a new {@link ComponentDesc} from the given annotated element
     * and the parent instance.
     *
     * @param field the annotated element
     * @param parent the GUI's instance
     */
    public ComponentDescImpl(AnnotatedElement field, Object parent) {
        super(field, parent);
        // To prevent errors these option descriptions are added by default
        addIfAbsent(new OptionDesc());
        addIfAbsent(new PositionDesc());
    }

    /**
     * Checks if this {@link ComponentDesc} contains an {@code OptionDesc}
     * linked to the given {@link Annotation} class.
     *
     * @param cls the annotation class
     * @return {@code true} if this {@link ComponentDesc} can describe an
     *         {@code OptionDesc} to the given annotation type
     * @throws NullPointerException if the given annotation class is {@code null}
     */
    @Override
    public <A extends Annotation> boolean describable(Class<A> cls) throws NullPointerException {
        for (GenericDesc<?> desc : list) {
            if (desc.annotationType() == cls) {
                return true;
            }
        }
        return false;
    }

    /**
     * Inserts the given generic description with the linked annotation type
     * if not already mapped.
     *
     * @param desc the option description
     * @throws NullPointerException if the option description is {@code null}
     */
    @Override
    public <A extends Annotation> void addIfAbsent(GenericDesc<A> desc) throws NullPointerException {
        Objects.requireNonNull(desc);
        if (!describable(desc.annotationType())) {
            list.add(desc);
        }
    }

    /**
     * Retrieves the current {@link GenericDesc} for the given annotation type
     * of {@code null} if none was specified yet.
     *
     * @param cls the annotation class
     * @return the current {@link GenericDesc} for the given annotation type
     *         of {@code null} if none was specified yet.
     * @throws NullPointerException if the given class was {@code null}
     */
    @Override
    public <A extends Annotation> GenericDesc<A> getDesc(Class<A> cls) throws NullPointerException {
        Objects.requireNonNull(cls);

        for (GenericDesc<?> desc : list) {
            if (desc.annotationType() == cls) {
                //noinspection unchecked
                return (GenericDesc<A>) desc;
            }
        }
        return null;
    }
}
