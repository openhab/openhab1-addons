/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lgtv.lginteraction;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhab.binding.lgtv.internal.LgtvConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XML Wrapper for the XML Application List of LGTV
 * 
 * @author Martin Fluch
 * @since 1.6.0
 */
public class LgTvAppSet {

	private static Logger logger = LoggerFactory.getLogger(LgtvConnection.class);

	@XmlRootElement(name = "envelope")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class envelope {

		@XmlElementWrapper(name = "dataList")
		@XmlElement(name = "data")
		private List<oneapp> apps;

		public List<oneapp> getList() {
			return apps;
		}

		public int size() {
			return apps.size();

		}

		/**
		 * 
		 * @param name
		 * @return
		 */
		public oneapp find(String name) {
			oneapp found = null;

			for (oneapp e : apps) {
				if (e.name.indexOf(name) == 0)
					found = e;
			}

			return found;

		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class oneapp {

		@XmlElement(name = "auid")
		private String id;

		@XmlElement(name = "name")
		private String name;

		@XmlElement(name = "type")
		private int type;

		@XmlElement(name = "cpid")
		private int cpid;

		@XmlElement(name = "adult")
		private String adult;

		@XmlElement(name = "icon_name")
		private String icon_name;

		public String getid() {
			return id;
		}

		public int gettype() {
			return type;
		}

		public int getcpid() {
			return cpid;
		}

		public String getname() {
			return name;
		}

		public String getadult() {
			return adult;
		}

		public String iconname() {
			return icon_name;
		}
	}

	private envelope envel;

	/**
	 * read applications out of string into a list
	 * 
	 * @param s
	 * @throws JAXBException
	 */
	public void loadapps(String s) throws JAXBException {

		JAXBContext jc;
		jc = JAXBContext.newInstance(envelope.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		int start = s.indexOf("<envelope>");
		int stop = s.indexOf("</envelope>") + "</envelope>".length();
		String t = s.substring(start, stop);
		StringReader reader = new StringReader(t);
		envel = null;

		envel = (envelope) unmarshaller.unmarshal(reader);

	}

	public int getsize() {
		if (envel != null && envel.apps != null) {
			return envel.apps.size();
		} else {
			return 0;
		}
	}

	/**
	 * 
	 * Save Application List to File f
	 * 
	 * @param f
	 */
	public void savetofile(String f) {
		Writer writer = null;
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(envelope.class);
			Marshaller marshaller = jc.createMarshaller();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "utf-8"));
			marshaller.marshal(envel, writer);

		} catch (PropertyException e) {
			logger.error("error in xml processing - save file", e);
		} catch (JAXBException e) {
			logger.error("error in xml processing - save file", e);
		} catch (IOException ex) {
			logger.error("error in xml processing - save file", ex);
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
			}
		}

	}

	public envelope getenvel() {
		return envel;
	}

}
