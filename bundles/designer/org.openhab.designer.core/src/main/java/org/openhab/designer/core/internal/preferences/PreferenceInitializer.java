/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.designer.core.internal.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.openhab.config.core.ConfigConstants;
import org.openhab.designer.core.CoreActivator;
import org.openhab.designer.core.DesignerCoreConstants;

/**
 * This class initializes the preference setting for the configuration folder.
 * If no other preference has been set yet, the default defined in the config.core bundle
 * will be used.
 * 
 * @author Kai Kreuzer
 *
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {
	
	public PreferenceInitializer() {
	}

	public void initializeDefaultPreferences() {
		IScopeContext context = DefaultScope.INSTANCE;
		IEclipsePreferences node = context.getNode(CoreActivator.getDefault().getBundle().getSymbolicName());
		String folderPath = ConfigConstants.MAIN_CONFIG_FOLDER;
		node.put(DesignerCoreConstants.CONFIG_FOLDER_PREFERENCE, folderPath);
	}

}
