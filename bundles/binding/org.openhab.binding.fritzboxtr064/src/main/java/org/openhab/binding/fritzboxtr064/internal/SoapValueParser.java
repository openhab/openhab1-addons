/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

import static org.openhab.binding.fritzboxtr064.internal.Tr064Comm.soapToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;

/***
 * Parse the responses of FritzBox SOAP services into the values for the
 * configured items.
 * <p>
 * The XML document returned by the FritzBox may contain values for one or more items
 * configured as {@link ItemMap#getItemCommands() item commands} in the item map of the service.
 * It generates an {@link ItemConfiguration} for each command with the parameters
 * given in the {@link ItemConfiguration} used for the service call (since the response
 * might depend on the parameters of the configuration). It then calls
 * {@link #parseValueFromSoapBody(ItemConfiguration, SOAPBody, ItemMap) parseValueFromSoapBody}
 * for every configuration to get the value, or
 * {@link #parseValueFromSoapFault(ItemConfiguration, SOAPFault, ItemMap) parseValueFromSoapFault}
 * if the response contained a fault. Subclasses can override these methods to implement
 * custom behaviour.
 * </p>
 *
 * @author gitbock
 * @author Michael Koch <tensberg@gmx.net>
 * @since 1.8.0
 *
 */
public class SoapValueParser {
    private static final Logger logger = LoggerFactory.getLogger(SoapValueParser.class);

    /***
     * Parse the FritzBox response to a SOAP service request into values for all defined
     * {@link ItemMap#getItemCommands() items} of the item map of this service request.
     *
     * @param sm soap message to parse
     * @param mapping itemmap with information about all TR064 parameters returned by the invoked SOAP service.
     * @param itemConfiguration the item configuration for which the SOAP service call was initiated.
     *            All item configuration in the returned map must have the same
     *            {@link ItemConfiguration#getDataInValue() dataInValue}
     *            and {@link ItemConfiguration#getAdditionalParameters() parameters} as this configuration.
     * @return Map of item values for the {@link ItemConfiguration item configurations} corresponding to all
     *         {@link ItemMap#getItemCommands() items} defined for this service request.
     */
    public Map<ItemConfiguration, String> parseValuesFromSoapMessage(SOAPMessage sm, ItemMap mapping,
            ItemConfiguration itemConfiguration) {
        Set<String> itemCommands = mapping.getItemCommands();
        Map<ItemConfiguration, String> itemConfigurationToValues = new HashMap<>(itemCommands.size());
        try {
            SOAPBody soapBody = sm.getSOAPBody();
            parseValuesFromSoapBody(sm, soapBody, mapping, itemCommands, itemConfigurationToValues, itemConfiguration);
        } catch (SOAPException e) {
            logger.warn("Error parsing SOAP response from FritzBox: {}. ", e.getMessage());
            setAllItemValuesUnavailable(itemCommands, itemConfigurationToValues, itemConfiguration);
        }
        return itemConfigurationToValues;
    }

    private void parseValuesFromSoapBody(SOAPMessage soapMessage, SOAPBody soapBody, ItemMap mapping,
            Set<String> itemCommands, Map<ItemConfiguration, String> itemConfigurationToValues,
            ItemConfiguration originalItemConfiguration) {
        SOAPFault soapFault = null;
        if (soapBody.hasFault()) {
            soapFault = soapBody.getFault();
        }
        boolean anyValueMissing = false;
        boolean soapFaultHandled = false;

        for (String itemCommand : itemCommands) {
            ItemConfiguration itemConfiguration = deriveConfiguration(itemCommand, originalItemConfiguration);
            String value;
            if (soapFault == null) {
                value = parseValueFromSoapBody(itemConfiguration, soapBody, mapping);
                anyValueMissing |= (value == null);
            } else {
                value = parseValueFromSoapFault(itemConfiguration, soapFault, mapping);
                soapFaultHandled |= (value != null);
            }
            itemConfigurationToValues.put(itemConfiguration, value);
        }

        // log the SOAP Message once if it contained an unhandled fault or if it did not contain an expected item
        if (soapFault != null && !soapFaultHandled) {
            logger.warn("Fault received from FritzBox for item {} in SOAP response {}", originalItemConfiguration,
                    soapToString(soapMessage));
        } else if (anyValueMissing) {
            logger.debug("Some values received from FritzBox for item {} could not be found in SOAP response {}",
                    originalItemConfiguration, soapToString(soapMessage));
            // which items exacly were missing was already logged earlier
        }
    }

    /**
     * Get the value for a single {@link ItemConfiguration} from the body of the SOAP service response.
     * Called for every configured item command of the mapping, unless the response contained a SOAP fault.
     * <p>
     * This implementation gets the name of the {@link ItemMap#getReadDataOutName(String) element for the item command}
     * from the
     * mapping and returns the text of the XML element with that name. If the element is not found in the response,
     * <code>null</code> is returned.
     * </p>
     * <p>
     * Subclasses can override this method to implement custom parsing behaviour.
     * </p>
     *
     * @param itemConfiguration Item configuration for which the value should be parsed.
     * @param soapBody Body of the SOAP service response from which the value should be parsed.
     * @param mapping Item mapping for which the SOAP service was invoked.
     * @return Value of this item command. <code>null</code> if the value could not be parsed (will be converted to an
     *         error value in {@link FritzboxTr064Binding#execute()}).
     */
    protected String parseValueFromSoapBody(ItemConfiguration itemConfiguration, SOAPBody soapBody, ItemMap mapping) {
        String value;
        String readDataOutName = mapping.getReadDataOutName(itemConfiguration.getItemCommand());
        NodeList nlDataOutNodes = soapBody.getElementsByTagName(readDataOutName);
        if (nlDataOutNodes != null && nlDataOutNodes.getLength() > 0) {
            // extract value from soap response
            value = nlDataOutNodes.item(0).getTextContent();
        } else {
            logger.warn("FritzBox returned unexpected response. Could not find expected data value {} in response.",
                    readDataOutName);
            // response XML will be logged once at the end of parseValuesFromSoapBody
            value = null;
        }
        return value;
    }

    /**
     * Get the value for a single {@link ItemConfiguration} from the body of the SOAP service response.
     * Called for every configured item command of the mapping if the response contained a SOAP fault.
     * <p>
     * Subclasses may override this method to implement custom error handling. They could for example
     * return a custom error value for certain known errors. The method should return <code>null</code>
     * for general errors, which will be converted to an actual error value in {@link FritzboxTr064Binding#execute()}).
     * </p>
     * <p>
     * This class usually logs the content of the SOAP response as warning in case of SOAP faults. If a subclass
     * returns a non-null value from this method, it is assumed that the SOAP fault occurs during normal operation
     * and the log message is suppressed.
     * </p>
     * <p>
     * This implementation always returns <code>null</code>.
     * </p>
     *
     * @param itemConfiguration Item configuration for which the value should be parsed.
     * @param soapFault fault contained in the SOAP service response.
     * @param mapping Item mapping for which the SOAP service was invoked.
     * @return Value of this item command for the given fault, or <code>null</code> to use the standard error value.
     */
    protected String parseValueFromSoapFault(ItemConfiguration itemConfiguration, SOAPFault soapFault,
            ItemMap mapping) {
        return null;
    }

    private void setAllItemValuesUnavailable(Set<String> itemCommands,
            Map<ItemConfiguration, String> itemConfigurationToValues, ItemConfiguration originalItemConfiguration) {
        for (String itemCommand : itemCommands) {
            itemConfigurationToValues.put(deriveConfiguration(itemCommand, originalItemConfiguration), null);
        }
    }

    private ItemConfiguration deriveConfiguration(String itemCommand, ItemConfiguration originalItemConfiguration) {
        return new ItemConfiguration(itemCommand, originalItemConfiguration.getDataInValue(),
                originalItemConfiguration.getAdditionalParameters());
    }

}
