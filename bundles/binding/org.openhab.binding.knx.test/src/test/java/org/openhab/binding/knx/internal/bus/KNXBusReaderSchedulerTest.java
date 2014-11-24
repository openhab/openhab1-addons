/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.knx.internal.bus;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.datapoint.CommandDP;
import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.exception.KNXFormatException;

/**
 * @author Volker Daube
 * @since 1.6.0
 */public class KNXBusReaderSchedulerTest {

	 private  KNXBusReaderScheduler  kNXBindingAutoRefreshScheduler;
	 private int dpCount=0;

	 @Before
	 public void setUp() throws Exception {
		 kNXBindingAutoRefreshScheduler= new KNXBusReaderScheduler();
	 }
	 @Test
	 public void testStart() throws KNXFormatException {
		 assertFalse(kNXBindingAutoRefreshScheduler.isRunning());
		 kNXBindingAutoRefreshScheduler.start();
		 assertTrue(kNXBindingAutoRefreshScheduler.isRunning());
		 kNXBindingAutoRefreshScheduler.stop();
		 assertFalse(kNXBindingAutoRefreshScheduler.isRunning());
	 }

	 @Test
	 public void testAdd() throws KNXFormatException {
		 kNXBindingAutoRefreshScheduler.start();
		 assertTrue(kNXBindingAutoRefreshScheduler.isRunning());

		 Datapoint datapoint = createDP("1.001");
		 assertTrue(kNXBindingAutoRefreshScheduler.scheduleRead(datapoint, 0 ));
		 assertFalse(kNXBindingAutoRefreshScheduler.scheduleRead(null, 0));
		 assertFalse(kNXBindingAutoRefreshScheduler.scheduleRead(null, 1));
		 assertFalse(kNXBindingAutoRefreshScheduler.scheduleRead(datapoint, -1));
		 
		 assertTrue(kNXBindingAutoRefreshScheduler.scheduleRead(datapoint, 2));
		 try {
			 Thread.sleep(5000);
		 } catch (InterruptedException e) {
			 e.printStackTrace();
		 }
		 assertTrue(kNXBindingAutoRefreshScheduler.scheduleRead(datapoint, 3));
		 try {
			 Thread.sleep(7000);
		 } catch (InterruptedException e) {
			 e.printStackTrace();
		 }
		 kNXBindingAutoRefreshScheduler.clear();
		 kNXBindingAutoRefreshScheduler.stop();
		 assertFalse(kNXBindingAutoRefreshScheduler.isRunning());
	 }

	 @Test
	 public void testLargeNumberOfDPs() throws KNXFormatException {
		 kNXBindingAutoRefreshScheduler.start();
		 assertTrue(kNXBindingAutoRefreshScheduler.isRunning());
		 Datapoint datapoint = createDP("1.001");
		 assertTrue(kNXBindingAutoRefreshScheduler.scheduleRead(datapoint, 1));
		 datapoint = createDP("1.001");
		 assertTrue(kNXBindingAutoRefreshScheduler.scheduleRead(datapoint, 1));
		 datapoint = createDP("1.001");
		 assertTrue(kNXBindingAutoRefreshScheduler.scheduleRead(datapoint, 1));
		 datapoint = createDP("1.001");
		 assertTrue(kNXBindingAutoRefreshScheduler.scheduleRead(datapoint, 1));
		 
		 datapoint = createDP("2.001");
		 assertTrue(kNXBindingAutoRefreshScheduler.scheduleRead(datapoint, 2));
		 datapoint = createDP("2.001");
		 assertTrue(kNXBindingAutoRefreshScheduler.scheduleRead(datapoint, 2));
		 datapoint = createDP("2.001");
		 assertTrue(kNXBindingAutoRefreshScheduler.scheduleRead(datapoint, 2));
		 datapoint = createDP("2.001");
		 assertTrue(kNXBindingAutoRefreshScheduler.scheduleRead(datapoint, 2));
		 
		 datapoint = createDP("2.001");
		 assertTrue(kNXBindingAutoRefreshScheduler.scheduleRead(datapoint, 10));
		 datapoint = createDP("2.001");
		 assertTrue(kNXBindingAutoRefreshScheduler.scheduleRead(datapoint, 11));
		 datapoint = createDP("2.001");
		 assertTrue(kNXBindingAutoRefreshScheduler.scheduleRead(datapoint, 12));
		 datapoint = createDP("2.001");
		 assertTrue(kNXBindingAutoRefreshScheduler.scheduleRead(datapoint, 13));
		 
		 try {
			 Thread.sleep(15000);
		 } catch (InterruptedException e) {
			 e.printStackTrace();
		 }
		 kNXBindingAutoRefreshScheduler.clear();
		 kNXBindingAutoRefreshScheduler.stop();
		 assertFalse(kNXBindingAutoRefreshScheduler.isRunning());
	 }

	 @Test
	 public void testClear() {
		 kNXBindingAutoRefreshScheduler.clear();
	 }

	 /**
	  * Convenience method creating a Datapoint
	  * 
	  * @param dpt datapoint type
	  * @return a new CommandDP
	  * @throws KNXFormatException
	  */
	 private Datapoint createDP(String dpt) throws KNXFormatException {
		 dpCount++;
		 int mainNumber=Integer.parseInt(dpt.substring(0, dpt.indexOf('.')));
		 return new CommandDP(new GroupAddress("1/1/"+dpCount), "test"+dpCount, mainNumber, dpt);
		 
	 }
 }
