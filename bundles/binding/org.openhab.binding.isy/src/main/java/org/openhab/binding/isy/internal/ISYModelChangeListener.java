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
package org.openhab.binding.isy.internal;

import com.universaldevices.device.model.UDControl;
import com.universaldevices.device.model.UDNode;

/**
 * @author Tim Diekmann
 * @since 1.10.0
 */
public interface ISYModelChangeListener {
    public void onModelChanged(final UDControl control, final Object action, final UDNode node);

}
