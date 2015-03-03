package org.openhab.binding.sagercaster;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.binding.sagercaster.internal.CommandType;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

/**
 * This is a class that stores Sager Caster binding configuration elements :
 * 	 - the type of configuration item
 *   - an interface to related openHAB item
 * @author Gaël L'hopital
 * @since 1.7.0
 *
 */

public class SagerCasterBindingConfig implements BindingConfig {
	public CommandType commandType;
	public Item item;
	
	public SagerCasterBindingConfig(CommandType commandType, Item item) {
		this.commandType = commandType;
		this.item = item;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ToStringBuilder tsb = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		tsb.append("commandType", commandType.toString());
		return tsb.toString();
	}
}
