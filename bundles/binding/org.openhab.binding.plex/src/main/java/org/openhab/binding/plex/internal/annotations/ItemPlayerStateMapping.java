/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plex.internal.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openhab.binding.plex.internal.PlexPlayerState;
import org.openhab.binding.plex.internal.PlexProperty;

/**
 * Annotation to map Plex player states to OnOffType items
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ItemPlayerStateMapping {
	PlexProperty property();
	PlexPlayerState state();
}
