/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.transform.internal.service;

import java.io.File;
import java.util.Locale;

import org.apache.commons.io.FilenameUtils;
import org.openhab.config.core.ConfigDispatcher;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.transform.internal.TransformationActivator;

/**
 * Base class for localizable transformation {@link TransformationService} 
 * Enables the search for a localised existing transform file
 *
 * @author Gaël L'hopital
 * @since 1.7.0
 */
public class LocalizableTransformationService {
	
	/**
	 * Returns the name of the localized transformation file 
	 * if it exists, keeps the original in the other case
	 * 
	 * @param filename name of the requested transformation file
	 * @return original or localized transformation file to use
	 */
	final String getLocalizedProposedFilename(String filename) {
		
		String basename = FilenameUtils.getBaseName(filename);
		String extension = FilenameUtils.getExtension(filename);
		String locale = Locale.getDefault().getLanguage();
		String basePath = ConfigDispatcher.getConfigFolder() + File.separator + TransformationActivator.TRANSFORM_FOLDER_NAME + File.separator;
		
		String path = basePath + filename;
		// something like : .../configurations/transform/test.extension
		
		String alternatePath = basePath + basename + "_" + locale + "." + extension;
		// something like : .../configurations/transform/test_en.extension

		File f = new File(alternatePath);
		if (f.exists()) {
			return alternatePath;
		} else
			return path;
	}
}

