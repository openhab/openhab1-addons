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

package org.openhab.config.misterhouse.internal.items;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.MeasurementItem;
import org.openhab.core.library.items.RollerblindItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;

public class MhtFileParser {

	protected static final Map<String, String> iconMap = new HashMap<String, String>();
	protected static final Map<String, String> labelMap = new HashMap<String, String>();
	
	private static final String MAP_ITEM = "item";

	public static GenericItem[] parse(InputStream is) throws IOException, ParserException {
		List<GenericItem> items = new ArrayList<GenericItem>();
		Map<String, GroupItem> groups = new  HashMap<String, GroupItem>();
		LineIterator iterator = IOUtils.lineIterator(is, "ISO-8859-1");
		checkCorrectFormat(iterator);
		while(iterator.hasNext()) {
			String line = iterator.nextLine();
			Map<String, ?> lineContents = parseLine(line, groups);
			if(lineContents!=null && !lineContents.isEmpty()) {
				items.add((GenericItem) lineContents.get(MAP_ITEM));
			}
		}
		return items.toArray(new GenericItem[items.size()]);
	}

	private static Map<String, ?> parseLine(String line, Map<String, GroupItem> groups) throws ParserException {
		line = line.trim();
		if(line.isEmpty()) return null;
		if(line.startsWith("#")) return null;

		String[] segments = line.split(",");
		String type = segments[0].trim();

		if(type.equalsIgnoreCase("GROUP")) return processGroup(segments, groups);

		if(type.equalsIgnoreCase("EIB1")) return processEIB1(segments, groups);
		if(type.equalsIgnoreCase("EIB1G")) return processEIB1G(segments, groups);
		if(type.equalsIgnoreCase("EIB2")) return processEIB2(segments, groups);
		if(type.equalsIgnoreCase("EIB5")) return processEIB5(segments, groups);
		if(type.equalsIgnoreCase("EIB7")) return processEIB7(segments, groups);
		if(type.equalsIgnoreCase("EIB15")) return processEIB15(segments, groups);

		if(type.equalsIgnoreCase("BT")) return processBT(segments, groups);

		return null;
	}

	/* reads the paramString and puts "icon=xxx" and "label=yyy" entries into the local
	 * hashmap, if these are found.
	 */
	private static void retrieveIconAndLabel(String groupName, String paramString) {
		if(paramString!=null && !paramString.equals("")) {
			String[] parameters = paramString.split("\\|");
			for(String param : parameters) {
				param= param.trim();
				if(param.startsWith("icon=")) iconMap.put(groupName, param.substring("icon=".length()).trim());
				if(param.startsWith("label=")) labelMap.put(groupName, param.substring("label=".length()).trim());
			}
		}
	}

	private static Map<String, ?> processGroup(String[] segments, Map<String, GroupItem> groups) {
		String groupName = segments[1].trim();
		GroupItem groupItem = new GroupItem(groupName);
		if(segments.length>2) {
			GroupItem parentGroupItem = groups.get(segments[2].trim());
			if(parentGroupItem!=null) {
				parentGroupItem.addMember(groupItem);
			}
		}
		groups.put(groupName, groupItem);
		if(segments.length>3) retrieveIconAndLabel(groupName, segments[3]);
		Map<String, Object> lineContents = new HashMap<String, Object>();
		lineContents.put(MAP_ITEM, groupItem);
		return lineContents;
	}

	private static Map<String, ?> processEIB1(String[] segments, Map<String, GroupItem> groups) throws ParserException {
		SwitchItem item = new SwitchItem(segments[2].trim());
		if(segments.length>3) {
			processGroupMemberships(segments[3], groups, item);
		}
		if(segments.length>4) retrieveIconAndLabel(segments[2].trim(), segments[4]);
		Map<String, Object> lineContents = new HashMap<String, Object>();
		lineContents.put(MAP_ITEM, item);
		return lineContents;
	}

	private static Map<String, ?> processEIB1G(String[] segments, Map<String, GroupItem> groups) throws ParserException {
		SwitchItem item = new SwitchItem(segments[2].trim());
		if(segments.length>3) {
			processGroupMemberships(segments[3], groups, item);
		}
		if(segments.length>4) retrieveIconAndLabel(segments[2].trim(), segments[4]);
		Map<String, Object> lineContents = new HashMap<String, Object>();
		lineContents.put(MAP_ITEM, item);
		return lineContents;
	}

	private static Map<String, ?> processEIB2(String[] segments, Map<String, GroupItem> groups) throws ParserException {
		SwitchItem item = new DimmerItem(segments[2].trim());
		if(segments.length>3) {
			processGroupMemberships(segments[3], groups, item);
		}
		if(segments.length>4) retrieveIconAndLabel(segments[2].trim(), segments[4]);
		Map<String, Object> lineContents = new HashMap<String, Object>();
		lineContents.put(MAP_ITEM, item);
		return lineContents;
	}

	private static Map<String, ?> processEIB5(String[] segments, Map<String, GroupItem> groups) throws ParserException {
		MeasurementItem item = new MeasurementItem(segments[2].trim());
		if(segments.length>3) {
			processGroupMemberships(segments[3], groups, item);
		}
		if(segments.length>4) retrieveIconAndLabel(segments[2].trim(), segments[4]);
		Map<String, Object> lineContents = new HashMap<String, Object>();
		lineContents.put(MAP_ITEM, item);
		return lineContents;
	}

	private static Map<String, ?> processEIB7(String[] segments, Map<String, GroupItem> groups) throws ParserException {
		RollerblindItem item = new RollerblindItem(segments[2].trim());
		if(segments.length>3) {
			processGroupMemberships(segments[3], groups, item);
		}
		if(segments.length>4) retrieveIconAndLabel(segments[2].trim(), segments[4]);
		Map<String, Object> lineContents = new HashMap<String, Object>();
		lineContents.put(MAP_ITEM, item);
		return lineContents;
	}

	private static Map<String, ?> processEIB15(String[] segments,
			Map<String, GroupItem> groups) throws ParserException {
		StringItem item = new StringItem(segments[2].trim());
		if(segments.length>3) {
			processGroupMemberships(segments[3], groups, item);
		}
		if(segments.length>4) retrieveIconAndLabel(segments[2].trim(), segments[4]);
		Map<String, Object> lineContents = new HashMap<String, Object>();
		lineContents.put(MAP_ITEM, item);
		return lineContents;
	}

	private static Map<String, ?> processBT(String[] segments,
			Map<String, GroupItem> groups) throws ParserException {
		StringItem item = new StringItem(segments[2].trim());
		if(segments.length>3) {
			processGroupMemberships(segments[3], groups, item);
		}
		if(segments.length>4) retrieveIconAndLabel(segments[2].trim(), segments[4]);
		Map<String, Object> lineContents = new HashMap<String, Object>();
		lineContents.put(MAP_ITEM, item);
		return lineContents;
	}

	private static void processGroupMemberships(String groupsSegment,
			Map<String, GroupItem> groups, Item item)
			throws ParserException {
		for(String groupName : groupsSegment.split("\\|")) {
			groupName = groupName.trim();
			if(groupName.isEmpty()) continue;
			GroupItem group = groups.get(groupName);
			if(group!=null) {
				group.addMember(item);
			} else {
				throw new ParserException("Item '" + item.getName() +
						"' is associated to a group that does not exist: '" + groupName + "'");
			}
		}
	}

	private static void checkCorrectFormat(LineIterator iterator) throws ParserException {
		if(iterator.hasNext()) {
			String firstLine = iterator.nextLine();
			if(firstLine.trim().equals("Format = A")) return;
		}
		throw new ParserException("Config file does not start with 'Format = A'");
	}
}
