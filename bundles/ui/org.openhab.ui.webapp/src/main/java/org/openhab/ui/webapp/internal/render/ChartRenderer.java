/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.model.sitemap.Chart;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.webapp.render.RenderException;
import org.openhab.ui.webapp.render.WidgetRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an implementation of the {@link WidgetRenderer} interface, which
 * can produce HTML code for Chart widgets.
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 *
 */
public class ChartRenderer extends AbstractWidgetRenderer {
	
	static final private Logger logger = LoggerFactory.getLogger(ChartRenderer.class);
	
	/**
	 * {@inheritDoc}
	 */
	public boolean canRender(Widget w) {
		return w instanceof Chart;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public EList<Widget> renderWidget(Widget w, StringBuilder sb) throws RenderException {
		Chart chart = (Chart) w;

		if(chart.getService()!=null && !chart.getService().equals("rrd4j")) {
			logger.warn("Chart service '{}' is not supported by the Classic UI, using rrd4j as fallback instead.");
		}
		
		try {
			String itemParam = null;
			Item item = itemUIRegistry.getItem(chart.getItem());
			if(item instanceof GroupItem) {
				itemParam = "groups=" + chart.getItem();
			} else {
				itemParam = "items=" + chart.getItem();
			}
			
			String url = "/rrdchart.png?" + itemParam + "&period=" + chart.getPeriod() + "&random=1";
			
			String snippet = getSnippet("image");			

			if(chart.getRefresh()>0) {
				snippet = StringUtils.replace(snippet, "%setrefresh%", "<script type=\"text/javascript\">imagesToRefreshOnPage=1</script>");
				snippet = StringUtils.replace(snippet, "%refresh%", "id=\"%id%\" onload=\"setTimeout('reloadImage(\\'%url%\\', \\'%id%\\')', " + chart.getRefresh() + ")\"");
			} else {
				snippet = snippet.replaceAll("%setrefresh%", "");
				snippet = snippet.replaceAll("%refresh%", "");
			}

			snippet = snippet.replaceAll("%id%", itemUIRegistry.getWidgetId(w));
			snippet = snippet.replaceAll("%url%", url);
			snippet = snippet.replaceAll("%refresh%", Integer.toString(chart.getRefresh()));
			
			sb.append(snippet);
		} catch (ItemNotFoundException e) {
			logger.warn("Chart cannot be rendered as item '{}' does not exist.", chart.getItem());
		}
		return null;
	}
}
