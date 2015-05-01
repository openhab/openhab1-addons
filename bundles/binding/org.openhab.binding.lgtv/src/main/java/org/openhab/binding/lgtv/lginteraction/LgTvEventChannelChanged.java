/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lgtv.lginteraction;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class handles xml encoded channel changed messages receiver.
 * 
 * @author Martin Fluch
 * @since 1.6.0
 */
public class LgTvEventChannelChanged {

	@XmlRootElement(name = "envelope")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class envelope {

		@XmlElement(name = "api")
		private onechannel channel;

		public onechannel getchannel() {
			return channel;
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class onechannel {
		@XmlElement(name = "name")
		private String eventname;

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

		public String getchname() {
			return chname;
		}

		public String geteventname() {
			return eventname;
		}
	}

	private envelope envel;

	public String readevent(String s) throws JAXBException {

		JAXBContext jc;
		jc = JAXBContext.newInstance(envelope.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		int start = s.indexOf("<envelope>");
		int stop = s.indexOf("</envelope>") + "</envelope>".length();
		String t = s.substring(start, stop);
		// System.out.println(t);
		StringReader reader = new StringReader(t);
		envel = null;

		envel = (envelope) unmarshaller.unmarshal(reader);

		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter sw = new StringWriter();
		marshaller.marshal(envel, sw);
		return new String(sw.toString());

	}

	public envelope getenvel() {
		return envel;
	}

}
