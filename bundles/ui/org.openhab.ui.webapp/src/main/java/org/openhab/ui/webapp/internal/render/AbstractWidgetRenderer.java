/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.items.ItemUIRegistry;
import org.openhab.ui.webapp.internal.WebAppActivator;
import org.openhab.ui.webapp.render.RenderException;
import org.openhab.ui.webapp.render.WidgetRenderer;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an abstract implementation of a widget renderer. It provides
 * methods that are very useful for any widget renderer implementation,
 * so it should be subclassed by most concrete implementations.
 * 
 * @author Kai Kreuzer
 * @since 0.6.0
 *
 */
abstract public class AbstractWidgetRenderer implements WidgetRenderer {

	private final static Logger logger = LoggerFactory.getLogger(AbstractWidgetRenderer.class);

	/* the file extension of the images */
	protected static final String IMAGE_EXT = ".png";

	protected ItemUIRegistry itemUIRegistry;

	/* the file extension of the snippets */
	protected static final String SNIPPET_EXT = ".html";

	/* the snippet location inside this bundle */
	protected static final String SNIPPET_LOCATION = "snippets/";

	/* a local cache so we do not have to read the snippets over and over again from the bundle */
	protected static final Map<String, String> snippetCache = new HashMap<String, String>(); 

	public void setItemUIRegistry(ItemUIRegistry itemUIRegistry) {
		this.itemUIRegistry = itemUIRegistry;
	}
	
	public void unsetItemUIRegistry(ItemUIRegistry itemUIRegistry) {
		this.itemUIRegistry = null;
	}
	
	public ItemUIRegistry getItemUIRegistry() {
		return itemUIRegistry;
	}

	protected void activate(ComponentContext context) {
	}

	protected void deactivate(ComponentContext context) {
	}

	/**
	 * This method provides the html snippet for a given elementType of the sitemap model.
	 * 
	 * @param elementType the name of the model type (e.g. "Group" or "Switch")
	 * @return the html snippet to be used in the UI (including placeholders for variables)
	 * @throws RenderException if snippet could not be read 
	 */
	protected synchronized String getSnippet(String elementType) throws RenderException {
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
				throw new RenderException("Cannot find a snippet for element type '" + elementType + "'");
			}
		}
		return snippet;
	}
 
	/**
	 * Retrieves the label for a widget and formats it for the WebApp.Net framework
	 * 
	 * @param w the widget to retrieve the label for
	 * @return the label to use for the widget
	 */
	public String getLabel(Widget w) {

		String label = itemUIRegistry.getLabel(w);
		
		// insert the span between the left and right side of the label, if state section exists 
		label = label.replaceAll("\\[", "<span>").replaceAll("\\]", "</span>");

		return label;
	}

	/**
	 * Escapes the path part of a URL as defined in RFC2396. This means, that for example the
	 * path "/hello world" gets escaped to "/hello%20world".
	 *  
	 * @param path The path of the URL that has to be escaped
	 * @return The escaped path
	 */
	protected String escapeURLPath(String path) {
		try {
			return new URI(null, null, path, null).toString();
		} catch (URISyntaxException use) {
			logger.warn("Cannot escape path '{}' in URL. Returning unmodified path.", path);
			return path;
		}
	}
}
