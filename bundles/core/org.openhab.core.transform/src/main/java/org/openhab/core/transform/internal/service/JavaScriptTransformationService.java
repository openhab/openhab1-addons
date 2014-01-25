/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.transform.internal.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;
import org.openhab.config.core.ConfigDispatcher;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.transform.internal.TransformationActivator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The implementation of {@link TransformationService} which transforms the
 * input by Java Script.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class JavaScriptTransformationService implements TransformationService {

	static final Logger logger = 
		LoggerFactory.getLogger(JavaScriptTransformationService.class);
	
	/**
	 * Transforms the input <code>source</code> by Java Script. It expects the
	 * transformation rule to be read from a file which is stored under the
	 * 'configurations/transform' folder. To organize the various
	 * transformations one should use subfolders.
	 * 
	 * @param filename
	 *            the name of the file which contains the Java script
	 *            transformation rule. Transformation service inject input
	 *            (source) to 'input' variable.
	 * @param source
	 *            the input to transform
	 */
	public String transform(String filename, String source) throws TransformationException {

		if (filename == null || source == null) {
			throw new TransformationException(
				"the given parameters 'filename' and 'source' must not be null");
		}

		logger.debug("about to transform '{}' by the Java Script '{}'", source, filename);

		Reader reader;

		try {
			String path = ConfigDispatcher.getConfigFolder() 
				+ File.separator + TransformationActivator.TRANSFORM_FOLDER_NAME
				+ File.separator + filename;
			reader = new InputStreamReader(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			throw new TransformationException("An error occured while loading script.", e);
		}

		ScriptEngineManager manager = new ScriptEngineManager();
		
		ScriptEngine engine = manager.getEngineByName("javascript");
		engine.put("input", source);

		Object result = null;

		long startTime = System.currentTimeMillis();

		try {
			result = engine.eval(reader);
		} catch (ScriptException e) {
			throw new TransformationException("An error occured while executing script.", e);
		} finally {
			IOUtils.closeQuietly(reader);
		}

		logger.trace("JavaScript execution elapsed {} ms", System.currentTimeMillis() - startTime);

		return String.valueOf(result);
	}

}
