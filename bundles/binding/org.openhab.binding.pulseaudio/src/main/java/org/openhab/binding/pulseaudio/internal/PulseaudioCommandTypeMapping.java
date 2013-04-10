/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.pulseaudio.internal;

/**
 * Represents all valid commands which could be processed by this binding
 * 
 * @author Tobias Bräutigam
 * @since 1.2.0
 */
public enum PulseaudioCommandTypeMapping {
	
	EXISTS,		// whether an pulseaudio item exists or not
	MUTED,		// whether an pulseaudio item is muted or not
	VOLUME, 	// the volume setting of an pulseaudio item
	ID,			// the ID of an pulseaudio item
	MODULE_ID,	// the module id of an pulseaudio item
	SLAVE_SINKS,// the names of slave sinks that are members of an combined sink
	RUNNING,	// wether an pulseaudio item is in state RUNNING or not
	IDLE,		// wether an pulseaudio item is in state IDLE or not
	SUSPENDED,	// wether an pulseaudio item is in state SUSPENDED or not
	CORKED		// wether an pulseaudio item is in state CORKED or not
	
}
