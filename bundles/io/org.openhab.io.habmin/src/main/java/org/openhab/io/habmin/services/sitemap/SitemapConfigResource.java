/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.habmin.services.sitemap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.atmosphere.cpr.Broadcaster;
import org.eclipse.emf.common.util.EList;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.io.habmin.HABminApplication;
import org.openhab.io.habmin.internal.resources.LabelSplitHelper;
import org.openhab.io.habmin.internal.resources.MediaTypeHelper;
import org.openhab.model.core.ModelRepository;
import org.openhab.model.sitemap.Chart;
import org.openhab.model.sitemap.ColorArray;
import org.openhab.model.sitemap.ConditionArray;
import org.openhab.model.sitemap.Image;
import org.openhab.model.sitemap.LinkableWidget;
import org.openhab.model.sitemap.Mapping;
import org.openhab.model.sitemap.Selection;
import org.openhab.model.sitemap.Setpoint;
import org.openhab.model.sitemap.Sitemap;
import org.openhab.model.sitemap.Slider;
import org.openhab.model.sitemap.Switch;
import org.openhab.model.sitemap.Video;
import org.openhab.model.sitemap.VisibilityRule;
import org.openhab.model.sitemap.Webview;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.items.ItemUIRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.json.JSONWithPadding;

/**
 * <p>
 * This class acts as a REST resource for history data and provides different
 * methods to interact with the, persistence store
 * 
 * <p>
 * The typical content types are plain text for status values and XML or JSON(P)
 * for more complex data structures
 * </p>
 * 
 * <p>
 * This resource is registered with the Jersey servlet.
 * </p>
 * 
 * @author Chris Jackson
 * @since 1.3.0
 */
@Path(SitemapConfigResource.PATH_CONFIG)
public class SitemapConfigResource {

	private static final Logger logger = LoggerFactory.getLogger(SitemapConfigResource.class);

	private static final Pattern SITEMAP_DEFINITION = Pattern
			.compile(".*?sitemap (.*?) label\\s*=\\s*[\"|'](.*?)[\"|']");

	protected static final String SITEMAP_FILEEXT = ".sitemap";

	/** The URI path to this resource */
	public static final String PATH_CONFIG = "config/sitemap";

	@Context
	UriInfo uriInfo;
	@Context
	Broadcaster sitemapBroadcaster;

	@GET
	@Produces({ MediaType.WILDCARD })
	public Response getSitemaps(@Context HttpHeaders headers, @QueryParam("type") String type,
			@QueryParam("jsoncallback") @DefaultValue("callback") String callback) {
		logger.debug("Received HTTP GET request at '{}' for media type '{}'.", new String[] { uriInfo.getPath(), type });
		String responseType = MediaTypeHelper.getResponseMediaType(headers.getAcceptableMediaTypes(), type);
		if (responseType != null) {
			Object responseObject = responseType.equals(MediaTypeHelper.APPLICATION_X_JAVASCRIPT) ? new JSONWithPadding(
					new SitemapListBean(getSitemapBeans(uriInfo.getAbsolutePathBuilder().build())), callback)
					: new SitemapListBean(getSitemapBeans(uriInfo.getAbsolutePathBuilder().build()));
			return Response.ok(responseObject, responseType).build();
		} else {
			return Response.notAcceptable(null).build();
		}
	}

	@GET
	@Path("/{sitemapname: [a-zA-Z_0-9]*}")
	@Produces({ MediaType.WILDCARD })
	public Response getSitemapData(@Context HttpHeaders headers, @PathParam("sitemapname") String sitemapname,
			@QueryParam("type") String type, @QueryParam("jsoncallback") @DefaultValue("callback") String callback) {
		logger.debug("Received HTTP GET request at '{}' for media type '{}'.", new String[] { uriInfo.getPath(), type });
		String responseType = MediaTypeHelper.getResponseMediaType(headers.getAcceptableMediaTypes(), type);
		if (responseType != null) {
			Object responseObject = responseType.equals(MediaTypeHelper.APPLICATION_X_JAVASCRIPT) ? new JSONWithPadding(
					getSitemap(sitemapname, uriInfo.getBaseUriBuilder().build()), callback) : getSitemap(sitemapname,
					uriInfo.getBaseUriBuilder().build());
			return Response.ok(responseObject, responseType).build();
		} else {
			return Response.notAcceptable(null).build();
		}
	}

	@PUT
	@Path("/{sitemapname: [a-zA-Z_0-9]*}")
	@Produces({ MediaType.WILDCARD })
	public Response putSitemapData(@Context HttpHeaders headers, @PathParam("sitemapname") String sitemapname,
			@QueryParam("type") String type, @QueryParam("jsoncallback") @DefaultValue("callback") String callback,
			WidgetConfigBean sitemap) {
		logger.debug("Received HTTP PUT request at '{}' for media type '{}'.", new String[] { uriInfo.getPath(), type });
		String responseType = MediaTypeHelper.getResponseMediaType(headers.getAcceptableMediaTypes(), type);
		if (responseType != null) {
			Object responseObject = responseType.equals(MediaTypeHelper.APPLICATION_X_JAVASCRIPT) ? new JSONWithPadding(
					saveSitemap(sitemapname, uriInfo.getBaseUriBuilder().build(), sitemap), callback) : saveSitemap(
					sitemapname, uriInfo.getBaseUriBuilder().build(), sitemap);
			return Response.ok(responseObject, responseType).build();
		} else {
			return Response.notAcceptable(null).build();
		}
	}

	@POST
	@Path("/{sitemapname: [a-zA-Z_0-9]*}")
	@Produces({ MediaType.WILDCARD })
	public Response postSitemapData(@Context HttpHeaders headers, @PathParam("sitemapname") String sitemapname,
			@QueryParam("type") String type, @FormParam("copy") String copyName,
			@QueryParam("jsoncallback") @DefaultValue("callback") String callback) {
		logger.debug("Received HTTP POST request at '{}' for media type '{}'.",
				new String[] { uriInfo.getPath(), type });
		String responseType = MediaTypeHelper.getResponseMediaType(headers.getAcceptableMediaTypes(), type);
		if (responseType != null) {
			Object responseObject = responseType.equals(MediaTypeHelper.APPLICATION_X_JAVASCRIPT) ? new JSONWithPadding(
					new SitemapListBean(createSitemap(sitemapname, copyName, uriInfo.getAbsolutePathBuilder().build())),
					callback)
					: new SitemapListBean(
							createSitemap(sitemapname, copyName, uriInfo.getAbsolutePathBuilder().build()));
			return Response.ok(responseObject, responseType).build();
		} else {
			return Response.notAcceptable(null).build();
		}
	}

	@DELETE
	@Path("/{sitemapname: [a-zA-Z_0-9]*}")
	@Produces({ MediaType.WILDCARD })
	public Response deleteSitemapData(@Context HttpHeaders headers, @PathParam("sitemapname") String sitemapname,
			@QueryParam("type") String type, @QueryParam("jsoncallback") @DefaultValue("callback") String callback) {
		logger.debug("Received HTTP DELETE request at '{}' for media type '{}'.", new String[] { uriInfo.getPath(),
				type });
		String responseType = MediaTypeHelper.getResponseMediaType(headers.getAcceptableMediaTypes(), type);
		if (responseType != null) {
			Object responseObject = responseType.equals(MediaTypeHelper.APPLICATION_X_JAVASCRIPT) ? new JSONWithPadding(
					new SitemapListBean(deleteSitemap(sitemapname, uriInfo.getAbsolutePathBuilder().build())), callback)
					: new SitemapListBean(deleteSitemap(sitemapname, uriInfo.getAbsolutePathBuilder().build()));
			return Response.ok(responseObject, responseType).build();
		} else {
			return Response.notAcceptable(null).build();
		}
	}

	public Collection<SitemapBean> getSitemapBeans(URI uri) {
		Collection<SitemapBean> beans = new LinkedList<SitemapBean>();
		logger.debug("Received HTTP GET request at '{}'.", UriBuilder.fromUri(uri).build().toASCIIString());
		ModelRepository modelRepository = HABminApplication.getModelRepository();
		for (String modelName : modelRepository.getAllModelNamesOfType("sitemap")) {
			Sitemap sitemap = (Sitemap) modelRepository.getModel(modelName);
			if (sitemap != null) {
				SitemapBean bean = new SitemapBean();
				bean.name = StringUtils.removeEnd(modelName, SITEMAP_FILEEXT);
				bean.label = sitemap.getLabel();
				bean.icon = sitemap.getIcon();
				bean.link = UriBuilder.fromUri(uri).path(bean.name).build().toASCIIString();
				beans.add(bean);
			}
		}
		return beans;
	}

	public Collection<SitemapBean> deleteSitemap(String sitemapname, URI uri) {
		String orgName = new String("configurations/sitemaps/" + sitemapname + SITEMAP_FILEEXT);
		String bakName = new String("configurations/sitemaps/" + sitemapname + SITEMAP_FILEEXT + ".bak");

		// Delete the sitemap
		File orgFile = new File(orgName);
		File bakFile = new File(bakName);

		// Rename the file
		orgFile.renameTo(bakFile);

		// Update the model repository
		ModelRepository repo = HABminApplication.getModelRepository();
		repo.removeModel(sitemapname + SITEMAP_FILEEXT);

		// Now return the sitemap list
		return getSitemapBeans(uri);
	}

	private Collection<SitemapBean> createSitemap(String sitemapname, String copyname, URI uri) {
		String fname = new String("configurations/sitemaps/" + sitemapname + SITEMAP_FILEEXT);

		try {
			List<String> sitemapData;
			if (copyname != null && !copyname.isEmpty()) {
				String fcopyname = new String("configurations/sitemaps/" + copyname + SITEMAP_FILEEXT);

				sitemapData = IOUtils.readLines(new FileInputStream(fcopyname));

				// Now find the sitemap name and replace it!
				for (int cnt = 0; cnt < sitemapData.size(); cnt++) {
					Matcher matcher = SITEMAP_DEFINITION.matcher(sitemapData.get(cnt));

					if (matcher.matches()) {
						sitemapData.set(cnt, "sitemap " + sitemapname + " label=\"" + matcher.group(2) + "\"");
						break;
					}
				}
			} else {
				// Default to a new file
				sitemapData = new ArrayList<String>();
				sitemapData.add("sitemap " + sitemapname + " label=\"Main Menu\"");
				sitemapData.add("{");
				sitemapData.add("}");
			}

			// Check if the file exists
			File file = new File(fname);
			if (!file.exists()) {
				// Create the new sitemap
				FileWriter fw;
				fw = new FileWriter(file, false);
				BufferedWriter out = new BufferedWriter(fw);

				IOUtils.writeLines(sitemapData, "\r\n", out);

				out.close();

				// Update the model repository
				ModelRepository repo = HABminApplication.getModelRepository();
				if (repo != null) {
					InputStream inFile;
					try {
						inFile = new FileInputStream(fname);
						repo.addOrRefreshModel(sitemapname + SITEMAP_FILEEXT, inFile);
					} catch (FileNotFoundException e) {
						logger.debug("Error refreshing new sitemap " + sitemapname + ":", e);
					}
				}
			}
		} catch (IOException e) {
			logger.debug("Error writing to sitemap file " + sitemapname + ":", e);
		}

		// Now return the sitemap list
		return getSitemapBeans(uri);
	}

	private void writeColor(BufferedWriter out, String colorName, List<ColorBean> colorList) throws IOException {
		if (colorList == null || colorList.size() == 0)
			return;

		boolean first = true;
		for (ColorBean color : colorList) {
			if (color.state == null && color.color == null)
				continue;
			if (first == true)
				out.write(colorName + "=[");
			else
				out.write(", ");

			if(color.color == null)
				out.write("\"" + color.state + "\"");
			else
				out.write(color.state + "=\"" + color.color + "\"");

			first = false;
		}
		if (first == false)
			out.write("]");
	}

	private void writeWidget(BufferedWriter out, java.util.List<WidgetConfigBean> widgets, int level) {
		String indent = new String();
		for (int c = 0; c < level; c++)
			indent += "\t";

		for (WidgetConfigBean child : widgets) {
			try {
				out.write(indent + child.type + " ");
				if (child.item != null && !child.item.isEmpty())
					out.write("item=" + child.item + " ");

				if (child.label != null && !child.label.isEmpty()) {
					LabelSplitHelper label = new LabelSplitHelper(child.label, child.format, child.units,
							child.translateService, child.translateRule);
					out.write("label=\"" + label.getLabelString() + "\" ");
				}
				
				writeColor(out, "iconcolor", child.iconcolor);
				writeColor(out, "valuecolor", child.valuecolor);
				writeColor(out, "labelcolor", child.labelcolor);

				if (child.visibility != null && child.visibility.size() != 0) {
					boolean first = true;
					for (VisibilityBean visibility : child.visibility) {
						if (visibility.item == null || visibility.state == null)
							continue;
						if (first == true)
							out.write("visibility=[");
						else
							out.write(", ");

						out.write(visibility.item + "=" + visibility.state);

						first = false;
					}
					if (first == false)
						out.write("]");
				}
				
				if (child.icon != null && !child.icon.isEmpty())
					out.write("icon=\"" + child.icon + "\" ");

				if (child.type.equals("Setpoint")) {
					if (child.minValue != null)
						out.write("minValue=" + child.minValue + " ");
					if (child.maxValue != null)
						out.write("maxValue=" + child.maxValue + " ");
					if (child.step != null)
						out.write("step=" + child.step + " ");
				}

				if (child.type.equals("Image") || child.type.equals("Video") || child.type.equals("Webview")) {
					if (child.url != null)
						out.write("url=" + child.url + " ");

					if (child.urlarray != null && child.urlarray.size() != 0) {
						boolean first = true;
						for (UrlBean url : child.urlarray) {
							if (url.state == null || url.url == null)
								continue;
							if (first == true)
								out.write("urlarray=[");
							else
								out.write(", ");

							out.write(url.state + "=\"" + url.url + "\"");

							first = false;
						}
						if (first == false)
							out.write("]");
					}
				}

				if (child.type.equals("Selection") || child.type.equals("Switch")) {
					if (child.mapping != null && child.mapping.size() != 0) {
						boolean first = true;
						for (MappingBean map : child.mapping) {
							if (map.command == null || map.label == null)
								continue;
							if (first == true)
								out.write("mappings=[");
							else
								out.write(", ");

							out.write(map.command + "=" + map.label);

							first = false;
						}
						if (first == false)
							out.write("]");
					}
				}
				
				if (child.type.equals("Slider")) {
					if(child.switchSupport)
						out.write("switchSupport ");
					if(child.sendFrequency != 0)
						out.write("sendFrequency=" + child.sendFrequency);
				}

				if (child.type.equals("Group") || child.type.equals("Frame") || child.type.equals("Text")
						|| child.type.equals("Image")) {
					if (child.widgets != null && child.widgets.size() != 0) {
						out.write("{\r\n");
						writeWidget(out, child.widgets, level + 1);
						out.write(indent + "}");
					}
				}
				out.write(indent + "\r\n");
			} catch (IOException e) {
				logger.debug("Error writing sitemap :", e);
			}
		}
	}

	public WidgetConfigBean saveSitemap(String sitemapname, URI uri, WidgetConfigBean sitemap) {
		String orgName = new String("configurations/sitemaps/" + sitemapname + SITEMAP_FILEEXT);
		String newName = new String("configurations/sitemaps/" + sitemapname + SITEMAP_FILEEXT + ".new");
		String bakName = new String("configurations/sitemaps/" + sitemapname + SITEMAP_FILEEXT + ".bak");

		if (sitemap == null)
			return null;

		if (sitemap.widgets == null)
			return null;

		// Check if the file exists
		File file = new File(newName);
		// Create the new sitemap
		FileWriter fw;
		try {
			fw = new FileWriter(file, false);
			BufferedWriter out = new BufferedWriter(fw);

			WidgetConfigBean main = sitemap.widgets.get(0);

			out.write("sitemap " + sitemapname);
			if(main.label != null && main.label.isEmpty() == false)
				out.write(" label=\"" + main.label + "\"");
			if(main.icon != null && main.icon.isEmpty() == false)
				out.write(" icon=\"" + main.icon + "\"");

			out.write("\r\n{\r\n");

			writeWidget(out, main.widgets, 1);

			out.write("}\r\n");
			out.close();
		} catch (IOException e) {
			logger.debug("Error writing to sitemap file " + sitemapname + ":", e);
		}

		// Rename the files.
		File bakFile = new File(bakName);
		File orgFile = new File(orgName);
		File newFile = new File(newName);

		// Delete any existing .bak file
		if (bakFile.exists())
			bakFile.delete();

		// Rename the existing item file to backup
//		orgFile.renameTo(bakFile);

		// Rename the new file to the item file
//		newFile.renameTo(orgFile);

		// Update the model repository
		ModelRepository repo = HABminApplication.getModelRepository();
		if (repo != null) {
			InputStream inFile;
			try {
				inFile = new FileInputStream(newName);
				repo.addOrRefreshModel(sitemapname + SITEMAP_FILEEXT, inFile);
			} catch (FileNotFoundException e) {
				logger.debug("Error refreeshing new sitemap " + sitemapname + ":", e);
			}
		}

		// Now return the sitemap
		return getSitemap(sitemapname, uri);
	}

	static private WidgetConfigBean getSitemap(String sitemapName, URI uri) {
		WidgetConfigBean bean = new WidgetConfigBean();
		Sitemap sitemap = getSitemap(sitemapName);
		bean.label = sitemap.getLabel();
		bean.icon = sitemap.getIcon();
		if (sitemap.getChildren() != null) {
			for (Widget widget : sitemap.getChildren()) {
				WidgetConfigBean subWidget = createWidgetBean(sitemapName, widget, uri);
				bean.widgets.add(subWidget);
			}
		}
		return bean;
	}

	static private WidgetConfigBean createWidgetBean(String sitemapName, Widget widget, URI uri) {
		ItemUIRegistry itemUIRegistry = HABminApplication.getItemUIRegistry();
		WidgetConfigBean bean = new WidgetConfigBean();
		if (widget.getItem() != null) {
			Item item = null;
			try {
				item = itemUIRegistry.getItem(widget.getItem());
			} catch (ItemNotFoundException e) {
			}
			if (item != null) {
				bean.item = item.getName();
			}
		}
		bean.icon = widget.getIcon();

		// Split the label into its constituent parts
		if (widget.getLabel() != null) {
			LabelSplitHelper label = new LabelSplitHelper(widget.getLabel());
			bean.label = label.getLabel();
			bean.format = label.getFormat();
			bean.units = label.getUnit();
			bean.translateService = label.getTranslationService();
			bean.translateRule = label.getTranslationRule();
		}

		// Add the visibility rules
		if (widget.getVisibility() != null && widget.getVisibility().size() > 0) {
			bean.visibility = new ArrayList<VisibilityBean>();
			for (VisibilityRule visibility : widget.getVisibility()) {
				VisibilityBean visibilityBean = new VisibilityBean();
				visibilityBean.item = visibility.getItem();
				visibilityBean.state = visibility.getState();
				bean.visibility.add(visibilityBean);
			}
		}

		// Add icon color array
		if (widget.getIconColor() != null && widget.getIconColor().size() > 0) {
			bean.iconcolor = new ArrayList<ColorBean>();
			for (ColorArray color : widget.getIconColor()) {
				ColorBean colorBean = new ColorBean();
				colorBean.state = color.getState();
				colorBean.color = color.getArg();
				if(colorBean.state.startsWith("\"") && colorBean.state.endsWith("\""))
					colorBean.state = colorBean.state.substring(1, colorBean.state.length()-1);
				bean.iconcolor.add(colorBean);
			}
		}

		// Add label color array
		if (widget.getLabelColor() != null && widget.getLabelColor().size() > 0) {
			bean.labelcolor = new ArrayList<ColorBean>();
			for (ColorArray color : widget.getLabelColor()) {
				ColorBean colorBean = new ColorBean();
				colorBean.state = color.getState();
				colorBean.color = color.getArg();
				if(colorBean.state.startsWith("\"") && colorBean.state.endsWith("\""))
					colorBean.state = colorBean.state.substring(1, colorBean.state.length()-1);
				bean.labelcolor.add(colorBean);
			}
		}

		// Add value color array
		if (widget.getValueColor() != null && widget.getValueColor().size() > 0) {
			bean.valuecolor = new ArrayList<ColorBean>();
			for (ColorArray color : widget.getValueColor()) {
				ColorBean colorBean = new ColorBean();
				colorBean.state = color.getState();
				colorBean.color = color.getArg();
				if(colorBean.state.startsWith("\"") && colorBean.state.endsWith("\""))
					colorBean.state = colorBean.state.substring(1, colorBean.state.length()-1);
				bean.valuecolor.add(colorBean);
			}
		}

		bean.type = widget.eClass().getName();
		if (widget instanceof LinkableWidget) {
			LinkableWidget linkableWidget = (LinkableWidget) widget;
			EList<Widget> children = itemUIRegistry.getChildren(linkableWidget);
			for (Widget child : children) {
				bean.widgets.add(createWidgetBean(sitemapName, child, uri));
			}
		}
		if (widget instanceof Switch) {
			Switch switchWidget = (Switch) widget;
			for (Mapping mapping : switchWidget.getMappings()) {
				MappingBean mappingBean = new MappingBean();
				mappingBean.command = mapping.getCmd();
				mappingBean.label = mapping.getLabel();
				bean.mapping.add(mappingBean);
			}
		}
		if (widget instanceof Selection) {
			Selection selectionWidget = (Selection) widget;
			for (Mapping mapping : selectionWidget.getMappings()) {
				MappingBean mappingBean = new MappingBean();
				mappingBean.command = mapping.getCmd();
				mappingBean.label = mapping.getLabel();
				bean.mapping.add(mappingBean);
			}
		}
		if (widget instanceof Slider) {
			Slider sliderWidget = (Slider) widget;
			bean.sendFrequency = sliderWidget.getFrequency();
			bean.switchSupport = sliderWidget.isSwitchEnabled();
		}
		if (widget instanceof List) {
			org.openhab.model.sitemap.List listWidget = (org.openhab.model.sitemap.List) widget;
			bean.separator = listWidget.getSeparator();
		}
		if (widget instanceof Image) {
			Image imageWidget = (Image) widget;
			bean.url = imageWidget.getUrl();
			if (imageWidget.getRefresh() > 0) {
				bean.refresh = imageWidget.getRefresh();
			}

			// Add url array
			if (imageWidget.getUrlArray() != null && imageWidget.getUrlArray().size() > 0) {
			for (ConditionArray url : imageWidget.getUrlArray()) {
				UrlBean urlBean = new UrlBean();
				urlBean.state = url.getState();
				urlBean.url = url.getArg();
				bean.urlarray.add(urlBean);
			}
			}
		}
		if (widget instanceof Video) {
			Video videoWidget = (Video) widget;
			bean.url = videoWidget.getUrl();

			// Add url array
			if (videoWidget.getUrlArray() != null && videoWidget.getUrlArray().size() > 0) {
			for (ConditionArray url : videoWidget.getUrlArray()) {
				UrlBean urlBean = new UrlBean();
				urlBean.state = url.getState();
				urlBean.url = url.getArg();
				bean.urlarray.add(urlBean);
			}
			}
		}
		if (widget instanceof Webview) {
			Webview webViewWidget = (Webview) widget;
			bean.url = webViewWidget.getUrl();
			bean.height = webViewWidget.getHeight();

			// Add url array
			//if (webviewWidget.getUrlArray() != null && webviewWidget.getUrlArray().size() > 0) {
			// for (ConditionArray url : webViewWidget.getUrlArray()) {
			// UrlBean urlBean = new UrlBean();
			// urlBean.state = url.getState();
			// urlBean.url = url.getArg();
			// bean.urlarray.add(urlBean);
			// }
			// }
		}
		if (widget instanceof Chart) {
			Chart chartWidget = (Chart) widget;
			bean.service = chartWidget.getService();
			bean.period = chartWidget.getPeriod();
			if (chartWidget.getRefresh() > 0) {
				bean.refresh = chartWidget.getRefresh();
			}
		}
		if (widget instanceof Setpoint) {
			Setpoint setpointWidget = (Setpoint) widget;
			bean.minValue = setpointWidget.getMinValue();
			bean.maxValue = setpointWidget.getMaxValue();
			bean.step = setpointWidget.getStep();
		}
		return bean;
	}

	static public Sitemap getSitemap(String sitemapname) {
		ModelRepository repo = HABminApplication.getModelRepository();
		if (repo != null) {
			Sitemap sitemap = (Sitemap) repo.getModel(sitemapname + SITEMAP_FILEEXT);
			return sitemap;
		}
		return null;
	}
}
