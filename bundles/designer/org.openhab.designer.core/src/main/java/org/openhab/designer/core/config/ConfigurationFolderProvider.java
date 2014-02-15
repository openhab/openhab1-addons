/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.designer.core.config;

import java.io.File;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.openhab.config.core.ConfigDispatcher;
import org.openhab.designer.core.CoreActivator;
import org.openhab.designer.core.DesignerCoreConstants;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationFolderProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigurationFolderProvider.class);
	
	private static IFolder folder; 
	
	static public synchronized IFolder getRootConfigurationFolder() throws CoreException {
		if(folder==null) {
			IProject defaultProject = ResourcesPlugin.getWorkspace().getRoot().getProject("config");
			if(!defaultProject.exists()) {
				initialize(defaultProject);
			}
			File configFolder = getFolderFromPreferences();
			if(configFolder!=null) {
				folder = defaultProject.getFolder("config");
				folder.createLink(configFolder.toURI(), IResource.BACKGROUND_REFRESH|IResource.REPLACE, null);
				ConfigDispatcher.setConfigFolder(configFolder.getAbsolutePath());
			}
		}
		return folder;
	}
	
	static public synchronized void setRootConfigurationFolder(final File configFolder) throws CoreException {
		ConfigDispatcher.setConfigFolder(configFolder.getAbsolutePath());
		IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				IProject defaultProject = ResourcesPlugin.getWorkspace().getRoot().getProject("config");
				if(!defaultProject.exists()) {
					initialize(defaultProject);
				}
				if(configFolder!=null) {
					folder = defaultProject.getFolder("config");
					if(folder.exists()) {
						folder.delete(true, null);
					}
					folder.createLink(configFolder.toURI(), IResource.ALLOW_MISSING_LOCAL, null);
				}
			}
		};
		ResourcesPlugin.getWorkspace().run(runnable, null);
	}	
	
	private static void initialize(IProject project) {
		try {			
			IProjectDescription desc = ResourcesPlugin.getWorkspace().newProjectDescription(project.getName());
			desc.setNatureIds(new String[] {
					"org.eclipse.xtext.ui.shared.xtextNature"
			});
			project.create(desc, null);
			project.open(null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private static File getFolderFromPreferences() {
		IPreferencesService service = Platform.getPreferencesService();
		Preferences node = service.getRootNode().node(ConfigurationScope.SCOPE).node(CoreActivator.PLUGIN_ID);
		if(node!=null) {
			String folderPath = node.get(DesignerCoreConstants.CONFIG_FOLDER_PREFERENCE, null);
			if(folderPath!=null) {
				File file = new File(folderPath);
				if(file!=null && file.isDirectory()) {
					return file;
				} else {
					logger.warn("'{}' is no valid directory.", folderPath);
				}
			}
		}
		return null;
	}

	public static void saveFolderToPreferences(String folderPath) {
		IPreferencesService service = Platform.getPreferencesService();
		Preferences node = service.getRootNode().node(ConfigurationScope.SCOPE).node(CoreActivator.PLUGIN_ID);
		try {
			if(node!=null) {
				node.put(DesignerCoreConstants.CONFIG_FOLDER_PREFERENCE, folderPath);
				node.flush();
				return;
			}
		} catch (BackingStoreException e) {}
		logger.warn("Could not save folder '{}' to preferences.", folderPath);
	}
}
