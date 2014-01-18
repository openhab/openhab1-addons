/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.device.channel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;

import org.openhab.binding.homematic.internal.device.AbstractHMDevice;
import org.openhab.binding.homematic.internal.device.physical.HMPhysicalDevice;
import org.openhab.binding.homematic.internal.xmlrpc.XmlRpcConnection;
import org.openhab.binding.homematic.internal.xmlrpc.impl.DeviceDescription;
import org.openhab.binding.homematic.internal.xmlrpc.impl.Paramset;
import org.openhab.binding.homematic.internal.xmlrpc.impl.ParamsetDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AbstractChannel implements the properties of a parent and a connection that
 * all Channel types have in common.
 * 
 * @author Mathias Ewald
 * 
 * @param <P>
 *            The type of the parent.
 * @param <C>
 *            The type of the connection.
 */
public abstract class AbstractHMChannel extends AbstractHMDevice implements HMChannel {

    private static final Logger logger = LoggerFactory.getLogger(AbstractHMChannel.class);

    private HMPhysicalDevice parent;
    private ParamsetDescription masterDescription;
    private ParamsetDescription valueDescription;
    private Paramset values;
    private PropertyChangeSupport pChangeSupport;

    public AbstractHMChannel(HMPhysicalDevice parent, DeviceDescription deviceDescription) {
        super(deviceDescription);
        if (parent == null) {
            throw new IllegalArgumentException("parent must not be null");
        }
        this.parent = parent;
        this.pChangeSupport = new PropertyChangeSupport(this);
        XmlRpcConnection connection = getParent().getCCU().getConnection();
        try {
            valueDescription = connection.getParamsetDescription(getAddress(), "VALUES");
        } catch (Exception e) {
            logger.warn("Could not retrieve ParamsetDescription of VALUES for device at " + getAddress() + ": " + e);
            valueDescription = new ParamsetDescription(new HashMap<String, Object>());
        }
        try {
            values = connection.getParamset(getAddress(), "VALUES");
        } catch (Exception e) {
            logger.debug("Could not retrieve Paramset for device of VALUES for device at " + getAddress() + ": " + e);
            values = new Paramset(new HashMap<String, Object>());
        }
        try {
            masterDescription = connection.getParamsetDescription(getAddress(), "MASTER");
        } catch (Exception e) {
            logger.warn("Could not retrieve ParamsetDescription of MASTER for device at " + getAddress() + ": " + e);
            masterDescription = new ParamsetDescription(new HashMap<String, Object>());
        }
    }

    @Override
    public HMPhysicalDevice getParent() {
        return parent;
    }

    @Override
    public ParamsetDescription getValuesDescription() {
        return valueDescription;
    }

    @Override
    public Paramset getValues() {
        return values;
    }

    @Override
    public void setValues(Paramset paramset) {
        values = paramset;
    }

    @Override
    public void setValue(String parameterKey, Object value) {
        logger.debug("Setting new value " + value + " of type " + value.getClass() + " on parameter " + getAddress() + "#" + parameterKey);
        try {
            getParent().getCCU().getConnection().setValue(getAddress(), parameterKey, value);
        } catch (RuntimeException e) {
            logger.error("Exception occured.", e);
            logger.error("Could not setValue " + value + " of type " + value.getClass() + " on parameter " + getAddress() + "#"
                    + parameterKey + ". Accepted value type: " + valueDescription.getParameterDescription(parameterKey).getType() + ".");
            throw e;
        }
        this.values.getValues().put(parameterKey, value);
    }

    @Override
    public ParamsetDescription getMaster() {
        return masterDescription;
    }

    @Override
    public void setMaster(ParamsetDescription paramset) {
        this.masterDescription = paramset;
    }

    @Override
    public void updateProperty(String parameterKey, Object value) {
        if (values != null) {
            this.values.getValues().put(parameterKey, value);
        }
    }

    protected void firePropertyChangeEvent(String propertyName, Object oldValue, Object newValue) {
        pChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        pChangeSupport.removePropertyChangeListener(l);
    }

}
