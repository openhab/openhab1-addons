/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.net.http;

import junit.framework.Assert;

import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.junit.Test;
import org.openhab.io.net.http.HttpUtil;


/**
 * @author Thomas.Eichstaedt-Engelen
 */
public class HttpUtilTest {

	@Test
	public void testExtractCredentials() {
		
		String expectedUsername = "userna/!&%)(me";
		String expectedPassword = "password67612/&%!$";
		String testUrl = "http://" + expectedUsername + ":" + expectedPassword + "@www.domain.org/123/user";
		
		// method under test
		UsernamePasswordCredentials credentials = 
			(UsernamePasswordCredentials) HttpUtil.extractCredentials(testUrl);
		
		// assert
		Assert.assertEquals(expectedUsername, credentials.getUserName());
		Assert.assertEquals(expectedPassword, credentials.getPassword());
	}

	@Test
	public void testCreateHttpMethod() {
		Assert.assertEquals(GetMethod.class, HttpUtil.createHttpMethod("GET", "").getClass());
		Assert.assertEquals(PutMethod.class, HttpUtil.createHttpMethod("PUT", "").getClass());
		Assert.assertEquals(PostMethod.class, HttpUtil.createHttpMethod("POST", "").getClass());
		Assert.assertEquals(DeleteMethod.class, HttpUtil.createHttpMethod("DELETE", "").getClass());
	}

}
