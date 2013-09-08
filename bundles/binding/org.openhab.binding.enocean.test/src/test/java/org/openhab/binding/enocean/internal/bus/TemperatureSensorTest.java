package org.openhab.binding.enocean.internal.bus;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.enocean.java.address.EnoceanId;
import org.enocean.java.address.EnoceanParameterAddress;
import org.enocean.java.common.values.NumberWithUnit;
import org.enocean.java.common.values.Unit;
import org.enocean.java.eep.TemperaturSensor;
import org.junit.Test;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;

public class TemperatureSensorTest extends BasicBindingTest {

    @Test
    public void testReceiveTempertureUpdate() {
        parameterAddress = new EnoceanParameterAddress(EnoceanId.fromString(EnoceanBindingProviderMock.DEVICE_ID),
                TemperaturSensor.PARAMETER_ID);
        provider.setParameterAddress(parameterAddress);
        binding.addBindingProvider(provider);
        provider.setItem(new NumberItem("dummie"));
        BigDecimal temperature = new BigDecimal("20.3");
        binding.valueChanged(parameterAddress, new NumberWithUnit(Unit.DEGREE_CELSIUS, temperature));
        assertEquals("Update State", new DecimalType(temperature), publisher.getUpdateState());
    }

}
