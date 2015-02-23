package org.openhab.action.hue.internal;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openhab.action.hue.internal.Group;
import org.openhab.action.hue.internal.Rule;


public class TestActionCreation {
	@Test
	public void testCreateAction() throws Exception {
		Group g=new Group("g1"); //Group.create("g1");
		String groupId="1";
		
		
		List<String> lights=new ArrayList<String>();
		g.setLights(lights);
		
		String j1= g.toJson();
		
		Rule r1=new Rule("Test1");
		
		String tapId="2";
		r1.addGroupAction(groupId, "on", true);
		r1.addTapButtonEqualsCondition(tapId, 1);
		r1.addTapDeviceChangedCondition(tapId);
		
		String j2=r1.toJson();
		
		assertEquals("/groups/1/action",r1.actions.get(0).address);
		assertEquals("/sensors/2/state/buttonevent",r1.conditions.get(0).address);
		assertEquals("/sensors/2/state/buttonevent",r1.conditions.get(0).address);
			
	}
}
