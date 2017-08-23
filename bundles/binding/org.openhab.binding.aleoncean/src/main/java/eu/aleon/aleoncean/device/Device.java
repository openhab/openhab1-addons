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

import java.util.Set;
import eu.aleon.aleoncean.packet.EnOceanId;
import eu.aleon.aleoncean.packet.RadioPacket;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public interface Device extends Comparable<Device> {

    /**
     * Parse a radio packet.
     *
     * The caller have to check if the radio packet is intended to be used by this device (e.g. addressed to it).
     *
     * @param packet The radio packet, that should be parsed by the device.
     */
    public void parseRadioPacket(final RadioPacket packet);

    /**
     * Get a set of available device parameters.
     *
     * A parameter could be used to fetch or set device values.
     *
     * @return Return a set of all supported parameters.
     */
    public Set<DeviceParameter> getParameters();

    /**
     * Get a value by using device parameter.
     *
     * @param parameter The parameter that value should be returned.
     * @return Return the value of the device parameter.
     * @throws IllegalDeviceParameterException if the device parameter could not be handled.
     */
    public Object getByParameter(final DeviceParameter parameter) throws IllegalDeviceParameterException;

    /**
     * Set a value by using device parameter.
     *
     * @param parameter The parameter that value should be changed.
     * @param value     The value that should be set.
     * @throws IllegalDeviceParameterException if the device parameter could not be handled.
     */
    public void setByParameter(final DeviceParameter parameter, final Object value) throws IllegalDeviceParameterException;

    /**
     * Get the EnOcean address of the remote / real device.
     *
     * @return Return the address of the remote / real device.
     */
    public EnOceanId getAddressRemote();

    /**
     * Get the EnOcean address of our self.
     *
     * @return Return the address of our self, the local one.
     */
    public EnOceanId getAddressLocal();

    public void addParameterUpdatedListener(DeviceParameterUpdatedListener listener);

    public void removeParameterUpdatedListener(DeviceParameterUpdatedListener listener);

}
