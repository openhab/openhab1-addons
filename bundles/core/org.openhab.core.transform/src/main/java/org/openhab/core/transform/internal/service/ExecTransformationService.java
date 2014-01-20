/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.transform.internal.service;

import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationService;
import org.openhab.io.net.exec.ExecUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The implementation of {@link TransformationService} which transforms the
 * input by command line.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class ExecTransformationService implements TransformationService {

	static final Logger logger = 
		LoggerFactory.getLogger(ExecTransformationService.class);

	/**
	 * Transforms the input <code>source</code> by the command line.
	 * 
	 * @param commandLine
	 *            the command to execute. Command line should contain %s string,
	 *            which will be replaced by the input data.
	 * @param source
	 *            the input to transform
	 */
	public String transform(String commandLine, String source) throws TransformationException {

		if (commandLine == null || source == null) {
			throw new TransformationException(
					"the given parameters 'commandLine' and 'source' must not be null");
		}

		logger.debug("about to transform '{}' by the commanline '{}'", source, commandLine);

		long startTime = System.currentTimeMillis();
		
		commandLine = String.format(commandLine, source);
		String result = ExecUtil.executeCommandLineAndWaitResponse(commandLine, 5000);
		logger.trace("command line execution elapsed {} ms", System.currentTimeMillis() - startTime);

		return result;
	}

}
