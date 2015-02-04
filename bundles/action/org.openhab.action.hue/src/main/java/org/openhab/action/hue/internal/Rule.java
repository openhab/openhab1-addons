package org.openhab.action.hue.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.openhab.action.hue.AbstractHueResource;
import org.openhab.binding.hue.internal.hardware.SwitchId;

/**
 * a rule as described in http://www.developers.meethue.com/documentation/rules-api
 * @
 * @author Gernot Eger
 *
 */

public class Rule extends AbstractHueResource {

		
	@JsonProperty
	String name;
	
	@JsonProperty
	List<Condition> conditions=new ArrayList<Condition>();
	
	@JsonProperty
	List<Action> actions=new ArrayList<Action>();
	
	
	//{"address":"/sensors/2/state/buttonevent","operator":"eq","value":"16"}
	
	
	@JsonSerialize(include=Inclusion.NON_NULL)
	public static class Condition {
		@JsonProperty
		public String address;
		@JsonProperty
		public String operator;
		@JsonProperty
		public String value;

		public Condition(String address, String operator, String value) {
			super();
			this.address = address;
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
				
		private Action(String address, String method, String bodyElement, Object bodyValue) {
			super();
			this.address = address;
			this.method = method;
			
			//this.body = new ActionBody(bodyElement,bodyValue);
			this.body= new HashMap<String,Object>();
			body.put(bodyElement, bodyValue);
			
		}
		
		
		public Action() {
			super();
		}


		@JsonProperty
		public String address;
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
	 * add tap button equals event
	 * @param tapId
	 * @param button Tap Button 1..4
	 */
	public void addTapButtonEqualsCondition(String tapId,int button){
		SwitchId buttonId=SwitchId.switchIdForConfigId(button);
		addCondition("/sensors/"+tapId+"/state/buttonevent", "eq",Integer.toString(buttonId.getButtonEvent()));
	}
	
	/**
	 * add condition for tap device with this id changed
	 * @param tapId
	 */
	public void addTapDeviceChangedCondition(String tapId){
		addCondition("/sensors/"+tapId+"/state/lastupdated", "dx",null);
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
	 * modify add action to set this groups item(s
	 * @param group
	 * @param bodyElement
	 * @param bodyValue
	 */
	public void addGroupAction(String group, String bodyElement, Object bodyValue){
		addAction("/groups/"+group+"/action","PUT",bodyElement,bodyValue);
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
	 * create Rule from json description
	 * @param json
	 * @return
	 * @throws IOException 
	 */
	@JsonIgnore
	public static Rule create(String json) throws IOException{		
		return (Rule) create(json,Rule.class);		
	}

	
}
