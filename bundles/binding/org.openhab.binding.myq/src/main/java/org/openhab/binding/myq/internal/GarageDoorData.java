/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.myq.internal;

import java.io.IOException;
import java.util.HashMap;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * This Class parses the JSON data and creates a HashMap of Garage Door Opener Devices
 * with the TypeName as the Key. 
 * <ul>
 * <li>success: json request was successful</li>
 * <li>devices: HashMap of Devices</li>
 * </ul>
 * 
 * @author Scott Hanson
 * @since 1.8.0
 */
public class GarageDoorData 
{
	static final Logger logger = LoggerFactory.getLogger(GarageDoorData.class);

	//JSON returnCode was 0 
	private boolean success = false;

	//List<Device> devices;
	HashMap<String, Device> devices = new HashMap<String, Device>();

	/**
	 * Constructor of the GarageDoorData.
	 * 
	 * @param deviceStatusData
	 *            The Json string as it has been returned myq website.
	 */
	public GarageDoorData(String deviceStatusData)
	{
		ObjectMapper mapper = new ObjectMapper();
		try 
		{
			JsonNode rootNode = mapper.readTree(deviceStatusData);
			int ReturnCode = rootNode.get("ReturnCode").asInt();
			logger.debug("myq ReturnCode: "+Integer.toString(ReturnCode));

			if(ReturnCode==0)
			{
				this.success = true;
				if(rootNode.has("Devices"))
				{
					JsonNode node = rootNode.get("Devices");
					//List<JsonNode> devices = node.findValues("MyQDeviceId");
					if(node.isArray())
					{
						int arraysize  = node.size();
						for(int i = 0;i<arraysize;i++)
						{
							int DeviceId = node.get(i).get("MyQDeviceId").asInt();
							String devicetype = RemoveQuotes(node.get(i).get("MyQDeviceTypeName").toString());
							String Devicename = RemoveQuotes(node.get(i).get("DeviceName").toString());
							String typename = RemoveQuotes(node.get(i).get("TypeName").toString());

							logger.debug("DeviceID: " + Integer.toString(DeviceId) + " DeviceName: " + Devicename +" Typename: " + typename);
							if(devicetype.contains("GarageDoorOpener"))
							{
								JsonNode Attributes = node.get(i).get("Attributes");
								if(Attributes.isArray())
								{
									int AttributesSize  = Attributes.size();
									for(int j = 0;j<AttributesSize;j++)
									{
										String AttributeName = RemoveQuotes(Attributes.get(j).get("Name").toString());
										if(AttributeName.contains("doorstate"))
										{
											int doorstate = Attributes.get(j).get("Value").asInt();
											logger.debug("Doorstate : " + Integer.toString(doorstate));
											this.devices.put(typename,new Device(DeviceId,devicetype,Devicename,typename,doorstate));
											break;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		catch (IOException e)
		{
			logger.error("Could not read GarageDoor JSON from MYQ Site.", e);
		}
	}

	public boolean getSuccess() {
		return this.success;
	}

	public HashMap<String, Device> getDevices() {
		return this.devices;
	}

	//remove double quotes from JSON string values
	private String RemoveQuotes(String input)
	{
		return input.replace("\"", "");
	}
}

/**
 * This Class holds the Garage Door Opener Device data.
 * <ul>
 * <li>MyQDeviceId: MyQDeviceId from API, need for http Posts</li>
 * <li>MyQDeviceType: Device Type. GarageDoorOpener or Gateway I've seen</li>
 * <li>DeviceName: Serial number of device I think</li>
 * <li>TypeName: Name That appers in myQ App</li>
 * <li>Status: Garage Door Opener "doorstate" Attribute</li>
 * </ul>
 * 
 * @author Scott Hanson
 * @since 1.8.0
 */
class Device
{
	private int MyQDeviceId;
	private String MyQDeviceType;
	private String DeviceName;
	private String TypeName;
	private int Status;

	public Device(int deviceId,String deviceType,String deviceName,String typeName, int status )
	{
		this.MyQDeviceId = deviceId;
		this.MyQDeviceType = deviceType;
		this.DeviceName = deviceName;
		this.Status = status;
	}

	public int getMyQDeviceId() 
	{
		return this.MyQDeviceId;
	}

	public String getMyQDeviceType()
	{
		return this.MyQDeviceType;
	}

	public String getDeviceName()
	{
		return this.DeviceName;
	}
	
	public String getTypeName()
	{
		return this.TypeName;
	}

	public int getStatus()
	{
		return this.Status;
	}

	public boolean getIsDoorClosed()
	{
		if(this.Status == 2)
			return true;
		return false;
	}

	public String GetStrStatus()
	{
		switch (this.Status)
		{
			case 1:
				return "Open";
			case 2:
				return "Closed";
			case 3:
				return "Partially Open/Closed";
			case 4:
				return "Opening";
			case 5:
				return "Closing";
		}
		return "Unknown";
	}

	public String toString() 
	{
		return this.DeviceName;
	}
}


