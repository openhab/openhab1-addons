#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
package ${artifactId}.internal;

import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class contains the methods that are made available in scripts and rules for ${action-name}.
 * 
 * @author ${author}
 * @since ${version}
 */
public class ${action-name} {

	private static final Logger logger = LoggerFactory.getLogger(${action-name}.class);

	// provide public static methods here
	
	// Example
	@ActionDoc(text="A cool method that does some ${action-name}", 
			returns="<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean do${action-name}(@ParamDoc(name="something", text="the something to do") String something) {
		if (!${action-name}ActionService.isProperlyConfigured) {
			logger.debug("${action-name} action is not yet configured - execution aborted!");
			return false;
		}
		// now do something cool
		return true;
	}

}
