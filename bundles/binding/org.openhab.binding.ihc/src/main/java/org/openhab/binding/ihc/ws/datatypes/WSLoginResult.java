/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc.ws.datatypes;

import org.openhab.binding.ihc.ws.IhcExecption;
import org.openhab.binding.ihc.ws.datatypes.WSUser;

/**
 * <p>Java class for WSLoginResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WSLoginResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="loggedInUser" type="{utcs}WSUser"/>
 *         &lt;element name="loginWasSuccessful" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="loginFailedDueToConnectionRestrictions" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="loginFailedDueToInsufficientUserRights" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="loginFailedDueToAccountInvalid" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
public class WSLoginResult extends WSBaseDataType {

    protected WSUser loggedInUser;
    protected boolean loginWasSuccessful;
    protected boolean loginFailedDueToConnectionRestrictions;
    protected boolean loginFailedDueToInsufficientUserRights;
    protected boolean loginFailedDueToAccountInvalid;

	public WSLoginResult() {
	}

    /**
     * Gets the value of the loggedInUser property.
     * 
     * @return
     *     possible object is
     *     {@link WSUser }
     *     
     */
    public WSUser getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Sets the value of the loggedInUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSUser }
     *     
     */
    public void setLoggedInUser(WSUser value) {
        this.loggedInUser = value;
    }

    /**
     * Gets the value of the loginWasSuccessful property.
     * 
     */
    public boolean isLoginWasSuccessful() {
        return loginWasSuccessful;
    }

    /**
     * Sets the value of the loginWasSuccessful property.
     * 
     */
    public void setLoginWasSuccessful(boolean value) {
        this.loginWasSuccessful = value;
    }

    /**
     * Gets the value of the loginFailedDueToConnectionRestrictions property.
     * 
     */
    public boolean isLoginFailedDueToConnectionRestrictions() {
        return loginFailedDueToConnectionRestrictions;
    }

    /**
     * Sets the value of the loginFailedDueToConnectionRestrictions property.
     * 
     */
    public void setLoginFailedDueToConnectionRestrictions(boolean value) {
        this.loginFailedDueToConnectionRestrictions = value;
    }

    /**
     * Gets the value of the loginFailedDueToInsufficientUserRights property.
     * 
     */
    public boolean isLoginFailedDueToInsufficientUserRights() {
        return loginFailedDueToInsufficientUserRights;
    }

    /**
     * Sets the value of the loginFailedDueToInsufficientUserRights property.
     * 
     */
    public void setLoginFailedDueToInsufficientUserRights(boolean value) {
        this.loginFailedDueToInsufficientUserRights = value;
    }

    /**
     * Gets the value of the loginFailedDueToAccountInvalid property.
     * 
     */
    public boolean isLoginFailedDueToAccountInvalid() {
        return loginFailedDueToAccountInvalid;
    }

    /**
     * Sets the value of the loginFailedDueToAccountInvalid property.
     * 
     */
    public void setLoginFailedDueToAccountInvalid(boolean value) {
        this.loginFailedDueToAccountInvalid = value;
    }

	@Override
	public void encodeData(String data) throws IhcExecption {
		
		loginWasSuccessful = parseValueToBoolean( data,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:authenticate2/ns1:loginWasSuccessful");

		loginFailedDueToConnectionRestrictions = parseValueToBoolean( data,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:authenticate2/ns1:loginFailedDueToConnectionRestrictions");

		loginFailedDueToInsufficientUserRights = parseValueToBoolean( data,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:authenticate2/ns1:loginFailedDueToInsufficientUserRights");

		loginFailedDueToAccountInvalid = parseValueToBoolean( data,
				"/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:authenticate2/ns1:loginFailedDueToAccountInvalid");
	}

}
