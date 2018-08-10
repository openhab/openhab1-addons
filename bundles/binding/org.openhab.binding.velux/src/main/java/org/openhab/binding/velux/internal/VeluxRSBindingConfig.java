/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.internal;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.openhab.binding.velux.VeluxBindingConstants;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This represents the configuration of a openHAB item that is binded to a Velux
 * KLF200 Gateway. It contains the following information:
 *
 * <ul>
 * <li><B>bindingItemType</B>
 * <P>
 * accessable via
 * {@link org.openhab.binding.velux.internal.VeluxRSBindingConfig#getBindingItemType
 * getBindingItemType} as representation of the Velux device is filed in the Velux bridge.</li>
 * <li><B>bindingConfig</B>
 * <P>
 * accessable via
 * {@link org.openhab.binding.velux.internal.VeluxRSBindingConfig#getBindingConfig getBindingConfig} containing the
 * device-specific binding configuration
 * as declared in the binding configuration (possibly adapted by preprocessing).</li>
 * </ul>
 *
 * @author Guenther Schreiner
 * @since 1.13.0
 */

public class VeluxRSBindingConfig extends VeluxBindingConfig {

    private final Logger logger = LoggerFactory.getLogger(VeluxRSBindingConfig.class);

    /**
     * The ascending sorted list of generic Objects indexed by an Integer
     */
    private SortedMap<Integer, String> mapAscending = new TreeMap<Integer, String>(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    });
    /**
     * The descending sorted list of generic Objects indexed by an Integer
     */
    private SortedMap<Integer, String> mapDescending = new TreeMap<Integer, String>(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2.compareTo(o1);
        }
    });

    /**
     * The sorted list of generic Objects indexed by an Integer
     */
    Integer rollershutterLevel = 0;

    public void VeluxRollershutterBindingParser(final String channelValue) {
        logger.debug("VeluxRollershutterBindingParser({}) called.", channelValue);

        String[] channelValueParts = channelValue.trim().split(VeluxBindingConstants.BINDING_VALUES_SEPARATOR);
        if ((channelValueParts.length % 2) != 0) {
            throw new IllegalArgumentException(
                    "Velux Rollershutter binding must contain an even number of configuration parts separated by '"
                            + VeluxBindingConstants.BINDING_VALUES_SEPARATOR + "' (ignoring binding '" + channelValue
                            + "').");
        }

        for (int idx = 0; idx < channelValueParts.length; idx++) {
            logger.trace("VeluxRollershutterBindingParser() processing {}.", channelValueParts[idx]);

            int degree;
            try {
                degree = Integer.parseInt(channelValueParts[idx]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Velux Rollershutter binding must contain an even number of configuration parts separated by '"
                                + VeluxBindingConstants.BINDING_VALUES_SEPARATOR
                                + "' each consisting of a shutter level followed by a scene name (ignoring binding '"
                                + channelValue + "').");
            }
            idx++;
            mapAscending.put(degree, channelValueParts[idx]);
            mapDescending.put(degree, channelValueParts[idx]);
        }
        for (Map.Entry<Integer, String> nextEntry : mapAscending.entrySet()) {
            logger.trace("VeluxRollershutterBindingParser({},{}) processed.", nextEntry.getKey(), nextEntry.getValue());
        }
    }

    /**
     * Constructor of the VeluxBindingConfig.
     *
     * @param bindingItemType
     *                            The Velux item type {@link org.openhab.binding.velux.internal.VeluxItemType
     *                            VeluxItemType} which the Velux device is filed in the Velux bridge.
     * @param channelValue
     *                            The optional configuration type of the Velux binding.
     *
     * @throws BindingConfigParseException
     *                                         does not really occur.
     */
    public VeluxRSBindingConfig(VeluxItemType bindingItemType, String channelValue) throws BindingConfigParseException {
        super(bindingItemType, channelValue);
        logger.trace("VeluxRSBindingConfig(constructor:{},{}) called.", bindingItemType, channelValue);
        VeluxRollershutterBindingParser(channelValue);
    }

    /**
     * Returns the next shutter level for an DOWN command w/ adjusting the actual position.
     *
     * @return <b>rollershutterLevel</b> of type Integer with next position after DOWN command.
     */
    public Integer getNextAscendingLevel() {
        logger.trace("getNextAscendingLevel() called.");

        for (Map.Entry<Integer, String> nextEntry : mapAscending.entrySet()) {
            if (nextEntry.getKey() > this.rollershutterLevel) {
                this.rollershutterLevel = nextEntry.getKey();
                break;
            }
        }
        logger.trace("getNextAscendingLevel() returning {}.", this.rollershutterLevel);
        return this.rollershutterLevel;

    }

    /**
     * Returns the next shutter level for an UP command w/ adjusting the actual position.
     *
     * @return <b>rollershutterLevel</b> of type Integer with next position after UP command.
     *
     */
    public Integer getNextDescendingLevel() {
        logger.trace("getNextDescendingLevel() called.");

        for (Map.Entry<Integer, String> nextEntry : mapDescending.entrySet()) {
            if (nextEntry.getKey() < this.rollershutterLevel) {
                this.rollershutterLevel = nextEntry.getKey();
                break;
            }
        }
        logger.trace("getNextDescendingLevel() returning {}.", this.rollershutterLevel);
        return this.rollershutterLevel;

    }

    /**
     * Returns the current shutter level w/o adjusting the actual positioning.
     *
     * @return <b>rollershutterLevel</b> of type Integer with current position.
     *
     */
    public Integer getLevel() {
        logger.trace("getLevel() returning {}.", this.rollershutterLevel);
        return this.rollershutterLevel;
    }

    /**
     * Returns the scene name of the current shutter level w/o adjusting the actual positioning.
     *
     * @return sceneName
     *         A String describing the next scene.
     */
    public String getSceneName() {
        return getSceneName(this.rollershutterLevel);
    }

    /**
     * Returns the scene name w/o adjusting the actual positioning.
     *
     * @param level
     *                  The shutter level is be queried.
     * @return sceneName
     *         A String describing the next scene.
     */
    public String getSceneName(Integer level) {
        logger.trace("getSceneName({}) called.", level);
        logger.trace("getSceneName() returning {}.", mapDescending.get(level));
        return mapDescending.get(level);
    }
}

/**
 * end-of-internal/VeluxRSBindingConfig.java
 */
