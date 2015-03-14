/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.transform.internal.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The implementation of {@link TransformationService} which transforms the
 * input by matching it between limits in a scale file
 *
 * @author Gaël L'hopital
 * @since 1.6.0
 */
public class ScaleTransformationService extends LocalizableTransformationService implements TransformationService {

	static final Logger logger = LoggerFactory.getLogger(ScaleTransformationService.class);

	/** RegEx to extract a scale definition */
	private static final Pattern limits_pattern = Pattern.compile("(\\[|\\])(.*)\\,(.*)(\\[|\\])\\=(.*)");

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
	String path = getLocalizedProposedFilename(filename);

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
				if (matcher.group(1).equals("]"))
					minLimit = minLimit + 0.0000000001;
				if (matcher.group(1).equals("["))
					minLimit = minLimit - 0.0000000001;
				if (matcher.group(4).equals("]"))
					maxLimit = maxLimit + 0.0000000001;
				if (matcher.group(4).equals("["))
					maxLimit = maxLimit - 0.0000000001;

				if ((minLimit < value) && (value < maxLimit)) {
					result = matcher.group(5);
					break;
				}
			} 
		}

		in.close();
	} catch (NumberFormatException e){
		// If it's not a number let's try it like a classical map
		// mainly for UnDefType value reason
		MapTransformationService map = new MapTransformationService();
		result = map.transform(filename, source);
		
	} catch (IOException e) {	
		throw new TransformationException("An error occured while scaling value ", e);
	}

	return result;
}

}
