/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.action.telegram.internal;

/**
 * This class is the model for a Telegram bot/chat, identified by chatId and
 * token
 *
 * @author Paolo Denti
 * @since 1.8.0
 *
 */
public class TelegramBot {

    private String chatId;
    private String token;
    private String parseMode;

    public TelegramBot(String chatId, String token) {
        this.chatId = chatId;
        this.token = token;
    }

    public TelegramBot(String chatId, String token, String parseMode) {
        this.chatId = chatId;
        this.token = token;
        this.parseMode = parseMode;
    }

    public String getChatId() {
        return chatId;
    }

    public String getToken() {
        return token;
    }

    public String getParseMode() {
        return parseMode;
    }
}
