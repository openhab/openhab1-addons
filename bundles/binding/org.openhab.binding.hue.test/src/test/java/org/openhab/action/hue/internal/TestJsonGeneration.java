package org.openhab.action.hue.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openhab.action.hue.internal.Rule;

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
		
		
		String s1=r1.toJson();
		assertTrue("condition1",s1.contains("buttonevent"));
		assertTrue("action1",s1.contains("\"scene\""));
		
		Rule r2=Rule.create(s1);
		assertEquals(1,r2.conditions.size());
		assertEquals(2,r2.actions.size());
		
		String s2=r2.toJson();
		assertTrue("action1",s2.contains("\"scene\""));
		
		String scene = (String) r2.actions.get(0).body.get("scene");
		assertEquals("S3",scene);

		int hue = (int) r2.actions.get(1).body.get("hue");
		assertEquals(155,hue);
}

}
