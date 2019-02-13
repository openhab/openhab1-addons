package org.openhab.binding.tinkerforge.internal.types;

import org.openhab.core.library.types.StringType;

public class StringValue implements TinkerforgeValue {

    private StringType stringType;

    public StringValue(StringType stringType) {
        this.stringType = stringType;
    }

    public StringValue(String value) {
        this(StringType.valueOf(value));
    }

    public StringType getStringType() {
        return stringType;
    }

    public void setStringType(StringType stringType) {
        this.stringType = stringType;
    }

    @Override
    public String toString() {
        return stringType.toString();
    }

}
