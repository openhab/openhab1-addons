/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
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

package org.openhab.core.transform.processor;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


/**
 * <p>The implementation of {@link TransformationProcessor} which transforms the
 * input by XPath Expressions.</p>
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.7.0
 */
public class XPathTransformationProcessor implements TransformationProcessor {

	static final Logger logger = LoggerFactory.getLogger(XPathTransformationProcessor.class);
	
	
	/**
	 * @{inheritDoc}
	 */
	@Override
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
		}
		catch (Exception e) {
			throw new TransformationException("transformation throws exceptions", e);
		}
		finally {
			if (stringReader != null) {
				stringReader.close();
			}
		}

	}
	

}
