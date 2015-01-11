package org.openhab.action.hue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;

import org.codehaus.jackson.annotate.*;
import org.codehaus.jackson.map.annotate.*;

/**
 * a rule as described in http://www.developers.meethue.com/documentation/rules-api
 * @
 * @author Gernot Eger
 *
 */
public class Rule {

		
	@JsonProperty
	String name;
	
	@JsonProperty
	List<Condition> conditions=new ArrayList<Condition>();
	
	@JsonProperty
	List<Action> actions=new ArrayList<Action>();
	
	
	//{"address":"/sensors/2/state/buttonevent","operator":"eq","value":"16"}
	
	public class Condition {
		@JsonProperty
		public String adress;
		@JsonProperty
		public String operator;
		@JsonProperty
		public String value;

		public Condition(String adress, String operator, String value) {
			super();
			this.adress = adress;
			this.operator = operator;
			this.value = value;
		}
	}
	
//	"address": "/groups/0/action",
//    "method": "PUT",
//    "body": {
//        "scene": "S3"
//    }
	/**
	 * action item
	 * @author Gernot Eger
	 *
	 */
	public static class Action {
				
		private Action(String adress, String method, String bodyElement, Object bodyValue) {
			super();
			this.adress = adress;
			this.method = method;
			this.body = new ActionBody(bodyElement,bodyValue);
		}
		
		@JsonProperty
		public String adress;
		@JsonProperty
		public String method;
		
		@JsonSerialize(using = ActionBodySerializer.class)
		@JsonProperty
		public ActionBody body;
		
		
		/**
		 * get action body here
		 * @author Gernot Eger
		 *
		 */
		private class ActionBody{
			
			String bodyElement;
			Object bodyValue;
			
			private ActionBody(String bodyElement, Object bodyValue) {
				super();
				this.bodyElement = bodyElement;
				this.bodyValue = bodyValue;
			}
		}

		public static class ActionBodySerializer extends JsonSerializer<ActionBody> {
			public void serialize(ActionBody value, JsonGenerator jgen,
					SerializerProvider provider) throws IOException,
					JsonProcessingException {
				jgen.writeStartObject();
				jgen.writeObjectField(value.bodyElement, value.bodyValue);
				jgen.writeEndObject();
			}
		}

	}
		
	/**
	 * raw condition adder
	 * @param adress
	 * @param operator
	 * @param value
	 */
	public void addCondition(String adress,String operator,String value){
		Condition condition=new Condition(adress,operator,value);
		conditions.add(condition);
	}
	
	/**
	 * raw action adder
	 * @param adress
	 * @param method
	 * @param bodyElement
	 * @param bodyValue
	 */
	public void addAction(String adress, String method, String bodyElement, Object bodyValue){
		Action action=new Action( adress,  method,  bodyElement,  bodyValue);
		actions.add(action);
	}
	
	/**
	 * create new Rule; name is a must
	 * @param name
	 */
	public Rule(String name) {
		super();
		this.name = name;
	}

	/**
	 * return Json for update
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	@JsonIgnore
	public String getUpdateJson() throws JsonGenerationException, JsonMappingException, IOException{
		
		ObjectMapper mapper = new ObjectMapper();
		
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
	}

}
