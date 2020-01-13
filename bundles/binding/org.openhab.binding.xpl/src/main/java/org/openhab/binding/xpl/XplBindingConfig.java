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
package org.openhab.binding.xpl;

import org.cdp1802.xpl.xPL_MutableMessageI;
import org.openhab.core.binding.BindingConfig;

/**
 * This is a class that stores xPL binding configuration elements :
 * - an interface to the message template to match toward incoming messages
 * - the name of the body key that will be returned if matched
 *
 * @author clinique
 * @since 1.6.0
 */
public class XplBindingConfig implements BindingConfig {
    public xPL_MutableMessageI Message;
    public String NamedParameter;
}
