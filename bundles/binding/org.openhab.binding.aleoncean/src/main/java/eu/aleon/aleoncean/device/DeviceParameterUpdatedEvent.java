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

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class DeviceParameterUpdatedEvent {

    private final Object source;

    private final DeviceParameter parameter;

    private final DeviceParameterUpdatedInitiation initiation;

    private final Object oldValue;

    private final Object newValue;

    public DeviceParameterUpdatedEvent(final Object source,
                                       final DeviceParameter parameter,
                                       final DeviceParameterUpdatedInitiation initiation,
                                       final Object oldValue,
                                       final Object newValue) {
        this.source = source;
        this.parameter = parameter;
        this.initiation = initiation;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public Object getSource() {
        return source;
    }

    public DeviceParameter getParameter() {
        return parameter;
    }

    public DeviceParameterUpdatedInitiation getInitiation() {
        return initiation;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

}
