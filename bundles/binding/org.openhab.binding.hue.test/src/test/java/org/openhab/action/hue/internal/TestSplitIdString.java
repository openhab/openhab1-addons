package org.openhab.action.hue.internal;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import static org.openhab.action.hue.internal.HueActions.*;

public class TestSplitIdString {

	@Test
	public void testEmpty() {
		List<String> ids=splitIdString("");
		assertEquals(0,ids.size());
	}

	@Test
	public void testSimple() {
		List<String> ids=splitIdString("1,2,3");
		assertEquals(3,ids.size());
		assertEquals("1",ids.get(0));
		assertEquals("2",ids.get(1));
		assertEquals("3",ids.get(2));
	}
	@Test
	public void testWitheEmpty() {
		List<String> ids=splitIdString("1,2,,3,");
		assertEquals(3,ids.size());
		assertEquals("1",ids.get(0));
		assertEquals("2",ids.get(1));
		assertEquals("3",ids.get(2));
	}
	
	@Test
	public void testSimpleWithBlanksAndTabs() {
		List<String> ids=splitIdString("1 , 2\t, 3");
		assertEquals(3,ids.size());
		assertEquals("1",ids.get(0));
		assertEquals("2",ids.get(1));
		assertEquals("3",ids.get(2));
	}
}
