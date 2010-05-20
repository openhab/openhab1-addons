package org.openhab.designer.core.internal.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.openhab.designer.core.CoreActivator;
import org.openhab.designer.core.DesignerCoreConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	private static final Logger logger = LoggerFactory.getLogger(PreferenceInitializer.class);
	
	public PreferenceInitializer() {
	}

	public void initializeDefaultPreferences() {
		IScopeContext context = new DefaultScope();
		IEclipsePreferences node = context.getNode(CoreActivator.getDefault().getBundle().getSymbolicName());
		String folderPath = System.getProperty("bundles.configuration.location");
		if(folderPath==null) {
			logger.warn("System property 'bundles.configuration.location' has not been set!");
		} else {
			logger.debug("System property 'bundles.configuration.location' set to {}.", folderPath);			
		}
		node.put(DesignerCoreConstants.CONFIG_FOLDER_PREFERENCE, folderPath);
	}

}
