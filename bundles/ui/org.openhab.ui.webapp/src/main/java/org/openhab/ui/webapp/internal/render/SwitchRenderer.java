/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.ui.webapp.internal.render;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.State;
import org.openhab.model.sitemap.Mapping;
import org.openhab.model.sitemap.Switch;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.webapp.internal.servlet.WebAppServlet;
import org.openhab.ui.webapp.render.RenderException;
import org.openhab.ui.webapp.render.WidgetRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an implementation of the {@link WidgetRenderer} interface, which
 * can produce HTML code for Switch widgets.
 * 
 * @author Kai Kreuzer
 * @since 0.6.0
 *
 */
public class SwitchRenderer extends AbstractWidgetRenderer {

	private static final Logger logger = LoggerFactory.getLogger(SwitchRenderer.class);
	
	/**
	 * {@inheritDoc}
	 */
	public boolean canRender(Widget w) {
		return w instanceof Switch;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public EList<Widget> renderWidget(Widget w, StringBuilder sb) throws RenderException {
		Switch s = (Switch) w;
		
		String snippetName = null;
		Item item;
		try {
			item = itemUIRegistry.getItem(w.getItem());
			if(s.getMappings().size()==0) {
				if(item instanceof RollershutterItem) {
					snippetName = "rollerblind";
				} else if (item instanceof GroupItem && ((GroupItem) item).getBaseItem() instanceof RollershutterItem) {
					snippetName = "rollerblind";
				} else {
					snippetName = "switch";
				}
			} else {
				snippetName = "buttons";
			}
		} catch (ItemNotFoundException e) {
			logger.warn("Cannot determine item type of '{}'", w.getItem(), e);
			snippetName = "switch";
		}

		String snippet = getSnippet(snippetName);

		snippet = StringUtils.replace(snippet, "%id%", itemUIRegistry.getWidgetId(w));
		snippet = StringUtils.replace(snippet, "%icon%", escapeURLPath(itemUIRegistry.getIcon(w)));
		snippet = StringUtils.replace(snippet, "%item%", w.getItem());
		snippet = StringUtils.replace(snippet, "%label%", getLabel(w));
		snippet = StringUtils.replace(snippet, "%servletname%", WebAppServlet.SERVLET_NAME);
		
		State state = itemUIRegistry.getState(w);
		
		if(s.getMappings().size()==0) {
			if(state instanceof PercentType) {
				state = ((PercentType) state).intValue() > 0 ? OnOffType.ON : OnOffType.OFF;
			}
			if(state.equals(OnOffType.ON)) {
				snippet = snippet.replaceAll("%checked%", "checked=true");
			} else {
				snippet = snippet.replaceAll("%checked%", "");
			}
		} else {
			StringBuilder buttons = new StringBuilder();
			for(Mapping mapping : s.getMappings()) {
				String button = getSnippet("button");
				button = StringUtils.replace(button, "%item%",w.getItem());
				button = StringUtils.replace(button, "%cmd%", mapping.getCmd());
				button = StringUtils.replace(button, "%label%", mapping.getLabel());
				if(s.getMappings().size()>1 && state.toString().equals(mapping.getCmd())) {
					button = StringUtils.replace(button, "%type%", "Warn"); // button with red color
				} else {
					button = StringUtils.replace(button, "%type%", "Action"); // button with blue color
				}
				buttons.insert(0, button);
			}
			snippet = StringUtils.replace(snippet, "%buttons%", buttons.toString());
		}
		
		// Process the color tags
		snippet = processColor(w, snippet);

		sb.append(snippet);
		return null;
	}
}
