/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.transform.internal;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.internal.service.XPathTransformationService;


/**
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.7.0
 */
public class XPathTransformationServiceTest extends AbstractTransformationServiceTest {

	private XPathTransformationService processor;

	@Before
	public void init() {
		processor = new XPathTransformationService();
	}
	
	@Test
	public void testTransformByXPath() throws TransformationException {

		// method under test
		String transformedResponse = processor.transform("//current_conditions/temp_c/@data", source);
		
		// Asserts
		Assert.assertEquals("8", transformedResponse);
	}

}
