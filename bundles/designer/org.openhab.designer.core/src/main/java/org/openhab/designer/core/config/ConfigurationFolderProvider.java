package org.openhab.designer.core.config;

import java.io.File;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.openhab.designer.core.CoreActivator;
import org.openhab.designer.core.DesignerCoreConstants;
import org.osgi.service.prefs.Preferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationFolderProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigurationFolderProvider.class);
	
	private static IFolder folder; 
	
	static public synchronized IFolder getRootConfigurationFolder() {
		if(folder==null) {
			IProject defaultProject = ResourcesPlugin.getWorkspace().getRoot().getProject("config");
			try {
				if(!defaultProject.exists()) defaultProject.create(null);
				defaultProject.open(null);
				folder = defaultProject.getFolder("config");
				folder.createLink(getFileFromPreferences().toURI(), IResource.BACKGROUND_REFRESH|IResource.REPLACE, null);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return folder;
	}
	
	private static File getFileFromPreferences() {
		IPreferencesService service = Platform.getPreferencesService();
		Preferences node = service.getRootNode().node(DefaultScope.SCOPE).node(CoreActivator.PLUGIN_ID);
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
}
