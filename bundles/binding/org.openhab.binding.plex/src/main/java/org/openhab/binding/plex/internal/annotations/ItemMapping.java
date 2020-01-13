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
package org.openhab.binding.plex.internal.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openhab.binding.plex.internal.PlexProperty;
import org.openhab.core.types.Command;

/**
 * Annotation to map bean property values to openHAB items identified by a String
 *
 * @author Jeroen Idserda
 * @since 1.7.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ItemMapping {
    PlexProperty property();

    Class<? extends Command>type();

    ItemPlayerStateMapping[]stateMappings() default {};
}
