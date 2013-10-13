#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
