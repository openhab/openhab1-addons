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

package org.openhab.core.transform.service;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>The implementation of {@link TransformationService} which transforms the
 * input by XSLT.</p>

 * @author Thomas.Eichstaedt-Engelen
 * @since 0.7.0
 */
public class XsltTransformationService implements TransformationService {

	static final Logger logger = LoggerFactory.getLogger(XsltTransformationService.class);
	
	
	/**
	 * <p>Transforms the input <code>source</code> by XSLT. It expects the 
	 * transformation rule to be read from a file which is stored under the 
	 * 'configurations/transform' folder. To organize the various 
	 * transformations one should use subfolders.
	 * </p>
	 * 
	 * @param filename the name of the file which contains the XSLT transformation
	 * rule. The name may contain subfoldernames as well
	 * @param source the input to transform
	 * 
	 * @{inheritDoc}
	 * 
	 */
	@Override
	public String transform(String filename, String source) throws TransformationException {
				
		if (filename == null || source == null) {
			throw new TransformationException("the given parameters 'filename' and 'source' must not be null");
		}
		
		Source xsl = null;
		
		try {
			String path = ConfigDispatcher.getConfigFolder() + File.pathSeparator + filename;
			xsl = new StreamSource(new File(path));			
		}
		catch (Exception e) {
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
		}
		catch (Exception e) {
			logger.error("transformation throws exception", e);
			throw new TransformationException("transformation throws exception", e);
		}
		
		logger.debug("transformation resulted in '{}'", out.toString());

		return out.toString();
	}

	@Override
	public String getName() {
		return "XSLT";
	}
	

}
