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
package org.openhab.binding.ecobee.messages;

/**
 * The delete vacation function deletes a vacation event from a thermostat. This is the only way to cancel a vacation
 * event. This method is able to remove vacation events not yet started and scheduled in the future.
 *
 * @see <a
 *      href="https://www.ecobee.com/home/developer/api/documentation/v1/functions/DeleteVacation.shtml">DeleteVacation
 *      </a>
 * @author John Cocula
 * @author Ecobee
 * @since 1.7.0
 */
public class DeleteVacationFunction extends AbstractFunction {

    /**
     * @param name
     *            the vacation event name to delete
     */
    public DeleteVacationFunction(String name) {
        super("deleteVacation");
        if (name == null) {
            throw new IllegalArgumentException("name argument is required.");
        }

        makeParams().put("name", name);
    }
}
