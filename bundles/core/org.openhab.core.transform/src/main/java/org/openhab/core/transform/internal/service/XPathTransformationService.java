/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.transform.internal.service;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * <p>
 * The implementation of {@link TransformationService} which transforms the input by XPath Expressions.
 * </p>
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.7.0
 */
public class XPathTransformationService implements TransformationService {

	static final Logger logger = LoggerFactory.getLogger(XPathTransformationService.class);

	/**
	 * @{inheritDoc
	 */
	public String transform(String xpathExpression, String source) throws TransformationException {

		if (xpathExpression == null || source == null) {
			throw new TransformationException("the given parameters 'xpath' and 'source' must not be null");
		}

		logger.debug("about to transform '{}' by the function '{}'", source, xpathExpression);

		StringReader stringReader = null;

		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
			domFactory.setValidating(false);
			DocumentBuilder builder = domFactory.newDocumentBuilder();

			stringReader = new StringReader(source);
			InputSource inputSource = new InputSource(stringReader);
			inputSource.setEncoding("UTF-8");

			Document doc = builder.parse(inputSource);

			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath.compile(xpathExpression);

			String transformationResult = (String) expr.evaluate(doc, XPathConstants.STRING);

			logger.debug("transformation resulted in '{}'", transformationResult);

			return transformationResult;
		} catch (Exception e) {
			throw new TransformationException("transformation throws exceptions", e);
		} finally {
			if (stringReader != null) {
				stringReader.close();
			}
		}

	}

}
