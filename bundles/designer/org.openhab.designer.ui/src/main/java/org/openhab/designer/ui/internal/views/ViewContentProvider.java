/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.designer.ui.internal.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.openhab.designer.core.config.ConfigurationFolderProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * The content provider class is responsible for
 * providing objects to the view. It can wrap
 * existing objects in adapters or simply return
 * objects as-is. These objects may be sensitive
 * to the current input of the view, or ignore
 * it and always show the same content 
 */
public class ViewContentProvider implements IStructuredContentProvider, 
									   ITreeContentProvider {

	private static final Logger logger = LoggerFactory.getLogger(ViewContentProvider.class); 
	
	private IFolder configRootFolder;

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		if(newInput instanceof IFolder) {
			configRootFolder = (IFolder) newInput;
		}
	}
	
	public void dispose() {
		configRootFolder = null; 
	}
	
	public Object[] getElements(Object parent) {
		if (parent instanceof IProject) {
			try {
				configRootFolder = ConfigurationFolderProvider.getRootConfigurationFolder();
				if(configRootFolder!=null) {
					return getChildren(configRootFolder);
				} else {
					return new String[] { "<select a configuration folder>" };
				}
			} catch (CoreException e) {
				logger.error("Cannot initialize configuration project in workspace", e);
				return new Object[0];
			}
		}
		return getChildren(parent);
	}

	public Object getParent(Object child) {
		if(child.equals(configRootFolder)) return null;
		
		if(child instanceof IResource) {
			IResource res = (IResource) child;
			return res.getParent();
		}
		return null;
	}

	public Object[] getChildren(Object parent) {
		if(parent instanceof IFolder) {
			IFolder folder = (IFolder) parent;
			try {
				IResource[] resources = folder.members();
				List<IResource> children  = new ArrayList<IResource>(resources.length);
				for(IResource child : resources) {
					// filter out hidden files
					if(!child.getName().startsWith(".")) {
						children.add(child);
					}
				}
				return children.toArray(new IResource[children.size()]);
			} catch (CoreException e) {
				logger.warn("Error getting children for folder '{}'", folder.getName(), e);
			}
		}
		return new Object[0];
	}

	public boolean hasChildren(Object parent) {
		return getChildren(parent).length > 0;
	}
}