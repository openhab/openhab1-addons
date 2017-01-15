package org.openhab.binding.tinkerforge.internal.tools;

public class LinePositionText {
    private short line;
    private short position;
    private String text;

    public LinePositionText(short line, short position, String text) {
        this.line = line;
        this.position = position;
        this.text = text;
    }

    public short getLine() {
        return line;
    }

    public short getPosition() {
        return position;
    }

    public String getText() {
        return text;
    }
}