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
package org.openhab.binding.jointspace.internal;

/**
 * This class bundles the volume configuration fields that the JointSpace API
 * provides for a GET request
 *
 * @author David Lenz
 * @since 1.5.0
 */
class volumeConfig {

    public volumeConfig() {
        volume = 20;
        mute = false;
        min = 0;
        max = 99;
    }

    /**
     * Volume that should lie within @see min and @see max
     */
    int volume;

    /**
     * Whether TV is mute or not
     */
    boolean mute;

    /**
     * The minimal possible value for @see volume
     */
    int min;

    /**
     * The maximal possible value for @see volume
     */
    int max;

}
