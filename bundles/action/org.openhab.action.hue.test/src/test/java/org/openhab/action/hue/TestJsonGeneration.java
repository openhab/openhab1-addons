package org.openhab.action.hue;

import static org.junit.Assert.*;

import org.junit.Test;
import org.codehaus.jackson.map.JsonSerializer;

public class TestJsonGeneration {

	@Test
	public void testGetUpdateJson() throws Exception {
		Rule r1=new Rule("Rule1");
		//{"address":"/sensors/2/state/buttonevent","operator":"eq","value":"16"}
		//need api for that!!
		
//		{  "name":"Wall Switch Rule",
//			   "conditions":[
//			        {"address":"/sensors/2/state/buttonevent","operator":"eq","value":"16"}
//			   ],
//			   "actions":[	
//			        {"address":"/groups/0/action","method":"PUT", "body":{"scene":"S3"}}
//			]}
		
		r1.addCondition("/sensors/2/state/buttonevent", "eq", "16");
		r1.addAction("/groups/0/action", "PUT", "scene", "S3");
		r1.addAction("/groups/0/action", "PUT", "hue", 155);
		
		
		String s1=r1.getUpdateJson();
		assertTrue("condition1",s1.contains("buttonevent"));
		assertTrue("action1",s1.contains("\"scene\""));
		
		
		
		
	}

}
