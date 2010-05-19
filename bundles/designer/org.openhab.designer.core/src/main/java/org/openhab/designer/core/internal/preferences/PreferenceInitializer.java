package org.openhab.designer.core.internal.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.openhab.designer.core.CoreActivator;
import org.openhab.designer.core.DesignerCoreConstants;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public PreferenceInitializer() {
	}

	public void initializeDefaultPreferences() {
		IScopeContext context = new DefaultScope();
		IEclipsePreferences node = context.getNode(CoreActivator.getDefault().getBundle().getSymbolicName());
		String folderPath = System.getProperty("bundles.configuration.location");
		node.put(DesignerCoreConstants.CONFIG_FOLDER_PREFERENCE, folderPath);
	}

}
