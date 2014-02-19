/**
 * 
 */
package org.openhab.binding.dmlsmeter.internal;

import org.openhab.binding.dmlsmeter.DmlsMeterBindingProvider;
import org.openhab.core.library.types.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Günter Speckhofer
 * @version 0.0.1
 * 
 */
public class SimulateDmlsMeterBinding extends DmlsMeterBinding {

	private static final Logger logger =  LoggerFactory.getLogger(SimulateDmlsMeterBinding.class);
	
	private static int i = 0;

	/**
	 * 
	 */
	public SimulateDmlsMeterBinding() {

	}

	@Override
	protected void execute() {
		for (DmlsMeterBindingProvider provider : providers) {
            logger.debug("Names",provider.getItemNames().size());
			for (String itemName : provider.getItemNames()) {
				if (itemName.equals("Tarif1")) {
					eventPublisher.postUpdate(itemName, new StringType("Yeah "+ i++));
				}
			}
		}
	}

}
