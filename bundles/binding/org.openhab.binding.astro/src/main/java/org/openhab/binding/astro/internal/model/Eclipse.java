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
package org.openhab.binding.astro.internal.model;

import java.util.Calendar;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Holds eclipse informations.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class Eclipse {
    private Calendar total;
    private Calendar partial;

    /**
     * Returns the date of the next total eclipse.
     */
    public Calendar getTotal() {
        return total;
    }

    /**
     * Sets the date of the next total eclipse.
     */
    public void setTotal(Calendar total) {
        this.total = total;
    }

    /**
     * Returns the date of the next partial eclipse.
     */
    public Calendar getPartial() {
        return partial;
    }

    /**
     * Sets the date of the next partial eclipse.
     */
    public void setPartial(Calendar partial) {
        this.partial = partial;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("total", total == null ? null : total.getTime())
                .append("partial", partial == null ? null : partial.getTime()).toString();
    }

}
