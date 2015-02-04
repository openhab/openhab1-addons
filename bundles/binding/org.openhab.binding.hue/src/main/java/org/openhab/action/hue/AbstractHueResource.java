package org.openhab.action.hue;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public abstract class AbstractHueResource {
	/**
	 * return Json for update
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	@JsonIgnore
	public String toJson() throws JsonGenerationException, JsonMappingException, IOException{
		
		ObjectMapper mapper = new ObjectMapper();
		
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@JsonIgnore
	public  static AbstractHueResource create(String json,Class clazz) throws IOException{
		ObjectMapper mapper = new ObjectMapper();
		
		return mapper.readValue(json,clazz);
	}
	
}
