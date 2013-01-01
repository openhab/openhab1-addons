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