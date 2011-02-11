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
