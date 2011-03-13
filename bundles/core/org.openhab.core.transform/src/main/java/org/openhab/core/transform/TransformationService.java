/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
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

package org.openhab.core.transform;


/**
 * A TransformationProcessor transforms a given input and returns the transformed
 * result. Transformations could make sense in various situations, for example:
 * <ul>
 * <li>extract certain informations from a weather forecast website</li>
 * <li>extract the status of your TV which provides it's status on a webpage</li>
 * <li>postprocess the output from a serial device to be human readable</li>
 * </ul>
 * One could provide his own processors by providing a new implementation of this 
 * Interface. 
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.7.0
 */
public interface TransformationService {
	
	/**
	 * Transforms the input <code>source</code> by means of the given <code>function</code>
	 * and returns the transformed output. If the transformation couldn't be completed
	 * for any reason, one should return the unchanged <code>source</code>. This
	 * method should never return <code>null</code>. In case of any error an 
	 * {@link TransformationException} should be thrown.
	 * 
	 * @param function the function to be used to transform the input
	 * @param source the input to be transformed
	 * 
	 * @return the transformed result or the unchanged <code>source</code> if the
	 * transformation couldn't be completed for any reason.
	 * 
	 * @throws TransformationException if any error occurs
	 */
	String transform(String function, String source) throws TransformationException;
	
	/**
	 * @return the name of the service which is used to identify it in caches,
	 * etc.
	 */
	String getName();

}
