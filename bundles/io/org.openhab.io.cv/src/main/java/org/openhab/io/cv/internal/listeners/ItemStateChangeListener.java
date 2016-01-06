/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.cv.internal.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.io.cv.internal.ReturnType;
import org.openhab.io.cv.internal.resources.ReadResource;
import org.openhab.io.cv.internal.resources.ResponseTypeHelper;
import org.openhab.io.cv.internal.resources.beans.ItemBean;
import org.openhab.io.cv.internal.resources.beans.ItemListBean;
import org.openhab.io.cv.internal.resources.beans.ItemStateListBean;
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
	private Map<String,List<ReturnType>> itemNames = new HashMap<String,List<ReturnType>>();
	
	
	public ItemStateChangeListener(List<ReturnType> rts) {
		for (ReturnType rt : rts) {
			if (!itemNames.containsKey(rt.getItem().getName())) {
				itemNames.put(rt.getItem().getName(),new ArrayList<ReturnType>());
			}
			itemNames.get(rt.getItem().getName()).add(rt);
		}
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
		if (itemNames.containsKey(item.getName())) {
			for (ReturnType rt : itemNames.get(item.getName())) {
				if (rt.getStateClass()!=null) {
					beans.add(new ItemBean(rt.getClientItemName(), item.getStateAs(rt.getStateClass()).toString()));
				} else {
					beans.add(new ItemBean(rt.getClientItemName(), item.getState().toString()));
				}
			}
		} else {
			beans.add(new ItemBean(item.getName(), item.getState().toString()));
		}
		responseBean = new ItemStateListBean( new ItemListBean(beans));
		responseBean.index = System.currentTimeMillis();
		return responseBean;
	}
	
	private ItemStateListBean getItemStateListBean(HttpServletRequest request){
		String pathInfo = request.getPathInfo();
		String responseType = (new ResponseTypeHelper()).getResponseType(request);
		if(responseType!=null) {
			if (pathInfo.startsWith("/" +ReadResource.PATH_READ)) {
	        	ItemStateListBean bean = new ItemStateListBean(new ItemListBean(getItemBeans()));
	        	bean.index = System.currentTimeMillis();
				return bean;
	        }
		}
		return null;
		
	}
	
	public Collection<ItemBean> getItemBeans() {
		Collection<ItemBean> beans = new LinkedList<ItemBean>();
		for (String itemName : itemNames.keySet()) {
			for (ReturnType rt : itemNames.get(itemName)) {
				beans.add(ReadResource.createItemBean(rt));
			}
		}
		return beans;
	}

	@Override
	protected Map<String,List<ReturnType>> getRelevantItemNames() {
		return itemNames;
	}
}
