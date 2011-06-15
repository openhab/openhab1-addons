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

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemNotUniqueException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationHelper;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.types.PrimitiveType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.openhab.model.sitemap.Group;
import org.openhab.model.sitemap.LinkableWidget;
import org.openhab.model.sitemap.Sitemap;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.items.ItemUIProvider;
import org.openhab.ui.webapp.internal.WebAppActivator;
import org.openhab.ui.webapp.render.RenderException;
import org.openhab.ui.webapp.render.WidgetRenderer;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an abstract implementation of a widget renderer. It provides
 * methods that are very useful for any widget renderer implementation,
 * so it should be subclassed by most concrete implementations.
 * 
 * @author Kai Kreuzer
 * @since 0.6.0
 *
 */
abstract public class AbstractWidgetRenderer implements WidgetRenderer {

	private final static Logger logger = LoggerFactory.getLogger(AbstractWidgetRenderer.class);

	/* the file extension of the images */
	protected static final String IMAGE_EXT = ".png";

	/* the image location inside this bundle */
	protected static final String IMAGE_LOCATION = "web/images/";

	protected ItemRegistry itemRegistry;

	protected Set<ItemUIProvider> itemUIProviders = new HashSet<ItemUIProvider>();

	protected ItemUIProvider delegatingItemUIProvider;

	/* the file extension of the snippets */
	protected static final String SNIPPET_EXT = ".html";

	/* the snippet location inside this bundle */
	protected static final String SNIPPET_LOCATION = "snippets/";

	/* a local cache so we do not have to read the snippets over and over again from the bundle */
	protected static final Map<String, String> snippetCache = new HashMap<String, String>(); 

	/* RegEx to extract a parse a function String <code>'\[(.*?)\((.*)\):(.*)\]'</code> */
	protected static final Pattern EXTRACT_TRANSFORMFUNCTION_PATTERN = Pattern.compile("\\[(.*?)\\((.*)\\):(.*)\\]");

	public void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}

	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = null;
	}

	public void addItemUIProvider(ItemUIProvider itemUIProvider) {
		itemUIProviders.add(itemUIProvider);
	}

	public void removeItemUIProvider(ItemUIProvider itemUIProvider) {
		itemUIProviders.remove(itemUIProvider);
	}

	public ItemRegistry getItemRegistry() {
		return itemRegistry;
	}


	public Set<ItemUIProvider> getItemUIProviders() {
		return itemUIProviders;
	}

	protected void activate(ComponentContext context) {
		// initialize the delegating provider
		delegatingItemUIProvider = new DelegatingItemUIProvider(this);
	}

	protected void deactivate(ComponentContext context) {
		delegatingItemUIProvider = null;
	}

	/**
	 * This method provides the html snippet for a given elementType of the sitemap model.
	 * 
	 * @param elementType the name of the model type (e.g. "Group" or "Switch")
	 * @return the html snippet to be used in the UI (including placeholders for variables)
	 * @throws RenderException if snippet could not be read 
	 */
	protected synchronized String getSnippet(String elementType) throws RenderException {
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
				throw new RenderException("Cannot find a snippet for element type '" + elementType + "'");
			}
		}
		return snippet;
	}

	/**
	 * Retrieves the label for a widget.
	 * 
	 * This first checks, if there is a label defined in the sitemap. If not, it checks
	 * all item UI providers for a label. If no label can be found, it is set to an empty string.
	 * 
	 * If the label contains a "[%format]" section, i.e. “[%s]" for a string or "[%.3f]" for a decimal,
	 * this is replaced by the current value of the item and padded by a "<span>" element.
	 * 
	 * @param w the widget to retrieve the label for
	 * @return the label to use for the widget
	 */
	public String getLabel(Widget w) {
		String label = null;
		if(w.getLabel()!=null) {
			// if there is a label defined for the widget, use this
			label = w.getLabel();
		} else {
			String itemName = w.getItem();
			if(itemName!=null) {
				// check if any item ui provider provides a label for this item 
				if(delegatingItemUIProvider!=null) {
					label = delegatingItemUIProvider.getLabel(itemName);
				}

				// if there is no item ui provider saying anything, simply use the name as a label
				if(label==null) label = itemName;
			}
		}
		// use an empty string, if no label could be found
		if(label==null) label = "";
		
		// now insert the value, if the state is a string or decimal value and there is some formatting pattern defined in the label 
		// (i.e. it contains at least a %)
		String itemName = w.getItem();
		if(itemName!=null && label.contains("[")) {
			
			int indexOpenBracket = label.indexOf("[");
			int indexCloseBracket = label.indexOf("]");
			
			if(itemRegistry!=null) { 
				State state = null;
				String formatPattern = label.substring(indexOpenBracket + 1, indexCloseBracket);
				try {
					
					Item item = itemRegistry.getItem(itemName);
					
					if (label.contains("%s")) {
						state = item.getState();
					} else if (label.contains("%t") || label.contains("%1$t")) {
						state = item.getState();
					} else {
						// a number is requested
						state = item.getStateAs(DecimalType.class);
					}
					
					
				} catch (ItemNotFoundException e) {
					logger.error("Cannot retrieve item for widget {}", w.eClass().getInstanceTypeName());
				} catch (ItemNotUniqueException e) {
					logger.error("Item with name '{}' is not unique.", itemName, e);
				}

				if (state==null || state instanceof UnDefType) {
					
					// insert "undefined, if the value is not defined
					if (label.contains("%s")) {
						formatPattern = String.format(formatPattern, "undefined");
					} else if (label.contains("%t") || label.contains("%1$t")) {
						formatPattern = String.format(formatPattern, Calendar.getInstance());
					} else if (label.contains("%d")) {
						// it is a integer value
						formatPattern = String.format(formatPattern, 0);
					} else { 
						// it is a float value
						formatPattern = String.format(formatPattern, 0f);
					}
					
				} else if (state instanceof PrimitiveType) {
					formatPattern = ((PrimitiveType) state).format(formatPattern);
				}

				label = label.substring(0, indexOpenBracket + 1) + formatPattern + label.substring(indexCloseBracket);
			}
		}
		
		// now check if there is a status value being displayed on the right side of the label (the right side is signified
		// by being enclosed in square brackets []. If so, check if the value starts with the call to a transformation service
		// (e.g. "[MAP(en.map):%s]") and execute the transformation in this case.
		if(label.contains("[") && label.endsWith("]")) {
			Matcher matcher = EXTRACT_TRANSFORMFUNCTION_PATTERN.matcher(label);
			if(matcher.find()) {
				String type = matcher.group(1);
				String pattern = matcher.group(2);
				String value = matcher.group(3);
				TransformationService transformation = TransformationHelper.getTransformationService(WebAppActivator.getContext(), type);
				if(transformation!=null) {
					try {
						label = label.substring(0, label.indexOf("[")+1) + transformation.transform(pattern, value) + "]";
					} catch (TransformationException e) {
						logger.error("transformation throws exception [transformation="
								+ transformation + ", value=" + value + "]", e);
						label = label.substring(0, label.indexOf("[")+1) + value + "]";
					}
				} else {
					logger.warn("couldn't transform value in label because transformationService of type '{}' is unavailable", type);
					label = label.substring(0, label.indexOf("[")+1) + value + "]";
				}
			}
			// at last, also insert the span between the left and right side of the label 
			label = label.replaceAll("\\[", "<span>").replaceAll("\\]", "</span>");
		}
		return label;
	}

	/**
	 * Retrieves the icon name for a widget.
	 * 
	 * This first checks, if there is an icon defined in the sitemap. If not, if checks
	 * all item UI providers for an icon. If no icon can be found, the default icon name is the widget
	 * type name, e.g. "switch".
	 * 
	 * If the icon name does not contain a "-" and has a state other than "undefined", its current state
	 * is appended to the icon name, e.g. "switch-on". If no such icon exists, the base icon ("switch") will
	 * be returned nonetheless.
	 * 
	 * @param w the widget to retrieve the icon name for
	 * @return the icon name to use for the widget
	 */
	protected String getIcon(Widget w) {
		String widgetTypeName = w.eClass().getInstanceTypeName().substring(w.eClass().getInstanceTypeName().lastIndexOf(".")+1);
		
		// the default is the widget type name, e.g. "switch"
		String icon = widgetTypeName.toLowerCase();
		
		// if an icon is defined for the widget, use it
		if(w.getIcon()!=null) {
			icon = w.getIcon();
		} else {
			// otherwise check if any item ui provider provides an icon for this item			
			String itemName = w.getItem();
			if(itemName!=null && delegatingItemUIProvider!=null) {
				String result = delegatingItemUIProvider.getIcon(itemName);
				if(result!=null) icon = result;
			}
		}
		
		// now add the state, if the string does not already contain a state information
		if(!icon.contains("-")) {
			Object state = getState(w);
			if(!state.equals(UnDefType.UNDEF)) {
				if(state instanceof PercentType) {
					// we do a special treatment for percent types; we try to find the icon of the biggest value
					// that is still smaller or equal to the current state. 
					// Example: if there are icons *-0.png, *-50.png and *-100.png, we choose *-0.png, if the state
					// is 40, and *-50.png, if the state is 70.
					int iconState = ((PercentType) state).toBigDecimal().intValue();
					String testIcon;
					do {
						testIcon = icon + "-" + String.valueOf(iconState--);
					} while(!iconExists(testIcon) && iconState>=0);
					icon = testIcon;
				} else {
					// for all other types, just add the string representation of the state
					icon += "-" + state.toString().toLowerCase();
				}
			}
		}
		
		// if the icon contains a status part, but does not exist, return the icon without status
		if(iconExists(icon) || !icon.contains("-")) {
			return icon;
		} else {
			return icon.substring(0, icon.indexOf("-"));
		}
	}

	/**
	 * Retrieves the current state of the item of a widget or <code>UnDefType.UNDEF</code>.
	 * 
	 * @param w the widget to retrieve the item state for
	 * @return the item state of the widget
	 */
	protected State getState(Widget w) {
		String itemName = w.getItem();
		if(itemRegistry!=null && itemName!=null) {
			try {
				Item item = itemRegistry.getItem(itemName);
				return item.getState();
			} catch (ItemNotFoundException e) {
				logger.error("Cannot retrieve item '{}' for widget {}", new String[] { itemName, w.eClass().getInstanceTypeName() });
			} catch (ItemNotUniqueException e) {
				logger.error("Item with name '{}' is not unique.", itemName, e);
			}
		}
		return UnDefType.UNDEF;
	}

	/**
	 * Provides an id for a widget.
	 * 
	 * This constructs a string out of the position of the sitemap, so if this widget
	 * is the third child of a page linked from the fifth widget on the home screen, its
	 * id would be "0503". 
	 * If the widget is dynamically created and not available in the sitemap, the name
	 * of its associated item is used instead.
	 * 
	 * @param w the widget to get the id for
	 * @return an id for this widget
	 */
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
			String itemName = w.getItem();
			id = itemName;
		}
		return id;
	}
	
	/**
	 * Retrieves the widget for a given id on a given sitemap.
	 * 
	 * @param sitemap the sitemap to look for the widget
	 * @param id the id of the widget to look for
	 * @return the widget for the given id
	 */
	public Widget getWidget(Sitemap sitemap, String id) {
		if(id.length()>0) {
			// see if the id is an itemName and try to get the a widget for it
			Widget w = delegatingItemUIProvider.getWidget(id);
			if(w==null) { 
				// try to get the default widget instead
				w = delegatingItemUIProvider.getDefaultWidget(null, id);
			}
			if(w!=null) {
				w.setItem(id);
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
	 * this should be used instead of LinkableWidget.getChildren() as there
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
						widget.setItem(member.getName());
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

	/**
	 * Checks whether an icon with a given name exists
	 * 
	 * @param icon the icon name to check
	 * @return true, if the icon exists
	 */
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
 
}
