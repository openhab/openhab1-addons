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
 * XML Wrapper for the XML Channel List of LGTV
 * 
 * @author Martin Fluch
 * @since 1.6.0
 */
public class LgTvChannelSet {

	private static Logger logger = LoggerFactory.getLogger(LgtvConnection.class);

	@XmlRootElement(name = "envelope")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class envelope {

		@XmlElementWrapper(name = "dataList")
		@XmlElement(name = "data")
		private List<onechannel> channels;

		public List<onechannel> getList() {
			return channels;
		}

		public onechannel find(int major) {
			onechannel found = null;

			for (onechannel e : channels) {
				if (e.major == major)
					found = e;
			}

			return found;

		}

		public int size() {
			return channels.size();
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class onechannel {
		@XmlElement(name = "chtype")
		private String chtype;

		@XmlElement(name = "major")
		private int major;

		@XmlElement(name = "minor")
		private int minor;

		@XmlElement(name = "sourceIndex")
		private int sourceindex;

		@XmlElement(name = "physicalNum")
		private int physicalnum;

		@XmlElement(name = "chname")
		private String chname;

		public int getmajor() {
			return major;
		}

		public int getminor() {
			return minor;
		}

		public int getsourceindex() {
			return sourceindex;
		}

		public int getphysicalnum() {
			return physicalnum;
		}
	}

	private envelope envel;

	public int getsize() {
		if (envel != null && envel.channels != null)
			return envel.channels.size();
		else
			return 0;
	}

	/**
	 * Load Channels from s into List
	 * 
	 * @param s
	 * @throws JAXBException
	 */
	public void loadchannels(String s) throws JAXBException {

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

	/**
	 * Save Channel List to File f
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
			logger.error("error in savetofile", e);
		} catch (JAXBException e) {
			logger.error("error in savetofile", e);
		} catch (IOException ex) {
			logger.error("error in savetofile", ex);
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
