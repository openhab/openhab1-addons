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
