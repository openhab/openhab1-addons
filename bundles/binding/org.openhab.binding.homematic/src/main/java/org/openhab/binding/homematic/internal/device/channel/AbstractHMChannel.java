/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
            logger.error("Could not setValue " + value + " of type " + value.getClass() + " on parameter " + getAddress() + "#" + parameterKey
                    + ". Accepted value type: " + valueDescription.getParameterDescription(parameterKey).getType() + ". Exception: " + e.getMessage());
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
        if(values != null) {
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
