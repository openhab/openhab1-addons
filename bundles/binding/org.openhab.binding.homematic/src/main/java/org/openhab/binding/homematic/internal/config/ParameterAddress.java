/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.config;

/**
 * A parameter is part of a physical device that holds a specific value. It can
 * be r/w or r/o.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3
 * 
 */
public interface ParameterAddress {

    String getAsString();

    String getParameterId();

    String getDeviceId();

    String getChannelId();

}
