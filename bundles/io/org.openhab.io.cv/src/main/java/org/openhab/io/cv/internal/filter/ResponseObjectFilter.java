/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.cv.internal.filter;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.BroadcastFilter.BroadcastAction.ACTION;
import org.atmosphere.cpr.PerRequestBroadcastFilter;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.io.cv.internal.resources.ResponseTypeHelper;
import org.openhab.io.cv.internal.resources.beans.ItemListBean;
import org.openhab.io.cv.internal.resources.beans.ItemStateListBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This Filter filters out the updated items from the page and returns them to streaming and websocket connections   
 *  
 * @author Tobias Bräutigam
 * @since 1.4.0
 *
 */
public class ResponseObjectFilter implements PerRequestBroadcastFilter {

	private static final Logger logger = LoggerFactory.getLogger(ResponseObjectFilter.class);
	
	@Override
	public BroadcastAction filter(Object originalMessage, Object message) {
//		logger.info("ResponseObjectFilter->filter("+originalMessage+","+message+")");
		return new BroadcastAction(ACTION.CONTINUE, message);
	}

	@Override
	public BroadcastAction filter(AtmosphereResource resource, Object originalMessage, Object message) {
		final  HttpServletRequest request = resource.getRequest();
		try {	
			// websocket and HTTP streaming
			if (ResponseTypeHelper.isStreamingTransport(request) && originalMessage instanceof Item) {
				if (message instanceof ItemStateListBean) {
					return new BroadcastAction(ACTION.CONTINUE, getSingleResponseObject((ItemStateListBean)message, (Item)originalMessage, request)	);
				} else {
					return new BroadcastAction(ACTION.CONTINUE, getSingleResponseObject(null, (Item)originalMessage, request) );
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BroadcastAction(ACTION.ABORT,  message);
		} 
		// pass message to next filter
		return new BroadcastAction(ACTION.CONTINUE,  message);
	}
	
	private Object getSingleResponseObject(ItemStateListBean stateBean, Item item, HttpServletRequest request) {
		ItemStateListBean responseBean;
		if (stateBean==null) {
			stateBean = new ItemStateListBean();
		}
		ItemListBean list = stateBean.stateList;
		
//		if(stateBean!=null) {
			
//			Collection<ItemBean> beans = new LinkedList<ItemBean>();
//			beans.add(new ItemBean(item.getName(), item.getState().toString()));
//			long newIndex = 0;
//			try {
//				long index = Long.valueOf(request.getParameter("i"));
//				CometVisuBroadcaster broadcaster = (CometVisuBroadcaster) BroadcasterFactory.getDefault().lookup(CometVisuBroadcaster.class, request.getPathInfo());
//				ConcurrentSkipListMap<Long,Item> changedItems = broadcaster.getChangedItems();
//				Hashtable<String,String> uniqueChanges = new Hashtable<String,String>();
//				for (Long i : changedItems.tailMap(index,false).keySet()) {
//					uniqueChanges.put(changedItems.get(i).getName(), changedItems.get(i).getState().toString());
////					newIndex=i;
//				}
//				uniqueChanges.put(item.getName(), item.getState().toString());
//				for (String name : uniqueChanges.keySet()) {
//					beans.add(new ItemBean(name, uniqueChanges.get(name)));
//				}
////				newIndex = Math.max(newIndex,broadcaster.getChangedItems().lastKey());
//			}
//			catch (NumberFormatException e) {
//				// Fallback
//				beans.add(new ItemBean(item.getName(), item.getState().toString()));
//			}
			if (item instanceof GroupItem) {
				GroupItem groupItem = (GroupItem)item; 
				// get the base item
				if (groupItem.getBaseItem()!=null) {
					item = groupItem.getBaseItem();
				}
			}
			list.entries.add(new JAXBElement(new QName(item.getName()), String.class, item.getState().toString()));
			
			responseBean = new ItemStateListBean(list);
			responseBean.index = System.currentTimeMillis();
			return responseBean;
	    	
//    	}
//		return null;
	}
}
