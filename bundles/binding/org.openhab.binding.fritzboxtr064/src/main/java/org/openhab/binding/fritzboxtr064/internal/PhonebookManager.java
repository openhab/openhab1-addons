/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

import java.util.ArrayList;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/***
 * Class managing all Phonebook related work
 *
 * @author gitbock
 * @since 1.8.0
 *
 */
public class PhonebookManager {

    // object for accessing fbox tr064
    private Tr064Comm _tr064comm = null;

    // default logger
    private static final Logger logger = LoggerFactory.getLogger(FritzboxTr064Binding.class);

    // all PhonebooksEntries
    private ArrayList<PhoneBookEntry> _alEntries = null;

    public PhonebookManager(Tr064Comm tr064comm) {
        this._tr064comm = tr064comm;
        this._alEntries = new ArrayList<PhoneBookEntry>();

    }

    /***
     * Looks up name in phone book entries and returns name and type if found
     * 
     * @param number number to look up name for
     * @param compareCount how many characters must match to accept a match
     * @return found name or null
     */
    public String getNameFromNumber(String number, int compareCount) {
        logger.info("Trying to resolve number {} to name comparing {} characters", number, compareCount);
        String name = null;
        Iterator<PhoneBookEntry> it = _alEntries.iterator();
        while (it.hasNext()) {
            PhoneBookEntry pbe = it.next();
            StringBuilder sbAskNumber = new StringBuilder(number);
            sbAskNumber.reverse(); // to be able to compare numbers from the end
            String numberToCompare = "";

            // WORK number
            StringBuilder sbPhonebookNumber = new StringBuilder(pbe.getBusinessTel());
            sbPhonebookNumber.reverse();
            // check if comparing numbers are within entire string range
            if (compareCount <= sbAskNumber.length()) {
                numberToCompare = sbAskNumber.substring(0, compareCount);
            } else {
                numberToCompare = sbAskNumber.substring(0, sbAskNumber.length());
            }
            if (sbPhonebookNumber.toString().startsWith(numberToCompare)) {
                logger.info("found name match {} in phonebook by comparing {} with {} ", pbe.getName(),
                        sbPhonebookNumber.toString(), numberToCompare);
                name = pbe.getName() + " (Work)";
                break; // no need to cycle through rest of phonebook
            }

            // HOME number
            sbPhonebookNumber = new StringBuilder(pbe.getPrivateTel());
            sbPhonebookNumber.reverse();
            // check if comparing numbers are within entire string range
            if (compareCount <= sbAskNumber.length()) {
                numberToCompare = sbAskNumber.substring(0, compareCount);
            } else {
                numberToCompare = sbAskNumber.substring(0, sbAskNumber.length());
            }
            if (sbPhonebookNumber.toString().startsWith(numberToCompare)) {
                logger.info("found name match {} in phonebook by comparing {} with {} ", pbe.getName(),
                        sbPhonebookNumber.toString(), numberToCompare);
                name = pbe.getName() + " (Home)";
                break; // no need to cycle through rest of phonebook
            }

            // MOBILE number
            sbPhonebookNumber = new StringBuilder(pbe.getMobileTel());
            sbPhonebookNumber.reverse();
            // check if comparing numbers are within entire string range
            if (compareCount <= sbAskNumber.length()) {
                numberToCompare = sbAskNumber.substring(0, compareCount);
            } else {
                numberToCompare = sbAskNumber.substring(0, sbAskNumber.length());
            }
            if (sbPhonebookNumber.toString().startsWith(numberToCompare)) {
                logger.info("found name match {} in phonebook by comparing {} with {} ", pbe.getName(),
                        sbPhonebookNumber.toString(), numberToCompare);
                name = pbe.getName() + " (Mobile)";
                break; // no need to cycle through rest of phonebook
            }
        }

        return name;
    }

    /***
     * 
     * @param Phonebook ID to download, can be determined using TR064 GetPhonebookList
     * @return XML Document downloaded
     */
    public Document downloadPhonebook(int id) {
        logger.info("Downloading phonebook ID {}", id);
        String phoneBookUrl = _tr064comm.getTr064Value("phonebook:" + id);
        Document phoneBook = _tr064comm.getFboxXmlResponse(phoneBookUrl);
        logger.debug("Downloaded Phonebook:");
        logger.trace(Helper.documentToString(phoneBook));

        return phoneBook;

    }

    /***
     * Downloads and parses phonebooks from fbox
     */
    public void downloadPhonebooks() {
        Document pb = downloadPhonebook(0);
        if (pb != null) {
            NodeList nlContacts = pb.getElementsByTagName("contact");
            for (int i = 0; i < nlContacts.getLength(); i++) {
                PhoneBookEntry pbe = new PhoneBookEntry();
                Node nContact = nlContacts.item(i);
                if (pbe.parseFromNode(nContact)) {
                    _alEntries.add(pbe);
                } else {
                    logger.warn("could not parse phonebook entry: {}", nContact.toString());
                }
            }
        } else {
            logger.error("Could not download phonebook");
        }

    }

}
