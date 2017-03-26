/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * String tokenizer for splitting strings. Understands quoting and escaped quotes.
 *
 * See tests for details
 *
 * @author Sami Salonen
 * @since 1.10.0
 *
 */
public class SimpleTokenizer {
    private final char delimiter;

    public SimpleTokenizer(char delimiter) {
        this.delimiter = delimiter;
    }

    public List<String> parse(String itemConfig) {
        boolean insideQuotes = false;
        boolean escaping = false;
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder(itemConfig.length());
        for (int i = 0; i <= itemConfig.length(); i++) {
            if (i == itemConfig.length()) {
                // We have the read the full string, store the final token
                tokens.add(getToken(currentToken));
                break;
            }
            char currentChar = itemConfig.charAt(i);
            boolean currentIsQuote = currentChar == '\"';
            if (currentChar == delimiter) {
                if (insideQuotes) {
                    currentToken.append(currentChar);
                } else if (!insideQuotes) {
                    tokens.add(getToken(currentToken));
                    currentToken.delete(0, currentToken.length());
                    escaping = false;
                }
            } else if (currentChar == '\\') {
                escaping = true;
                currentToken.append(currentChar);
            } else if (currentIsQuote) {
                if (!escaping) {
                    insideQuotes = !insideQuotes;
                } else {
                    escaping = false;
                }
                currentToken.append(currentChar);
            } else {
                escaping = false;
                currentToken.append(currentChar);
            }
        }

        return tokens;
    }

    private String getToken(StringBuilder currentToken) {
        String token;
        if (currentToken.length() > 0 && currentToken.charAt(0) == '\"') {
            if (currentToken.charAt(currentToken.length() - 1) == '\"') {
                token = StringEscapeUtils.unescapeJava(currentToken.toString().substring(1, currentToken.length() - 1));
            } else {
                // String started with a quote but did not end with one
                // basically we have invalid quoting. Let's just pass string as is then
                token = StringEscapeUtils.unescapeJava(currentToken.toString());
            }
        } else {
            // token is empty or does not start with a quote
            token = currentToken.toString();
        }
        return token;
    }

}