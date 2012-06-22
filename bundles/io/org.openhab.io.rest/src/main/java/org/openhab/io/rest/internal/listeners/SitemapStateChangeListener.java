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
package org.openhab.io.rest.internal.listeners;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.openhab.core.items.Item;
import org.openhab.io.rest.internal.RESTApplication;
import org.openhab.io.rest.internal.resources.MediaTypeHelper;
import org.openhab.io.rest.internal.resources.SitemapResource;
import org.openhab.io.rest.internal.resources.beans.PageBean;
import org.openhab.io.rest.internal.resources.beans.WidgetBean;
import org.openhab.io.rest.internal.resources.beans.WidgetListBean;
import org.openhab.model.sitemap.Frame;
import org.openhab.model.sitemap.LinkableWidget;
import org.openhab.model.sitemap.Sitemap;
import org.openhab.model.sitemap.Widget;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.sun.jersey.api.json.JSONWithPadding;

/**
 * This is the {@link ResourceStateChangeListener} implementation for sitemap REST requests.
 * Note: We only support suspended requests for page requests, not for complete sitemaps.
 * 
 * @author Kai Kreuzer
 * @author Oliver Mazur
 * @since 0.9.0
 *
 */
public class SitemapStateChangeListener extends ResourceStateChangeListener {

	private static final Logger logger = LoggerFactory.getLogger(ResourceStateChangeListener.class);
	
	@Override
	protected Object getResponseObject(HttpServletRequest request) {
		String responseType = getResponseType(request);
		PageBean pageBean = getPageBean(request);
		if(pageBean!=null) {
	    	Object responseObject = responseType.equals(MediaTypeHelper.APPLICATION_X_JAVASCRIPT) ?
	    			new JSONWithPadding(pageBean, getQueryParam(request, "callback")) : pageBean;
	    	return Response.ok(responseObject, responseType).build();
    	}
		return null;
	}
		
	@Override
	protected Object getSingleResponseObject(Item item, HttpServletRequest request) {
		String responseType = getResponseType(request);
		PageBean pageBean = getPageBean(request);
		WidgetListBean responseBeam ;
		if(pageBean!=null) {
			responseBeam = new WidgetListBean( getItemsOnPage(pageBean.widgets, item));
			Object responseObject = responseType.equals(MediaTypeHelper.APPLICATION_X_JAVASCRIPT) ?
	    			new JSONWithPadding(responseBeam, getQueryParam(request, "callback")) : responseBeam;  			
	    	return Response.ok(responseObject, responseType).build();
	    	
    	}
		return null;
	}


	@Override
	protected Set<String> getRelevantItemNames(String pathInfo) {

        // check, if it is a request for a page of a sitemap 
        if (pathInfo.startsWith("/" + SitemapResource.PATH_SITEMAPS)) {
        	String[] pathSegments = pathInfo.substring(1).split("/");

            if(pathSegments.length>=3) {
            	String sitemapName = pathSegments[1];
            	String pageName = pathSegments[2];

            	Sitemap sitemap = (Sitemap) RESTApplication.getModelRepository().getModel(sitemapName + ".sitemap");
            	if(sitemap!=null) {
            		List<Widget> children = null;
            		if(pageName.equals(sitemapName)) {
            			children = sitemap.getChildren();
            		} else {            		
	            		Widget widget = RESTApplication.getItemUIRegistry().getWidget(sitemap, pageName);
	            		if(widget instanceof LinkableWidget) {
	            			LinkableWidget page = (LinkableWidget) widget;
	            			children = RESTApplication.getItemUIRegistry().getChildren(page);
	            		}
            		}
            		if(children!=null) {
	            		return getRelevantItemNamesForWidgets(children);
            		}
				}
            }
        }
        return new HashSet<String>();
	}

	private Set<String> getRelevantItemNamesForWidgets(List<Widget> children) {
		Set<String> itemNames = new HashSet<String>();
		for(Widget child : children) {
			if (child instanceof Frame) {
				Frame frame = (Frame) child;
				itemNames.addAll(getRelevantItemNamesForWidgets(frame.getChildren()));
			} else {
				String itemName = child.getItem();
				if(itemName!=null) {
					itemNames.add(itemName);
				}
			}
		}
		return itemNames;
	}
	
	private PageBean getPageBean(HttpServletRequest request){
		String pathInfo = request.getPathInfo();
		String responseType = getResponseType(request);
		if(responseType!=null) {
			URI basePath = UriBuilder.fromUri(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+(request.getContextPath().equals("null")?"":request.getContextPath()) + RESTApplication.REST_SERVLET_ALIAS +"/").build();
			if (pathInfo.startsWith("/" + SitemapResource.PATH_SITEMAPS)) {
	        	String[] pathSegments = pathInfo.substring(1).split("/");
	            if(pathSegments.length>=3) {
	            	String sitemapName = pathSegments[1];
	            	String pageId = pathSegments[2];
	            	Sitemap sitemap = (Sitemap) RESTApplication.getModelRepository().getModel(sitemapName + ".sitemap");
	            	if(sitemap!=null) {
						return SitemapResource.getPageBean(sitemapName, pageId, basePath);
	            	}
	            }
	        }
		}
		return null;
		
	}
	
	private List <WidgetBean> getItemsOnPage(List<WidgetBean> widgets, Item searchItem){
		List <WidgetBean> foundWidgets = new ArrayList <WidgetBean>();
		try{
		for(WidgetBean widget : widgets) {	
			if(widget.item !=null && widget.item.name.equals(searchItem.getName())){
				foundWidgets.add(widget);
			}
			else{
				if (!widget.widgets.isEmpty()){
					List <WidgetBean> tmpWidgets =  getItemsOnPage(widget.widgets, searchItem);
					if(!tmpWidgets.isEmpty()) {
						foundWidgets.addAll(tmpWidgets); }
					
				}
			}
			
			if (widget.linkedPage != null && widget.linkedPage.widgets != null) {
				List <WidgetBean> tmpWidgets =  getItemsOnPage(widget.linkedPage.widgets, searchItem);
				if(!tmpWidgets.isEmpty()) {
					foundWidgets.addAll(tmpWidgets); }
			}			
		}
		}catch (Exception e){
			logger.error(e.getMessage());
		}
		return foundWidgets;
	}

}
