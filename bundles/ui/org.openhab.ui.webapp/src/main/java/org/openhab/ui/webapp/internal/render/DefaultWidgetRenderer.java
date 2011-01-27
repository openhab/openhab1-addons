/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
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

package org.openhab.ui.webapp.internal.render;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.openhab.ui.webapp.internal.WebAppActivator;
import org.openhab.ui.webapp.render.WidgetRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultWidgetRenderer implements WidgetRenderer {

	private final static Logger logger = LoggerFactory.getLogger(DefaultWidgetRenderer.class);

	/* the file extension of the snippets */
	private static final String SNIPPET_EXT = ".html";

	/* the snippet location inside this bundle */
	private static final String SNIPPET_LOCATION = "snippets/";

	/* a local cache so we do not have to read the snippets over and over again from the bundle */
	private static final Map<String, String> snippetCache = new HashMap<String, String>(); 
	
	/**
	 * This method provides the html snippet for a given elementType of the sitemap model.
	 * 
	 * @param elementType the name of the model type (e.g. "Group" or "Switch")
	 * @return the html snippet to be used in the UI (including placeholders for variables)
	 * @throws ServletException 
	 */
	protected synchronized String getSnippet(String elementType) throws ServletException {
		elementType = elementType.toLowerCase();
		String snippet = snippetCache.get(elementType);
		if(snippet==null) {
			String snippetLocation = SNIPPET_LOCATION + elementType + SNIPPET_EXT;
			URL entry = WebAppActivator.getContext().getBundle().getEntry(snippetLocation);
			if(entry!=null) {
				try {
					snippet = IOUtils.toString(entry.openStream());
					snippetCache.put(elementType, snippet);
				} catch (IOException e) {
					logger.warn("Cannot load snippet for element type '{}'", elementType, e);
				}
			} else {
				throw new ServletException("Cannot find a snippet for element type '" + elementType + "'");
			}
		}
		return snippet;
	}

}
