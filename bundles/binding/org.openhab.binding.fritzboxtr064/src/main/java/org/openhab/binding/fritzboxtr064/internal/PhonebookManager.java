/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class managing all Phonebook related work.
 *
 * @author gitbock
 * @since 1.8.0
 */
public class PhonebookManager {

    // object for accessing fbox tr064
    private Tr064Comm _tr064comm = null;

    // default logger
    private static final Logger logger = LoggerFactory.getLogger(PhonebookManager.class);

    // all PhonebooksEntries
    private ArrayList<PhoneBookEntry> _alEntries = null;

    public PhonebookManager(Tr064Comm tr064comm) {
        this._tr064comm = tr064comm;
        this._alEntries = new ArrayList<PhoneBookEntry>();

    }

    /**
     * Looks up name in phone book entries and returns name and type if found.
     *
     * @param number number to look up name for
     * @param compareCount how many characters must match to accept a match
     * @return found name or null
     */
    public String getNameFromNumber(final String number, final int compareCount) {
        logger.info("Trying to resolve number {} to name comparing {} characters", number, compareCount);
        String reversedAskNumber = new StringBuilder(number).reverse().toString();    // reverse to be able to compare numbers from the end
        for (PhoneBookEntry pbe : _alEntries) {
            for (TelType t : TelType.values()) {
                final String phoneBookTel = getTel(t, pbe);
                if (phoneBookTel == null) {
                    continue;
                }
                String reversedPhonebookTel = new StringBuilder(phoneBookTel.replaceAll("\\s+","")).reverse().toString();
                if (reversedPhonebookTel.length() == 0) {
                    continue;
                }
                String numberToCompare =  reversedAskNumber;
                // check if comparing numbers are within entire string range
                if (compareCount <= numberToCompare.length()) {
                    numberToCompare = numberToCompare.substring(0, compareCount);
                }
                if (reversedPhonebookTel.startsWith(numberToCompare)) {
                    logger.info("Found name match '{}' in phonebook by comparing incoming number '{}' with address book entry '{}' ", pbe.getName(),
                            number, phoneBookTel);
                    return pbe.getName() + " (" + t + ")";
                }
            }
        }
        return null;
    }
    
    private static enum TelType {
        Work, Home, Mobile, Fax;
    }
    private static String getTel(TelType t, PhoneBookEntry pbe) {
        switch (t) {
        case Work: return pbe.getBusinessTel();
        case Home: return pbe.getPrivateTel();
        case Mobile: return pbe.getMobileTel();
        case Fax: return pbe.getFax();
        default: throw new RuntimeException("Not supported telephone type: " + t);
        }
    }

    /**
     * @param Phonebook ID to download, can be determined using TR064 GetPhonebookList
     * @return XML Document downloaded
     */
    public Document downloadPhonebook(int id) {
        logger.info("Downloading phonebook ID {}", id);
        String phoneBookUrl = _tr064comm.getTr064Value(new ItemConfiguration("phonebook", String.valueOf(id)));
        Document phoneBook = _tr064comm.getFboxXmlResponse(phoneBookUrl);
        logger.debug("Downloaded Phonebook:");
        logger.trace(Helper.documentToString(phoneBook));

        return phoneBook;
    }

    /**
     * Downloads and parses phonebooks from fbox.
     */
    public void downloadPhonebooks(int phonebookid) {
        Document pb = downloadPhonebook(phonebookid);
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
            logger.warn("Could not download phonebook");
        }
    }
}
