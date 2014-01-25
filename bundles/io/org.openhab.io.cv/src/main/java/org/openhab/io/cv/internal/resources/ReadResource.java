/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.cv.internal.resources;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.atmosphere.annotation.Suspend.SCOPE;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.HeaderConfig;
import org.atmosphere.jersey.SuspendResponse;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.io.cv.CVApplication;
import org.openhab.io.cv.internal.broadcaster.CometVisuBroadcaster;
import org.openhab.io.cv.internal.listeners.ItemStateChangeListener;
import org.openhab.io.cv.internal.resources.beans.GroupItemBean;
import org.openhab.io.cv.internal.resources.beans.ItemBean;
import org.openhab.io.cv.internal.resources.beans.ItemListBean;
import org.openhab.io.cv.internal.resources.beans.ItemStateListBean;
import org.openhab.ui.items.ItemUIRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class acts as a Cometvisu resource for reading Items.
 * </p>
 * 
 * <p>
 * The typical content types are XML or JSON.
 * </p>
 * 
 * <p>
 * This resource is registered with the Jersey servlet.
 * </p>
 * 
 * @author Tobias Bräutigam
 * @since 1.4.0
 */
@Path(ReadResource.PATH_READ)
public class ReadResource {

	private static final Logger logger = LoggerFactory
			.getLogger(ReadResource.class);

	public static final String PATH_READ = "r";

	@Context
	UriInfo uriInfo;
	@Context
	Broadcaster itemBroadcaster;

	@GET
	@Produces({ MediaType.WILDCARD })
	public SuspendResponse<Response> getResults(
			@Context HttpHeaders headers,
			@QueryParam("a") List<String> itemNames,
			@QueryParam("i") long index,
			@QueryParam("t") long time,
			@QueryParam("jsoncallback") @DefaultValue("callback") String callback,
			@HeaderParam(HeaderConfig.X_ATMOSPHERE_TRANSPORT) String atmosphereTransport,
			@HeaderParam(HeaderConfig.X_CACHE_DATE) long cacheDate,
    		@Context AtmosphereResource resource) {
		String responseType = MediaTypeHelper.getResponseMediaType(headers.getAcceptableMediaTypes());
		logger.debug("Received HTTP GET request at '{}' for {} items at index '{}', time '{}', ResponseType: '{}'.",
				new String[] { uriInfo.getPath(), String.valueOf(itemNames.size()), String.valueOf(index), String.valueOf(cacheDate), responseType});
		if(index==0) {
			// first request => return all values
			if (responseType != null) {
				throw new WebApplicationException(Response.ok(getItemStateListBean(itemNames,System.currentTimeMillis()), responseType).build());
			} else {
				throw new WebApplicationException(Response.notAcceptable(null).build());
			}
		}
		CometVisuBroadcaster itemBroadcaster = (CometVisuBroadcaster) BroadcasterFactory.getDefault().lookup(CometVisuBroadcaster.class, resource.getRequest().getPathInfo(), true);
		itemBroadcaster.addStateChangeListener(new ItemStateChangeListener(itemNames));
		return new SuspendResponse.SuspendResponseBuilder<Response>()
			.scope(SCOPE.REQUEST)
			.resumeOnBroadcast(!ResponseTypeHelper.isStreamingTransport(resource.getRequest()))
			.broadcaster(itemBroadcaster)
			.outputComments(true).build();
	}
	
	public ItemStateListBean getItemStateListBean(List<String> itemNames, long index) {
		ItemStateListBean stateList = new ItemStateListBean(new ItemListBean(getItemBeans(itemNames)));
		stateList.index = index+1;
		return stateList;
	}
	
	public Collection<ItemBean> getItemBeans(List<String> itemNames) {
		Collection<ItemBean> beans = new LinkedList<ItemBean>();
		ItemUIRegistry registry = CVApplication.getItemUIRegistry();
		for (String itemName : itemNames) {
			try {
				Item item = registry.getItem(itemName);
				beans.add(createItemBean(item,false));
			} catch (ItemNotFoundException e) {
				logger.debug(e.getMessage());
			}
		}
		return beans;
	}

	public static ItemBean createItemBean(Item item, boolean drillDown) {
		ItemBean bean;
		if (item instanceof GroupItem && drillDown) {
			GroupItem groupItem = (GroupItem) item;
			GroupItemBean groupBean = new GroupItemBean();
			Collection<ItemBean> members = new HashSet<ItemBean>();
			for (Item member : groupItem.getMembers()) {
				members.add(createItemBean(member, false));
			}
			groupBean.members = members.toArray(new ItemBean[members.size()]);
			bean = groupBean;
		} else {
			bean = new ItemBean(item.getName(), item.getState().toString());
		}
		return bean;
	}

	static public Item getItem(String itemname) {
		ItemUIRegistry registry = CVApplication.getItemUIRegistry();
		if (registry != null) {
			try {
				Item item = registry.getItem(itemname);
				return item;
			} catch (ItemNotFoundException e) {
				logger.debug(e.getMessage());
			}
		}
		return null;
	}
}
