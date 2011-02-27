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

package org.openhab.model.core.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.openhab.model.core.EventType;
import org.openhab.model.core.ModelRepository;
import org.openhab.model.core.ModelRepositoryChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class ModelRepositoryImpl implements ModelRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(ModelRepositoryImpl.class);
	private final ResourceSet resourceSet;
	
	private final Set<ModelRepositoryChangeListener> listeners = new HashSet<ModelRepositoryChangeListener>();

	public ModelRepositoryImpl() {
		XtextResourceSet xtextResourceSet = new XtextResourceSet();
		xtextResourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		this.resourceSet = xtextResourceSet;
		// don't use XMI as a default
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().remove("*");
	}
	
	@Override
	public EObject getModel(String name) {
		Resource resource = getResource(name);
		if(resource!=null) {
			if(resource.getContents().size()>0) {
				return resource.getContents().get(0);
			} else {
				logger.warn("Configuration model '{}' cannot be parsed correctly!", name);
				resourceSet.getResources().remove(resource);
				return null;
			}
		} else {
			logger.warn("Configuration model '{}' can not be found", name);
			return null;
		}
	}

	@Override
	public boolean addOrRefreshModel(String name, InputStream inputStream) {
		Resource resource = getResource(name);
		if(resource==null) {
			synchronized(resourceSet) {
				// seems to be a new file
				resource = resourceSet.createResource(URI.createURI(name));
				if(resource!=null) {
					try {
						Map<String, String> options = new HashMap<String, String>();
						options.put(XtextResource.OPTION_ENCODING, "UTF-8");
						resource.load(inputStream, options);
						notifyListeners(name, EventType.ADDED);
						return true;
					} catch (IOException e) {
						logger.warn("Configuration model '" + name + "' cannot be parsed correctly!", e);
						resourceSet.getResources().remove(resource);
					}
				}
			}
		} else {
			synchronized(resourceSet) {
				resource.unload();
				try {
					resource.load(inputStream, Collections.EMPTY_MAP);
					notifyListeners(name, EventType.MODIFIED);
					return true;
				} catch (IOException e) {
					logger.warn("Configuration model '" + name + "' cannot be parsed correctly!", e);
					resourceSet.getResources().remove(resource);
				}
			}
		}
		return false;
	}

	@Override
	public boolean removeModel(String name) {
		Resource resource = getResource(name);
		if(resource!=null) {
			synchronized(resourceSet) {
				// do not physically delete it, but remove it from the resource set
				resourceSet.getResources().remove(resource);
				notifyListeners(name, EventType.REMOVED);
				return true;
			}
		} else {
			return false;
		}
	}

	@Override
	public Iterable<String> getAllModelNamesOfType(final String modelType) {
		synchronized(resourceSet) {
			Iterable<Resource> matchingResources = Iterables.filter(resourceSet.getResources(), new Predicate<Resource>() {
				public boolean apply(Resource input) {
					if(input!=null) {
						return input.getURI().fileExtension().equalsIgnoreCase(modelType);
					} else {
						return false;
					}
				}});
			return Iterables.transform(matchingResources, new Function<Resource, String>() {
				public String apply(Resource from) {
					return from.getURI().path();
				}});
		}
	}

	@Override
	public void addModelRepositoryChangeListener(
			ModelRepositoryChangeListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeModelRepositoryChangeListener(
			ModelRepositoryChangeListener listener) {
		listeners.remove(listener);
	}

	private Resource getResource(String name) {
		 return resourceSet.getResource(URI.createURI(name), false);
	}

	private void notifyListeners(String name, EventType type) {
		for(ModelRepositoryChangeListener listener : listeners) {
			listener.modelChanged(name, type);
		}
	}

}
