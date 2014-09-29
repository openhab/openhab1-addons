/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.ui.webapp.internal.render;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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
		label = label.replaceAll("\\[", "<span style=\"%valuestyle%\">").replaceAll("\\]", "</span>");

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
	
	/**
	 * Process the color tags - labelcolor and valuecolor
	 * 
	 * @param w
	 *            The widget to process
	 * @param snippet
	 *            The snippet to translate
	 * @return The updated snippet
	 */
	protected String processColor(Widget w, String snippet) {
		String style = "";
		String color = itemUIRegistry.getLabelColor(w);
		if(color != null)
			style = "color:"+ color;
		snippet = StringUtils.replace(snippet, "%labelstyle%", style);

		style = "";
		color = itemUIRegistry.getValueColor(w);
		if(color != null)
			style = "color:"+ color;
		snippet = StringUtils.replace(snippet, "%valuestyle%", style);
		
		return snippet;
	}
}
