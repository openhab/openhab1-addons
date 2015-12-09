/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal;

import org.openhab.binding.souliss.SoulissBindingProvider;

import org.openhab.binding.souliss.internal.network.typicals.Constants;
import org.openhab.binding.souliss.internal.network.typicals.SoulissGenericTypical;
import org.openhab.binding.souliss.internal.network.typicals.SoulissNetworkParameter;
import org.openhab.binding.souliss.internal.network.typicals.SoulissT31;
import org.openhab.binding.souliss.internal.network.typicals.SoulissT12;
import org.openhab.binding.souliss.internal.network.typicals.SoulissTypicals;
import org.openhab.binding.souliss.internal.network.typicals.StateTraslator;
import org.openhab.binding.souliss.internal.network.typicals.TypicalFactory;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class can parse information from the generic binding format and provides
 * souliss binding information from it
 * 
 * eg, from {souliss="T11:1:2" , autoupdate="false"}
 * it extract these informations:
 * -Typical T11
 * -Node 1
 * -Slot 2
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class SoulissGenericBindingProvider extends
		AbstractGenericBindingProvider implements SoulissBindingProvider {
	private static Logger logger = LoggerFactory
			.getLogger(TypicalFactory.class);
	public static SoulissTypicals SoulissTypicalsRecipients = new SoulissTypicals();

	public String getBindingType() {
		return "souliss";
	}

	/**
	 * This method create typicals and add it to hastable
	 * 
	 * @author Tonino Fazio
	 * @since 1.7.0
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		// Create Souliss Typicals
		//
		//...from wiki of Dario De Maio
		// In Souliss the logics that drive your lights, curtains, LED, and
		// others are pre-configured into so called Typicals. A Typical is a
		// logic with a predefined set of inputs and outputs and a know
		// behavior, are used to standardize the user interface and have a
		// configuration-less behavior.
		final String itemName = item.getName();
		logger.trace("Starting to load Souliss config for item {}", itemName);
		
		super.processBindingConfiguration(context, item, bindingConfig);
		String[] sNameArray = bindingConfig.split("\\:");
		String sTypical = sNameArray[0];
		int iNodeID = Integer.parseInt(sNameArray[1]);
		int iSlot = Integer.parseInt(sNameArray[2]);
		byte iBit = 0;
		String sUseSlot = "";
		// gestisce i casi particolari per T31 e T1A, per la presenza del terzo
		// parametro
		if (sNameArray.length > 3) {
			if ((StateTraslator.stringToSOULISSTypicalCode(sTypical) == Constants.Souliss_T31) || (StateTraslator.stringToSOULISSTypicalCode(sTypical) == Constants.Souliss_T12))
				sUseSlot = sNameArray[3];
			else
				iBit = Byte.parseByte(sNameArray[3]);
		}

		String sNote = item.getClass().getSimpleName();

		SoulissGenericTypical soulissTypicalNew = null;
		// gestisce il caso particolare del T31.
		// nel caso del T31 tre definizioni OH devono confluire in un unico
		// Tipico Souliss
		if ((StateTraslator.stringToSOULISSTypicalCode(sTypical) == Constants.Souliss_T31) || (StateTraslator.stringToSOULISSTypicalCode(sTypical) == Constants.Souliss_T12)){
			soulissTypicalNew = SoulissTypicalsRecipients
					.getTypicalFromAddress(iNodeID, iSlot, 0);
			
			//creazione tipico, solo se non si tratta di un T31 al quale è stato aggiunto un parametro
			if(soulissTypicalNew==null){
				soulissTypicalNew = TypicalFactory.getClass(
						StateTraslator.stringToSOULISSTypicalCode(sTypical),
						SoulissNetworkParameter.datagramsocket,
						SoulissNetworkParameter.IPAddressOnLAN, iNodeID, iSlot,
						sNote, iBit, sUseSlot);
			}
			
			if (soulissTypicalNew != null) {
//in base al campo use slot inserisco nel tipico il nome item di riferimento				
				switch (sUseSlot) { 
				case Constants.Souliss_T12_Use_Of_Slot_AUTOMODE:
					((SoulissT12) soulissTypicalNew).setsItemNameAutoModeValue(item.getName());
					((SoulissT12) soulissTypicalNew).setsItemTypeAutoModeValue(sNote);
					break;
				case Constants.Souliss_T12_Use_Of_Slot_SWITCH:
					((SoulissT12) soulissTypicalNew).setsItemNameSwitchValue(item.getName());
					((SoulissT12) soulissTypicalNew).setsItemTypeSwitchValue(sNote);
					break;
				case Constants.Souliss_T31_Use_Of_Slot_SETPOINT:
					((SoulissT31) soulissTypicalNew).setsItemNameSetpointValue(item.getName());
					((SoulissT31) soulissTypicalNew).setsItemTypeSetpointValue(sNote);
					break;
				case Constants.Souliss_T31_Use_Of_Slot_MEASURED:
					((SoulissT31) soulissTypicalNew).setsItemNameMeasuredValue(item.getName());
					((SoulissT31) soulissTypicalNew).setsItemTypeMeasuredValue(sNote);
					break;
				case Constants.Souliss_T31_Use_Of_Slot_SETASMEASURED:
					((SoulissT31) soulissTypicalNew).setAsMeasured.setName(item.getName());
					((SoulissT31) soulissTypicalNew).setAsMeasured.setNote(sNote);
					break;
				case Constants.Souliss_T31_Use_Of_Slot_HEATING:
					((SoulissT31) soulissTypicalNew).heating.setName(item.getName());
					((SoulissT31) soulissTypicalNew).heating.setNote(sNote);
					break;
				case Constants.Souliss_T31_Use_Of_Slot_COOLING:
					((SoulissT31) soulissTypicalNew).cooling.setName(item.getName());
					((SoulissT31) soulissTypicalNew).cooling.setNote(sNote);
					break;
				case Constants.Souliss_T31_Use_Of_Slot_FANOFF:
					((SoulissT31) soulissTypicalNew).fanOff.setName(item.getName());
					((SoulissT31) soulissTypicalNew).fanOff.setNote(sNote);
					break;
				case Constants.Souliss_T31_Use_Of_Slot_FANLOW:
					((SoulissT31) soulissTypicalNew).fanLow.setName(item.getName());
					((SoulissT31) soulissTypicalNew).fanLow.setNote(sNote);
					break;
				case Constants.Souliss_T31_Use_Of_Slot_FANMED:
					((SoulissT31) soulissTypicalNew).fanMed.setName(item.getName());
					((SoulissT31) soulissTypicalNew).fanMed.setNote(sNote);
					break;
				case Constants.Souliss_T31_Use_Of_Slot_FANHIGH:
					((SoulissT31) soulissTypicalNew).fanHigh.setName(item.getName());
					((SoulissT31) soulissTypicalNew).fanHigh.setNote(sNote);
					break;
				case Constants.Souliss_T31_Use_Of_Slot_HEATING_COOLING:
					((SoulissT31) soulissTypicalNew).heatingCoolingModeValue.setName(item.getName());
					((SoulissT31) soulissTypicalNew).heatingCoolingModeValue.setNote(sNote);
					break;
				case Constants.Souliss_T31_Use_Of_Slot_FANAUTOMODE:
					((SoulissT31) soulissTypicalNew).fanAutoMode.setName(item.getName());
					((SoulissT31) soulissTypicalNew).fanAutoMode.setNote(sNote);
					break;
				case Constants.Souliss_T31_Use_Of_Slot_POWER:
					((SoulissT31) soulissTypicalNew).power.setName(item.getName());
					((SoulissT31) soulissTypicalNew).power.setNote(sNote);
					break;
				}
				logger.info("Add parameter to T31/T12 : " + sUseSlot);
			}
			}
		
			
		//creazione tipico, solo se non si tratta di un T31 / T12 al quale è stato aggiunto un parametro
				if(soulissTypicalNew==null){
					soulissTypicalNew = TypicalFactory.getClass(
							StateTraslator.stringToSOULISSTypicalCode(sTypical),
							SoulissNetworkParameter.datagramsocket,
							SoulissNetworkParameter.IPAddressOnLAN, iNodeID, iSlot,
							sNote, iBit, sUseSlot);
				}
				
				if (soulissTypicalNew != null) {
					SoulissTypicalsRecipients.addTypical(item.getName(),
							soulissTypicalNew);
					SoulissNetworkParameter.nodes = SoulissTypicalsRecipients
							.getNodeNumbers();
				}
	}		
	
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		logger.trace("validateItemType for item {} called with bindingConfig={}", item.getName(), bindingConfig);
	}
}
