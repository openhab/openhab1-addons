/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.bus;

/**
 * The virtual command for admin functionality.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public enum AdminCommand {
    DUMP_UNCONFIGURED_DEVICES, DUMP_UNSUPPORTED_DEVICES
}
