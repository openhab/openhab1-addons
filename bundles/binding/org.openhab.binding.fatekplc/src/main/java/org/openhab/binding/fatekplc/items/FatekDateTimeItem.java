/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fatekplc.items;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openhab.core.items.Item;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.simplify4u.jfatek.registers.Reg;
import org.simplify4u.jfatek.registers.RegValue;
import org.simplify4u.jfatek.registers.UnknownRegNameException;

/**
 * DateTime item implementation.
 *
 * @author Slawomir Jaranowski
 * @since 1.9.0
 */
public class FatekDateTimeItem extends FatekPLCItem {

    class WordBits {

        protected Integer value;

        protected Reg reg;

        public WordBits(String regName) throws BindingConfigParseException {

            if (Character.isDigit(regName.charAt(0))) {
                value = Integer.parseInt(regName);
            } else {

                try {
                    reg = Reg.parse(regName);
                } catch (UnknownRegNameException e) {
                    throw new BindingConfigParseException(e.getMessage());
                }

                if (reg.is32Bits() || reg.isDiscrete()) {
                    throw new BindingConfigParseException("Please use 16 bit data register for DateTime item");
                }
            }
        }

        public void fillRegs(Set<Reg> regs) {

            if (value == null) {
                regs.add(reg);
            }
        }

        public int getValue(Map<Reg, RegValue> response) {

            if (value != null) {
                return value;
            }

            return response.get(reg).intValueUnsigned();
        }

        protected String toStringSuffix() {
            return "";
        }

        @Override
        public String toString() {

            if (value != null) {
                return value.toString();
            }

            return reg.toString() + toStringSuffix();
        }

    }

    class WordBitsLB extends WordBits {

        public WordBitsLB(String regName) throws BindingConfigParseException {
            super(regName);
        }

        @Override
        public int getValue(Map<Reg, RegValue> response) {

            if (value != null) {
                return value;
            }

            return response.get(reg).intValueUnsigned() & 0x00ff;
        }

        @Override
        protected String toStringSuffix() {
            return "L";
        }
    }

    class WordBitsHB extends WordBits {

        public WordBitsHB(String regName) throws BindingConfigParseException {
            super(regName);
        }

        @Override
        public int getValue(Map<Reg, RegValue> response) {

            if (value != null) {
                return value;
            }

            return (response.get(reg).intValueUnsigned() >> 8) & 0x00ff;
        }

        @Override
        protected String toStringSuffix() {
            return "H";
        }
    }

    // registers for data
    private WordBits year;
    private WordBits month;
    private WordBits day;
    private WordBits hour;
    private WordBits minute;
    private WordBits second;

    private boolean relative;
    private Reg regSec;
    private BigDecimal factor;

    public FatekDateTimeItem(Item item, List<String> confItems) throws BindingConfigParseException {

        super(item, confItems);

        String sFactor = getParamsFromConf(confItems, "factor", null);
        if (sFactor != null) {
            factor = new BigDecimal(sFactor);
        }

        if (confItems.size() == 6) {
            year = getWordBits(confItems.get(0));
            month = getWordBits(confItems.get(1));
            day = getWordBits(confItems.get(2));
            hour = getWordBits(confItems.get(3));
            minute = getWordBits(confItems.get(4));
            second = getWordBits(confItems.get(5));
        } else if (confItems.size() == 1) {
            String regNamString = confItems.get(0);
            if (regNamString.endsWith("R")) {
                // relative time in second
                relative = true;
                regNamString = regNamString.substring(0, regNamString.length() - 1);
            }

            try {
                regSec = Reg.parse(regNamString);
            } catch (UnknownRegNameException e) {
                throw new BindingConfigParseException(e.getMessage());
            }
        }
    }

    @Override
    public Collection<? extends Reg> getRegs() {

        Set<Reg> regs = new HashSet<>();

        if (regSec == null) {
            year.fillRegs(regs);
            month.fillRegs(regs);
            day.fillRegs(regs);
            hour.fillRegs(regs);
            minute.fillRegs(regs);
            second.fillRegs(regs);
        } else {
            regs.add(regSec);
        }

        return regs;
    }

    @Override
    public State getState(Map<Reg, RegValue> response) {

        Calendar cal = Calendar.getInstance();

        if (regSec == null) {
            // calculate some value
            int y = year.getValue(response);
            if (y < 50) {
                y += 2000;
            } else if (y < 100) {
                y += 1900;
            }


            int m = month.getValue(response) - 1;

            cal.clear();
            cal.set(y, m, day.getValue(response),
                    hour.getValue(response), minute.getValue(response), second.getValue(response));
        } else {
            long value = response.get(regSec).longValue() * 1000;

            if (factor != null) {
                value = factor.multiply(BigDecimal.valueOf(value)).longValue();
            }

            if (relative) {
                value = System.currentTimeMillis() - value;
            }

            cal.setTimeInMillis(value);
            cal.set(Calendar.MILLISECOND, 0);
        }

        return new DateTimeType(cal);
    }

    private WordBits getWordBits(String regName) throws BindingConfigParseException {

        if (regName.endsWith("L")) {
            return new WordBitsLB(regName.substring(0, regName.length() - 1));
        } else if (regName.endsWith("H")) {
            return new WordBitsHB(regName.substring(0, regName.length() - 1));
        } else {
            return new WordBits(regName);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append("[");
        sb.append("slave=").append(getSlaveName());
        sb.append(", name=").append(getItemName());
        if (regSec == null) {
            sb.append(", Y=").append(year);
            sb.append(", m=").append(month);
            sb.append(", d=").append(day);
            sb.append(", H=").append(hour);
            sb.append(", M=").append(minute);
            sb.append(", S=").append(second);
        } else {
            sb.append(", regSec=").append(regSec);
            if (relative) {
                sb.append(" relative");
            }
        }
        sb.append("]");

        return sb.toString();
    }

}
