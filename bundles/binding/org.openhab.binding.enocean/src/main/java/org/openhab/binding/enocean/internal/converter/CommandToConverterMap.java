/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
