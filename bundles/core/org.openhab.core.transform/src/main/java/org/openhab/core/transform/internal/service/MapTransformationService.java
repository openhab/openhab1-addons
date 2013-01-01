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
package org.openhab.core.transform.internal.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.openhab.config.core.ConfigDispatcher;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.transform.internal.TransformationActivator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * The implementation of {@link TransformationService} which simply maps strings to other strings
 * </p>
 * 
 * @author Kai Kreuzer
 * @since 0.8.0
 */
public class MapTransformationService implements TransformationService {

	static final Logger logger = LoggerFactory.getLogger(MapTransformationService.class);

	/**
	 * <p>
	 * Transforms the input <code>source</code> by mapping it to another string. It expects the mappings to be read from a file which
	 * is stored under the 'configurations/transform' folder. This file should be in property syntax, i.e. simple lines with "key=value" pairs.
	 * To organize the various transformations one might use subfolders.
	 * </p>
	 * 
	 * @param filename
	 *            the name of the file which contains the key value pairs for the mapping. The name may contain subfoldernames
	 *            as well
	 * @param source
	 *            the input to transform
	 * 
	 * @{inheritDoc}
	 * 
	 */
	public String transform(String filename, String source) throws TransformationException {

		if (filename == null || source == null) {
			throw new TransformationException("the given parameters 'filename' and 'source' must not be null");
		}

		Reader reader = null;
		try {
			String path = ConfigDispatcher.getConfigFolder() + File.separator + TransformationActivator.TRANSFORM_FOLDER_NAME + File.separator + filename;
			Properties properties = new Properties();
			reader = new FileReader(path);
			properties.load(reader);
			String target = properties.getProperty(source);
			if(target!=null) {
				logger.debug("transformation resulted in '{}'", target);
				return target;
			} else {
				logger.warn("Could not find a mapping for '{}' in the file '{}'.", source, filename);
				return "";
			}
		} catch (IOException e) {
			String message = "opening file '" + filename + "' throws exception";
			logger.error(message, e);
			throw new TransformationException(message, e);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

}
