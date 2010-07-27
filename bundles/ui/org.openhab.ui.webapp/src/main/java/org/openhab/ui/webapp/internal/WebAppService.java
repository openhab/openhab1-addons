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

package org.openhab.ui.webapp.internal;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemNotUniqueException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.openhab.model.sitemap.Group;
import org.openhab.model.sitemap.LinkableWidget;
import org.openhab.model.sitemap.Sitemap;
import org.openhab.model.sitemap.SitemapProvider;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.items.ItemUIProvider;
import org.openhab.ui.webapp.internal.servlet.CmdServlet;
import org.openhab.ui.webapp.internal.servlet.WebAppServlet;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This service registers all relevant resource folders and servlets for the WebApp UI.
 * Furthermore it provides access to all UI relevant information, like
 * HTML snippets, icons, labels etc.
 * 
 * @author Kai Kreuzer
 *
 */
public class WebAppService {
	
	private final static Logger logger = LoggerFactory.getLogger(WebAppService.class);

	public static final String WEBAPP_ALIAS = "/";
	
	private HttpService httpService;
	private SitemapProvider sitemapProvider;
	private ItemRegistry itemRegistry;
	private EventPublisher eventPublisher;
	private Set<ItemUIProvider> itemUIProviders = new HashSet<ItemUIProvider>();
	private ItemUIProvider delegatingItemUIProvider;
	
	/* the file extension of the snippets */
	private static final String SNIPPET_EXT = ".html";

	/* the file extension of the images */
	private static final String IMAGE_EXT = ".png";

	/* the snippet location inside this bundle */
	private static final String SNIPPET_LOCATION = "snippets/";

	/* the image location inside this bundle */
	private static final String IMAGE_LOCATION = "web/images/";

	/* a local cache so we do not have to read the snippets over and over again from the bundle */
	private static final Map<String, String> snippetCache = new HashMap<String, String>(); 
	
	public void setHttpService(HttpService httpService) {
		this.httpService = httpService;
	}

	public void unsetHttpService(HttpService httpService) {
		this.httpService = null;
	}

	protected void startup() {
		try {
			logger.debug("Starting up webapp at " + WEBAPP_ALIAS);
			httpService.registerResources(WEBAPP_ALIAS, "web", null);
			
			Hashtable<String, String> props = new Hashtable<String, String>();
			httpService.registerServlet(WEBAPP_ALIAS + WebAppServlet.SERVLET_NAME, new WebAppServlet(this), props, null);
			httpService.registerServlet(WEBAPP_ALIAS + CmdServlet.SERVLET_NAME, new CmdServlet(this), props, null);
			
		} catch (NamespaceException e) {
			logger.error("Error during servlet startup", e);
		} catch (ServletException e) {
			logger.error("Error during servlet startup", e);
		}
		
		// initialize the delegating provider
		delegatingItemUIProvider = new DelegatingItemUIProvider(this);
	}
	
	public void setSitemapProvider(SitemapProvider sitemapProvider) {
		this.sitemapProvider = sitemapProvider;
	}

	public void unsetSitemapProvider(SitemapProvider sitemapProvider) {
		this.sitemapProvider = null;
	}

	public void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}

	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = null;
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}

	public void addItemUIProvider(ItemUIProvider itemUIProvider) {
		itemUIProviders.add(itemUIProvider);
	}

	public void removeItemUIProvider(ItemUIProvider itemUIProvider) {
		itemUIProviders.remove(itemUIProvider);
	}

	public SitemapProvider getSitemapProvider() {
		return sitemapProvider;
	}

	public ItemRegistry getItemRegistry() {
		return itemRegistry;
	}

	public EventPublisher getEventPublisher() {
		return eventPublisher;
	}

	public Set<ItemUIProvider> getItemUIProviders() {
		return itemUIProviders;
	}
	
	public ItemUIProvider getDelegatingItemUIProvider() {
		return delegatingItemUIProvider;
	}

	protected void shutdown() {
		httpService.unregister(WEBAPP_ALIAS);
		delegatingItemUIProvider = null;
	}

	/**
	 * This method provides the html snippet for a given elementType of the sitemap model.
	 * 
	 * @param elementType the name of the model type (e.g. "Group" or "Switch")
	 * @return the html snippet to be used in the UI (including placeholders for variables)
	 * @throws ServletException 
	 */
	public synchronized String getSnippet(String elementType) throws ServletException {
		elementType = elementType.toLowerCase();
		String snippet = snippetCache.get(elementType);
		if(snippet==null) {
			String snippetLocation = SNIPPET_LOCATION + elementType + SNIPPET_EXT;
			URL entry = WebAppActivator.getContext().getBundle().getEntry(snippetLocation);
			if(entry!=null) {
				try {
					snippet = IOUtils.toString(entry.openStream());
					snippetCache.put(elementType, snippet);
				} catch (IOException e) {
					logger.warn("Cannot load snippet for element type '{}'", elementType, e);
				}
			} else {
				throw new ServletException("Cannot find a snippet for element type '" + elementType + "'");
			}
		}
		return snippet;
	}

	public String getItem(Widget w) {
		try {
			Method method = w.getClass().getMethod("getItem");
			return (String) method.invoke(w);
		} catch (NoSuchMethodException e) {
			// this widget has no item, that's ok
		} catch (Exception e) {
			logger.error("Cannot retrieve item for widget {}", w.eClass().getInstanceTypeName(), e);
		}
		return null;
	}

	public String getLabel(Widget w) {
		String label = null;
		if(w.getLabel()!=null) {
			// if there is a label defined for the widget, use this
			label = w.getLabel();
		} else {
			String itemName = getItem(w);
			if(itemName!=null) {
				// check if any item ui provider provides a label for this item 
				label = delegatingItemUIProvider.getLabel(itemName);

				// if there is no item ui provider saying anything, simply use the name as a label
				if(label==null) label = itemName;
			}
		}
		// use an empty string, if no label could be found
		if(label==null) label = "";
		
		// now insert the value, if the state is a string or decimal value and there is some formatting pattern defined in the label 
		// (i.e. it contains at least a %)
		String itemName = getItem(w);
		if(itemName!=null && label.contains("%")) {
			try {
				Item item = getItemRegistry().getItem(itemName);
				State state = item.getState();
				if(state instanceof DecimalType) {
					label = ((DecimalType) state).format(label);
				}
				if(state instanceof StringType) {
					label = ((StringType) state).format(label);
				}
				if(state instanceof UnDefType) {
					// insert "undefined, if the value is not defined
					if(label.contains("%s")) {
						label = String.format(label, "undefined");
					} else { 
						// it is a numeric value
						label = String.format(label, 0f);
					}
				}
			} catch (ItemNotFoundException e) {
				logger.error("Cannot retrieve item for widget {}", w.eClass().getInstanceTypeName());
			} catch (ItemNotUniqueException e) {
				logger.error("Item with name '{}' is not unique.", itemName, e);
			}
		}
		
		// at last, also insert the span between the left and right side of the label (the right side is signified by being enclosed in
		// square brackets [].
		if(label.contains("[") && label.endsWith("]")) {
			label = label.replaceAll("\\[", "<span>").replaceAll("\\]", "</span>");
		}
		return label;
	}

	public String getIcon(Widget w) {
		String widgetTypeName = w.eClass().getInstanceTypeName().substring(w.eClass().getInstanceTypeName().lastIndexOf(".")+1);
		
		// the default is the widget type name, e.g. "switch"
		String icon = widgetTypeName.toLowerCase();
		
		// if an icon is defined for the widget, use it
		if(w.getIcon()!=null) {
			icon = w.getIcon();
		} else {
			// otherwise check if any item ui provider provides an icon for this item			
			String itemName = getItem(w);
			if(itemName!=null) {
				String result = delegatingItemUIProvider.getIcon(itemName);
				if(result!=null) icon = result;
			}
		}
		
		// now add the state, if the string does not already contain a state information
		if(!icon.contains("-")) {
			Object state = getState(w);
			if(!state.equals(UnDefType.UNDEF)) {
				icon += "-" + state.toString().toLowerCase();
			}
		}
		
		// if the icon contains a status part, but does not exist, return the icon without status
		if(iconExists(icon) || !icon.contains("-")) {
			return icon;
		} else {
			return icon.substring(0, icon.indexOf("-"));
		}
	}

	public State getState(Widget w) {
		String itemName = getItem(w);
		if(itemName!=null) {
			try {
				Item item = getItemRegistry().getItem(itemName);
				return item.getState();
			} catch (ItemNotFoundException e) {
				logger.error("Cannot retrieve item for widget {}", w.eClass().getInstanceTypeName());
			} catch (ItemNotUniqueException e) {
				logger.error("Item with name '{}' is not unique.", itemName, e);
			}
		}
		return UnDefType.UNDEF;
	}

	private boolean iconExists(String icon) {
		String iconLocation = IMAGE_LOCATION + icon + IMAGE_EXT;
		URL entry = WebAppActivator.getContext().getBundle().getEntry(iconLocation);
		if(entry==null) return false;
		try {
			entry.openConnection();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public String getWidgetId(Widget w) {
		String id = "";
		while(w.eContainer() instanceof Widget) {
			Widget parent = (Widget) w.eContainer();
			String index = String.valueOf(((LinkableWidget)parent).getChildren().indexOf(w));
			if(index.length()==1) index = "0" + index; // make it two digits
			id =  index + id;
			w = parent;
		}
		if(w.eContainer() instanceof Sitemap) {
			Sitemap sitemap = (Sitemap) w.eContainer();
			String index = String.valueOf(sitemap.getChildren().indexOf(w));
			if(index.length()==1) index = "0" + index; // make it two digits
			id =  index + id;
		}
		
		// if the widget is dynamically created and not available in the sitemap,
		// use the item name as the id
		if(w.eContainer()==null) {
			String itemName = getItem(w);
			id = itemName;
		}
		return id;
	}
	
	public Widget getWidget(Sitemap sitemap, String id) {
		if(id.length()>0) {
			// see if the id is an itemName and try to get the default widget for it
			Widget w = getDelegatingItemUIProvider().getDefaultWidget(null, id);
			if(w!=null) {
				return w;
			} else {
				w = sitemap.getChildren().get(Integer.valueOf(id.substring(0, 2)));
				for(int i = 2; i < id.length(); i+=2) {
					w = ((LinkableWidget)w).getChildren().get(Integer.valueOf(id.substring(i, i+2)));
				}
				return w;
			}
		} else {
			logger.warn("Cannot find widget for id {}.", id);
			return null;
		}
	}
	
	/**
	 * this should be used instead of Widget.getChildren() as there
	 * might be no children defined on the widget, but they should
	 * be dynamically determined by looking at the members of the underlying
	 * item.
	 * 
	 * @param w the widget to retrieve the children for
	 * @return the (dynamically or statically defined) children of the widget
	 */
	public EList<Widget> getChildren(LinkableWidget w) {
		if(w instanceof Group && ((LinkableWidget)w).getChildren().isEmpty()) {
			return getDynamicGroupChildren((Group) w);
		} else {
			return ((LinkableWidget)w).getChildren();
		}
	}
	
	/** 
	 * This method creates a list of children for a group dynamically.
	 * If there are no explicit children defined in a sitemap, the children
	 * can thus be created on the fly by iterating over the members of the group item.
	 * 
	 * @param group The group widget to get children for
	 * @return a list of default widgets provided for the member items
	 */
	private EList<Widget> getDynamicGroupChildren(Group group) {
		EList<Widget> children = new BasicEList<Widget>();
		String itemName = group.getItem();
		try {
			Item item = getItemRegistry().getItem(itemName);
			if(item instanceof GroupItem) {
				GroupItem groupItem = (GroupItem) item;
				for(Item member : groupItem.getMembers()) {
					Widget widget = delegatingItemUIProvider.getDefaultWidget(member.getClass(), member.getName());
					if(widget!=null) {
						children.add(widget);
					}					
				}
			} else {
				logger.warn("Item '{}' is not a group.", item.getName());
			}
		} catch (ItemNotFoundException e) {
			logger.warn("Group '{}' could not be found.", group.getLabel(), e);
		} catch (ItemNotUniqueException e) {
			logger.warn("Group '{}' is not unique.", group.getLabel(), e);
		}
		return children;
		
	}
}
