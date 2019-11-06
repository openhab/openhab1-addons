/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.myq.internal;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Class parses the JSON data and throws if the login request was not
 * successful.
 * <ul>
 * <li>accountId: accountId from Account request</li>
 * </ul>
 *
 * @author Scott Hanson
 * @since 1.8.0
 */
public class AccountData {
    static final Logger logger = LoggerFactory.getLogger(AccountData.class);

    String accountId;

    /**
     * Constructor of the AccountData.
     * 
     * @param AccountData
     *            The Json node from the myq website.
     */
    @SuppressWarnings("unchecked")
    public AccountData(JsonNode root) throws IOException, InvalidDataException {
        JsonNode data = root.get("Account");

        if (data == null) {
            throw new InvalidDataException("Could not find 'Account' in JSON data");
        }

        JsonNode jsonID = data.get("Id");
        if (jsonID == null) {
            throw new InvalidDataException("Could not find 'Id' in JSON data");
        }

        accountId = jsonID.isTextual() ? jsonID.asText() : "";
        logger.trace("myq accountId: {}", accountId);
    }

    /**
     * @return UserName AccountId
     */
    public String getAccountId() {
        return accountId;
    }
}
