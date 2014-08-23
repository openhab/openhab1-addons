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
		
		try {
			String itemParam = null;
			Item item = itemUIRegistry.getItem(chart.getItem());
			if(item instanceof GroupItem) {
				itemParam = "groups=" + chart.getItem();
			} else {
				itemParam = "items=" + chart.getItem();
			}
			
			String url = "/chart?" + itemParam + "&period=" + chart.getPeriod() + "&random=1";
			if(chart.getService() != null)
				url += "&service=" + chart.getService();
			
			String snippet = getSnippet("image");			

			if(chart.getRefresh()>0) {
				snippet = StringUtils.replace(snippet, "%setrefresh%", "<script type=\"text/javascript\">imagesToRefreshOnPage=1</script>");
				snippet = StringUtils.replace(snippet, "%refresh%", "id=\"%id%\" onload=\"setTimeout('reloadImage(\\'%url%\\', \\'%id%\\')', " + chart.getRefresh() + ")\"");
			} else {
				snippet = StringUtils.replace(snippet, "%setrefresh%", "");
				snippet = StringUtils.replace(snippet, "%refresh%", "");
			}

			snippet = StringUtils.replace(snippet, "%id%", itemUIRegistry.getWidgetId(w));
			snippet = StringUtils.replace(snippet, "%url%", url);
			snippet = StringUtils.replace(snippet, "%refresh%", Integer.toString(chart.getRefresh()));
			
			sb.append(snippet);
		} catch (ItemNotFoundException e) {
			logger.warn("Chart cannot be rendered as item '{}' does not exist.", chart.getItem());
		}
		return null;
	}
}
