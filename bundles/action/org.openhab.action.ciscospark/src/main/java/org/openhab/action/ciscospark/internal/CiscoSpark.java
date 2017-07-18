/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.ciscospark.internal;

import java.util.UUID;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ciscospark.Message;
import com.ciscospark.Spark;
import com.ciscospark.SparkException;

/**
 * This class provides static methods that can be used in automation rules for
 * sending Messages via Cisco Spark.
 *
 * @author Tom Deckers
 * @since 1.10.0
 */
public class CiscoSpark {

    private static final Logger logger = LoggerFactory.getLogger(CiscoSpark.class);

    static Spark spark = null;

    /**
     * Sends a Message via Cisco Spark
     *
     * @param msgTxt the Message to send
     * @param roomId the Room to which to send
     *
     * @return <code>true</code>, if sending the message has been successful and
     *         <code>false</code> in all other cases.
     */
    @ActionDoc(text = "Sends a Message via Cisco Spark", returns = "<code>true</code>, if sending the tweet has been successful and <code>false</code> in all other cases.")
    public static boolean sparkMessage(@ParamDoc(name = "msgTxt", text = "the Message to send") String msgTxt,
            @ParamDoc(name = "roomId", text = "the Room to which to send") String roomId) {
        if (!CiscoSparkActionService.isProperlyConfigured) {
            logger.debug("Cisco Spark is not yet configured > execution aborted!");
            return false;
        }

        // Validate message
        if (msgTxt == null || "".equals(msgTxt)) {
            logger.warn("Message can't be empty");
            return false;
        }

        // Validate room id
        try {
            UUID.fromString(roomId);
        } catch (IllegalArgumentException e) {
            logger.warn("Room id is not a UUID");
            return false;
        }

        try {
            logger.debug("Creating message");
            // Create the message
            Message msg = new Message();
            msg.setRoomId(roomId);
            msg.setMarkdown(msgTxt);
            logger.debug("About to send message");
            // send the Message
            spark.messages().post(msg);
            logger.debug("Successfully sent Message '{}'", msg.getMarkdown());
            return true;
        } catch (SparkException se) {
            logger.warn("Failed to send message.", se);
            return false;
        } catch (Exception e) {
            logger.warn("Failed to send message!", e);
            return false;
        }
    }

    /**
     * Sends a Message to the default room via Cisco Spark
     *
     * @param msgTxt the Message to send
     *
     * @return <code>true</code>, if sending the message has been successful and
     *         <code>false</code> in all other cases.
     */
    @ActionDoc(text = "Sends a Message to the default room via Cisco Spark", returns = "<code>true</code>, if sending the tweet has been successful and <code>false</code> in all other cases.")
    public static boolean sparkMessage(@ParamDoc(name = "msgTxt", text = "the Message to send") String msgTxt) {
        if (CiscoSparkActionService.defaultRoomId == null || "".equals(CiscoSparkActionService.defaultRoomId)) {
            logger.warn("No default room configured");
            return false;
        }
        return sparkMessage(msgTxt, CiscoSparkActionService.defaultRoomId);
    }

    /**
     * Sends a Message to a Person via Cisco Spark
     *
     * @param msgTxt the Message to send
     * @param personEmail the email of the person to which to send
     *
     * @return <code>true</code>, if sending the message has been successful and
     *         <code>false</code> in all other cases.
     */
    @ActionDoc(text = "Sends a Message via Cisco Spark", returns = "<code>true</code>, if sending the tweet has been successful and <code>false</code> in all other cases.")
    public static boolean sparkPerson(@ParamDoc(name = "msgTxt", text = "the Message to send") String msgTxt,
            @ParamDoc(name = "personEmail", text = "the email of the person to which to send") String personEmail) {
        if (!CiscoSparkActionService.isProperlyConfigured) {
            logger.warn("Cisco Spark is not yet configured > execution aborted!");
            return false;
        }

        // Validate message
        if (msgTxt == null || "".equals(msgTxt)) {
            logger.warn("Message can't be empty");
            return false;
        }

        // Validate email
        try {
            InternetAddress email = new InternetAddress(personEmail);
            email.validate();
        } catch (AddressException e) {
            logger.warn("Email address is not valid");
            return false;
        }

        try {
            logger.debug("Creating message");
            // Create the message
            Message msg = new Message();
            msg.setToPersonEmail(personEmail);
            msg.setMarkdown(msgTxt);
            logger.debug("About to send message");
            // send the Message
            spark.messages().post(msg);
            logger.debug("Successfully sent Message '{}'", msg.getMarkdown());
            return true;
        } catch (SparkException se) {
            logger.warn("Failed to send message.", se);
            return false;
        } catch (Exception e) {
            logger.warn("Failed to send message!", e);
            return false;
        }
    }

}
