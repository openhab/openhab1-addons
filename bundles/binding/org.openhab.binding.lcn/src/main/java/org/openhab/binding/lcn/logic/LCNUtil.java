/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.logic;

import java.util.List;

import org.openhab.binding.lcn.logic.data.LCNInputModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements some helpful methods to handle certain aspects of the LCNBinding.
 * 
 * @author Patrik Pastuschek
 * @since 1.7.0
 *
 */
public class LCNUtil {

	/**The logger, which handles output.*/
	static private final Logger logger = LoggerFactory.getLogger(LCNUtil.class);
	
	/**
	 * Prints the LCNInputModule information on the screen.
	 * Additional information for binary states can be printed as well.
	 * 
	 * @param mod The LCNInputModule in question.
	 * @param extended True if additional information shall be printed.
	 */
	public static void printModule(LCNInputModule mod, boolean extended) {
		
		if (null != mod) {
			
			System.out.println("S:" + mod.segment + " M:" + mod.module + " A:" + mod.outlet + " Type:" + mod.type + " ~ " + mod.dataId + " DataType: " 
					+ mod.datatype + " Value:" + mod.value + " isBinary:" + (mod.type == LCNInputModule.ModuleType.BINARY) + " modifier: " + mod.modifier);
			if (extended) {
				for (int i = 0; i < 8; i++) {
					System.out.print(mod.bools[i] + " ");
				}
				System.out.println();
				if (null != mod.sceneData) {
					System.out.println(mod.sceneData.toString());
				}
			}			
			
		} else {
			System.out.println("MOD IS NULL");
		}
		
	}
	
	/**
	 * Prints the LCNInputModule information on the screen.
	 * 
	 * @param mod The LCNInputModule in question.
	 */
	public static void printModule(LCNInputModule mod) {
		
		printModule(mod, false);
		
	}
	
	/**
	 * Compares to dates in hex form.
	 * @param date The first date which is compared.
	 * @param date2 The Second date which is the base.
	 * @return int: '<0' = date1 is earlier; 0 = equal; '>0' = date1 is later
	 */
	public static int compareHexDate(String date, String date2) {
		
		int result = 0;
		
		if (null == date && null == date2) {
			result = 0;
			logger.debug("Both Hexdates were NULL!");
		} else if (null == date) {
			result = -1;
			logger.debug("Hexdate 1 was NULL!");
		} else if (null == date2) {
			result = 1;
			logger.debug("Hexdate 2 was NULL!");
		} else {
		
			for (int i = 0; i < 6; i ++) {
				
				int interim = date.substring(i, i + 1).compareTo(date2.substring(i, i + 1));
				
				if (interim != 0) {
					
					result = interim;
					break;
					
				}
				
			}
		
		}
		
		return result;
		
	}

	
	/**
	 * Compares an Object to a whole list of objects. If any of them equals the original, true is returned.
	 * @param original The original object.
	 * @param others A list of objects for comparison.
	 * @return True if any object of the list equals the original, false otherwise.
	 */
	public static <T> boolean equalsAny(T original, List<T> others) {
		
		boolean result = false;
		
		for (T other : others) {
			
			if (original.equals(other)) {
				
				result = true;
				break;
				
			}
			
		}
		
		return result;
		
	}
	
}
