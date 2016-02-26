/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.gc100ir.util;

import java.util.HashMap;

/**
 * Utility class for storing and retrieving items.
 * 
 * @author Parikshit Thakur & Team
 * @since 1.6.0
 * 
 */
public class ItemUtility {

	static ItemUtility itemUtility;
	static HashMap<String, GC100ItemBean> itemBeanMap = new HashMap<String, GC100ItemBean>();

	public static ItemUtility getInstance() {
		if (itemUtility == null) {
			itemUtility = new ItemUtility();
		}
		return itemUtility;
	}

	public void addItem(String itemName, GC100ItemBean itemBean) {
		itemBeanMap.put(itemName, itemBean);
	}

	public static void clearAllItems() {
		itemBeanMap.clear();
	}

	public HashMap<String, GC100ItemBean> getAllItems() {
		return itemBeanMap;
	}

	public String prepareCode(GC100ItemBean itemBean) {
		if (itemBean != null) {
			return getIRCode(itemBean);
		}
		return null;
	}

	private String getIRCode(GC100ItemBean itemBean) {
		return "sendir," + itemBean.getModule() + ":" + itemBean.getConnector()
				+ ",1," + itemBean.getCode() + "\r";
	}
}
