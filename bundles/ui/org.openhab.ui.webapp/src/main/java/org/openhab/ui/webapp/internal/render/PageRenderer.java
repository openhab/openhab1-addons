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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.openhab.model.sitemap.Frame;
import org.openhab.model.sitemap.Sitemap;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.webapp.internal.servlet.WebAppServlet;
import org.openhab.ui.webapp.render.RenderException;
import org.openhab.ui.webapp.render.WidgetRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an implementation of the {@link WidgetRenderer} interface, which
 * is the main entry point for HTML code construction.
 * 
 * It provides the HTML header and skeleton and delegates the rendering of
 * widgets on the page to the dedicated widget renderers.
 * 
 * @author Kai Kreuzer
 * @since 0.6.0
 *
 */
public class PageRenderer extends AbstractWidgetRenderer {

	private final static Logger logger = LoggerFactory.getLogger(PageRenderer.class);

	List<WidgetRenderer> widgetRenderers = new ArrayList<WidgetRenderer>();

	public void addWidgetRenderer(WidgetRenderer widgetRenderer) {
		widgetRenderers.add(widgetRenderer);
	}

	public void removeWidgetRenderer(WidgetRenderer widgetRenderer) {
		widgetRenderers.remove(widgetRenderer);
	}

	/**
	 * This is the main method, which is called to produce the HTML code for a servlet request.
	 * 
	 * @param id the id of the parent widget whose children are about to appear on this page
	 * @param sitemap the sitemap to use
	 * @param label the title of this page
	 * @param children a list of widgets that should appear on this page
	 * @param async true, if this is an asynchronous request. This will use a different HTML skeleton
	 * @return a string builder with the produced HTML code
	 * @throws RenderException if an error occurs during the processing
	 */
	public StringBuilder processPage(String id, String sitemap, String label, EList<Widget> children, boolean async) throws RenderException {
		
		String snippet = getSnippet(async ? "layer" : "main");
		snippet = snippet.replaceAll("%id%", id);

		// if the label contains a value span, we remove this span as
		// the title of a page/layer cannot deal with this
		// Note: we can have a span here, if the parent widget had a label
		// with some value defined (e.g. "Windows [%d]"), which getLabel()
		// will convert into a "Windows <span>5</span>".
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
			EList<Widget> children) throws RenderException {
		
		// put a single frame around all children widgets, if there are no explicit frames 
		if(!children.isEmpty()) {
			EObject firstChild = children.get(0);
			EObject parent = firstChild.eContainer();
			if(!(firstChild instanceof Frame || parent instanceof Frame || parent instanceof Sitemap || parent instanceof List)) {
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
			EList<Widget> nextChildren = renderWidget(w, widgetSB);
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

	/**
	 * {@inheritDoc}
	 */
	public EList<Widget> renderWidget(Widget w, StringBuilder sb) throws RenderException {
		for(WidgetRenderer renderer : widgetRenderers) {
			if(renderer.canRender(w)) {
				return renderer.renderWidget(w, sb);
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canRender(Widget w) {
		return false;		
	}
}
