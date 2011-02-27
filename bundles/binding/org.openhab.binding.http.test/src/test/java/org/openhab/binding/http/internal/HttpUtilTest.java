/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.binding.http.internal;

import junit.framework.Assert;

import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.junit.Test;


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
