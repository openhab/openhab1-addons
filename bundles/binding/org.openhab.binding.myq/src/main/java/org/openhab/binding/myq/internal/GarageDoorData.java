package org.openhab.binding.myq.internal;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GarageDoorData {
	static final Logger logger = LoggerFactory.getLogger(GarageDoorData.class);

	private boolean success = false;
	private String myQName;
	private int id;
	private int status;
	
	public GarageDoorData(String loginData) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(loginData);
			
			Map<String, Object> treeData = mapper.readValue(rootNode, Map.class);
			
			String temp = treeData.get("ReturnCode").toString();
			logger.warn("myq treeData: "+temp);
			
			if(Integer.parseInt(treeData.get("ReturnCode").toString())==0)
			{
				this.success = true;
				//this.securityToken = treeData.get("SecurityToken").toString();				
			}
			
		} catch (IOException e) {
			logger.error("Could not read GarageDoor JSON from MYQ Site.", e);
		}
	}
	public boolean getSuccess() {
		return this.success;
	}
	
	public int GetIntDoorStatus()
    {
		return this.status;
    }
    
	
	public String GetStrDoorStatus()
    {
        switch (this.status)
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
	
}
