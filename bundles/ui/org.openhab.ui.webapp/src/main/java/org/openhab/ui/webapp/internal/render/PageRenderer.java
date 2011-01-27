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

import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemNotUniqueException;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.State;
import org.openhab.model.sitemap.Frame;
import org.openhab.model.sitemap.Group;
import org.openhab.model.sitemap.Image;
import org.openhab.model.sitemap.LinkableWidget;
import org.openhab.model.sitemap.List;
import org.openhab.model.sitemap.Selection;
import org.openhab.model.sitemap.Sitemap;
import org.openhab.model.sitemap.Switch;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.webapp.internal.WebAppService;
import org.openhab.ui.webapp.internal.servlet.WebAppServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageRenderer extends DefaultWidgetRenderer {

	private final static Logger logger = LoggerFactory.getLogger(PageRenderer.class);
	private WebAppService service;

	public PageRenderer(WebAppService service) {
		this.service = service;
	}

	public StringBuilder processPage(String id, String sitemap, String label, EList<Widget> children, boolean async) throws IOException, ServletException {
		
		String snippet = getSnippet(async ? "layer" : "main");
		snippet = snippet.replaceAll("%id%", id);

		// if the label contains a value span, we remove this span as
		// the title of a page/layer cannot deal with this
		if(label.contains("<span>")) {
			label = label.substring(0, label.indexOf("<span>"));
		}
		snippet = snippet.replaceAll("%label%", label);
		snippet = snippet.replaceAll("%servletname%", WebAppServlet.SERVLET_NAME);
		snippet = snippet.replaceAll("%sitemap%", sitemap);

		String[] parts = snippet.split("%children%");

		StringBuilder pre_children = new StringBuilder(parts[0]);
		StringBuilder post_children = new StringBuilder(parts[1]);
		
		if(parts.length==2) {
			processChildren(pre_children, post_children, children);
		} else if(parts.length > 2){
			logger.error("Snippet '{}' contains multiple %children% sections, but only one is allowed!", async ? "layer" : "main");
		}
		return pre_children.append(post_children);
	}

	private void processChildren(StringBuilder sb_pre, StringBuilder sb_post,
			EList<Widget> children) throws IOException, ServletException {
		
		// put a single frame around all children widgets, if there are no explicit frames 
		if(!children.isEmpty()) {
			EObject firstChild = children.get(0).eContainer();
			if(!(firstChild instanceof Frame || firstChild instanceof Sitemap || firstChild instanceof List)) {
				String frameSnippet = getSnippet("frame");
				frameSnippet = frameSnippet.replace("%label%", "");
				
				String[] parts = frameSnippet.split("%children%");
				if(parts.length>1) {
					sb_pre.append(parts[0]);
				}
				if(parts.length>2) {
					sb_post.insert(0, parts[1]);
				} 
				if(parts.length > 2){
					logger.error("Snippet 'frame' contains multiple %children% sections, but only one is allowed!");
				}
			}
		}

		for(Widget w : children) {
			StringBuilder new_pre = new StringBuilder();
			StringBuilder new_post = new StringBuilder();
			StringBuilder widgetSB = new StringBuilder();
			EList<Widget> nextChildren = processWidget(w, widgetSB);
			if(nextChildren!=null) {
				String[] parts = widgetSB.toString().split("%children%");
				// no %children% placeholder found or at the end
				if(parts.length==1) {
					new_pre.append(widgetSB);
				}
				// %children% section found 
				if(parts.length>1) {
					new_pre.append(parts[0]);
					new_post.insert(0, parts[1]);
				} 
				// multiple %children% sections found -> log an error and ignore all code starting from the second occurance
				if(parts.length > 2){
					String widgetType = w.eClass().getInstanceTypeName().substring(w.eClass().getInstanceTypeName().lastIndexOf(".")+1);
					logger.error("Snippet for widget '{}' contains multiple %children% sections, but only one is allowed!", widgetType);
				}
				processChildren(new_pre, new_post, nextChildren);
				sb_pre.append(new_pre);
				sb_pre.append(new_post);
			} else {
				sb_pre.append(widgetSB);
			}
		}
		
	}

	private EList<Widget> processWidget(Widget w, StringBuilder sb) throws IOException, ServletException {
		String snippetName;
		if(w instanceof Switch) {
			Item item;
			try {
				item = service.getItemRegistry().getItem(service.getItem(w));
				if(item instanceof RollershutterItem) {
					snippetName = "rollerblind";
				} else {
					snippetName = "switch";
				}
			} catch (ItemNotFoundException e) {
				logger.warn("Cannot determine item type of '{}'", service.getItem(w), e);
				snippetName = "switch";
			} catch (ItemNotUniqueException e) {
				logger.warn("Cannot determine item type of '{}'", service.getItem(w), e);
				snippetName = "switch";
			}
		} else {
			// for all others, we choose the snippet with the name of the instance
			snippetName = w.eClass().getInstanceTypeName().substring(w.eClass().getInstanceTypeName().lastIndexOf(".")+1);
		}
		
		if(!(w instanceof Frame || w instanceof Group) && 
			(w instanceof LinkableWidget) && ((LinkableWidget)w).getChildren().size() > 0) {
			snippetName += "_link";
		}
		String snippet = getSnippet(snippetName);

		snippet = snippet.replaceAll("%id%", service.getWidgetId(w));
		snippet = snippet.replaceAll("%icon%", service.getIcon(w));
		snippet = snippet.replaceAll("%item%", service.getItem(w));
		snippet = snippet.replaceAll("%label%", service.getLabel(w));
		snippet = snippet.replaceAll("%servletname%", WebAppServlet.SERVLET_NAME);
		
		if(w instanceof Switch) {
			State state = service.getState(w);
			if(state.equals(OnOffType.ON)) {
				snippet = snippet.replaceAll("%checked%", "checked=true");
			} else {
				snippet = snippet.replaceAll("%checked%", "");
			}
		}
		
		if(w instanceof Image) {
			snippet = snippet.replaceAll("%url%", ((Image) w).getUrl());
		}
		
		if(w instanceof List) {
			String rowSnippet = getSnippet("list_row");
			String state = service.getState(w).toString();
			String[] rowContents = state.split(((List) w).getSeparator());
			StringBuilder rowSB = new StringBuilder();
			for(String row : rowContents) {
				rowSB.append(rowSnippet.replace("%title%", row));
			}
			snippet = snippet.replace("%rows%", rowSB.toString());
		}

		if(w instanceof Selection) {
			String state = service.getState(w).toString();
			String[] labels = service.getLabel(w).split(";");
			if(labels.length > 0) {
				snippet = snippet.replace("%label_header%", labels[0]);
				StringBuilder rowSB = new StringBuilder();
				for(int i=1; i<labels.length; i++) {
					String valuelabel = labels[i];
					String rowSnippet = getSnippet("selection_row");
					String value = StringUtils.substringBefore(valuelabel, "=");
					String label = StringUtils.substringAfter(valuelabel, "=");
					rowSnippet = rowSnippet.replace("%item%", service.getItem(w));
					rowSnippet = rowSnippet.replace("%value%", value);
					rowSnippet = rowSnippet.replace("%label%", label);
					if(value.equals(state)) {
						rowSnippet = rowSnippet.replace("%checked%", "checked=\"true\"");
					} else {
						rowSnippet = rowSnippet.replace("%checked%", "");
					}
					rowSB.append(rowSnippet);
				}
				snippet = snippet.replace("%rows%", rowSB.toString());
			}
		}
		sb.append(snippet);
		if(w instanceof Frame) {
			return service.getChildren((Frame)w);
		} else {
			return null;
		}
	}
}
