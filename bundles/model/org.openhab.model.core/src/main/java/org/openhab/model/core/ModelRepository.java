/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
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

package org.openhab.model.core;

import java.io.InputStream;

import org.eclipse.emf.ecore.EObject;

/**
 * The model repository stores the configuration files (EMF models).
 * It takes care of loading these resources and serving them to clients.
 * By this abstraction, the clients do not need to know where the models
 * come from.
 *  
 * @author Kai Kreuzer
 *
 */
public interface ModelRepository {

	/**
	 * Returns a model of a given name
	 * 
	 * @param name name of the requested model
	 * @return the model or null, if not found
	 */
	public EObject getModel(String name);

	/**
	 * Adds a model to the repository or refreshes it if it already exists
	 * 
	 * @param name the model name to add/refresh
	 * @param inputStream an input stream with the model's content
	 * 
	 * @return true, if it was successfully processed, false otherwise
	 */
	public boolean addOrRefreshModel(String name, InputStream inputStream);

	/**
	 * Removes a model from the repository
	 * 
	 * @param name the name of the model to remove
	 * 
	 * @return true, if model was removed, false, if it did not exist
	 */
	public boolean removeModel(String name);
	
	/**
	 * Returns all names of models of a given type (file extension)
	 * 
	 * @param modelType the model type to get the names for
	 * 
	 * @return all names of available models
	 */
	public Iterable<String> getAllModelNamesOfType(String modelType);

	/**
	 * Adds a change listener
	 * 
	 * @param listener the listener to add
	 */
	public void addModelRepositoryChangeListener(
			ModelRepositoryChangeListener listener);

	/**
	 * Removes a change listener
	 * 
	 * @param listener the listener to remove
	 */
	public void removeModelRepositoryChangeListener(
			ModelRepositoryChangeListener listener);

}
