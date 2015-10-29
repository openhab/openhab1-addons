package org.openhab.binding.myq.internal;

import java.io.IOException;
import java.util.HashMap;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GarageDoorData {
	static final Logger logger = LoggerFactory.getLogger(GarageDoorData.class);

	private boolean success = false;	
	//List<Device> devices;
	HashMap<String, Device> devices = new HashMap<String, Device>();
	
	public GarageDoorData(String loginData)
	{
		ObjectMapper mapper = new ObjectMapper();
		try 
		{
			JsonNode rootNode = mapper.readTree(loginData);
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
	
	private String RemoveQuotes(String input)
	{
		return input.replace("\"", "");		
	}
}



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
     
     public int getMyQDeviceId() {
 		return this.MyQDeviceId;
 	}
     
     public String getMyQDeviceType() {
  		return this.MyQDeviceType;
     }
  		
	public String getDeviceName() {
  		return this.DeviceName;
  	}
	
	public String getTypeName() {
  		return this.TypeName;
  	}
	
	public int getStatus() {
  		return this.Status;
  	}
	
	public boolean getIsDoorClosed() 
	{
		if(this.Status==2)
			return true;
		return false;			
  	}
	public String GetStrStatus()
    {
        switch (this.Status)
        {
            case 1:
                return "Open";
                //break;
            case 2:
                return "Closed";
                //break;
            case 3:
                return "Partially Open/Closed";
                //break;
            case 4:
                return "Opening";
                //break;
            case 5:
                return "Closing";
                //break;            
        }
        return "Unknown";
    }

	public String toString() {
		return this.DeviceName;
	}
}


