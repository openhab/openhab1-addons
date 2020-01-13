/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.netatmo;

import org.openhab.binding.netatmo.internal.camera.NetatmoCameraAttributes;
import org.openhab.binding.netatmo.internal.weather.NetatmoMeasureType;
import org.openhab.binding.netatmo.internal.weather.NetatmoScale;
import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Netatmo items.
 *
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 *
 * @author Andreas Brenk
 * @author Rob Nielsen
 * @author Ing. Peter Weiss
 * @since 1.4.0
 */
public interface NetatmoBindingProvider extends BindingProvider {

    /**
     * Returns an Id of the user the OAuth credentials do refer to.
     *
     * @param itemName
     * @return
     */
    String getUserid(String itemName);

    /**
     * Queries the Netatmo device id of the given {@code itemName}.
     *
     * @param itemName
     *            the itemName to query
     * @return the Netatmo device id of the Item identified by {@code itemName}
     *         if it has a Netatmo binding, <code>null</code> otherwise
     */
    String getDeviceId(String itemName);

    /**
     * Queries the Netatmo measure of the given {@code itemName}.
     *
     * @param itemName
     *            the itemName to query
     * @return the Netatmo measure of the Item identified by {@code itemName} if
     *         it has a Netatmo binding, <code>null</code> otherwise
     */
    NetatmoMeasureType getMeasureType(String itemName);

    /**
     * Queries the Netatmo module of the given {@code itemName}.
     *
     * @param itemName
     *            the itemName to query
     * @return the Netatmo module id if the item has a Netatmo binding and
     *         specifies a module (i.e. is not a device binding),
     *         <code>null</code> otherwise
     */
    String getModuleId(String itemName);

    /**
     * Returns the scale to use when querying the Netatmo measure of the given
     * {@code itemName}.
     *
     * @param itemName
     * @return the Netatmo scale of the Item identified by {@code itemName} if
     *         it has a Netatmo binding, <code>null</code> otherwise
     */
    NetatmoScale getNetatmoScale(String itemName);

    /**
     * Queries the Camera home of the given {@code itemName}.
     *
     * @param itemName
     *            the itemName to query
     * @return the NetatmoCamera HomeId of the Item identified by {@code itemName}
     *         if it has a NetatmoCamera binding, <code>null</code> otherwise
     */
    String getHomeId(String itemName);

    /**
     * Queries the Netatmo Camera Person of the given {@code itemName}.
     *
     * @param itemName
     *            the itemName to query
     * @return the NetatmoCamera PersonId of the Item identified by {@code itemName} if
     *         it has a NetatmoCamera binding, <code>null</code> otherwise
     */
    String getPersonId(String itemName);

    /**
     * Queries the Netatmo Attribute of the given {@code itemName}.
     *
     * @param itemName
     *            the itemName to query
     * @return the NetatmoCamera Attribute of the Item identified by {@code itemName} if
     *         it has a NetatmoCamera binding, <code>null</code> otherwise
     */
    NetatmoCameraAttributes getAttribute(String itemName);

    /**
     * Queries the Netatmo Camera of the given {@code itemName}.
     *
     * @param itemName
     *            the itemName to query
     * @return the Netatmo CameraId of the Item identified by {@code itemName} if
     *         it has a Netatmo binding, <code>null</code> otherwise
     */
    String getCameraId(String itemName);

    /**
     * Queries the Netatmo Type of the given {@code itemName}.
     *
     * @param itemName
     *            the itemName to query
     *
     * @return <code>weather</code> if its a weather Item or <code>camera</code> if it is a camera item
     */
    String getItemType(String itemName);

}
