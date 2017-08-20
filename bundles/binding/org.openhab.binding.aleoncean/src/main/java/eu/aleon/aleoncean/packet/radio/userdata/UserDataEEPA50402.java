package eu.aleon.aleoncean.packet.radio.userdata;

/**
 *
 * @author Stephan Meyer {@literal <smeyersdev@gmail.com>}
 */
public class UserDataEEPA50402 extends UserDataEEPA504 {

    public static final double HUMIDITY_SCALE_MIN = 0;
    public static final double HUMIDITY_SCALE_MAX = 100;

    public static final double TEMPERATURE_SCALE_MIN = -20;
    public static final double TEMPERATURE_SCALE_MAX = 60;

    public UserDataEEPA50402(final byte[] eepData) {
        super(eepData, HUMIDITY_SCALE_MIN, HUMIDITY_SCALE_MAX, TEMPERATURE_SCALE_MIN, TEMPERATURE_SCALE_MAX);
    }

}
