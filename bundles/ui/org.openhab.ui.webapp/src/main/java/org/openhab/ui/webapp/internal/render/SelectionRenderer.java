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

import org.eclipse.emf.common.util.EList;
import org.openhab.model.sitemap.Mapping;
import org.openhab.model.sitemap.Selection;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.webapp.render.RenderException;

/**
 * This is an implementation of the {@link WidgetRenderer} interface, which
 * can produce HTML code for Selection widgets.
 * 
 * @author Kai Kreuzer
 * @since 0.6.0
 *
 */
public class SelectionRenderer extends AbstractWidgetRenderer {
	
	/**
	 * {@inheritDoc}
	 */
	public boolean canRender(Widget w) {
		return w instanceof Selection;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public EList<Widget> renderWidget(Widget w, StringBuilder sb) throws RenderException {
		String snippet = getSnippet("selection");

		snippet = snippet.replaceAll("%icon%", itemUIRegistry.getIcon(w));
		snippet = snippet.replace("%label_header%", getLabel(w));
		
		String state = itemUIRegistry.getState(w).toString();
		Selection selection = (Selection) w;
		
		StringBuilder rowSB = new StringBuilder();
		for(Mapping mapping : selection.getMappings()) {
			String rowSnippet = getSnippet("selection_row");
			rowSnippet = rowSnippet.replace("%item%", w.getItem());
			rowSnippet = rowSnippet.replace("%cmd%", mapping.getCmd());
			rowSnippet = rowSnippet.replace("%label%", mapping.getLabel());
			if(mapping.getCmd().equals(state)) {
				rowSnippet = rowSnippet.replace("%checked%", "checked=\"true\"");
			} else {
				rowSnippet = rowSnippet.replace("%checked%", "");
			}
			rowSB.append(rowSnippet);
		}
		snippet = snippet.replace("%rows%", rowSB.toString());
		
		sb.append(snippet);
		return null;
	}
}
