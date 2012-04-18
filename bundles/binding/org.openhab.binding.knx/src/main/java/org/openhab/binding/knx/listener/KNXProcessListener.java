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
package org.openhab.binding.knx.listener;

import tuwien.auto.calimero.process.ProcessEvent;
import tuwien.auto.calimero.process.ProcessListener;


/**
 * Enhancement of the {@link ProcessListener}-Interface. It contains additional
 * methods which Calimero provides through the <code>ProcessListenerEx</code>.
 * In order to use dependency injection mechanism we extracted the additional
 * methods as new Interface. 
 * 
 * @author Thomas.Eichstaedt-Engelen
 */
public interface KNXProcessListener extends ProcessListener {

	/**
	 * Indicates that a KNX group read request message was received from the KNX network.
	 * 
	 * @param e process event object
	 */
	void groupReadRequest(ProcessEvent e);

	/**
	 * Indicates that a KNX group read response message was received from the KNX network.
	 * 
	 * @param e process event object
	 */
	void groupReadResponse(ProcessEvent e);

}
