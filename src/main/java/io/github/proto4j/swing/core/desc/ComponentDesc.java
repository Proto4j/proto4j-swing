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

import io.github.proto4j.swing.FieldReference;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * This class provides basic features of {@link java.awt.Component} descriptions,
 * to minimize the effort required to implement inheritors.
 * <p>
 * This class is used within the generation of the GUI. It represents the
 * annotated component together with its declared properties.
 *
 * @since 1.0
 */
public abstract class ComponentDesc {

    /**
     * The annotated element declared in the GUI class. This can also be the
     * class itself if annotated.
     */
    private transient final AnnotatedElement field;

    /**
     * The field's name.
     */
    private final String fieldName;

    /**
     * The type of the stored {@link java.awt.Component}.
     */
    private final Class<?> componentType;

    /**
     * The linked component
     */
    private Object component;

    /**
     * Returns the default instance for component descriptions. The used class
     * is: {@code OrderedArrayListComponentDesc}. This implementation of the
     * {@link ComponentDesc} provides a concurrent approach for retrieving
     * the stored values.
     *
     * @param field the source
     * @param root the GUI's instance
     * @return a new {@link ComponentDesc} object
     * @throws NullPointerException if one of the given arguments was {@code null}
     */
    public static ComponentDesc createDesc(AnnotatedElement field, Object root)
            throws NullPointerException {
        Objects.requireNonNull(field);
        Objects.requireNonNull(root);

        return new ComponentDescImpl(field, root);
    }

    /**
     * Creates a new {@link ComponentDesc} from the given annotated element
     * and the parent instance.
     *
     * @param field the annotated element
     * @param parent the GUI's instance
     */
    protected ComponentDesc(AnnotatedElement field, Object parent) {
        Objects.requireNonNull(parent);
        this.component = null;
        this.field = Objects.requireNonNull(field);

        // manually check the type of the given AnnotatedElement
        if (field instanceof Field) {
            fieldName     = ((Field) field).getName();
            componentType = ((Field) field).getType();
        } else {
            fieldName     = FieldReference.THIS;
            componentType = parent.getClass();
        }
    }

    /**
     * Checks if this {@link ComponentDesc} contains an {@code OptionDesc}
     * linked to the given {@link Annotation} class.
     *
     * @param cls the annotation class
     * @param <A> the bound annotation type
     * @return {@code true} if this {@link ComponentDesc} can describe an
     *         {@code OptionDesc} to the given annotation type
     * @throws NullPointerException if the given annotation class is {@code null}
     */
    public abstract <A extends Annotation> boolean describable(Class<A> cls)
            throws NullPointerException;

    /**
     * Inserts the given generic description with the linked annotation type
     * if not already mapped.
     *
     * @param desc the option description
     * @param <A> the linked annotation type
     * @throws NullPointerException if the option description is {@code null}
     */
    public abstract <A extends Annotation> void addIfAbsent(GenericDesc<A> desc)
            throws NullPointerException;

    /**
     * Retrieves the current {@link GenericDesc} for the given annotation type
     * of {@code null} if none was specified yet.
     *
     * @param cls the annotation class
     * @param <A> the annotation type
     * @return the current {@link GenericDesc} for the given annotation type
     *         of {@code null} if none was specified yet.
     * @throws NullPointerException if the given class was {@code null}
     */
    public abstract <A extends Annotation> GenericDesc<A> getDesc(Class<A> cls)
            throws NullPointerException;

    /**
     * @return the referenced {@link AnnotatedElement}.
     */
    public AnnotatedElement getField() {
        return field;
    }

    /**
     * @return The referenced annotated element's name
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @return The type of the stored {@link java.awt.Component}.
     */
    public Class<?> getComponentType() {
        return componentType;
    }

    /**
     * @return the component instance for this description
     */
    public Object getInstance() {
        return component;
    }

    /**
     * Applies a new value to the referenced component.
     *
     * @param value the new component value
     */
    public void setInstance(Object value) {
        this.component = value;
    }

}
