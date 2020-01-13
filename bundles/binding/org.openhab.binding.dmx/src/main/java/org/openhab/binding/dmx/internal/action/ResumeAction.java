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
package org.openhab.binding.dmx.internal.action;

import org.openhab.binding.dmx.internal.core.DmxChannel;

/**
 * Resume action. Restores previously suspended value or actions on an item.
 *
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public class ResumeAction extends BaseAction {

    @Override
    protected int calculateNewValue(DmxChannel channel, long currentTime) {

        channel.resume();
        return channel.getNextValue(currentTime);
    }

}
