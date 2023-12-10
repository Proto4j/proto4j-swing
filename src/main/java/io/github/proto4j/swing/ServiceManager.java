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

package io.github.proto4j.swing; //@date 05.09.2022

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * The basic class for managing a set of services (Thread-Safe).
 * <p>
 * As part of its initialization, the {@code ServiceManager} class will
 * attempt to load available {@code S} classes by using:
 * <ul>
 * <li>Service providers of the {@code io.github.laf.swing.proto4j.LAFProvider} class,
 * that are loaded via the {@link ServiceLoader#load(Class)} mechanism.</li>
 * </ul>
 *
 * @apiNote {@code ServiceManager} initialization is done lazily and looks up
 *         service providers using the thread context class loader. The services loaded
 *         and available to an application will depend on the thread context class
 *         loader of the thread that triggers driver initialization by
 *         {@code ServiceManager}.
 * @since 1.0
 */
public final class ServiceManager<S> {

    // list of registered service classes
    private final CopyOnWriteArrayList<Info<S>> providers = new CopyOnWriteArrayList<>();

    // used to synchronize all callers that access the registered services
    private final Object LOCK = new Object();
    private transient final Class<S> cls;
    // used to indicate whether the services were initialized once
    private volatile boolean servicesLoaded;

    private ServiceManager(Class<S> cls) {this.cls = cls;}

    /**
     * Creates a new {@link ServiceManager} for the given service type.
     *
     * @param cls the service clas
     * @param <S> the type of service this manager will provide
     * @return a new {@link ServiceManager} for the given service type.
     */
    public static <S> ServiceManager<S> from(Class<S> cls) {
        return new ServiceManager<>(cls);
    }

    /**
     * Registers the given service with the {@code ServiceManager}.
     * <p>
     * A newly-loaded driver class should call the method {@code register} to
     * make itself known to the {@code ServiceManager}. If the driver is
     * currently registered, no action is taken.
     *
     * @param service the new {@code service} that is to be registered with the
     *         {@code ServiceManager}
     * @throws NullPointerException if {@code service} is null
     */
    public void register(S service) {
        synchronized (LOCK) {
            Objects.requireNonNull(service);

            providers.addIfAbsent(new Info<>(service));
        }
    }

    /**
     * Removes the specified service from the {@code ServiceManager}'s list of
     * registered services.
     * <p>
     * If a {@code null} value is specified for the service to be removed, then no
     * action is taken.
     * <p>
     * If the specified service is not found in the list of registered services,
     * then no action is taken. If the service was found, it will be removed
     * from the list of registered services.
     *
     * @param service the {@code service} to remove
     */
    public void deregister(S service) {
        if (service == null) {
            return;
        }

        synchronized (LOCK) {
            Info<S> info = new Info<>(service);

            providers.remove(info);
        }
    }

    /**
     * Attempts to locate a service that is bound to this manager with the
     * given {@link Predicate} to filter.
     *
     * @param predicate a filter to locate the service
     * @return a service object of type {@code <S>}
     */
    public S get(Predicate<? super S> predicate) {
        S service = getOrDefault(predicate, null);

        if (service == null) {
            throw new IllegalArgumentException("Could not find service");
        }
        return service;
    }

    /**
     * Attempts to locate a service that is bound to this manager with the
     * given {@link Predicate} to filter.
     *
     * @param predicate a filter to locate the service
     * @return a service object of type {@code <S>} or the given default value
     */
    public S getOrDefault(Predicate<? super S> predicate, S defaultVal) {
        Objects.requireNonNull(predicate);

        ensureProvidersInitialized();
        synchronized (LOCK) {
            for (Info<S> info : providers) {
                if (predicate.test(info.service)) {
                    return info.service;
                }
            }
        }
        return defaultVal;
    }

    /**
     * Retrieves an Enumeration with all the currently loaded services
     * to which the current caller has access.
     *
     * <P><B>Note:</B> The classname of a service can be found using
     * <CODE>d.getClass().getName()</CODE>
     *
     * @return the list of services loaded by the caller's class loader
     * @see #services()
     */
    public Enumeration<S> getServices() {
        return Collections.enumeration(getServicesInternal());
    }

    /**
     * Retrieves a Stream with all the currently loaded services
     * to which the current caller has access.
     *
     * @return the stream of {@code services} loaded by the caller's
     *         class loader
     * @since Java 9
     */
    public Stream<S> services() {
        return getServicesInternal().stream();
    }

    private List<S> getServicesInternal() {
        List<S> result = new LinkedList<>();

        for (Info<S> info : providers) {
            result.add(info.service);
        }
        return result;
    }

    private Void loadProviders() {
        if (servicesLoaded) {
            return null;
        }

        ServiceLoader<S> lafProviders = ServiceLoader.load(cls);
        Iterator<S>      iterator     = lafProviders.iterator();

        try {
            while (iterator.hasNext()) {
                // Because instances are created lazily at runtime this statement
                // is needed.
                iterator.next();
            }
        } catch (Throwable t) {
            // ignore that
        }
        return null;
    }

    private void ensureProvidersInitialized() {
        if (servicesLoaded) {
            return;
        }

        synchronized (LOCK) {
            if (servicesLoaded) {
                return;
            }

            AccessController.doPrivileged((PrivilegedAction<Void>) this::loadProviders);
            servicesLoaded = true;
        }
    }

    private static class Info<S> {

        final S service;

        Info(S service) {this.service = service;}

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Info
                    && ((Info<?>) obj).service == this.service;
        }

        @Override
        public int hashCode() {
            return this.service.hashCode();
        }
    }
}
