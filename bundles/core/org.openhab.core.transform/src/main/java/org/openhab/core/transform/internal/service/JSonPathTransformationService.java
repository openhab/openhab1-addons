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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.InvalidPathException;


/**
 * <p>
 * The implementation of {@link TransformationService} which transforms the input by JSonPath Expressions.
 * </p>
 * 
 * @author Gaël L'hopital
 * @since 1.6.0
 */
public class JSonPathTransformationService implements TransformationService {

	static final Logger logger = LoggerFactory.getLogger(JSonPathTransformationService.class);

	/**
	 * @{inheritDoc
	 */
	public String transform(String JSonPathExpression, String source) throws TransformationException {

		if (JSonPathExpression == null || source == null) {
			throw new TransformationException("the given parameters 'JSonPath' and 'source' must not be null");
		}

		logger.debug("about to transform '{}' by the function '{}'", source, JSonPathExpression);

		try {
		   String transformationResult = JsonPath.read(source, JSonPathExpression);
		   logger.debug("transformation resulted in '{}'", transformationResult);
		   return transformationResult;
		} catch(InvalidPathException e) {
		   logger.error(e.getMessage());
		   return null;
		} 

	}

}
