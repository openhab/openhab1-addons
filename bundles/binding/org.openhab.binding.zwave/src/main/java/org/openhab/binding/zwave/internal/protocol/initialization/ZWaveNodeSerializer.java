/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.initialization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveMeterCommandClass.MeterScale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * ZWaveNodeSerializer class. Serializes nodes to XML and back again.
 * 
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public class ZWaveNodeSerializer {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveNodeSerializer.class);
	private final XStream stream = new XStream(new StaxDriver());
	private String folderName = "etc/zwave";

	/**
	 * Constructor. Creates a new instance of the {@link ZWaveNodeSerializer}
	 * class.
	 */
	public ZWaveNodeSerializer() {
		logger.trace("Initializing ZWaveNodeSerializer.");

		// Change the folder for OH2
		// ConfigConstants.getUserDataFolder();
		final String USERDATA_DIR_PROG_ARGUMENT = "smarthome.userdata";
		final String eshUserDataFolder = System.getProperty(USERDATA_DIR_PROG_ARGUMENT);
		if (eshUserDataFolder != null) {
		    folderName = eshUserDataFolder + "/zwave";
		}

		final File folder = new File(folderName);

		// create path for serialization.
		if (!folder.exists()) {
			logger.debug("Creating directory {}", folderName);
			folder.mkdirs();
		}
		stream.processAnnotations(ZWaveNode.class);
		stream.processAnnotations(ZWaveEndpoint.class);
		stream.processAnnotations(ZWaveDeviceClass.class);
		stream.processAnnotations(ZWaveCommandClass.class);
		stream.processAnnotations(CommandClass.class);
		for (CommandClass commandClass : CommandClass.values()) {
			Class<? extends ZWaveCommandClass> cc = commandClass.getCommandClassClass();

			if (cc == null) {
				continue;
			}

			stream.processAnnotations(cc);
			for (Class<?> inner : cc.getDeclaredClasses()) {
				stream.processAnnotations(inner);
			}
		}
		stream.processAnnotations(MeterScale.class);
		logger.trace("Initialized ZWaveNodeSerializer.");
	}

	/**
	 * Serializes an XML tree of a {@link ZWaveNode}
	 * 
	 * @param node
	 *            the node to serialize
	 */
	public void SerializeNode(ZWaveNode node) {
		synchronized (stream) {
			// Don't serialise if the stage is not at least finished static
			// If we do serialise when we haven't completed the static stages
			// then when the binding starts it will have incomplete information!
			if(node.getNodeInitializationStage().isStaticComplete() == false) {
				logger.debug("NODE {}: Serialise aborted as static stages not complete", node.getNodeId());
				return;
			}

			File file = new File(this.folderName, String.format("node%d.xml", node.getNodeId()));
			BufferedWriter writer = null;

			logger.debug("NODE {}: Serializing to file {}", node.getNodeId(), file.getPath());

			try {
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
				stream.marshal(node, new PrettyPrintWriter(writer));
				writer.flush();
			} catch (IOException e) {
				logger.error("NODE {}: Error serializing to file: {}", node.getNodeId(), e.getMessage());
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	/**
	 * Deserializes an XML tree of a {@link ZWaveNode}
	 * 
	 * @param nodeId
	 *            the number of the node to deserialize
	 * @return returns the Node or null in case Serialization failed.
	 */
	public ZWaveNode DeserializeNode(int nodeId) {
		synchronized (stream) {
			File file = new File(this.folderName, String.format("node%d.xml", nodeId));
			BufferedReader reader = null;

			logger.debug("NODE {}: Serializing from file {}", nodeId, file.getPath());

			if (!file.exists()) {
				logger.debug("NODE {}: Error serializing from file: file does not exist.", nodeId);
				return null;
			}

			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
				return (ZWaveNode)stream.fromXML(reader);
			} catch (IOException e) {
				logger.error("NODE {}: Error serializing from file: {}", nodeId, e.getMessage());
			} finally {
				if (reader != null)
					try {
						reader.close();
					} catch (IOException e) {
					}
			}
			return null;
		}
	}
	
	/**
	 * Deletes the persistence store for the specified node.
	 * 
	 * @param nodeId The node ID to remove
	 * @return true if the file was deleted
	 */
	public boolean DeleteNode(int nodeId) {
		synchronized (stream) {
			File file = new File(this.folderName, String.format("node%d.xml", nodeId));

			return file.delete();
		}
	}
}
