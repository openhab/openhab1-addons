/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.rest.internal.listeners;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriBuilder;

import org.openhab.core.items.Item;
import org.openhab.io.rest.RESTApplication;
import org.openhab.io.rest.internal.resources.ResponseTypeHelper;
import org.openhab.io.rest.internal.resources.SitemapResource;
import org.openhab.io.rest.internal.resources.beans.PageBean;
import org.openhab.io.rest.internal.resources.beans.WidgetBean;
import org.openhab.io.rest.internal.resources.beans.WidgetListBean;
import org.openhab.model.sitemap.Frame;
import org.openhab.model.sitemap.LinkableWidget;
import org.openhab.model.sitemap.Sitemap;
import org.openhab.model.sitemap.Widget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		PageBean pageBean = getPageBean(request);
		if(pageBean!=null) {
			return pageBean;
    	}
		return null;
	}
		
	@Override
	protected Object getSingleResponseObject(Item item, HttpServletRequest request) {
		PageBean pageBean = getPageBean(request);
		WidgetListBean responseBeam ;
		if(pageBean!=null) {
			responseBeam = new WidgetListBean( getItemsOnPage(pageBean.widgets, item));
			return responseBeam;
	    	
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
		
		String responseType = (new ResponseTypeHelper()).getResponseType(request);
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
