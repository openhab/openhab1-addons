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

package org.openhab.io.rest.internal.resources;

import java.util.Collection;
import java.util.LinkedList;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.openhab.core.items.Item;
import org.openhab.io.rest.internal.RESTApplication;
import org.openhab.io.rest.internal.resources.beans.MappingBean;
import org.openhab.io.rest.internal.resources.beans.PageBean;
import org.openhab.io.rest.internal.resources.beans.SitemapBean;
import org.openhab.io.rest.internal.resources.beans.SitemapListBean;
import org.openhab.io.rest.internal.resources.beans.WidgetBean;
import org.openhab.model.core.ModelRepository;
import org.openhab.model.sitemap.Frame;
import org.openhab.model.sitemap.LinkableWidget;
import org.openhab.model.sitemap.List;
import org.openhab.model.sitemap.Mapping;
import org.openhab.model.sitemap.Selection;
import org.openhab.model.sitemap.Sitemap;
import org.openhab.model.sitemap.Slider;
import org.openhab.model.sitemap.Switch;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.items.ItemUIRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.json.JSONWithPadding;

/**
 * <p>This class acts as a REST resource for sitemaps and provides different methods to interact with them,
 * like retrieving a list of all available sitemaps or just getting the widgets of a single page.</p>
 * 
 * <p>The typical content types are XML or JSON.</p>
 * 
 * <p>This resource is registered with the Jersey servlet.</p>
 *
 * @author Kai Kreuzer
 * @since 0.8.0
 */
@Path(SitemapResource.PATH_SITEMAPS)
public class SitemapResource {

	private static final Logger logger = LoggerFactory.getLogger(SitemapResource.class); 

    protected static final String SITEMAP_FILEEXT = ".sitemap";

	static final String PATH_SITEMAPS = "sitemaps";
    
	@Context UriInfo uriInfo;

	@GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Collection<SitemapBean> getSitemaps() {
		return getSitemapBeans();
	}

	@GET @Path("/jsonp")
    @Produces( { "application/x-javascript" })
    public JSONWithPadding getJSONPSitemaps(@QueryParam("jsoncallback") @DefaultValue("callback") String callback) {
   		return new JSONWithPadding(new SitemapListBean(getSitemapBeans()), callback);
    }

    @GET @Path("/{sitemapname: [a-zA-Z_0-9]*}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public SitemapBean getSitemapData(@PathParam("sitemapname") String sitemapname) {
		logger.debug("Received HTTP GET request at '{}'.", uriInfo.getPath());
    	return getSitemapBean(sitemapname);
    }

	@GET @Path("/{sitemapname: [a-zA-Z_0-9]*}/jsonp")
    @Produces( { "application/x-javascript" })
    public JSONWithPadding getJSONPSitemapData(@PathParam("sitemapname") String sitemapname, 
    		@QueryParam("jsoncallback") @DefaultValue("callback") String callback) {
		logger.debug("Received HTTP GET request at '{}' for JSONP.", uriInfo.getPath());
   		return new JSONWithPadding(getSitemapBean(sitemapname), callback);
    }

    @GET @Path("/{sitemapname: [a-zA-Z_0-9]*}/{pageid: [a-zA-Z_0-9]*}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public PageBean getPageData(@PathParam("sitemapname") String sitemapName, @PathParam("pageid") String pageId) {
		logger.debug("Received HTTP GET request at '{}'.", uriInfo.getPath());
    	return getPageBean(sitemapName, pageId);
    }

	@GET @Path("/{sitemapname: [a-zA-Z_0-9]*}/{pageid: [a-zA-Z_0-9]*}/jsonp")
    @Produces( { "application/x-javascript" })
    public JSONWithPadding getJSONPPageData(@PathParam("sitemapname") String sitemapname, @PathParam("pageid") String pageId,
    		@QueryParam("jsoncallback") @DefaultValue("callback") String callback) {
		logger.debug("Received HTTP GET request at '{}' for JSONP.", uriInfo.getPath());
   		return new JSONWithPadding(getPageBean(sitemapname, pageId), callback);
    }

    private PageBean getPageBean(String sitemapName, String pageId) {
		ItemUIRegistry itemUIRegistry = RESTApplication.getItemUIRegistry();
		Sitemap sitemap = getSitemap(sitemapName);
		if(sitemap!=null) {
			Widget pageWidget = itemUIRegistry.getWidget(sitemap, pageId);
			if(pageWidget instanceof LinkableWidget) {
				return createPageBean(sitemapName, pageId, (itemUIRegistry.getChildren((LinkableWidget) pageWidget)));
			} else {
				if(logger.isDebugEnabled()) {
					if(pageWidget==null) {
		    			logger.debug("Received HTTP GET request at '{}' for the unknown page id '{}'.", uriInfo.getPath(), pageId);
					} else {
		    			logger.debug("Received HTTP GET request at '{}' for the page id '{}'. " + 
		    					"This id refers to a non-linkable widget and is therefore no valid page id.", uriInfo.getPath(), pageId);
					}
				}
	    		throw new WebApplicationException(404);
			}
		} else {
			logger.info("Received HTTP GET request at '{}' for the unknown sitemap '{}'.", uriInfo.getPath(), sitemapName);
			throw new WebApplicationException(404);
		}
	}

	private Collection<SitemapBean> getSitemapBeans() {
		Collection<SitemapBean> beans = new LinkedList<SitemapBean>();
		logger.debug("Received HTTP GET request at '{}'.", uriInfo.getPath());
		ModelRepository modelRepository = RESTApplication.getModelRepository();
		for(String modelName : modelRepository.getAllModelNamesOfType("sitemap")) {
			SitemapBean bean = new SitemapBean();
			bean.name = StringUtils.removeEnd(modelName, SITEMAP_FILEEXT);
			bean.link = uriInfo.getAbsolutePathBuilder().path(bean.name).build().toASCIIString();
			beans.add(bean);
		}
		return beans;
	}

	private SitemapBean getSitemapBean(String sitemapname) {
		Sitemap sitemap = getSitemap(sitemapname);
		if(sitemap!=null) {
			return createSitemapBean(sitemapname, sitemap, true);
		} else {
			logger.info("Received HTTP GET request at '{}' for the unknown sitemap '{}'.", uriInfo.getPath(), sitemapname);
			throw new WebApplicationException(404);
		}
	}

	private SitemapBean createSitemapBean(String sitemapName, Sitemap sitemap, boolean drillDown) {
    	SitemapBean bean = new SitemapBean();
    	bean.name = sitemapName;
    	bean.link = uriInfo.getBaseUriBuilder().path(SitemapResource.PATH_SITEMAPS).path(bean.name).build().toASCIIString();
    	bean.homepage = createPageBean(sitemapName, sitemap.getName(), sitemap.getChildren());
    	return bean;
    }
    
    private PageBean createPageBean(String sitemapName, String pageId, EList<Widget> children) {
    	PageBean bean = new PageBean();
    	bean.id = pageId;
    	bean.link = uriInfo.getBaseUriBuilder().path(PATH_SITEMAPS).path(sitemapName).path(pageId).build().toASCIIString();
    	for(Widget widget : children) {
    		bean.widgets.add(createWidgetBean(sitemapName, widget));
    	}
		return bean;
	}

	private WidgetBean createWidgetBean(String sitemapName, Widget widget) {
		ItemUIRegistry itemUIRegistry = RESTApplication.getItemUIRegistry();
    	WidgetBean bean = new WidgetBean();
    	if(widget.getItem()!=null) {
    		Item item = ItemResource.getItem(widget.getItem());
        	if(item!=null) {
        		bean.item = ItemResource.createItemBean(item, false, uriInfo.getBaseUri().toASCIIString());
        	}
    	}
    	bean.icon = itemUIRegistry.getIcon(widget);
    	bean.label = itemUIRegistry.getLabel(widget);
    	bean.type = widget.eClass().getName();
    	if (widget instanceof LinkableWidget) {
			LinkableWidget linkableWidget = (LinkableWidget) widget;
			EList<Widget> children = itemUIRegistry.getChildren(linkableWidget);
    		if(widget instanceof Frame) {
    			for(Widget child : children) {
    				bean.widgets.add(createWidgetBean(sitemapName, child));
    			}
    		} else if(children.size()>0)  {
				String pageName = itemUIRegistry.getWidgetId(linkableWidget);
				bean.linkedPage = createPageBean(sitemapName, pageName, itemUIRegistry.getChildren(linkableWidget));
    		}
		}
    	if(widget instanceof Switch) {
    		Switch switchWidget = (Switch) widget;
    		for(Mapping mapping : switchWidget.getMappings()) {
    			MappingBean mappingBean = new MappingBean();
    			mappingBean.command = mapping.getCmd();
    			mappingBean.label = mapping.getLabel();
    			bean.mappings.add(mappingBean);
    		}
    	}
    	if(widget instanceof Selection) {
    		Selection selectionWidget = (Selection) widget;
    		for(Mapping mapping : selectionWidget.getMappings()) {
    			MappingBean mappingBean = new MappingBean();
    			mappingBean.command = mapping.getCmd();
    			mappingBean.label = mapping.getLabel();
    			bean.mappings.add(mappingBean);
    		}
    	}
    	if(widget instanceof Slider) {
    		Slider sliderWidget = (Slider) widget;
    		bean.sendFrequency = sliderWidget.getFrequency();
    		bean.switchSupport = sliderWidget.isSwitchEnabled();
    	}
    	if(widget instanceof List) {
    		List listWidget = (List) widget;
    		bean.separator = listWidget.getSeparator();
    	}
		return bean;
	}

	private Sitemap getSitemap(String sitemapname) {
        ModelRepository repo = RESTApplication.getModelRepository();
        if(repo!=null) {
			Sitemap sitemap = (Sitemap) repo.getModel(sitemapname + SITEMAP_FILEEXT);
			return sitemap;
        }
        return null;
    }
}
