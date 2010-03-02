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
import org.openhab.core.library.items.SwitchItem;

public class MhtFileParser {

	private static final String MAP_ITEM = "item";

	public static GenericItem[] parse(InputStream is) throws IOException, ParserException {
		List<GenericItem> items = new ArrayList<GenericItem>();
		LineIterator iterator = IOUtils.lineIterator(is, "ISO-8859-1");
		checkCorrectFormat(iterator);
		while(iterator.hasNext()) {
			String line = iterator.nextLine();
			Map<String, ?> lineContents = parseLine(line);
			if(lineContents!=null && !lineContents.isEmpty()) {
				items.add((GenericItem) lineContents.get(MAP_ITEM));
			}
		}
		return items.toArray(new GenericItem[items.size()]);
	}

	private static Map<String, ?> parseLine(String line) {
		line = line.trim();
		if(line.isEmpty()) return null;
		if(line.startsWith("#")) return null;

		String[] segments = line.split(",");
		String type = segments[0].trim();
		if(type.equalsIgnoreCase("EIB1")) return processEIB1(segments);
		
		return null;
	}

	private static Map<String, ?> processEIB1(String[] segments) {
		Map<String, Object> lineContents = new HashMap<String, Object>();
		lineContents.put(MAP_ITEM, new SwitchItem(segments[2].trim()));
		return lineContents;
	}

	private static void checkCorrectFormat(LineIterator iterator) throws ParserException {
		if(iterator.hasNext()) {
			String firstLine = iterator.nextLine();
			if(firstLine.trim().equals("Format = A")) return;
		}
		throw new ParserException("Config file does not start with 'Format = A'");
	}
}
