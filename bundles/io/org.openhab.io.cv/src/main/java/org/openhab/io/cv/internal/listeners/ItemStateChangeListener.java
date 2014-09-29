/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.cv.internal.listeners;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.io.cv.CVApplication;
import org.openhab.io.cv.internal.resources.ReadResource;
import org.openhab.io.cv.internal.resources.ResponseTypeHelper;
import org.openhab.io.cv.internal.resources.beans.ItemBean;
import org.openhab.io.cv.internal.resources.beans.ItemListBean;
import org.openhab.io.cv.internal.resources.beans.ItemStateListBean;
import org.openhab.ui.items.ItemUIRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the {@link ResourceStateChangeListener} implementation for cometvisu's read requests.
 * Note: We only support suspended requests for page requests, not for complete sitemaps.
 * 
 * @author Kai Kreuzer
 * @author Oliver Mazur
 * @author Tobias Bräutigam
 * @since 1.4.0
 *
 */
public class ItemStateChangeListener extends ResourceStateChangeListener {

	private static final Logger logger = LoggerFactory.getLogger(ResourceStateChangeListener.class);
	private List<String> itemNames;
	
	public ItemStateChangeListener(List<String> itemNames) {
		this.itemNames = itemNames;
	}

	@Override
	protected Object getResponseObject(HttpServletRequest request) {
		ItemStateListBean stateBean = getItemStateListBean(request);
		if(stateBean!=null) {
			return stateBean;
    	}
		return null;
	}

	@Override
	protected Object getSingleResponseObject(Item item, HttpServletRequest request) {
		ItemStateListBean responseBean ;
		Collection<ItemBean> beans = new LinkedList<ItemBean>();
		beans.add(new ItemBean(item.getName(), item.getState().toString()));
		responseBean = new ItemStateListBean( new ItemListBean(beans));
		responseBean.index = System.currentTimeMillis();
		return responseBean;
	}
	
	private ItemStateListBean getItemStateListBean(HttpServletRequest request){
		String pathInfo = request.getPathInfo();
		String responseType = (new ResponseTypeHelper()).getResponseType(request);
		if(responseType!=null) {
			if (pathInfo.startsWith("/" +ReadResource.PATH_READ)) {
	        	ItemStateListBean bean = new ItemStateListBean(new ItemListBean(getItemBeans(request)));
	        	bean.index = System.currentTimeMillis();
				return bean;
	        }
		}
		return null;
		
	}
	
	public Collection<ItemBean> getItemBeans(HttpServletRequest request) {
		Collection<ItemBean> beans = new LinkedList<ItemBean>();
		ItemUIRegistry registry = CVApplication.getItemUIRegistry();

		for (String itemName : request.getParameterValues("a")) {
			try {
				Item item = registry.getItem(itemName);
				beans.add(ReadResource.createItemBean(item,false));
			} catch (ItemNotFoundException e) {
				logger.debug(e.getMessage());
			}
		}
		return beans;
	}

	@Override
	protected List<String> getRelevantItemNames() {
		return itemNames;
	}
}
