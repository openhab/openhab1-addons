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
		IScopeContext context = new DefaultScope();
		IEclipsePreferences node = context.getNode(CoreActivator.getDefault().getBundle().getSymbolicName());
		String folderPath = ConfigConstants.MAIN_CONFIG_FOLDER;
		node.put(DesignerCoreConstants.CONFIG_FOLDER_PREFERENCE, folderPath);
	}

}
