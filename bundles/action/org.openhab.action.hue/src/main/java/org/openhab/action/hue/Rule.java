package org.openhab.action.hue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

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
	
	public static class Condition {
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

		public Condition() {
			super();
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
			
			//this.body = new ActionBody(bodyElement,bodyValue);
			this.body= new HashMap<String,Object>();
			body.put(bodyElement, bodyValue);
			
		}
		
		
		public Action() {
			super();
		}


		@JsonProperty
		public String adress;
		@JsonProperty
		public String method;
		
		@JsonProperty
		public Map<String,Object> body;

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
	 * empty constructor for parser
	 */
	public Rule() {
		super();
	}

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
	
	/**
	 * create Rule from json description
	 * @param json
	 * @return
	 * @throws IOException 
	 */
	@JsonIgnore
	public static Rule createRule(String json) throws IOException{
		ObjectMapper mapper = new ObjectMapper();
		
		return mapper.readValue(json,Rule.class);
		
	}

	
}
