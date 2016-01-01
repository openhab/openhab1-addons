/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ebus.tools;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openhab.binding.ebus.internal.parser.EBusConfigurationProvider;

/**
 * eBUS Helper Script to check configurations and create markdown files
 * 
 * @author Christian Sowada
 * @since 1.7.1
 */
public class EBusJsonConfTool {

	static String ALLOWED_LEVEL_1_PARAMS[] = {"comment", "device", "id", "class", "command", "data", "values", "computed_values", "debug"};


	/**
	 * @param args
	 */
	public static void main(String[] args) {

		EBusJsonConfTool m = new EBusJsonConfTool();
		try {
			m.run();

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	private List<List<String>> createTelegramIdTable(List<Map<String, ?>> configurationMap) {

		List<List<String>> m = new ArrayList<List<String>>();

		for (Map<String, ?> entry : configurationMap) {

			Map<String, Map<String, ?>> valueEntries = new HashMap<String, Map<String,?>>();

			Map<String, Map<String, ?>> x =  (Map<String, Map<String, ?>>)entry.get("values");
			if(x != null && !x.isEmpty()) {
				valueEntries.putAll(x);
			}

			boolean singleValue = x!= null ? x.size() == 1 : false;
			
			x =  (Map<String, Map<String, ?>>)entry.get("computed_values");
			if(x != null && !x.isEmpty()) {
				valueEntries.putAll(x);
			}

			for (String valueKey : valueEntries.keySet()) {
				Map<String, ?> map = valueEntries.get(valueKey);

				if(!map.containsKey("hide") && !StringUtils.startsWith(valueKey, "_")) {
					List<String> line = new ArrayList<String>();
					m.add(line);

					if(entry.containsKey("class") && entry.containsKey("id")) {
						if(singleValue && !((String)entry.get("id")).startsWith("set_")) {
							line.add("**" + (String) entry.get("class") + "." + (String) entry.get("id") + "**");
						} else {
							line.add("**" + (String) entry.get("class") + "." + (String) entry.get("id") + "**." + valueKey);
						}
					} else if(entry.containsKey("class") ) {
							line.add((String) entry.get("class") + "." + valueKey);
						
					} else {
						line.add(valueKey);
					}
					
//					if(!StringUtils.isEmpty((String) entry.get("id"))) {
//						line.add((String) entry.get("class") + "." + (String) entry.get("id"));
//					} else {
//						line.add("---");
//					}
//					line.add("---");
					//line.add((String) entry.get("class"));
					//line.add(StringUtils.defaultIfEmpty((String) entry.get("id"), "-"));

					String t = (String) map.get("type");
					if(map.containsKey("type_hint")) {
						t = (String) map.get("type_hint");
					}

					t = t.equalsIgnoreCase("bit") ? "Switch" : t.equalsIgnoreCase("script") ? "???" : t.equalsIgnoreCase("string") ? "Text" : "Number";
					t = "``" + t + "``";
					line.add(t);

					t = (String) map.get("label");
					t = t == null || t.equals("") ? "": t;
					
					if(map.containsKey("mapping")) {
						t += " - " + map.get("mapping").toString();
					}
					
					line.add(t);
				}
			}
		}

		return m;
	}

	private void writeMarkdownIdTable(List<Map<String, ?>> configurationMap, PrintStream out) {

		if(configurationMap == null || configurationMap.isEmpty())
			return;
		
		List<List<String>> table = createTelegramIdTable(configurationMap);

		// set sort order by column number
		final int sortColumns[] = new int[] {1, 0};
		Collections.sort(table, new Comparator<List<String>>() {
			@Override
			public int compare(List<String>  line1, List<String>  line2) {
				int compareTo = 0;
				for (int i : sortColumns) {
					compareTo = ObjectUtils.compare(line1.get(i), line2.get(i));
					if(compareTo != 0) {
						return compareTo;
					}
				}
				return compareTo;
			}
		});

		// Add headers
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("ID");
		//arrayList.add("Class");
//		arrayList.add("Command");
		arrayList.add("Item type");
		arrayList.add("Description");
		table.add(0, arrayList);

		// Add Markdown header delimiters
		arrayList = new ArrayList<String>();
		for (int i = 0; i < table.get(0).size(); i++) {
			arrayList.add("---");
		}
		table.add(1, arrayList);

		// compute max colum width
		int columnWidth[] = new int[arrayList.size()];
		for (List<String> column : table) {
			for (int i = 0; i < column.size(); i++) {
				String elm = column.get(i);
				if(elm != null && elm.length() > columnWidth[i]) {
					columnWidth[i] = elm.length();
				}
			}
		}

		for (List<String> column : table) {
			for (int i = 0; i < column.size(); i++) {
				String elm = column.get(i);
				out.print(String.format("%-"+columnWidth[i]+"s", elm));
				if(i < column.size()-1) {
					out.print(" | ");
				}
			}

			// line end
			out.print("\n");
		}
	}

	private void checkConfiguration(List<Map<String, ?>> configurationMap) {

		Map<String, String> doubleEntries = new HashMap<String,String>();

		for (Map<String, ?> entryMap : configurationMap) {

			StringBuilder sb = new StringBuilder();
			String unid = (String) (entryMap.containsKey("command") ? entryMap.get("command") + " XX " + entryMap.get("data") : entryMap.get("filter"));

			if(doubleEntries.containsKey(unid)) {
				sb.append("Combination of command and data already existing [" + doubleEntries.get(unid) + "]... " + unid + "\n");
			} else {
				doubleEntries.put(unid, (String) entryMap.get("comment"));
			}

			if(!entryMap.containsKey("comment") || entryMap.get("comment").equals("") ) {
				sb.append("No comment item ...\n");
			}

			if(entryMap.containsKey("debug")) {
				sb.append("Remove debug switch ...\n");
			}

			if(sb.length() > 0) {
				System.err.println("Error/Warning on item " + entryMap.get("comment"));
				System.err.println(sb.toString());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void run() throws JsonParseException, JsonMappingException, IOException {

		final File currentDir = new File("").getAbsoluteFile();
		final ObjectMapper mapper = new ObjectMapper();
		
		List<Map<String, ?>> configurationMap =  new ArrayList<Map<String, ?>>();
		
		File folder = new File(currentDir, "src/main/resources/");
		File[] listOfFiles = folder.listFiles();
		PrintStream out = null;
		
		PrintStream mainOut = new PrintStream(new File(currentDir, "docs/json-configs.md"));
		mainOut.print("# JSON configuration files\n");
		mainOut.print("\n");
		mainOut.print("This is an automatic created list of all included configuration files.\n");
		mainOut.print("\n");
		
		EBusConfigurationProvider ebuscfg = new EBusConfigurationProvider();

		for (File file : listOfFiles) {
			if(file.getName().endsWith("configuration.json")) {
				String string = StringUtils.substringBefore(file.getName(), "-configuration.json");
				out = new PrintStream(new File(currentDir, "docs/json-files/" + string + ".md"));

				out.print("# JSON configuration for _" + string + "_\n");
				out.print("\n");
				
				ebuscfg.loadConfigurationFile(file.toURI().toURL());

				
				List<Map<String, ?>> readValue = mapper.readValue(file, List.class);
				writeMarkdownIdTable(readValue, out);
				
				out.print("\n");
				out.print("_bold part is the command-id part_\n");
				out.print("\n");
				
				configurationMap.addAll(readValue);
				out.flush();
				out.close();
				
				mainOut.print("* ["+ string + "](./json-files/"+ string + ".md)\n");
			}


			
		}

		mainOut.println();
		mainOut.flush();
		mainOut.close();

		checkConfiguration(configurationMap);
	}

}