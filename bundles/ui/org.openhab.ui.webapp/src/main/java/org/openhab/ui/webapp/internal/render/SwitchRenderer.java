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

package org.openhab.ui.webapp.internal.render;

import org.eclipse.emf.common.util.EList;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemNotUniqueException;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.State;
import org.openhab.model.sitemap.Switch;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.webapp.internal.servlet.WebAppServlet;
import org.openhab.ui.webapp.render.RenderException;
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
	@Override
	public boolean canRender(Widget w) {
		return w instanceof Switch;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public EList<Widget> renderWidget(Widget w, StringBuilder sb) throws RenderException {
		String snippetName = null;

		Item item;
		try {
			item = itemRegistry.getItem(getItem(w));
			if(item instanceof RollershutterItem) {
				snippetName = "rollerblind";
			} else {
				snippetName = "switch";
			}
		} catch (ItemNotFoundException e) {
			logger.warn("Cannot determine item type of '{}'", getItem(w), e);
			snippetName = "switch";
		} catch (ItemNotUniqueException e) {
			logger.warn("Cannot determine item type of '{}'", getItem(w), e);
			snippetName = "switch";
		}

		String snippet = getSnippet(snippetName);

		snippet = snippet.replaceAll("%id%", getWidgetId(w));
		snippet = snippet.replaceAll("%icon%", getIcon(w));
		snippet = snippet.replaceAll("%item%", getItem(w));
		snippet = snippet.replaceAll("%label%", getLabel(w));
		snippet = snippet.replaceAll("%servletname%", WebAppServlet.SERVLET_NAME);
		
		State state = getState(w);
		if(state.equals(OnOffType.ON)) {
			snippet = snippet.replaceAll("%checked%", "checked=true");
		} else {
			snippet = snippet.replaceAll("%checked%", "");
		}
		
		sb.append(snippet);
		return null;
	}
}
