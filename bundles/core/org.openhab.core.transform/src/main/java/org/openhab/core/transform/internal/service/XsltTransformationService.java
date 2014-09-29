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
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.openhab.config.core.ConfigDispatcher;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.transform.internal.TransformationActivator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * The implementation of {@link TransformationService} which transforms the input by XSLT.
 * </p>
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.7.0
 */
public class XsltTransformationService implements TransformationService {

	static final Logger logger = LoggerFactory.getLogger(XsltTransformationService.class);

	/**
	 * <p>
	 * Transforms the input <code>source</code> by XSLT. It expects the transformation rule to be read from a file which
	 * is stored under the 'configurations/transform' folder. To organize the various transformations one should use
	 * subfolders.
	 * </p>
	 * 
	 * @param filename
	 *            the name of the file which contains the XSLT transformation rule. The name may contain subfoldernames
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

		Source xsl = null;

		try {
			String path = ConfigDispatcher.getConfigFolder() + File.separator + TransformationActivator.TRANSFORM_FOLDER_NAME + File.separator + filename;
			xsl = new StreamSource(new File(path));
		} catch (Exception e) {
			String message = "opening file '" + filename + "' throws exception";

			logger.error(message, e);
			throw new TransformationException(message, e);
		}

		logger.debug("about to transform '{}' by the function '{}'", source, xsl);

		StringReader xml = new StringReader(source);
		StringWriter out = new StringWriter();

		Transformer transformer;

		try {
			transformer = TransformerFactory.newInstance().newTransformer(xsl);
			transformer.transform(new StreamSource(xml), new StreamResult(out));
		} catch (Exception e) {
			logger.error("transformation throws exception", e);
			throw new TransformationException("transformation throws exception", e);
		}

		logger.debug("transformation resulted in '{}'", out.toString());

		return out.toString();
	}

}
