/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.ui;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.ContentHandler;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.model.IResourceForEditorInputFactory;
import org.eclipse.xtext.ui.resource.IStorage2UriMapper;

import com.google.inject.Inject;

public class ResourceForFileEditorFactory implements
		IResourceForEditorInputFactory {

	// our singleton resource set to be used
	private static final ResourceSet resourceSet = new ResourceSetImpl();
	
	@Inject
	private IStorage2UriMapper storageToUriMapper;

	public Resource createResource(IEditorInput editorInput) {
		try {
			if (editorInput instanceof IStorageEditorInput) {
				IStorage storage = ((IStorageEditorInput) editorInput).getStorage();
				return createResourceFor(storage);
			}
			throw new IllegalArgumentException("Couldn't create EMF Resource for input " + editorInput);
		} catch (CoreException e) {
			throw new WrappedException(e);
		}
	}

	private Resource createResourceFor(IStorage storage) {
		ResourceSet resourceSet = getResourceSet(storage);
		URI uri = storageToUriMapper.getUri(storage);
		XtextResource resource = getResource(resourceSet, uri);
		resource.setValidationDisabled(true);
		return resource;
	}

	protected ResourceSet getResourceSet(IStorage storage) {
		return resourceSet;
	}

	private XtextResource getResource(ResourceSet resourceSet, URI uri) {
		Resource aResource = resourceSet.createResource(uri, ContentHandler.UNSPECIFIED_CONTENT_TYPE);
		if (!(aResource instanceof XtextResource))
			throw new IllegalStateException("The resource factory registered for " + uri
					+ " does not yield an XtextResource. Make sure the right resource factory has been registered.");
		return (XtextResource) aResource;
	}

}
