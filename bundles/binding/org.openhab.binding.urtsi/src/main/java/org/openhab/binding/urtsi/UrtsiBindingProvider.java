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
package org.openhab.binding.urtsi;

import org.openhab.core.autoupdate.AutoUpdateBindingProvider;

/**
 * @author Oliver Libutzki
 * @since 1.3.0
 *
 */
public interface UrtsiBindingProvider extends AutoUpdateBindingProvider {

    String getDeviceId(String itemName);

    int getChannel(String itemName);

    int getAddress(String itemName);
}
