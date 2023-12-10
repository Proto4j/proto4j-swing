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

package io.github.proto4j.swing.core; //@date 05.09.2022

import io.github.proto4j.swing.annotation.*;
import io.github.proto4j.swing.core.desc.ComponentDesc;
import io.github.proto4j.swing.core.desc.GenericDesc;
import io.github.proto4j.swing.core.desc.LayoutDesc;
import io.github.proto4j.swing.core.desc.MarginDesc;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

/**
 * This class wraps general and utility methods on providing the option
 * namespace. At initialization this manager will look up the /META-INF/options
 * directory in order to retrieve all defined options.
 *
 * @since 1.0
 */
public final class GlobalDesc {

    /**
     * The standard directory name for the defined options.
     */
    public static final String OPTIONS_PREFIX = "META-INF/options/";
    // the system property
    public static final String DESC_PATH_PROPERTY = "proto4j.swing.desc.path";
    // covers all defined options
    private static final Properties sharedOptions = new Properties();
    // used to determine whether the options were loaded
    private static volatile boolean optionsInitialized;

    static {
        String pathBase = GenericDesc.class.getPackageName();
        String path     = String.join(":", pathBase + ".layout", pathBase + ".margin");

        // This value is used to set the default package paths
        System.setProperty(DESC_PATH_PROPERTY, path);
    }

    /**
     * Tries to locate all options defined for the given class.
     *
     * @param cls the given class
     * @return {@code null} if no options are registered to that class name;
     *         a {@link Properties} instance otherwise.
     * @throws NullPointerException the given class is {@code null}
     */
    public static Properties getSharedOption(Class<?> cls)
            throws NullPointerException {
        Objects.requireNonNull(cls);
        return getSharedOption(cls.getName());
    }

    /**
     * Tries to locate all options defined for the given class name.
     *
     * @param className the class name the options are mapped to
     * @return {@code null} if no options are registered to that class name;
     *         a {@link Properties} instance otherwise.
     * @throws NullPointerException the given name is {@code null}
     */
    public static Properties getSharedOption(String className)
            throws NullPointerException {
        Objects.requireNonNull(className);
        ensureOptionsInitialized();

        return (Properties) sharedOptions.get(className);
    }

    /**
     * Resolves the given {@link Border} class to the related {@link MarginDesc}
     * type. This method uses the {@link Class#forName(String)} method to
     * retrieve the class instance for the {@link MarginDesc}.
     *
     * @param cls the border type
     * @return a new instance of the qualified {@link MarginDesc}
     * @throws NullPointerException if the border type was {@code null}
     */
    public static MarginDesc getMarginDesc(Class<?> cls)
            throws NullPointerException {
        String path = System.getProperty(DESC_PATH_PROPERTY);
        if (path == null) {
            path = GlobalDesc.class.getPackageName() + ".margin";
        }

        return getMarginDesc(cls, path.split(":"));
    }

    /**
     * Resolves the given {@link Border} class to the related {@link MarginDesc}
     * type. This method uses the {@link Class#forName(String)} method to
     * retrieve the class instance for the {@link MarginDesc}.
     *
     * @param cls the border type
     * @param packageNames the root packages where the corresponding Desc-class
     *         are located
     * @return a new instance of the qualified {@link MarginDesc}
     * @throws NullPointerException if the border type was {@code null}
     */
    public static MarginDesc getMarginDesc(Class<?> cls, String... packageNames)
            throws NullPointerException {
        Objects.requireNonNull(cls);

        if (cls == Border.class || cls.isInterface()) {
            return null;
        }

        try {
            for (String packageName : packageNames) {
                String name = String.join(".", packageName, cls.getSimpleName());

                try {
                    Class<?> mDescClass = Class.forName(name + "Desc");
                    return (MarginDesc) mDescClass.getDeclaredConstructor().newInstance();
                } catch (ClassNotFoundException notFound) {
                    // ignore that exception
                }
            }
        } catch (ReflectiveOperationException e) {
            // The given border class is not implemented, so we should return
            // null instead of throwing an exception.
        }
        return null;
    }

    /**
     * Resolves the given {@link LayoutManager} class to the related
     * {@link LayoutDesc} type. This method uses the {@link Class#forName(String)}
     * method to retrieve the class instance for the {@link LayoutDesc}.
     *
     * @param cls the layout manager type
     * @return a new instance of the qualified {@link LayoutDesc}
     * @throws NullPointerException if the layout manager type was {@code null}
     */
    public static LayoutDesc getLayoutDesc(Class<?> cls, ComponentDesc parent)
            throws NullPointerException {
        String path = System.getProperty(DESC_PATH_PROPERTY);
        if (path == null) {
            path = GlobalDesc.class.getPackageName() + ".layout";
        }

        return getLayoutDesc(cls, parent, path.split(":"));
    }

    /**
     * Resolves the given {@link LayoutManager} class to the related
     * {@link LayoutDesc} type. This method uses the {@link Class#forName(String)}
     * method to retrieve the class instance for the {@link LayoutDesc}.
     *
     * @param cls the layout manager type
     * @param parent the component description
     * @param packageNames the root packages where the corresponding Desc-class
     *         are located
     * @return a new instance of the qualified {@link LayoutDesc}
     * @throws NullPointerException if the layout manager type was {@code null}
     */
    public static LayoutDesc getLayoutDesc(Class<?> cls, ComponentDesc parent, String... packageNames)
            throws NullPointerException {
        Objects.requireNonNull(cls);
        Objects.requireNonNull(parent);

        if (cls == LayoutManager.class || cls.isInterface()) {
            return null;
        }

        try {
            for (String packageName : packageNames) {
                String name = String.join(".", packageName, cls.getSimpleName());

                try {
                    Class<?> mDescClass = Class.forName(name + "Desc");
                    return (LayoutDesc) mDescClass.getDeclaredConstructor(ComponentDesc.class)
                                                  .newInstance(parent);
                } catch (ClassNotFoundException notFound) {
                    // ignore that exception
                }
            }
        } catch (ReflectiveOperationException e) {
            // The given border class is not implemented, so we should return
            // null instead of throwing an exception.
        }
        return null;
    }

    /**
     * Tries to locate the given color name.
     * <p>
     *  RGB values can be represented
     * through three numbers separated by a {@code ,}. It is also possible
     * to query a color that has been added to the {@link UIDefaults} of the
     * current {@link LookAndFeel}. To do that, just put the
     * {@link Option.Query#indicator} in font of the color's name.
     *
     * @param value the color's name
     * @return a new {@link Color} object representing the color
     */
    public static Color getColor(String value) {
        if (value.contains(",")) {
            int[] rgb = Arrays.stream(value.split(","))
                              .mapToInt(Integer::parseInt).toArray();
            if (rgb.length == 3) {
                return new Color(rgb[0], rgb[1], rgb[2]);
            }
        }
        else if (Option.Query.isQuery(value)) {
            return UIManager.getColor(value.substring(1));
        }
        return Color.getColor(value);
    }

    public static void ensureOptionsInitialized() {
        if (optionsInitialized) {
            return;
        }

        AccessController.doPrivileged((PrivilegedAction<Void>) GlobalDesc::loadOptions);
        optionsInitialized = true;
    }

    private static Void loadOptions() {
        if (optionsInitialized) {
            return null;
        }
        registerDefaults();

        URL url = ClassLoader.getSystemResource(OPTIONS_PREFIX);
        if (url == null) {
            return null;
        }

        try {
            File   optionsDir = new File(url.getFile() + "/");
            File[] options    = optionsDir.listFiles();
            if (options == null || options.length == 0) {
                return null;
            }

            for (File file : options) {
                // This replacement enables the extra properties' extension at
                // the end of the loaded file.
                String className = file.getName().replace(".properties", "");

                Properties local = new Properties();
                local.load(new FileReader(file));
                sharedOptions.putIfAbsent(className, local);
            }

        } catch (IOException e) {
            throw new UnsupportedOperationException(e);
        }

        return null;
    }

    private static void registerDefaults() {
        sharedOptions.putIfAbsent(Layout.class.getName(), new GlobalOptions.LayoutOptions());
        sharedOptions.putIfAbsent(Margin.class.getName(), new GlobalOptions.MarginOptions());
        sharedOptions.putIfAbsent(Model.class.getName(), new GlobalOptions.ModelOptions());
        sharedOptions.putIfAbsent(Position.class.getName(), new GlobalOptions.PositionOptions());
        sharedOptions.putIfAbsent(Option.class.getName(), new GlobalOptions.SwingOptions());
        sharedOptions.putIfAbsent(SwingWindow.class.getName(), new GlobalOptions.SwingWindowOptions());
        sharedOptions.putIfAbsent(Bounds.class.getName(), new GlobalOptions.BoundsOptions());
    }
}
