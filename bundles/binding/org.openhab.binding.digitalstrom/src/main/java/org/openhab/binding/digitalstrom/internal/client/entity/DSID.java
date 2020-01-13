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
package org.openhab.binding.digitalstrom.internal.client.entity;

/**
 * @author Alexander Betker
 * @since 1.3.0
 */
public class DSID {

    private String dsid = null;
    private final String DEFAULT_DSID = "3504175fe000000000000001";
    private final String PRE = "3504175fe0000000";

    public DSID(String dsid) {
        if (dsid != null && !dsid.trim().equals("")) {
            if (dsid.trim().length() == 24) {
                this.dsid = dsid;
            } else if (dsid.trim().length() == 8) {
                this.dsid = this.PRE + dsid;
            } else if (dsid.trim().toUpperCase().equals("ALL")) {
                this.dsid = "ALL";
            } else {
                this.dsid = DEFAULT_DSID;
            }
        } else {
            this.dsid = DEFAULT_DSID;
        }
    }

    public String getValue() {
        return dsid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DSID) {
            return ((DSID) obj).getValue().equals(this.getValue());
        }
        return false;
    }

    @Override
    public String toString() {
        return dsid;
    }

}
