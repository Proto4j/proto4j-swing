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

package io.github.proto4j.swing.core.desc; //@date 07.09.2022

import io.github.proto4j.swing.annotation.Model;

import java.awt.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Pattern;

public class ModelDesc extends GenericDesc<Model> {

    public static final Pattern DEFAULT_MODELS =
            Pattern.compile("(Table)|(Spinner)|(Combobox)|(List)|(Tree)|(ComboBox)|(Single)");

    /**
     * Creates a new {@code ModelDesc} object.
     *
     * @throws NullPointerException if the given annotation class is {@code null}
     */
    public ModelDesc() {
        super(Model.class);
    }

    /**
     * Applies the stored options on the given component.
     *
     * @param component the {@link Component} reference
     */
    @Override
    public void applyTo(Component component) {
        Objects.requireNonNull(component);

        Properties options = getDefinedOptions();
        String     key     = options.getProperty("TYPE", UNDEFINED);
        if (!hasOption(key)) {
            // Actually, this is unlikely to happen, because the value() method
            // always returns a class value.
            return;
        }

        Class<?> cls = getOption(key, Class.class);
        if (cls.isInterface() || Modifier.isAbstract(cls.getModifiers())) {
            return;
        }

        String   methodName = "set";
        Class<?> base       = getBaseClass(cls);

        // Now, we try to create the setXXXModel() method by inspecting
        // the base class name
        String clsName = base.getSimpleName()
                             .replace("Model", "")
                             .replace("Abstract", "");

        // This replacement is needed as it removed unnecessary List or Table
        // tags at the beginning og the name
        clsName = clsName.replaceAll(DEFAULT_MODELS.pattern(), "");
        methodName += clsName + "Model";

        try {
            Method target = component.getClass().getMethod(methodName, base);

            Object model = cls.getDeclaredConstructor().newInstance();
            target.invoke(component, model);
        } catch (ReflectiveOperationException e) {
            // This exception is ignored by default
            // REVISIT: maybe add a global logger
        }

    }

    private Class<?> getBaseClass(Class<?> cls) {
        Class<?> base = cls.getSuperclass();
        while (true) {
            if (base.getSimpleName().contains("Abstract")) {
                for (Class<?> iface : base.getInterfaces()) {
                    if (iface.getSimpleName().contains("Model")) {
                        return iface;
                    }
                }
            }
            Class<?> next = base.getSuperclass();
            if (next == Object.class || next == null) {
                break;
            }
            base = next;
        }
        return base;
    }


}
