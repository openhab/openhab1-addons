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
package org.openhab.binding.enocean.internal.converter;

import java.util.HashMap;

import org.openhab.core.types.Command;

/**
 * A map of all converters for an OpenHAB {@link Command};
 *
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3.0
 *
 */
public class CommandToConverterMap extends HashMap<Class<? extends Command>, Class<? extends CommandConverter<?, ?>>> {

    private static final long serialVersionUID = 1L;

}
