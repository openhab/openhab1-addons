/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.transform.internal.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.openhab.config.core.ConfigDispatcher;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.transform.internal.TransformationActivator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The implementation of {@link TransformationService} which transforms the
 * input by matching it between limits in a scale file
 *
 * @author Gaël L'hopital
 * @since 1.6.0
 */
public class ScaleTransformationService implements TransformationService {

	static final Logger logger =
	LoggerFactory.getLogger(ScaleTransformationService.class);

	/** RegEx to extract a scale definition */
	private static final Pattern limits_pattern =
	Pattern.compile("(\\[|\\])(.*)\\,(.*)(\\[|\\])\\=(.*)");

	/**
	 * <p>
	 * Transforms the input <code>source</code> by matching it to another string. It expects the scaling to be read from a file which
	 * is stored under the 'configurations/transform' folder. This file should be in property syntax, i.e.
	 * [min,max]=value or ]min,max]=value
	 * </p>
	 *
	 * @param filename
	 * the name of the file which contains the key value pairs for the mapping. The name may contain subfoldernames
	 * as well
	 * @param source
	 * the input to transform
	 *
	 * @{inheritDoc}
	 *
	 */
	public String transform(String filename, String source) throws TransformationException {

	if (filename == null || source == null) {
		throw new TransformationException("the given parameters 'filename' and 'source' must not be null");
	}
	

	String result = "not found";
	String basename = FilenameUtils.getBaseName(filename);
	String extension = FilenameUtils.getExtension(filename);
	String locale = Locale.getDefault().getLanguage();
	String basePath = ConfigDispatcher.getConfigFolder() + File.separator + TransformationActivator.TRANSFORM_FOLDER_NAME + File.separator;
	String path = basePath + filename;
	// eg : /home/sysadmin/projects/openhab/distribution/openhabhome/configurations/transform/test.scale
	String alternatePath = basePath + basename + "_" + locale + "." + extension;
	// eg : /home/sysadmin/projects/openhab/distribution/openhabhome/configurations/transform/test-en.scale

	File f = new File(alternatePath);
	if (f.exists()) {
		path = alternatePath;
	}
	logger.debug("Using scale file '{}'",path);

	try{
		double value = Double.parseDouble(source);
		FileInputStream fstream = new FileInputStream(path);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String strLine;
		while ((strLine = br.readLine()) != null) {
			Matcher matcher = limits_pattern.matcher(strLine);
			if (matcher.matches() && (matcher.groupCount()==5)) {
				double minLimit = Double.parseDouble(matcher.group(2));
				double maxLimit = Double.parseDouble(matcher.group(3));

				// a bit of a trick to include/exclude limits of the segment
				if (matcher.group(1).equals(']'))
					minLimit = minLimit - 0.0000000001;
				if (matcher.group(1).equals('['))
					minLimit = minLimit + 0.0000000001;

				if ((minLimit < value) && (value < maxLimit)) {
					result = matcher.group(5);
					break;
				}

			} else {
				logger.warn("Line '{}' does not match scale pattern in the file '{}'.",strLine,path );
			}

		}

		in.close();
	} catch (NumberFormatException e){
		logger.warn("Scale transform can only work with numeric values, '{}' is not ", source);
	} catch (IOException e) {	
		throw new TransformationException("An error occured while scaling value ", e);
	}

	return result;
}

}
