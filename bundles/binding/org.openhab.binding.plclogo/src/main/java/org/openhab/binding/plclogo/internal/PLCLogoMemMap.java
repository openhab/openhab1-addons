package org.openhab.binding.plclogo.internal;


/*******************
 * Utility Class to help with memory mapping
 * @author g8kmh
 * @since 1.5.0
 *
 */
public class PLCLogoMemMap {
	private int[] retval = new int[2]; // bit location[0] and real mem loc[1]

	public int[] convertToReal(String memloc) {
		retval[0] = -1;
		retval[1] = -1;
		// I , Q and M have bit values derived: I1 is equivalent to VB923.0
		// TODO Add some validation to input parameters!
		
		if ((memloc.substring(0,2).equalsIgnoreCase("VB"))||(memloc.substring(0,2).equalsIgnoreCase("VW"))){
			retval[1] = Integer.parseInt(memloc.substring(2, memloc.length()));
			
		}
		if (memloc.substring(0,1).equalsIgnoreCase("I")){
			// I starts at 923 for three bytes
			retval[1]= 923 + ((Integer.parseInt(memloc.substring(1, memloc.length()))-1)/8);
			retval[0] = (Integer.parseInt(memloc.substring(1, memloc.length()))%8-1);
		}
		if (memloc.substring(0,1).equalsIgnoreCase("Q")){
			// Q starts at 942 for two bytes
			retval[1]=  942 + ((Integer.parseInt(memloc.substring(1, memloc.length()))-1)/8);
			retval[0] = (Integer.parseInt(memloc.substring(1, memloc.length()))%8-1);
		}
		if (memloc.substring(0,1).equalsIgnoreCase("M")){
			// Markers starts at 948 for two bytes
			retval[1]= 948 + ((Integer.parseInt(memloc.substring(1, memloc.length()))-1)/8);
			retval[0] = ((Integer.parseInt(memloc.substring(1, memloc.length()))%8)-1);
		}
		if (memloc.substring(0,2).equalsIgnoreCase("AI")){
			// AI starts at 926 for 8 words
			retval[1]=  926 + ((Integer.parseInt(memloc.substring(2, memloc.length()))-1)*2);
		}
		if (memloc.substring(0,2).equalsIgnoreCase("AQ")){
			// AQ starts at 944 for 2 words
			retval[1]=  944 + ((Integer.parseInt(memloc.substring(2, memloc.length()))-1)*2);
		}
		if (memloc.substring(0,2).equalsIgnoreCase("AM")){
			// AM starts at 952 for 16 words
			retval[1]=  952 + ((Integer.parseInt(memloc.substring(2, memloc.length()))-1)*2);
		}

		return retval;
	}

	
	
	
}
