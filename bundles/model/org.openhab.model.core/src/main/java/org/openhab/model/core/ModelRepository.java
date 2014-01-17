/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
