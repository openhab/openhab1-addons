/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package eu.aleon.aleoncean.device;

import java.util.LinkedList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class DeviceParameterUpdatedSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceParameterUpdatedSupport.class);

    private final Object source;
    private final boolean fireChangesOnly;

    private final List<DeviceParameterUpdatedListener> unnamedListeners = new LinkedList<>();
    private final NavigableMap<DeviceParameter, List<DeviceParameterUpdatedListener>> namedListeners = new TreeMap<>();

    public DeviceParameterUpdatedSupport(final Object source) {
        this(source, false);
    }

    public DeviceParameterUpdatedSupport(final Object source, final boolean fireChangesOnly) {
        this.source = source;
        this.fireChangesOnly = fireChangesOnly;
    }

    public void addParameterUpdatedListener(final DeviceParameterUpdatedListener listener) {
        unnamedListeners.add(listener);
    }

    public void removeParameterUpdatedListener(final DeviceParameterUpdatedListener listener) {
        unnamedListeners.remove(listener);
    }

    public void addParameterUpdatedListener(final DeviceParameter parameter, final DeviceParameterUpdatedListener listener) {
        List<DeviceParameterUpdatedListener> listNamed = namedListeners.get(parameter);
        if (listNamed == null) {
            listNamed = new LinkedList<>();
            namedListeners.put(parameter, listNamed);
        }
        listNamed.add(listener);
    }

    public void removeParameterUpdatedListener(final DeviceParameter parameter, final DeviceParameterUpdatedListener listener) {
        final List<DeviceParameterUpdatedListener> listNamed = namedListeners.get(parameter);
        if (listNamed != null) {
            listNamed.remove(listener);
            if (listNamed.isEmpty()) {
                namedListeners.remove(parameter);
            }
        }
    }

    private void fireParameterUpdated(final List<DeviceParameterUpdatedListener> listeners,
                                      final DeviceParameterUpdatedEvent event) {
        for (final DeviceParameterUpdatedListener listener : listeners) {
            listener.parameterUpdated(event);
        }
    }

    public void fireParameterUpdated(final DeviceParameter parameter,
                                     final DeviceParameterUpdatedInitiation initiation,
                                     final Object oldValue, final Object newValue) {
        if (fireChangesOnly
            && ((oldValue == null && newValue == null)
                || (oldValue != null && oldValue.equals(newValue)))) {
            LOGGER.debug("Do not fire parameter updated: oldValue: {}, newValue: {}, equals: {}"+oldValue, newValue, oldValue == null ? "--" : oldValue.equals(newValue));
            return;
        }

        final DeviceParameterUpdatedEvent event = new DeviceParameterUpdatedEvent(source, parameter, initiation, oldValue, newValue);

        DeviceParameterUpdatedSupport.this.fireParameterUpdated(unnamedListeners, event);
        if (parameter
            != null) {
            final List<DeviceParameterUpdatedListener> listNamed = namedListeners.get(parameter);
            if (listNamed != null) {
                DeviceParameterUpdatedSupport.this.fireParameterUpdated(listNamed, event);
            }
        }
    }
}
