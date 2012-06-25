/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.core.service;


/**
 * Active Services provides their current status through this Interface. One
 * might want to add {@link ActiveServiceStatusListener}s to get informed of
 * lifecycle updates by callback methods.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public interface ActiveServiceStatusProvider {

	/**
	 * Add a new {@link ActiveServiceStatusListener} to the listeners.
	 * 
	 * @param listener the new {@link ActiveServiceStatusListener} to add
	 * @return <code>true</code> if adding of the listener succeeded.
	 */
	boolean addStatusListener(ActiveServiceStatusListener listener);

	/**
	 * Removes the given {@link ActiveServiceStatusListener} from the listeners.
	 * 
	 * @param listener the {@link ActiveServiceStatusListener} to remove
	 * @return <code>true</code> if removing of the listener succeeded.
	 */
	boolean removeStatusListener(ActiveServiceStatusListener listener);

	/**
	 * Indicates whether the underlying refresh thread of this Service is running or
	 * not.
	 * 
	 * @return <code>true</code> if the RefreshThread is alive and <code>false</code>
	 * in all other cases.
	 */
	boolean isRunning();

}
